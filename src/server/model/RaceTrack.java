package server.model;

import java.util.Objects;

public class RaceTrack
{
  private String name;
  private int length;
  private String location;

  public RaceTrack(String name,int length,String location)
  {
    this.name=name;
    this.length=length;
    this.location=location;
  }

  public String getName()
  {
    return name;
  }

  public int getLength()
  {
    return length;
  }

  public String getLocation()
  {
    return location;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    RaceTrack raceTrack = (RaceTrack) o;
    return length == raceTrack.length && Objects.equals(name, raceTrack.name)
        && Objects.equals(location, raceTrack.location);
  }

  @Override public String toString()
  {
    return "RaceTrack{" + "name='" + name + '\'' + ", length=" + length
        + ", location='" + location + '\'' + '}';
  }
}
