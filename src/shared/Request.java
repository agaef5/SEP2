package shared;

import java.io.Serializable;

import com.google.gson.JsonElement;

public record Request(String handler, String action, JsonElement payload) implements  Serializable {}



// handler: auth
//action : login, register

//handler: racer
//action: getRacer, getRacerList