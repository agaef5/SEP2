package client.ui.util;

import com.google.gson.*;
import server.model.Horse;


import java.lang.reflect.Type;

public class HorseDeserializer implements JsonDeserializer<Horse>
{
  public Horse deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
        return context.deserialize(jsonObject, Horse.class);
  }
}

