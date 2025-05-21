package server.model;

import java.util.Objects;

/**
 * Represents a race track where horse races take place.
 * Contains information about the track's name, length, and location.
 */
public class RaceTrack
{
  private String name;
  private int length;
  private String location;

  /**
   * Constructs a RaceTrack with the given name, length, and location.
   *
   * @param name     the name of the racetrack
   * @param length   the length of the racetrack (in meters)
   * @param location the location of the racetrack
   */
  public RaceTrack(String name,int length,String location)
  {
    this.name=name;
    this.length=length;
    this.location=location;
  }

  /**
   * Gets the name of the racetrack.
   *
   * @return the name of the racetrack
   */
  public String getName()
  {
    return name;
  }

  /**
   * Gets the length of the racetrack.
   *
   * @return the length of the racetrack in meters
   */
  public int getLength()
  {
    return length;
  }


  /**
   * Gets the location of the racetrack.
   *
   * @return the location of the racetrack
   */
  public String getLocation()
  {
    return location;
  }

  /**
   * Checks equality between this racetrack and another object.
   * Two RaceTrack objects are equal if they have the same name, length, and location.
   *
   * @param object the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override public boolean equals(Object object)
  {
    if (this == object)
      return true;
    if (object == null || getClass() != object.getClass())
      return false;
    RaceTrack raceTrack = (RaceTrack) object;
    return length == raceTrack.length && Objects.equals(name, raceTrack.name)
        && Objects.equals(location, raceTrack.location);
  }


  /**
   * Returns a string representation of the racetrack.
   *
   * @return a string with the racetrack's name, length, and location
   */
  @Override public String toString()
  {
    return "RaceTrack{" + "name='" + name + '\'' + ", length=" + length
        + ", location='" + location + '\'' + '}';
  }
}
