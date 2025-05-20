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

public class RaceListenerImpl implements RaceListener {
    private RaceListenerImpl() { }

    private static volatile RaceListenerImpl instance;
    /**
     * Double-checked locking for a thread-safe singleton
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

    @Override
    public void bettingOpen(Race race) {
        BettingOpenUpdate payload = new BettingOpenUpdate(race.getName());
        Server.broadcast("bettingOpen", payload);

    }

    @Override
    public void onHorseFinished(Horse  horse, int position)
    {
        HorseDTO horseDTO = DTOMapper.horseToDTO(horse);
        OnHorseFinished payload = new OnHorseFinished(horseDTO,position);
        Server.broadcast("onHorseFinished",payload);
    }

    @Override
    public void onRaceStarted(Race race)
    {
        OnRaceStarted payload = new OnRaceStarted(race.getName());
        Server.broadcast("onRaceStarted",payload);
        System.out.println("on race started sent");
    }

    @Override
    public void onRaceFinished(Race race, HorseList finalPositions)
    {
        List<HorseDTO> finalPositionsDTO = DTOMapper.horseListToDTO(finalPositions);
        OnRaceFinished payload = new OnRaceFinished(race.getName(),finalPositionsDTO);
        Server.broadcast("onRaceFinished",payload);
    }
}
