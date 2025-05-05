package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HorseTest
{
  private Horse horse;

  @BeforeEach
  void setUp() {
    horse = new Horse(1, "Lightning", 5, 10);
  }

  @Test
  void testConstructorAndGetters() {
    assertEquals("Lightning", horse.getName());
    assertEquals(5, horse.getSpeedMin());
    assertEquals(10, horse.getSpeedMax());
    assertEquals(0, horse.getPosition());
    assertTrue(horse.getId() > 0);
  }

  @Test
  void testReset() {
    horse.move(); // move first to change position
    assertTrue(horse.getPosition() > 0);
    horse.reset();
    assertEquals(0, horse.getPosition());
  }

  @Test
  void testMoveWithinSpeedRange() {
    for (int i = 0; i < 10; i++) {
      int oldPosition = horse.getPosition();
      horse.move();
      int newPosition = horse.getPosition();
      int step = newPosition - oldPosition;
      assertTrue(step >= 5 && step <= 10, "Step was: " + step);
    }
  }
}