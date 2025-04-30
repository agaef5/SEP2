package server.model;

import java.util.Objects;

public class RaceTrack
{
  private String name;
  private int lenght;
  private String location;

  public RaceTrack(String name,int lenght,String location)
  {
    this.name=name;
    this.lenght=lenght;
    this.location=location;
  }

  public String getName()
  {
    return name;
  }

  public int getLenght()
  {
    return lenght;
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
    return lenght == raceTrack.lenght && Objects.equals(name, raceTrack.name)
        && Objects.equals(location, raceTrack.location);
  }

  @Override public String toString()
  {
    return "RaceTrack{" + "name='" + name + '\'' + ", lenght=" + lenght
        + ", location='" + location + '\'' + '}';
  }
}
