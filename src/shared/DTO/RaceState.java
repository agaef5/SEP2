package shared.DTO;

/**
 * {@code RaceState} is an enum representing the different states of a race.
 * The state indicates the current status of a race during its lifecycle.
 * <p>
 * The possible states are:
 * <ul>
 *   <li>{@code NOT_STARTED} - The race has not started yet.</li>
 *   <li>{@code IN_PROGRESS} - The race is currently in progress.</li>
 *   <li>{@code FINISHED} - The race has completed.</li>
 * </ul>
 * </p>
 */
public enum RaceState {
  /**
   * Indicates that the race has not started yet.
   */
  NOT_STARTED,

  /**
   * Indicates that the race is currently in progress.
   */
  IN_PROGRESS,

  /**
   * Indicates that the race has finished.
   */
  FINISHED
}
