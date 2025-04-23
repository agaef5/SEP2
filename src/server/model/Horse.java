package server.model;

import java.util.Objects;

public class Horse implements Racer {
  private static int nextId = 1;

  private int id;
  private String name;
  private int speedMin;
  private int speedMax;
  private int position;
  private String misc;
  private String type;

  public Horse(int id, String name, int speedMin, int speedMax, String misc) {
    this.id = id;
    this.name = name;
    this.speedMin = speedMin;
    this.speedMax = speedMax;
    this.misc = misc;
    this.type = "Horse";
    this.position = 0;
  }

  @Override public int getId() { return id; }

  @Override public String getName() { return name; }

  @Override public int getSpeedMin() { return speedMin; }

  @Override public int getSpeedMax() { return speedMax; }

  @Override public int getPosition() { return position; }

  @Override public String getType() { return type; }

  @Override public String getMisc() { return misc; }

  @Override public void setMisc(String s) { this.misc = s; }

  @Override public void setSpeedMax(int i) { this.speedMax = i; }

  @Override public void setSpeedMin(int i) { this.speedMin = i; }

  @Override public void setName(String s) { this.name = s; }

  @Override public void setType(String s) { this.type = s; }

  @Override public void move() {
    int step = (int) (Math.random() * (speedMax - speedMin + 1)) + speedMin;
    position += step;
  }

  @Override public void reset() {
    position = 0;
  }

  @Override public String toString() {
    return "Horse{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", speedMin=" + speedMin +
        ", speedMax=" + speedMax +
        ", position=" + position +
        '}';
  }

  @Override public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Horse horse = (Horse) o;
    return id == horse.id &&
        speedMin == horse.speedMin &&
        speedMax == horse.speedMax &&
        position == horse.position &&
        Objects.equals(name, horse.name);
  }
}
