package server.model;

import server.persistence.horses.HorseRepositoryImpl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Represents a horse race. Manages participants, start time, status, and notifies listeners
 * when the race begins.
 */
public class Race implements Runnable {
  private String name;
  private int raceCapacity;
  private RaceState status;
  private Timestamp dateTime;
  private HorseList horseList;
  private HorseList finalpositionlist;
  private List<RaceListener> listeners = new ArrayList<>();
  private RaceTrack raceTrack;

  /**
   * Constructs a new {@code Race}.
   *
   * @param name         the name of the race
   * @param raceTrack    the track on which the race is run
   * @throws SQLException if assigning horses from the DB fails
   */
  public Race(String name, Date dateTime, RaceTrack raceTrack) throws SQLException {
    this.raceTrack = raceTrack;
    this.name = name;
    this.dateTime = null;
    this.horseList = new HorseList(raceCapacity);
    this.finalpositionlist = new HorseList(raceCapacity);
    this.status = RaceState.NOT_STARTED;
    assignRandomHorsesFromDatabase();
  }

  /** @return the race's name */
  public String getName() { return name; }

  /** @return the race's current status */
  public RaceState getStatus() { return status; }

  /** @return the scheduled start time of the race */
  public Timestamp getDateTime() { return dateTime; }

  /** @return the list of participating horses */
  public HorseList getHorseList() { return horseList; }

  /** @return the list of horses in their final positions */
  public HorseList getFinalPositionlist() { return finalpositionlist; }

  /**
   * Registers a listener to be notified when the race starts.
   *
   * @param listener the listener to add
   */
  public void addListener(RaceListener listener) {
    listeners.add(listener);
  }

  /**
   * Assigns a random selection of horses from the database up to the race capacity.
   *
   * @throws SQLException              on DB errors
   * @throws IllegalArgumentException if there aren’t enough horses in the DB
   */
  public void assignRandomHorsesFromDatabase() throws SQLException {
    ArrayList<Horse> allHorses = HorseRepositoryImpl.getInstance().readAll();
    if (allHorses.size() < horseList.getCapacity()) {
      throw new IllegalArgumentException(
          "Not enough horses available in database to start the race.");
    }
    Collections.shuffle(allHorses);
    for (int i = 0; i < horseList.getCapacity(); i++) {
      horseList.addToList(allHorses.get(i));
    }
  }

  /** Notifies listeners that the race has started. */
  public void updateListenersOnRaceStarted() {
    for (RaceListener listener : listeners) {
      listener.onRaceStarted(name);
    }
  }

  /**
   * Runs the race in its own thread. Sets the start time, waits for the scheduled delay,
   * updates status, and notifies listeners.
   */
  @Override
  public void run() {
    try {
      dateTime = Timestamp.valueOf(LocalDateTime.now());
      // Wait until the scheduled start time
      Thread.sleep(100000); // Simulating the delay before race starts
    } catch (InterruptedException e) {
      e.printStackTrace();
      return;
    }
    status = RaceState.IN_PROGRESS;
    System.out.println("Race " + name + " Started!");
    updateListenersOnRaceStarted();

    // TODO: Finish the rest of the race logic (e.g., horse movement, race end conditions)
  }

  public RaceTrack getRaceTrack()
  {
    return raceTrack;
  }
}
