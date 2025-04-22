package shared;

import java.io.Serializable;

public record Request(String handler, String action, Object payload)implements
    Serializable
{

}

// handler: auth
//action : login, register