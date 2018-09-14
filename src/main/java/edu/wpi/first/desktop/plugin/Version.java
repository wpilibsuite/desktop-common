package edu.wpi.first.desktop.plugin;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A mostly semver-compliant version class. For simplicity, this deviates from the semver standard in the following
 * ways:
 * <ul>
 * <li>Version identifiers have no special cases for comparison; identifiers beginning with {@code -} or {@code +}
 * have no special significance</li>
 * <li>A version with <i>no</i> identifier is always treated as greater than a version <i>with</i> an identifier if
 * they share the same major, minor, and patch version numbers</li>
 * </ul>
 *
 * @see <a href="https://semver.org">semver.org</a>
 */
public final class Version implements Comparable<Version> {

  private final int major;
  private final int minor;
  private final int patch;
  private final String identifier;

  private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(.*)?$");

  // Private constructors - use the static factory methods instead
  private Version(int major, int minor, int patch) {
    this(major, minor, patch, null);
  }

  // Private constructors - use the static factory methods instead
  private Version(int major, int minor, int patch, String identifier) {
    if (major < 0) {
      throw new IllegalArgumentException("Major version cannot be negative: " + major);
    }
    if (minor < 0) {
      throw new IllegalArgumentException("Minor version cannot be negative: " + minor);
    }
    if (patch < 0) {
      throw new IllegalArgumentException("Patch version cannot be negative: " + patch);
    }
    this.major = major;
    this.minor = minor;
    this.patch = patch;
    this.identifier = (identifier == null || identifier.isEmpty()) ? "" : identifier;
  }

  /**
   * Creates a version object from parsing a version string such as {@code "1.2.3"} or {@code "22.0.1-rc1"}.
   *
   * @param versionString the version string to parse
   *
   * @return a version object for the given string
   *
   * @throws IllegalArgumentException if the version string does not match the format
   *                                  {@code <major>.<minor>.<patch><identifier>}
   *                                  (where {@code identifier} may be omitted)
   */
  public static Version parse(String versionString) {
    Matcher matcher = VERSION_PATTERN.matcher(versionString);
    if (!matcher.find()) {
      throw new IllegalArgumentException("Invalid version string: '" + versionString + "'");
    }
    int major = Integer.parseInt(matcher.group(1));
    int minor = Integer.parseInt(matcher.group(2));
    int patch = Integer.parseInt(matcher.group(3));
    String identifier = matcher.group(4);
    return new Version(major, minor, patch, identifier);
  }

  /**
   * Creates a new version object for the version number {@code major.minor.patch}.
   *
   * @param major      the major version number
   * @param minor      the minor version number
   * @param patch      the patch version number
   * @param identifier identifier build information. This may be null or empty.
   *
   * @return a version object with the given version numbers
   *
   * @throws IllegalArgumentException if any of {@code major}, {@code minor}, {@code patch} are negative
   */
  public static Version of(int major, int minor, int patch, String identifier) {
    return new Version(major, minor, patch, identifier);
  }

  /**
   * Creates a new version object for the version number {@code major.minor.patch}.
   *
   * @param major the major version number
   * @param minor the minor version number
   * @param patch the patch version number
   *
   * @return a version object with the given version numbers
   *
   * @throws IllegalArgumentException if any of {@code major}, {@code minor}, {@code patch} are negative
   */
  public static Version of(int major, int minor, int patch) {
    return new Version(major, minor, patch);
  }

  /**
   * Gets the major version number.
   *
   * @return the major version number
   */
  public int getMajor() {
    return major;
  }

  /**
   * Gets the minor version number.
   *
   * @return the minor version number
   */
  public int getMinor() {
    return minor;
  }

  /**
   * Gets the patch version number.
   *
   * @return the patch version number
   */
  public int getPatch() {
    return patch;
  }

  /**
   * Gets the identifier identifier, if one has been specified.
   *
   * @return the identifier identifier, or {@code null} if none was specified when the version object was created
   */
  public String getIdentifier() {
    return identifier;
  }

  @Override
  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    Version that = (Version) obj;
    return this.major == that.major
        && this.minor == that.minor
        && this.patch == that.patch
        && Objects.equals(this.identifier, that.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(major, minor, patch, identifier);
  }

  @Override
  public String toString() {
    // 1.2.3identifier
    return String.format("%d.%d.%d%s", major, minor, patch, identifier);
  }

  /**
   * Compares this version object with the specified version object for order. Versions are compared first by major
   * version number, then by minor, and then by patch. If all three numbers are equal, a version with no identifier will
   * be "greater than" a version with an identifier. If both version objects have an identifier, then order will be
   * determined by their {@link String#compareTo(String) compareTo} method.
   *
   * @param other the object to be compared
   *
   * @return a negative integer, zero, or a positive integer as this version object is less than, equal to, or greater
   *         than the specified version.
   */
  @Override
  public int compareTo(Version other) {
    if (this.major != other.major) {
      return this.major - other.major;
    }
    if (this.minor != other.minor) {
      return this.minor - other.minor;
    }
    if (this.patch != other.patch) {
      return this.patch - other.patch;
    }
    if (this.identifier.isEmpty()) {
      if (other.identifier.isEmpty()) {
        return 0;
      } else {
        return 1;
      }
    } else if (other.identifier.isEmpty()) {
      return -1;
    } else {
      return this.identifier.compareTo(other.identifier);
    }
  }
}
