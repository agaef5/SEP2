//package server.model;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import server.persistence.horses.HorseRepositoryImpl;
//import shared.DTO.RaceState;
//
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class RaceTest {
//
//  private Race race;
//  private RaceTrack raceTrack;
//  private Horse horse1;
//  private Horse horse2;
//  private Horse horse3;
//  private Horse horse4;
//  private Horse horse5;
//
//
//
//  @BeforeEach
//  public void setUp() throws SQLException {
//    // Set up a sample RaceTrack for the race
//    raceTrack = new RaceTrack("ThunderTrack", 1200, "Las Vegas");
//
//    // Create a Race with a name, capacity of 5 horses, and the race track
//    race = new Race("Grand Derby", 5, raceTrack);
//
//     horse1 = HorseRepositoryImpl.getInstance().create("Thunder",100,200);
//     horse2 =HorseRepositoryImpl.getInstance().create("Storm",110,210);
//     horse3 =HorseRepositoryImpl.getInstance().create("Blaze",120,220);
//     horse4 =HorseRepositoryImpl.getInstance().create("Whirlwind",130,230);
//     horse5 =HorseRepositoryImpl.getInstance().create("Flash",140,240);
//  }
//
//  @Test
//  public void testConstructor() {
//    // Test that the race is initialized correctly
//    assertNotNull(race, "Race object should be initialized.");
//    assertEquals("Grand Derby", race.getName(), "Race name should match the constructor value.");
//    assertEquals(RaceState.NOT_STARTED, race.getStatus(), "Race status should be 'NOT_STARTED' by default.");
//    assertEquals(5, race.getHorseList().getCapacity(), "Horse list should have capacity of 5.");
//    assertEquals(5, race.getHorseList().getList().size(), "Horse list should be empty initially.");
//  }
//
//
//  @Test
//  public void testAssignRandomHorsesFromDatabase() throws SQLException {
//    // Assuming the HorseRepositoryImpl contains horses for testing
//    // Here we add some mock horses to simulate database behavior
//
//
//    // Assign horses to the race
//    race.assignRandomHorsesFromDatabase();
//
//    // Verify that the race has the correct number of horses (capacity 5)
//    assertEquals(5, race.getHorseList().getList().size(), "Horse list should contain 5 horses.");
//  }
//
//  @Test
//  public void testAssignRandomHorsesFromDatabase_NotEnoughHorses()
//      throws SQLException
//  {
//    // Clear the list and simulate a situation where not enough horses are available
//    HorseRepositoryImpl.getInstance().delete(horse1);
//    System.out.println(HorseRepositoryImpl.getInstance().readAll());
//
//    // Try to assign horses and catch the exception
//    assertThrows(IllegalArgumentException.class, () -> {
//      race.assignRandomHorsesFromDatabase();
//    }, "Should throw an IllegalArgumentException if not enough horses are available in the database.");
//  }
//
//
//  @Test
//  public void testGetRaceTrack() {
//    // Test if the race track is properly set
//    assertEquals(raceTrack, race.getRaceTrack(), "Race track should match the track set during initialization.");
//  }
//
//
//  @Test
//  public void testGetHorseList() {
//    assertNotNull(race.getHorseList(), "Horse list should not be null.");
//  }
//
//  @Test
//  public void testGetFinalPositionlist() {
//    // Test if the final position list is initially empty
//    assertNotNull(race.getFinalPositionlist(), "Final position list should not be null.");
//    assertEquals(0, race.getFinalPositionlist().getList().size(), "Final position list should be empty initially.");
//  }
//}
