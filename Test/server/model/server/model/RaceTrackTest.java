package server.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RaceTrackTest {

  private RaceTrack raceTrack1;
  private RaceTrack raceTrack2;
  private RaceTrack raceTrack3;

  @BeforeEach
  public void setUp() {
    // Setup before each test, initializing the RaceTrack objects
    raceTrack1 = new RaceTrack("Fast Track", 1000, "New York");
    raceTrack2 = new RaceTrack("Fast Track", 1000, "New York");
    raceTrack3 = new RaceTrack("Speedway", 1200, "Los Angeles");
  }

  @Test
  public void testConstructorAndGetters() {
    // Test the constructor and getters
    assertEquals("Fast Track", raceTrack1.getName());
    assertEquals(1000, raceTrack1.getLength());
    assertEquals("New York", raceTrack1.getLocation());
  }

  @Test
  public void testEquals_SameAttributes() {
    // Test the equals method for objects with the same attributes
    assertTrue(raceTrack1.equals(raceTrack2), "RaceTracks with the same attributes should be equal.");
  }

  @Test
  public void testEquals_DifferentAttributes() {
    // Test the equals method for objects with different attributes
    assertFalse(raceTrack1.equals(raceTrack3), "RaceTracks with different attributes should not be equal.");
  }

  @Test
  public void testEquals_Null() {
    // Test the equals method with a null object
    assertFalse(raceTrack1.equals(null), "A RaceTrack should not be equal to null.");
  }

  @Test
  public void testEquals_DifferentClass() {
    // Test the equals method with an object of a different class
    assertFalse(raceTrack1.equals("Not a RaceTrack"), "RaceTrack should not be equal to an object of a different class.");
  }

  @Test
  public void testToString() {
    // Test the toString method
    String expectedToString = "RaceTrack{name='Fast Track', length=1000, location='New York'}";
    assertEquals(expectedToString, raceTrack1.toString(), "toString should return a correctly formatted string.");
  }
}
