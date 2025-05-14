package shared.loginRegister;

import java.io.Serializable;

public record RegisterRequest(String username, String email, String password)implements
    Serializable
{

}

