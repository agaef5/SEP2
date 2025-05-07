package server.model;

/**
 * The {@code Admin} class implements the {@link User} interface and represents an admin user in the system.
 * An admin has a username, email, and password, and these fields can be accessed and modified via getter
 * and setter methods.
 * <p>
 * Admins typically have higher privileges compared to regular users, such as managing users or overseeing system operations.
 */
public class Admin implements User
{
  private String username;
  private String email;
  private String password;

  /**
   * Constructs an {@code Admin} object with the specified username, email, and password.
   *
   * @param username the username for the admin
   * @param email the email address of the admin
   * @param password the password for the admin account
   */
  public Admin(String username, String email, String password){
    this.username = username;
    this.email = email;
    this.password = password;
  }

  /**
   * Returns the username of the admin.
   *
   * @return the username of the admin
   */
  @Override public String getUsername()
  {
    return username;
  }

  /**
   * Sets the username of the admin.
   *
   * @param username the new username to be set
   */
  @Override public void setUsername(String username)
  {
    this.username = username;
  }

  /**
   * Returns the email address of the admin.
   *
   * @return the email address of the admin
   */
  @Override public String getEmail()
  {
    return email;
  }

  /**
   * Sets the email address of the admin.
   *
   * @param email the new email address to be set
   */
  @Override public void setEmail(String email)
  {
    this.email = email;
  }

  /**
   * Returns the password of the admin.
   *
   * @return the password of the admin
   */
  public String getPassword(){
    return password;
  }

  /**
   * Sets the password of the admin.
   *
   * @param password the new password to be set
   */
  public void setPassword(String password){
    this.password = password;
  }
}
