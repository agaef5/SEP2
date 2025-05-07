package server.model;

/**
 * Represents a player in the system, including login credentials and account balance.
 * Players begin with a default balance of 1000.
 */
public class Player implements User {
  private Balance balance;
  private String username;
  private String email;
  private String password;

  /**
   * Constructs a new {@code Player}.
   *
   * @param username the player's username
   * @param email    the player's email
   * @param password the player's password
   */
  public Player(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    balance = new Balance(1000);
  }

  /** @return the player's username */
  @Override public String getUsername() { return username; }

  /** @param username the new username to set */
  @Override public void setUsername(String username) { this.username = username; }

  /** @return the player's email */
  @Override public String getEmail() { return email; }

  /** @param email the new email to set */
  @Override public void setEmail(String email) { this.email = email; }

  /** @return the player's password */
  public String getPassword() { return password; }

  /** @param password the new password to set */
  public void setPassword(String password) { this.password = password; }

  /** @return the player's current balance */
  public Balance getBalance() { return balance; }

  /**
   * Upgrade this player to an admin role.
   * // TODO: create logic for making player an admin
   */
  public void setAdmin() {
    // TODO: create logic for making player an admin
  }
}
