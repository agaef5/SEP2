package server.model;

public class Admin implements User
{
  private String username;
  private String email;
  private String password;

  @Override public String getUsername()
  {
    return username;
  }

  @Override public void setUsername(String username)
  {
    this.username = username;
  }

  @Override public String getEmail()
  {
    return email;
  }

  @Override public void setEmail(String email)
  {
    this.email = email;
  }

  public String getPassword(){
    return password;
  }

  public void setPassword(String password){
    this.password = password;
  }
}
