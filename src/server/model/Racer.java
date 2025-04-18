package server.model;

public interface Racer
{
  public void move();
  public void reset();
  public int getId();
  public String getName();
  public int getSpeedMin();
  public int getSpeedMax();
  public int getPosition();
}
