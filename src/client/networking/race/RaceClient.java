package client.networking.race;

import shared.CreateRaceRequest;

public interface RaceClient
{
   void getRace();
   void createRace(CreateRaceRequest createRaceRequest);

}
