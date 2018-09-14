package edu.wpi.first.desktop.plugin;

/**
 * Describes a plugins group ID (a unique identifier for the group or organization that develops the plugin); and its
 * name, version, and a summary of what the plugin provides.
 *
 * <p>Note that the version <i>must</i> follow <a href="http://semver.org">semantic versioning</a> guidelines.
 */
public final class Descriptor {

  private final String groupId;
  private final String name;
  private final Version version;
  private final String summary;

  /**
   * Creates a new descriptor.
   *
   * @param groupId the group ID of the owner of the plugin. By convention this is in
   *                reverse domain-name notation e.g. {@code "com.example.my.plugin"}
   * @param name    the name of the plugin
   * @param version the version of the plugin
   * @param summary a summary of what the plugin provides
   */
  public Descriptor(String groupId, String name, Version version, String summary) {
    if (groupId == null || groupId.isEmpty()) {
      throw new IllegalArgumentException("Group ID must be specified");
    }
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name must be specified");
    }
    if (version == null) {
      throw new IllegalArgumentException("Version must be specified");
    }
    this.groupId = groupId;
    this.name = name;
    this.version = version;
    this.summary = summary == null ? "" : summary;
  }

  /**
   * Creates a new descriptor.
   *
   * @param groupId       the group ID of the owner of the plugin. By convention this is in
   *                      reverse domain-name notation e.g. {@code "com.example.my.plugin"}
   * @param name          the name of the plugin
   * @param versionString a version string of the plugin. This must follow
   *                      <a href="https://semver.org/">Semantic versioning guidelines</a>
   * @param summary       a summary of what the plugin provides
   */
  public Descriptor(String groupId, String name, String versionString, String summary) {
    this(groupId, name, Version.parse(versionString), summary);
  }

  /**
   * Creates a descriptor object from a {@link Description @Description} annotation on an {@link AnnotatedPlugin}
   * plugin.
   *
   * @param annotation the annotation to create a descriptor from
   *
   * @return a descriptor
   *
   * @throws IllegalArgumentException if the version in the annotation does not follow semantic versioning
   */
  public static Descriptor fromAnnotation(Description annotation) {
    return new Descriptor(annotation.groupId(), annotation.name(), annotation.version(), annotation.summary());
  }

  public String getGroupId() {
    return groupId;
  }

  public String getName() {
    return name;
  }

  public Version getVersion() {
    return version;
  }

  public String getSummary() {
    return summary;
  }

  public String idString() {
    return groupId + ":" + name;
  }

  public String fullIdString() {
    return groupId + ":" + name + ":" + version;
  }
}
