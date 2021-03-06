package edu.wpi.first.desktop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Utility class for detecting the current operating system.
 */
public final class OsDetector extends UtilityClass {

  private static final OperatingSystemType operatingSystemType;
  private static final String unknownDistribution = "unknown distribution";

  static {
    String osName = SystemProperties.OS_NAME.toLowerCase(Locale.US);
    if (osName.contains("windows")) {
      operatingSystemType = OperatingSystemType.WINDOWS;
    } else if (osName.contains("mac")) {
      operatingSystemType = OperatingSystemType.MAC;
    } else if (osName.contains("linux")) {
      operatingSystemType = OperatingSystemType.LINUX;
    } else {
      operatingSystemType = OperatingSystemType.UNKNOWN;
    }
  }

  /**
   * Gets the distribution of the Linux-based operating system that the application is running on.
   *
   * @throws IllegalStateException if the operating system is not Linux-based
   */
  public static String getLinuxDistribution() throws IllegalStateException {
    if (!isLinux()) {
      throw new IllegalStateException(
          "Not running on a Linux-based operating system! OS is " + getOperatingSystemType());
    }
    try {
      Process lsbRelease = new ProcessBuilder("lsb_release", "-a").start();
      try (InputStreamReader isr = new InputStreamReader(lsbRelease.getInputStream(), StandardCharsets.UTF_8);
           BufferedReader reader = new BufferedReader(isr)) {
        return reader.lines()
            .filter(line -> line.startsWith("Description:"))
            .findFirst()
            .map(line -> line.substring("Description:\t".length()))
            .orElse(unknownDistribution);
      }
    } catch (IOException e) {
      return unknownDistribution;
    }
  }

  public enum OperatingSystemType {
    /**
     * A windows operating system.
     */
    WINDOWS,
    /**
     * OS X or Mac OS.
     */
    MAC,
    /**
     * Generic linux-based operating system.
     */
    LINUX,
    /**
     * An unknown operating system.
     */
    UNKNOWN
  }

  /**
   * Gets the type of the operating system.
   */
  public static OperatingSystemType getOperatingSystemType() {
    return operatingSystemType;
  }

  public static boolean isWindows() {
    return operatingSystemType == OperatingSystemType.WINDOWS;
  }

  public static boolean isMac() {
    return operatingSystemType == OperatingSystemType.MAC;
  }

  public static boolean isLinux() {
    return operatingSystemType == OperatingSystemType.LINUX;
  }

  public static boolean isUnknown() {
    return operatingSystemType == OperatingSystemType.UNKNOWN;
  }

}
