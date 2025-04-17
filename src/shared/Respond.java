package shared;

import java.io.Serializable;

public record Respond(String type, Object payload)implements Serializable
{
}
// type : login_respond, register_respond, broadcast, error
// payload : LoginRespond,RegisterRespond, error
