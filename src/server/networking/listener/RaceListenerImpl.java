package server.networking.listener;

import server.model.Horse;
import server.model.HorseList;
import server.model.Race;
import server.model.RaceListener;
import server.networking.Server;
import server.networking.socketHandling.ClientHandler;
import server.util.DTOMapper;
import shared.DTO.HorseDTO;
import shared.updates.OnHorseFinished;
import shared.updates.OnRaceFinished;
import shared.updates.OnRaceStarted;

import java.util.List;

public class RaceListenerImpl implements RaceListener {


    @Override
    public void bettingOpen(Race race) {

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

    }

    @Override
    public void onRaceFinished(Race race, HorseList finalPositions)
    {
        List<HorseDTO> finalPositionsDTO = DTOMapper.horseListToDTO(finalPositions);
        OnRaceFinished payload = new OnRaceFinished(race.getName(),finalPositionsDTO);
        Server.broadcast("onRaceFinished",payload);
    }
}
