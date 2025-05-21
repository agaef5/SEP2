package server.networking.listener;

import server.model.*;
import server.networking.Server;
import server.networking.socketHandling.ClientHandler;
import server.util.DTOMapper;
import shared.DTO.HorseDTO;
import shared.updates.BettingOpenUpdate;
import shared.updates.OnHorseFinished;
import shared.updates.OnRaceFinished;
import shared.updates.OnRaceStarted;

import java.util.List;

/**
 * Singleton implementation of the {@link RaceListener} interface.
 * Responsible for handling race-related events and broadcasting them to all clients.
 */
public class RaceListenerImpl implements RaceListener {

    // Private constructor to enforce singleton pattern
    private RaceListenerImpl() { }

    // Volatile singleton instance for thread-safe access
    private static volatile RaceListenerImpl instance;

    /**
     * Returns the singleton instance of {@code RaceListenerImpl}.
     * Uses double-checked locking for thread safety and performance.
     *
     * @return the singleton instance
     */
    public static RaceListenerImpl getInstance() {
        if (instance == null) {
            synchronized (RaceListenerImpl.class) {
                if (instance == null) {
                    instance = new RaceListenerImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Called when betting opens for a race.
     * Broadcasts a {@link BettingOpenUpdate} to all connected clients.
     *
     * @param race the race for which betting has opened
     */
    @Override
    public void bettingOpen(Race race) {
        BettingOpenUpdate payload = new BettingOpenUpdate(race.getName());
        Server.broadcast("bettingOpen", payload);

    }


    /**
     * Called when a horse finishes the race.
     * Broadcasts an {@link OnHorseFinished} update to all clients with the horse and its position.
     *
     * @param horse    the horse that finished
     * @param position the position the horse finished in
     */
    @Override
    public void onHorseFinished(Horse  horse, int position)
    {
        HorseDTO horseDTO = DTOMapper.horseToDTO(horse);
        OnHorseFinished payload = new OnHorseFinished(horseDTO,position);
        Server.broadcast("onHorseFinished",payload);
    }

    /**
     * Called when the race starts.
     * Broadcasts an {@link OnRaceStarted} update to all clients.
     *
     * @param race the race that has started
     */
    @Override
    public void onRaceStarted(Race race)
    {
        OnRaceStarted payload = new OnRaceStarted(race.getName());
        Server.broadcast("onRaceStarted",payload);
        System.out.println("on race started sent");
    }

    /**
     * Called when the race finishes.
     * Broadcasts an {@link OnRaceFinished} update with the final order of horses.
     *
     * @param race           the race that finished
     * @param finalPositions the list of horses in their final order
     */
    @Override
    public void onRaceFinished(Race race, HorseList finalPositions)
    {
        List<HorseDTO> finalPositionsDTO = DTOMapper.horseListToDTO(finalPositions);
        OnRaceFinished payload = new OnRaceFinished(race.getName(),finalPositionsDTO);
        Server.broadcast("onRaceFinished",payload);
    }
}
