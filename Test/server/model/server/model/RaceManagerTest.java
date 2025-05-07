package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RaceManagerTest {

  private RaceManager raceManager;
  private Race race1;
  private Race race2;

  @BeforeEach
  public void setUp() {
    // Get the singleton instance of RaceManager
    raceManager = RaceManager.getInstance();

    // Create mock races (using Mockito to avoid actual execution logic)
    race1 = Mockito.mock(Race.class);
    race2 = Mockito.mock(Race.class);
  }

  @Test
  public void testSingletonInstance() {
    // Get another instance and verify that it is the same as the original
    RaceManager anotherInstance = RaceManager.getInstance();
    assertSame(raceManager, anotherInstance, "RaceManager should be a singleton.");
  }

  @Test
  public void testAddRaceToQueue() {
    // Add races to the queue
    raceManager.addRace(race1);
    raceManager.addRace(race2);

    // Get all races from the queue
    List<Race> races = raceManager.getAllRaces();

    // Verify that the queue contains the correct number of races
    assertEquals(2, races.size(), "The race queue should contain 2 races.");
    assertTrue(races.contains(race1), "Race1 should be in the queue.");
    assertTrue(races.contains(race2), "Race2 should be in the queue.");
  }

  @Test
  public void testRaceExecution() throws InterruptedException {
    // Simulate race execution
    raceManager.addRace(race1);
    raceManager.addRace(race2);

    // Verify that races are executed in sequence
    Mockito.verify(race1, Mockito.timeout(1000).times(1)).run();
    Mockito.verify(race2, Mockito.timeout(1000).times(1)).run();
  }

  @Test
  public void testQueueBehavior() throws InterruptedException {
    // Add a race to the queue
    raceManager.addRace(race1);

    // Check that the race queue contains 1 race
    Thread.sleep(2000); // Allow time for race to be processed
    List<Race> races = raceManager.getAllRaces();
    assertEquals(1, races.size(), "The race queue should contain 1 race.");

    // Add race2 to the queue
    raceManager.addRace(race2);

    // Check again after a delay to give time for race2 to be added
    Thread.sleep(2000);
    races = raceManager.getAllRaces();
    assertEquals(2, races.size(), "The race queue should now contain 2 races.");
  }


  @Test
  public void testThreadHandling() throws InterruptedException {
    // Here, we'll simulate a thread running and executing races
    raceManager.addRace(race1);
    raceManager.addRace(race2);

    // Wait for the races to execute in the background (with a small delay)
    Thread.sleep(500);

    // Verify that the races are executed in order (race1 first, race2 second)
    Mockito.verify(race1, Mockito.times(1)).run();
    Mockito.verify(race2, Mockito.times(1)).run();
  }
}
