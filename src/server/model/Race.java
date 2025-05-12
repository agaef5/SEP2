package server.model;

import server.networking.Server;
import server.persistence.horses.HorseRepositoryImpl;
import server.persistence.raceRepository.RaceRepositoryImpl;
import shared.DTO.RaceState;

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

  /**
   * Notifies listeners that the race has started.
   */
  public void updateListenersOnRaceStarted() {
    for (RaceListener listener : listeners) {
      listener.onRaceStarted(name);
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
    Server.broadcast("horseMoveUpdate",positions);
  }

  private void notifyRaceFinished()
  {
    for (RaceListener listener : listeners)
    {
      listener.onRaceFinished(name,finalpositionlist);
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
      System.out.println("Betting window opened");
      Thread.sleep(10000);
      System.out.println("Betting window closed");
    } catch (InterruptedException e) {
      e.printStackTrace();
      return;
    }

    status = RaceState.IN_PROGRESS;
    System.out.println("Race " + name + " Started!");
    updateListenersOnRaceStarted();

    List<Horse> horses = horseList.getList();
    int trackLength = raceTrack.getLength();
    Set<Horse> finished = new HashSet<>();

    while (finished.size() < horses.size()) {
      int[] positions = new int[horses.size()];
      for (int i = 0; i < horses.size(); i++) {
        Horse horse = horses.get(i);
        if (!finished.contains(horse)) {
          horse.move();
        }
        positions[i] = horse.getPosition();
      }
      System.out.println("Horse positions: " + Arrays.toString(positions));
      broadcastHorsePositions(positions);

      for (int i = 0; i < horses.size(); i++) {
        Horse horse = horses.get(i);
        if (!finished.contains(horse) && horse.getPosition() >= trackLength) {
          finished.add(horse);
          finalpositionlist.addToList(horse);
          System.out.println("Horse " + horse.getName() + " finished in place " + finished.size());
          notifyHorseFinished(horse, finished.size());
        }
      }

      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

    status = RaceState.FINISHED;
    System.out.println("Race " + name + " finished");
    notifyRaceFinished();
    persistAndPrintResults();
  }

  /**
   * Persists the race, then prints the formatted final results with a winning flag.
   */
  private void persistAndPrintResults() {
   // try {


      System.out.println("Saving race " + name + "...");
    //  RaceRepositoryImpl.getInstance().save(this);
      System.out.println("Race " + name + " saved");
  //  } catch (SQLException e) {
   //   e.printStackTrace();
   // }
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


