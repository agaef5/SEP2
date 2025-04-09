package shared;

public class RegisterRequest {
  private String username;
  private String password;
  private String repassword;
  private String email;

  // No-arg constructor for Gson
  public RegisterRequest() {}

  // Full constructor
  public RegisterRequest(String username, String password, String repassword, String email) {
    this.username = username;
    this.password = password;
    this.repassword = repassword;
    this.email = email;
  }

  // Getters
  public String getUsername() { return username; }
  public String getPassword() { return password; }
  public String getRepassword() { return repassword; }
  public String getEmail() { return email; }

  // Setters (optional, if you want to be extra Gson-friendly)
  public void setUsername(String username) { this.username = username; }
  public void setPassword(String password) { this.password = password; }
  public void setRepassword(String repassword) { this.repassword = repassword; }
  public void setEmail(String email) { this.email = email; }
}

