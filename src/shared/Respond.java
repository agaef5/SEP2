package shared;

public record Respond(String type, Object payload)
{
}
// type : login_respond, register_respond, broadcast, error
// payload : LoginRespond,RegisterRespond, error
