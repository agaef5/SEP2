package client.ui.util;

import com.google.gson.*;
import server.model.Horse;
import server.model.Racer;

import java.lang.reflect.Type;

public class RacerDeserializer implements JsonDeserializer<Racer>
{
  public Racer deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    // Check for "type" property
    if (!jsonObject.has("type")) {
      throw new JsonParseException("Missing 'type' field in Racer JSON.");
    }

    String racerType = jsonObject.get("type").getAsString();

    // Use type field to choose correct class
    switch (racerType) {
      case "Horse":
        return context.deserialize(jsonObject, Horse.class);
      // Add more types here as needed
      default:
        throw new JsonParseException("Unknown racer type: " + racerType);
    }
  }
}

