package server.model;

import java.util.Objects;

public class Horse implements Racer
{

  private static int nextId = 1;

  private int id;
  private String name;
  private int speedMin;
  private int speedMax;
  private int position;

  public Horse (String name,int speedMin, int speedMax)
  {
    this.id = nextId++;
    this.name = name;
    this.speedMin=speedMin;
    this.speedMax=speedMax;
    this.position=0;
  }

  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public int getSpeedMin()
  {
    return speedMin;
  }

  public int getSpeedMax()
  {
    return speedMax;
  }

  public int getPosition()
  {
    return position;
  }

  public void move()
  {
    int step = (int)(Math.random() * (speedMax - speedMin + 1)) + speedMin;
    position += step;
  }

  public void reset()
  {
    position=0;
  }

  @Override public String toString()
  {
    return "Horse{" + "id=" + id + ", name='" + name + '\'' + ", speedMin="
        + speedMin + ", speedMax=" + speedMax + ", position=" + position + '}';
  }

  @Override public boolean equals(Object o)
  {
    if (o == null || getClass() != o.getClass())
      return false;
    Horse horse = (Horse) o;
    return id == horse.id && speedMin == horse.speedMin
        && speedMax == horse.speedMax && position == horse.position
        && Objects.equals(name, horse.name);
  }

}
