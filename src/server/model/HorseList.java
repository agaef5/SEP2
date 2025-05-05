package server.model;

import java.util.ArrayList;

public class HorseList
{
  private final ArrayList<Horse> horses;
  private final Integer capacity;

  public HorseList()
  {
    this.horses = new ArrayList<Horse>();
    this.capacity=null;
  }

  public HorseList(int capacity)
  {
    this.horses = new ArrayList<>();
    this.capacity=capacity;
  }

  public ArrayList<Horse> getList()
  {
    return horses;
  }

  public Horse getHorseById(int id)
  {
    for (int i = 0; i< horses.size(); i++)
    {
      if (id == horses.get(i).getId())
      {
        return horses.get(i);
      }
    }
    return null; // later return exception maybe (depends how we want to handle this)
  }

  public Horse getHorseByName(String name)
  {
    for (int i = 0; i< horses.size(); i++)
    {
      if (name.equals(horses.get(i).getName()))
      {
        return horses.get(i);
      }
    }
    return null;
  }

  public boolean addToList(Horse horse)
  {
    if (capacity!=null && horses.size()>=capacity)
    {
      return false;
    }
    return horses.add(horse);
  }

  public void removeFromList(Horse horse)
  {
    horses.remove(horse);
  }

  public void removeFromListById (int id)
  {
    for (int i = 0; i< horses.size(); i++)
    {
      if (id == horses.get(i).getId())
      {
        horses.remove(i);
        break;
      }
    }
  }

  public Integer getCapacity()
  {
    return capacity;
  }


}
