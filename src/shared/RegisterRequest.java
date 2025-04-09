package shared;

public record RegisterRequest(String username, String password, String repassword, String email)
{
}
