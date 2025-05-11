package server.networking.listener;

import server.model.Horse;
import server.model.HorseList;
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
    public void onHorseFinished(Horse  horse, int position)
    {
        HorseDTO horseDTO = DTOMapper.horseToDTO(horse);
        OnHorseFinished payload = new OnHorseFinished(horseDTO,position);
        Server.broadcast("onHorseFinished",payload);
    }

    @Override
    public void onRaceStarted(String raceName)
    {
        OnRaceStarted payload = new OnRaceStarted(raceName);
        Server.broadcast("onRaceStarted",payload);

    }

    @Override
    public void onRaceFinished(String raceName, HorseList finalPositions)
    {
        List<HorseDTO> finalPositionsDTO = DTOMapper.horseListToDTO(finalPositions);
        OnRaceFinished payload = new OnRaceFinished(raceName,finalPositionsDTO);
        Server.broadcast("onRaceFinished",payload);

    }
}
