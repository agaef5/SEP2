package server.model;

import java.util.Objects;

/**
 * Represents a horse that participates in a race. Holds information about the horse's
 * identity, speed range, current position, and whether it is active in a race.
 */
public class Horse {

  private int id;
  private String name;
  private int speedMin;
  private int speedMax;
  private int position;
  private boolean inRace = false;

  /**
   * Constructs a new {@code Horse}.
   *
   * @param id       the unique identifier for the horse
   * @param name     the name of the horse
   * @param speedMin the minimum speed of the horse
   * @param speedMax the maximum speed of the horse
   */
  public Horse(int id, String name, int speedMin, int speedMax) {
    this.id = id;
    this.name = name;
    this.speedMin = speedMin;
    this.speedMax = speedMax;
    this.position = 0;
  }

  /** @return the horse's ID */
  public int getId() { return id; }

  /** @return the horse's name */
  public String getName() { return name; }

  /** @param s the new name to set */
  public void setName(String s) { this.name = s; }

  /** @return the horse's minimum speed */
  public int getSpeedMin() { return speedMin; }

  /** @param i the new minimum speed to set */
  public void setSpeedMin(int i) { this.speedMin = i; }

  /** @return the horse's maximum speed */
  public int getSpeedMax() { return speedMax; }

  /** @param i the new maximum speed to set */
  public void setSpeedMax(int i) { this.speedMax = i; }

  /** @return the current position of the horse in the race */
  public int getPosition() { return position; }

  /**
   * Moves the horse forward by a random step within its speed range.
   */
  public void move() {
    int step = (int) (Math.random() * (speedMax - speedMin + 1)) + speedMin;
    position += step;
  }

  /** Resets the horse's position back to the start (0). */
  public void reset() {
    position = 0;
  }

  /**
   * @return a string representation including id, name, speeds, and position
   */
  @Override
  public String toString() {
    return "Horse{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", speedMin=" + speedMin +
        ", speedMax=" + speedMax +
        ", position=" + position +
        '}';
  }

  /**
   * Two horses are equal if they share id, name, speed range, and position.
   */
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Horse horse = (Horse) o;
    return id == horse.id &&
        speedMin == horse.speedMin &&
        speedMax == horse.speedMax &&
        position == horse.position &&
        Objects.equals(name, horse.name);
  }
}
