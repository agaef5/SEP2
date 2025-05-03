package server.model;

import server.persistence.racer.RacerRepositoryImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Race implements Runnable
{
  private String raceName;
  private RaceState status;
  private Date startTime; // Dont know in which format we want to work on, So now I just put Date but feel free to change it
  private RacerList racerList;
  private RacerList finalPositionList;
  private List<RaceListener> listeners = new ArrayList<>();
  private RaceTrack raceTrack;

  public Race(String name, Date startTime,int raceCapacity) throws SQLException
  {
    this.raceName =name;
    this.startTime=startTime;
    this.racerList= new RacerList(raceCapacity);
    this.finalPositionList= new RacerList(raceCapacity);
    this.status= RaceState.NOT_STARTED;
    assignRandomRacersFromDatabase("horse");
  }

  public String getRaceName()
  {
    return raceName;
  }

  public RaceState getStatus()
  {
    return status;
  }

  public Date getStartTime()
  {
    return startTime;
  }

  public RacerList getRacerList()
  {
    return racerList;
  }

  public RacerList getFinalPositionList()
  {
    return finalPositionList;
  }

  // Listener registration
  public void addListener(RaceListener listener) {
    listeners.add(listener);
  }

  public void assignRandomRacersFromDatabase(String racerType) throws //this method will get random set of horses from database
      SQLException
  {
    ArrayList<Racer> allRacers = RacerRepositoryImpl.getInstance().readAll(racerType);

    if (allRacers.size() < racerList.getCapacity()) {
      throw new IllegalArgumentException("Not enough racers available in database to start the race.");
    }

    Collections.shuffle(allRacers);

    for (int i = 0; i < racerList.getCapacity(); i++) {
      racerList.addToList(allRacers.get(i));
    }
  }

public void updateListenersOnRaceStarted() // notify Listeners about starting of an race
{
  for (RaceListener listener : listeners) {
    listener.onRaceStarted(raceName);
  }
}




  @Override public void run()//TODO Finish late the run() method
  {
    try {
      // Wait until the scheduled start time
      while (new Date().before(startTime)) { //wait until the scheduled time of race
        Thread.sleep(1000);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      return;
    }
    status=RaceState.IN_PROGRESS;
    System.out.println("Race "+ raceName +" Started!");
    updateListenersOnRaceStarted(); // update listeners that the race started

  }
}
