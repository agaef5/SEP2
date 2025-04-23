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
  public String getType();
  public String getMisc();
  void setMisc(String s);
  void setSpeedMax(int i);
  void setSpeedMin(int i);
  void setName(String s);
  void setType(String s);
}
