package server.util;

import server.model.Horse;
import server.model.HorseList;
import server.model.Race;
import server.model.RaceTrack;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;

import java.util.ArrayList;
import java.util.List;

public class DTOMapper
{
    //-----------To DTO-----------//
    public static HorseDTO horseToDTO(Horse horse) {
        return new HorseDTO(horse.getId(), horse.getName(), horse.getSpeedMin(), horse.getSpeedMax());
    }

    public static RaceDTO raceToDTO(Race race) {
        // Convert RaceTrack and HorseList to appropriate DTOs
        List<HorseDTO> horseDTOs = new ArrayList<>();
        for (Horse horse : race.getHorseList().getList()) {
            horseDTOs.add(new HorseDTO(horse.getId(), horse.getName(), horse.getSpeedMin(), horse.getSpeedMax()));
        }
        List<HorseDTO> finalPositionDTOs = new ArrayList<>();
        for (Horse horse : race.getFinalPositionlist().getList()) {
            finalPositionDTOs.add(new HorseDTO(horse.getId(), horse.getName(), horse.getSpeedMin(), horse.getSpeedMax()));
        }
        return new RaceDTO(
                race.getName(),
                race.getDateTime(),
                horseDTOs,
                finalPositionDTOs,
                raceTrackToDTO(race.getRaceTrack()),
                race.getStatus()
        );
    }

    public static RaceTrackDTO raceTrackToDTO(RaceTrack track) {
        return new RaceTrackDTO(track.getName(), track.getLength(), track.getLocation());
    }

    public static List<HorseDTO> horseListToDTO(HorseList horseList)
    {
        List<HorseDTO> finalPositionDTOs = new ArrayList<>();
        for (Horse horse : horseList.getList()) {
            finalPositionDTOs.add(new HorseDTO(horse.getId(), horse.getName(), horse.getSpeedMin(), horse.getSpeedMax()));
        }
        return finalPositionDTOs;
    }


    //-----------From DTO-----------//


    public static Horse horseFromDTO(HorseDTO dto) {
        return new Horse(dto.id(), dto.name(), dto.speedMin(), dto.speedMax());
    }

    public static RaceTrack raceTrackfromDTO(RaceTrackDTO dto) {
        return new RaceTrack(dto.name(), dto.length(), dto.location());
    }
}




