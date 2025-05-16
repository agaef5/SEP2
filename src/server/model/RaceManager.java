package server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * {@code RaceManager} is a singleton class that manages the execution of races.
 * It holds a queue of races and ensures that each race is executed sequentially in a separate thread.
 * <p>
 * The class implements the {@link Runnable} interface, allowing it to be run in a separate thread.
 * This ensures that race executions happen asynchronously without blocking other processes.
 * </p>
 */
public class RaceManager implements Runnable {

  /**
   * The singleton instance of {@code RaceManager}.
   */
  private static RaceManager instance;

  /**
   * A blocking queue to hold races awaiting execution.
   */
  private final BlockingQueue<Race> raceQueue = new LinkedBlockingQueue<>();

  /**
   * Private constructor to enforce the singleton pattern.
   */
  private RaceManager() {}

  /**
   * Returns the singleton instance of {@code RaceManager}.
   * If the instance does not exist, it is created and started in a new thread.
   *
   * @return The singleton instance of {@code RaceManager}.
   */
  public static synchronized RaceManager getInstance() {
    if (instance == null) {
      instance = new RaceManager();
      // If we use a start, don't we also need a stop?
      new Thread(instance).start();
    }
    return instance;
  }

  /**
   * Adds a new race to the queue for execution.
   *
   * @param race The race to be added to the queue.
   */
  public void addRace(Race race) {
    raceQueue.add(race);
  }

  /**
   * Returns a list of all races in the queue. The list is a snapshot of the current state of the race queue.
   *
   * @return A list containing all races in the queue.
   */
  public List<Race> getAllRaces() {
    return new ArrayList<>(raceQueue); // creates a snapshot
  }

  /**
   * Continuously takes races from the queue and executes them.
   * This method is run in a separate thread and processes each race sequentially.
   */
  @Override
  public synchronized void run() {
    while (true) {
      try {
        Race race;

        // Wait until there's something in the queue
        while ((race = raceQueue.peek()) == null) {
          Thread.sleep(3000); // small delay to avoid busy waiting
        }
        race.run();
        raceQueue.take();   // Now remove it from the queue
               // Run the race (still in the queue)

        // TODO: Add race to the database

      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

}
