package shared;

public record Request(String handler, String action, Object payload)
{
}
// handler: auth
//action : login, register