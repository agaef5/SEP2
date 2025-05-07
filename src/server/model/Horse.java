package server.model;

import java.util.Objects;

public class Horse {
  private static int nextId = 1;

  private int id;
  private String name;
  private int speedMin;
  private int speedMax;
  private int position;
  private String type;
  private boolean inRace = false;

  public Horse(int id, String name, int speedMin, int speedMax)
  {
    this.id = id;
    this.name = name;
    this.speedMin = speedMin;
    this.speedMax = speedMax;
    this.type = "Horse";
    this.position = 0;
  }

  public int getId() { return id; }

  public String getName() { return name; }

  public void setName(String s) { this.name = s; }

  public int getSpeedMin() { return speedMin; }

  public void setSpeedMin(int i) { this.speedMin = i; }

  public int getSpeedMax() { return speedMax; }

  public void setSpeedMax(int i) { this.speedMax = i; }

  public int getPosition() { return position; }

  public String getType() { return type; }

  public void setType(String s) { this.type = s; }


  public void move() {
    int step = (int) (Math.random() * (speedMax - speedMin + 1)) + speedMin;
    position += step;
  }

  public void reset() {
    position = 0;
  }

  public String toString() {
    return "Horse{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", speedMin=" + speedMin +
        ", speedMax=" + speedMax +
        ", position=" + position +
        '}';
  }

  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Horse horse = (Horse) o;
    return id == horse.id &&
        speedMin == horse.speedMin &&
        speedMax == horse.speedMax &&
        position == horse.position &&
        Objects.equals(name, horse.name);
  }

  public boolean isInRace()
  {
    return inRace;
  }

  public void setInRace(boolean inRace)
  {
    this.inRace = inRace;
  }
}
