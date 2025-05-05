package server.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RaceManager implements Runnable
{
  private static RaceManager instance;
  private final BlockingQueue<Race> raceQueue = new LinkedBlockingQueue<>();

  private RaceManager(){}

  public static synchronized RaceManager getInstance()
  {
    if (instance ==null){
      instance = new RaceManager();
      new Thread(instance).start();
  }
    return instance;
  }

  public void addRace(Race race)
  {
    raceQueue.add(race);
  }

  @Override public void run()
  {
  while (true)
  {
    try{
      Race race = raceQueue.take();
      race.run();
      // TODO after the race is done add it to the database

    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      break;
    }
  }
  }
}
