package server.model;

/**
 * The {@code User} interface represents a user in the system.
 * It includes methods for getting and setting the user's username, email, and password.
 */
public class User
{
 private String username;
 private String email;
 private String password;
 private boolean isAdmin;

 public User(String username, String email, String password, boolean isAdmin){
     this.username = username;
     this.email = email;
     this.password = password;
     this.isAdmin = isAdmin;
 }

 /**
  * Gets the username of the user.
  *
  * @return The username of the user.
  */
 public String getUsername() {
  return username;
 };

 /**
  * Sets the username of the user.
  *
  * @param username The new username of the user.
  */
 void setUsername(String username){
  this.username = username;
 };

 /**
  * Gets the email of the user.
  *
  * @return The email of the user.
  */
 public String getEmail(){
  return email;
 };

 /**
  * Sets the email of the user.
  *
  * @param email The new email of the user.
  */
 public void setEmail(String email){
  this.email = email;
 };

 /**
  * Gets the password of the user.
  *
  * @return The password of the user.
  */
 public String getPassword(){
  return password;
 };

 /**
  * Sets the password of the user.
  *
  * @param password The new password of the user.
  */
 public void setPassword(String password){
  this.password = password;
 };

 public boolean isAdmin(){
  return isAdmin;
 }
}
