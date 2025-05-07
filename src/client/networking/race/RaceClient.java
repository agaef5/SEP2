package client.networking.race;

import shared.CreateRaceRequest;
import shared.GetRaceListRequest;
import shared.GetRaceTracksRequest;

public interface RaceClient
{
   void getRaces(GetRaceListRequest getRaceListRequest);
   void createRace(CreateRaceRequest createRaceRequest);
   void getRaceTracks(GetRaceTracksRequest getRaceTracksRequest);

}
