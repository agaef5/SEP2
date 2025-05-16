package client.networking.race;

import shared.race.CreateRaceRequest;
import shared.race.GetRaceListRequest;
import shared.race.GetRaceTracksRequest;

/**
 * Interface for interacting with race-related requests in the system.
 * Provides methods for retrieving races, creating races, and getting race tracks.
 */
public interface RaceClient {

   /**
    * Sends a request to retrieve all races without any specific criteria.
    * This method fetches the complete list of races from the server.
    */
   void getRaceList();

   /**
    * Sends a request to create a new race using the provided {@link CreateRaceRequest}.
    * After successful creation, the new race will be added to the race queue.
    *
    * @param createRaceRequest the request containing the details for the new race
    */
   void createRace(CreateRaceRequest createRaceRequest);

   /**
    * Sends a request to retrieve a list of available race tracks based on the provided {@link GetRaceTracksRequest}.
    * These race tracks can be used when creating new races.
    *
    * @param getRaceTracksRequest the request containing the criteria for retrieving race tracks
    */
   void getRaceTracks(GetRaceTracksRequest getRaceTracksRequest);
}