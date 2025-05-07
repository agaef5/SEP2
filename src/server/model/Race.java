package server.model;

import server.persistence.horses.HorseRepositoryImpl;

import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Timestamp;
import java.util.List;

public class Race implements Runnable
{
  private String name;
  private RaceState status;
  private Timestamp dateTime;
  private HorseList horseList;
  private HorseList finalpositionlist;
  private List<RaceListener> listeners = new ArrayList<>();
  private RaceTrack raceTrack;

  public Race(String name,int raceCapacity,RaceTrack raceTrack) throws SQLException
  {
    this.raceTrack=raceTrack;
    this.name=name;
    this.dateTime =null;
    this.horseList = new HorseList(raceCapacity);
    this.finalpositionlist = new HorseList(raceCapacity);
    this.status= RaceState.NOT_STARTED;
    assignRandomHorsesFromDatabase();
  }

  public String getName()
  {
    return name;
  }

  public RaceState getStatus()
  {
    return status;
  }

  public Timestamp getDateTime()
  {
    return dateTime;
  }

  public HorseList getHorseList()
  {
    return horseList;
  }

  public HorseList getFinalPositionlist()
  {
    return finalpositionlist;
  }

  // Listener registration
  public void addListener(RaceListener listener) {
    listeners.add(listener);
  }

  public void assignRandomHorsesFromDatabase() throws //this method will get random set of horses from database
      SQLException
  {
    ArrayList<Horse> allHorses = HorseRepositoryImpl.getInstance().readAll();

    if (allHorses.size() < horseList.getCapacity()) {
      throw new IllegalArgumentException("Not enough horses available in database to start the race.");
    }

    Collections.shuffle(allHorses);

    for (int i = 0; i < horseList.getCapacity(); i++) {
      horseList.addToList(allHorses.get(i));
    }
  }

public void updateListenersOnRaceStarted() // notify Listeners about starting of an race
{
  for (RaceListener listener : listeners) {
    listener.onRaceStarted(name);
  }
}




  @Override public void run()//TODO Finish late the run() method
  {
    try {
      dateTime = Timestamp.valueOf(LocalDateTime.now());
      // Wait until the scheduled start time
        Thread.sleep(100000);
    } catch (InterruptedException e) {
      e.printStackTrace();
      return;
    }
    status=RaceState.IN_PROGRESS;
    System.out.println("Race "+name+" Started!");
    updateListenersOnRaceStarted(); // update listeners that the race started

  }
}
