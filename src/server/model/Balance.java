package server.model;

/**
 * The {@code Balance} class represents a monetary balance, typically associated with a user or account.
 * It holds the amount of money in an integer value and provides methods to retrieve or modify this balance.
 * <p>
 * The class supports setting and getting the balance amount as an integer.
 */
public class Balance
{
  private int amount;

  /**
   * Constructs a {@code Balance} object with the specified initial amount.
   *
   * @param amount the initial amount of money to be set in the balance
   */
  public Balance(int amount){
    this.amount = amount;
  }

  /**
   * Returns the current amount of money in the balance.
   *
   * @return the current amount in the balance
   */
  public int getAmount(){
    return amount;
  }

  /**
   * Sets the amount of money in the balance.
   *
   * @param amount the new amount to set for the balance
   */
  public void setAmount(int amount)
  {
    this.amount = amount;
  }
}
