package client.networking.race;

import shared.CreateRaceRequest;
import shared.GetRaceListRequest;
import shared.GetRaceTracksRequest;

/**
 * Interface for interacting with race-related requests in the system.
 * Provides methods for retrieving races, creating races, and getting race tracks.
 */
public interface RaceClient {

   /**
    * Sends a request to retrieve a list of races based on the provided {@link GetRaceListRequest}.
    *
    * @param getRaceListRequest the request containing the criteria for retrieving races
    */
   void getRaces(GetRaceListRequest getRaceListRequest);

   /**
    * Sends a request to create a new race using the provided {@link CreateRaceRequest}.
    *
    * @param createRaceRequest the request containing the details for the new race
    */
   void createRace(CreateRaceRequest createRaceRequest);

   /**
    * Sends a request to retrieve a list of available race tracks based on the provided {@link GetRaceTracksRequest}.
    *
    * @param getRaceTracksRequest the request containing the criteria for retrieving race tracks
    */
   void getRaceTracks(GetRaceTracksRequest getRaceTracksRequest);

}
