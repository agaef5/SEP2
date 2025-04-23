package shared;

import java.io.Serializable;

public record RegisterRequest(String email, String username, String password, String repeat)implements
    Serializable
{

}

