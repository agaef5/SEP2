package shared;

import java.io.Serializable;

public record LoginRespond(String message, Object payload)implements
    Serializable
{
}
