//package server.model;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RacerListTest
//{
//
//  private RacerList racerList;
//  private Horse horse1;
//  private Horse horse2;
//
//  @BeforeEach void setUp()
//  {
//    racerList = new RacerList();
//    horse1 = new Horse("Thunder", 4, 8);
//    horse2 = new Horse("Blaze", 5, 10);
//    racerList.addToList(horse1);
//    racerList.addToList(horse2);
//  }
//
//  @Test void testAddAndGetList()
//  {
//    ArrayList<Racer> list = racerList.getList();
//    assertEquals(2, list.size());
//    assertTrue(list.contains(horse1));
//    assertTrue(list.contains(horse2));
//  }
//
//  @Test void testGetRacerById()
//  {
//    Racer found = racerList.getRacerById(horse1.getId());
//    assertEquals(horse1, found);
//
//    Racer notFound = racerList.getRacerById(9999); // unlikely ID
//    assertNull(notFound);
//  }
//
//  @Test void testGetRacerByName()
//  {
//    Racer found = racerList.getRacerByName("Blaze");
//    assertEquals(horse2, found);
//
//    Racer notFound = racerList.getRacerByName("Unknown");
//    assertNull(notFound);
//  }
//
//  @Test void testRemoveFromListByObject()
//  {
//    racerList.removeFromList(horse1);
//    assertEquals(1, racerList.getList().size());
//    assertFalse(racerList.getList().contains(horse1));
//  }
//
//  @Test void testRemoveFromListById()
//  {
//    racerList.removeFromListById(horse2.getId());
//    assertEquals(1, racerList.getList().size());
//    assertFalse(racerList.getList().contains(horse2));
//  }
//}