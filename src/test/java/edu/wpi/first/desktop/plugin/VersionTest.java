package edu.wpi.first.desktop.plugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class VersionTest {

  @Test
  void testParse() {
    String ver = "1.2.3-abc";
    Version version = Version.parse(ver);
    assertEquals(Version.of(1, 2, 3, "-abc"), version);
  }

  @Test
  void testParseLongVersionNumbers() {
    String ver = "1234.567.890-verylongidentifiertext";
    Version version = Version.parse(ver);
    assertEquals(Version.of(1234, 567, 890, "-verylongidentifiertext"), version);
  }

  @Test
  void testParseNoMajor() {
    assertThrows(IllegalArgumentException.class, () -> Version.parse(".1.2-abc"));
  }

  @Test
  void testParseInvalidMajor() {
    assertThrows(IllegalArgumentException.class, () -> Version.parse("a.1.2"));
  }

  @Test
  void testParseNoMinor() {
    assertThrows(IllegalArgumentException.class, () -> Version.parse("1..3"));
  }

  @Test
  void testParseInvalidMinor() {
    assertThrows(IllegalArgumentException.class, () -> Version.parse("1.b.3"));
  }

  @Test
  void testParseNoPatch() {
    assertThrows(IllegalArgumentException.class, () -> Version.parse("1.2."));
  }

  @Test
  void testParseInvalidPatch() {
    assertThrows(IllegalArgumentException.class, () -> Version.parse("1.2.c"));
  }

  @Test
  void testOfNegativeMajor() {
    assertThrows(IllegalArgumentException.class, () -> Version.of(-1, 0, 0));
  }

  @Test
  void testOfNegativeMinor() {
    assertThrows(IllegalArgumentException.class, () -> Version.of(1, -1, 0));
  }

  @Test
  void testOfNegativePatch() {
    assertThrows(IllegalArgumentException.class, () -> Version.of(1, 1, -1));
  }

  @Test
  void testToStringNoIdentifier() {
    Version version = Version.of(1, 2, 3);
    assertEquals("1.2.3", version.toString());
  }

  @Test
  void testToStringWithIdentifier() {
    Version version = Version.of(3, 4, 5, "some-identifier-text");
    assertEquals("3.4.5some-identifier-text", version.toString());
  }

  @Test
  void testIdentifierWhenSetToNull() {
    Version version = Version.of(1, 2, 3, null);
    assertEquals("", version.getIdentifier());
  }

  @Test
  void testIdentifierWhenSetToEmptyString() {
    Version version = Version.of(1, 2, 3, "");
    assertEquals("", version.getIdentifier());
  }

  @Test
  void testCompareToHigherMajor() {
    Version a = Version.of(1, 0, 0);
    Version b = Version.of(20, 0, 0);

    assertLessThan(a, b);
    assertGreaterThan(b, a);
  }

  @Test
  void testCompareToHigherMinor() {
    Version a = Version.of(1, 0, 0);
    Version b = Version.of(1, 100, 0);

    assertLessThan(a, b);
    assertGreaterThan(b, a);
  }

  @Test
  void testCompareToHigherPatch() {
    Version a = Version.of(1, 0, 0);
    Version b = Version.of(1, 0, 10);

    assertLessThan(a, b);
    assertGreaterThan(b, a);
  }

  @Test
  void testCompareToOneNullIdentifier() {
    Version a = Version.of(1, 2, 3);
    Version b = Version.of(1, 2, 3, "identifier");

    // In the event of a tie with the version numbers, a version with no identifier is ALWAYS greater than
    // a version with one
    assertLessThan(b, a);
    assertGreaterThan(a, b);
  }

  @Test
  void testCompareToTwoIdentifiers() {
    Version a = Version.of(1, 2, 3, "a");
    Version b = Version.of(1, 2, 3, "b");

    assertLessThan(a, b);
    assertGreaterThan(b, a);
  }

  @Test
  @SuppressWarnings("EqualsWithItself")
  void testCompareToSelfNoIdentifier() {
    Version version = Version.of(1, 2, 3);
    assertEquals(0, version.compareTo(version));
  }

  @Test
  @SuppressWarnings("EqualsWithItself")
  void testCompareToSelfWithIdentifier() {
    Version version = Version.of(1, 2, 3, "identifier");
    assertEquals(0, version.compareTo(version));
  }

  // Sanity checks

  @Test
  void testGetters() {
    // This is really just a sanity check to make sure the getters aren't broken
    Version version = Version.of(1, 2, 3, "identifier");
    assertAll(
        () -> assertEquals(1, version.getMajor()),
        () -> assertEquals(2, version.getMinor()),
        () -> assertEquals(3, version.getPatch()),
        () -> assertEquals("identifier", version.getIdentifier())
    );
  }

  @Test
  @SuppressWarnings({"ConstantConditions", "SimplifiableJUnitAssertion"})
  void testEqualsNull() {
    assertFalse(Version.of(0, 0, 0).equals(null), "Version object was null!");
  }

  @Test
  void testEqualsSameObject() {
    Version version = Version.of(1, 2, 3);
    assertEquals(version, version, "Version object did not equal itself!");
  }

  @Test
  void sanityCheckHashcode() {
    Version version = Version.of(1, 2, 3, "identifier");
    assertNotEquals(0, version.hashCode());
  }

  private static <T extends Comparable<T>> void assertLessThan(T lhs, T rhs) {
    if (lhs.compareTo(rhs) >= 0) {
      fail(lhs + " should be less than " + rhs);
    }
  }

  private static <T extends Comparable<T>> void assertGreaterThan(T lhs, T rhs) {
    if (lhs.compareTo(rhs) <= 0) {
      fail(lhs + " should be greater than " + rhs);
    }
  }
}