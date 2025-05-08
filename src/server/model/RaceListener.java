package server.model;

/**
 * The {@code RaceListener} interface defines the methods that must be implemented by
 * any class that wants to listen for events during a race.
 * These events include race start, race finish, and individual horse finishes.
 * <p>
 * Classes implementing this interface will respond to changes in the race state
 * such as when a horse finishes or when the race has started or finished.
 */
public interface RaceListener {

  /**
   * This method is called when a horse finishes the race.
   *
   * @param horse The horse that finished.
   * @param position The finishing position of the horse.
   */
  void onHorseFinished(Horse horse, int position);

  /**
   * This method is called when the race starts.
   *
   * @param raceName The name of the race that has started.
   */
  void onRaceStarted(String raceName);

  /**
   * This method is called when the race finishes.
   *
   * @param raceName The name of the race that has finished.
   * @param finalPositions The list of horses with their final positions in the race.
   */
  void onRaceFinished(String raceName, HorseList finalPositions);

}
