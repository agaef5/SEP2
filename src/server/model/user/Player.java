package server.model.user;

import server.model.balance.Balance;

public class Player implements User
{
  private Balance balance;
  private String username;
  private String email;
  private String password;

  public Player(String username, String email, String password){
    this.username = username;
    this.email = email;
    this.password = password;
    balance = new Balance(1000);
  }

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

  public Balance getBalance()
  {
    return balance;
  }

  public void setAdmin(){
//    create logic for making player an admin;
  }
}
