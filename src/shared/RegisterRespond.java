package shared;

import java.io.Serializable;

public record RegisterRespond(String message, Object payload)implements
    Serializable
{
}
