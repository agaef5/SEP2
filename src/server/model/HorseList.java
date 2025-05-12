package server.model;

import java.util.ArrayList;

/**
 * The {@code HorseList} class manages a collection of {@link Horse} objects.
 * It allows adding, removing, and retrieving horses by their attributes (ID or name).
 * It also supports limiting the number of horses in the list based on a specified capacity.
 */
public class HorseList
{
  private final ArrayList<Horse> horses;
  private final Integer capacity;

  /**
   * Constructs a {@code HorseList} with no capacity limit.
   */
  public HorseList()
  {
    this.horses = new ArrayList<Horse>();
    this.capacity = null;
  }

  /**
   * Constructs a {@code HorseList} with a specified capacity.
   *
   * @param capacity the maximum number of horses the list can hold
   */
  public HorseList(int capacity)
  {
    this.horses = new ArrayList<>();
    this.capacity = capacity;
  }

  /**
   * Returns the list of all horses in the {@code HorseList}.
   *
   * @return an {@code ArrayList} containing all the horses
   */
  public ArrayList<Horse> getList()
  {
    return horses;
  }

  /**
   * Retrieves a horse by its unique ID.
   *
   * @param id the ID of the horse to retrieve
   * @return the {@code Horse} object with the specified ID, or {@code null} if not found
   */
  public Horse getHorseById(int id)
  {
    for (int i = 0; i < horses.size(); i++)
    {
      if (id == horses.get(i).getId())
      {
        return horses.get(i);
      }
    }
    return null; // later return exception maybe (depends on the desired behavior)
  }

  /**
   * Retrieves a horse by its name.
   *
   * @param name the name of the horse to retrieve
   * @return the {@code Horse} object with the specified name, or {@code null} if not found
   */
  public Horse getHorseByName(String name)
  {
    for (int i = 0; i < horses.size(); i++)
    {
      if (name.equals(horses.get(i).getName()))
      {
        return horses.get(i);
      }
    }
    return null;
  }

  /**
   * Adds a horse to the list. If the list has a capacity limit, the horse will not be added
   * if the capacity is already reached.
   *
   * @param horse the horse to add to the list
   * @return {@code true} if the horse was successfully added, {@code false} otherwise
   */
  public boolean addToList(Horse horse)
  {
    if (capacity != null && horses.size() >= capacity)
    {
      return false; // Capacity reached, cannot add more horses
    }
    return horses.add(horse);
  }

  /**
   * Removes a horse from the list.
   *
   * @param horse the horse to remove from the list
   */
  public void removeFromList(Horse horse)
  {
    horses.remove(horse);
  }

  /**
   * Removes a horse from the list based on its ID.
   *
   * @param id the ID of the horse to remove
   */
  public void removeFromListById(int id)
  {
    for (int i = 0; i < horses.size(); i++)
    {
      if (id == horses.get(i).getId())
      {
        horses.remove(i);
        break;
      }
    }
  }

  /**
   * Returns the capacity of the horse list, or {@code null} if there is no capacity limit.
   *
   * @return the capacity of the horse list, or {@code null} if no limit is set
   */
  public Integer getCapacity()
  {
    return capacity;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("HorseList");
    if (capacity != null) {
      sb.append(" (Capacity: ").append(capacity).append(")");
    } else {
      sb.append(" (No capacity limit)");
    }
    sb.append("\nHorses:\n");

    if (horses.isEmpty()) {
      sb.append("  [No horses in the list]");
    } else {
      for (Horse horse : horses) {
        sb.append("  ").append(horse).append("\n");
      }
    }

    return sb.toString();
  }

}
