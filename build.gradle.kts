
import edu.wpi.first.wpilib.versioning.ReleaseType
import org.gradle.api.Project
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.jvm.tasks.Jar
import org.ajoberstar.grgit.Grgit
import org.ajoberstar.grgit.exception.GrgitException
import org.ajoberstar.grgit.operation.DescribeOp
import java.time.Instant

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0")
    }
}

plugins {
    `java-library`
    `maven-publish`
    id("com.zyxist.chainsaw") version "0.3.1"
    id("edu.wpi.first.wpilib.versioning.WPILibVersioningPlugin") version "2.0"
    id("org.ajoberstar.grgit") version "1.7.2"
}

group = "edu.wpi.first.desktop"
version = getWPILibVersion() ?: getVersionFromGitTag(fallback = "0.0.0") // fall back to git describe if no WPILib version is set

repositories {
    mavenCentral()
    maven {
        name = "WPILib Development"
        setUrl("http://first.wpi.edu/FRC/roborio/maven/development")
    }
    maven {
        name = "WPILib Release"
        setUrl("http://first.wpi.edu/FRC/roborio/maven/release")
    }
}

dependencies {
    fun junitJupiter(name: String, version: String = "5.3.0") =
            create(group = "org.junit.jupiter", name = name, version = version)
    testCompile(junitJupiter(name = "junit-jupiter-api"))
    testCompile(junitJupiter(name = "junit-jupiter-engine"))
    testCompile(junitJupiter(name = "junit-jupiter-params"))
    testRuntime(create(group = "org.junit.platform", name = "junit-platform-launcher", version = "1.0.0"))

    fun testFx(name: String, version: String = "+") =
            create(group = "org.testfx", name = name, version = version)
    testCompile(testFx(name = "testfx-core"))
    testCompile(testFx(name = "testfx-junit5"))
    testRuntime(testFx(name = "openjfx-monocle", version = "+"))
}

tasks.withType<Jar> {
    manifest {
        attributes["Built-Date"] = System.currentTimeMillis()
        attributes["Implementation-Version"] = project.version
    }
}

val sourceJar = task<org.gradle.jvm.tasks.Jar>("sourceJar") {
    description = "Creates a JAR that contains the source code."
    from(java.sourceSets["main"].allSource)
    classifier = "sources"
}
val javadocJar = task<org.gradle.jvm.tasks.Jar>("javadocJar") {
    dependsOn("javadoc")
    description = "Creates a JAR that contains the javadocs."
    from(java.docsDir)
    classifier = "javadoc"
}

publishing {
    publications {
        create<MavenPublication>("desktop-common") {
            groupId = "edu.wpi.first.desktop"
            artifactId = "desktop-common"
            version = getWPILibVersion() ?: project.version.toString()
            val jar: Jar by tasks
            artifact (jar) {
                classifier = null
            }
            artifact(sourceJar)
            artifact(javadocJar)
        }
    }
}

/**
 * @return [edu.wpi.first.wpilib.versioning.WPILibVersioningPluginExtension.version] value or null
 * if that value is the empty string.
 */
fun getWPILibVersion(): String? = if (WPILibVersion.version != "") WPILibVersion.version else null

/**
 * Gets the build version from git-describe. This is a combination of the most recent tag, the number of commits since
 * that tag, and the abbreviated hash of the most recent commit, in this format: `<tag>-<n>-<hash>`; for example,
 * v1.0.0-11-9ab123f when the most recent tag is `"v1.0.0"`, with 11 commits since that tag, and the most recent commit
 * hash starting with `9ab123f`.
 *
 * @param fallback the version string to fall back to if git-describe fails. Default value is `"v0.0.0"`.
 *
 * @see <a href="https://git-scm.com/docs/git-describe">git-describe documentation</a>
 */
fun getVersionFromGitTag(fallback: String = "v0.0.0"): String = try {
    val git = Grgit.open()
    DescribeOp(git.repository).call() ?: fallback
} catch (e: GrgitException) {
    logger.log(LogLevel.WARN, "Cannot get the version from git-describe, falling back to $fallback", e)
    fallback
}
