package shared.loginRegister;

import java.io.Serializable;

public record LoginRequest(String username, String password)implements
    Serializable
{
}
