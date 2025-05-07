package server.model;

/**
 * The {@code User} interface represents a user in the system.
 * It includes methods for getting and setting the user's username, email, and password.
 */
public interface User
{
 /**
  * Gets the username of the user.
  *
  * @return The username of the user.
  */
 String getUsername();

 /**
  * Sets the username of the user.
  *
  * @param username The new username of the user.
  */
 void setUsername(String username);

 /**
  * Gets the email of the user.
  *
  * @return The email of the user.
  */
 String getEmail();

 /**
  * Sets the email of the user.
  *
  * @param email The new email of the user.
  */
 void setEmail(String email);

 /**
  * Gets the password of the user.
  *
  * @return The password of the user.
  */
 String getPassword();

 /**
  * Sets the password of the user.
  *
  * @param password The new password of the user.
  */
 void setPassword(String password);
}
