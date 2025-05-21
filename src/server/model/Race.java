package server.model;

import client.ui.util.ErrorHandler;
import server.networking.Server;
import server.persistence.horses.HorseRepositoryImpl;
import server.persistence.raceRepository.RaceRepositoryImpl;
import shared.DTO.RaceState;
import shared.race.RaceUpdate;
import shared.updates.HorsePositionsUpdate;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Timestamp;
import java.util.stream.Collectors;

/**
 * Represents a horse race. Manages participants, start time, status, and notifies listeners
 * when the race begins.
 */
public class Race implements Runnable {
  private String name;
  private RaceState status;
  private Timestamp dateTime;
  private HorseList horseList;
  private HorseList finalpositionlist;
  private List<RaceListener> listeners = new ArrayList<>();
  private RaceTrack raceTrack;

  /**
   * Constructs a new {@code Race}.
   *
   * @param name      the name of the race
   * @param raceTrack the track on which the race is run
   * @throws SQLException if assigning horses from the DB fails
   */
  public Race(String name, RaceTrack raceTrack, Integer raceCapacity) throws SQLException {
    this.raceTrack = raceTrack;
    this.name = name;
    this.dateTime = null;
    this.horseList = new HorseList(raceCapacity);
    this.finalpositionlist = new HorseList(raceCapacity);
    this.status = RaceState.NOT_STARTED;
    assignRandomHorsesFromDatabase();
  }

  public Race(String name, Timestamp timestamp, HorseList finalpositionlist, RaceTrack raceTrack) {
    this.name = name;
    this.status = RaceState.FINISHED;
    this.dateTime = timestamp;
    this.horseList = finalpositionlist;
    this.finalpositionlist = finalpositionlist;
    this.raceTrack = raceTrack;
  }

  /**
   * @return the race's name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the race's current status
   */
  public RaceState getStatus() {
    return status;
  }

  public void setStatus(RaceState raceState) {
    this.status = raceState;
  }

  /**
   * @return the scheduled start time of the race
   */
  public Timestamp getDateTime() {
    return dateTime;
  }

  /**
   * @return the list of participating horses
   */
  public HorseList getHorseList() {
    return horseList;
  }

  /**
   * @return the list of horses in their final positions
   */
  public HorseList getFinalPositionlist() {
    return finalpositionlist;
  }

  /**
   * @return the race track on which is the race located
   */
  public RaceTrack getRaceTrack() {
    return raceTrack;
  }

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
   * @throws SQLException             on DB errors
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

  public void updateListenersOnBettingOpen(){
    for(RaceListener listener : listeners){
      listener.bettingOpen(this);
    }
  }

  /**
   * Notifies listeners that the race has started.
   */
  public void updateListenersOnRaceStarted() {
    for (RaceListener listener : listeners) {
      listener.onRaceStarted(this);
    }
  }



  private void notifyHorseFinished(Horse horse, int position)
  {
    for (RaceListener listener: listeners)
    {
      listener.onHorseFinished(horse,position);
    }
  }

  private void broadcastHorsePositions(int[] positions)
  {
    List<Integer> positionsList = Arrays.stream(positions).boxed().toList();
    HorsePositionsUpdate payload = new HorsePositionsUpdate(name, positionsList);
    Server.broadcast("horseMoveUpdate", payload);
  }

  private void notifyRaceFinished()
  {
    for (RaceListener listener : listeners)
    {
      listener.onRaceFinished(this,finalpositionlist);
    }
  }

  /**
   * Runs the race in its own thread. Sets the start time, waits for the scheduled delay,
   * updates status, and notifies listeners.
   */

  @Override
  public void run() {
    try {
      // Set race start time to now
      dateTime = Timestamp.valueOf(LocalDateTime.now());

      // Notify listeners that betting is now open
      updateListenersOnBettingOpen();
      System.out.println("Betting window opened");

      // Simulate a 60-second betting window
      Thread.sleep(60000);
      System.out.println("Betting window closed");

    } catch (InterruptedException e) {
      // Stop race if thread is interrupted
      e.printStackTrace();
      return;
    }

    // Set status to IN_PROGRESS and notify listeners
    status = RaceState.IN_PROGRESS;
    System.out.println("Race " + name + " Started!");
    updateListenersOnRaceStarted();

    // Initializing variables - to keep track of horses that crossed the finish line
    List<Horse> horses = horseList.getList();
    int trackLength = raceTrack.getLength();
    Set<Horse> finished = new HashSet<>();

    // Main loop: update horse positions until all finish
    while (finished.size() < horses.size()) {
      StringBuilder horsePositionsText = new StringBuilder();
      int[] positions = new int[horses.size()];

      for (int i = 0; i < horses.size(); i++) {
        Horse horse = horses.get(i);

        // Move horse if not finished yet
        if (!finished.contains(horse)) {
          horse.move();
        }

        // Collect positions for UI update
        horsePositionsText.append("\n").append(horse.getName()).append(": ").append(horse.getPosition());
        positions[i] = horse.getPosition();
      }

      // Print positions to console for logging
      System.out.println("\nHorse positions in race " + name + ": " + horsePositionsText);

      // Send updated positions to clients
      broadcastHorsePositions(positions);

      // Check if any horses have finished the race
      for (int i = 0; i < horses.size(); i++) {
        Horse horse = horses.get(i);

        if (!finished.contains(horse) && horse.getPosition() >= trackLength) {
          finished.add(horse); // Mark horse as finished
          finalpositionlist.addToList(horse); // Add to final placement

          System.out.println("Horse " + horse.getName() + " finished in place " + finished.size());

          // Notify listeners of this horse's finish
          notifyHorseFinished(horse, finished.size());
        }
      }

      try {
        // Delay next update for realism (2 seconds per "tick")
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        // Stop if thread is interrupted
        e.printStackTrace();
        break;
      }
    }

    // Race is finished
    status = RaceState.FINISHED;
    System.out.println("Race " + name + " finished");

    // Notify listeners and persist race results
    notifyRaceFinished();
    persistAndPrintResults();
  }

  /**
   * Persists the race, then prints the formatted final results with a winning flag.
   */
  private void persistAndPrintResults() {
    try {
      System.out.println("Saving race " + name + "...");
      RaceRepositoryImpl.getInstance().save(this);
      System.out.println("Race " + name + " saved");
    } catch (SQLException e) {
      ErrorHandler.handleError(e, getClass().getName());
    }
    printFinalResults();
  }

  /**
   * Prints the final standings, marking the winner with a chequered flag.
   */
  private void printFinalResults() {
    System.out.println("\nFinal Results for " + name + ":");
    List<Horse> results = finalpositionlist.getList();
    for (int i = 0; i < results.size(); i++) {
      Horse horse = results.get(i);
      String flag = (i == 0) ? "🏁 " : "";
      System.out.printf("%2d. %s%s%n", i + 1, flag, horse.getName());
    }
  }
}


