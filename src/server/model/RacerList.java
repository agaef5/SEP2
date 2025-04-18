package server.model;

import java.util.ArrayList;

public class RacerList
{
  private ArrayList<Racer> racers;

  public RacerList()
  {
    this.racers = new ArrayList<Racer>();
  }

  public ArrayList<Racer> getList()
  {
    return racers;
  }

  public Racer getRacerById(int id)
  {
    for (int i = 0;i<racers.size();i++)
    {
      if (id == racers.get(i).getId())
      {
        return racers.get(i);
      }
    }
    return null; // later return exception maybe (depends how we want to handle this)
  }

  public Racer getRacerByName(String name)
  {
    for (int i = 0;i<racers.size();i++)
    {
      if (name == racers.get(i).getName())
      {
        return racers.get(i);
      }
    }
    return null;
  }

  public void addToList(Racer racer)
  {
    racers.add(racer);
  }

  public void removeFromList(Racer racer)
  {
    racers.remove(racer);
  }

  public void removeFromListById (int id)
  {
    for (int i = 0;i<racers.size();i++)
    {
      if (id == racers.get(i).getId())
      {
        racers.remove(i);
        break;
      }
    }
  }


}
