package server.util;

import server.model.*;
import shared.DTO.*;

import java.util.ArrayList;
import java.util.List;

public class DTOMapper
{
    //-----------To DTO-----------//
    public static UserDTO UserToDTO(User user){
        return new UserDTO(user.getUsername(), user.getEmail(), user.getPassword(), user.isAdmin(), user.getBalance().getAmount());
    }

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
                raceTrackToDTO(race.getRaceTrack())
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
    public static User userPlayerFromDTO(UserDTO userDTo){
        return new User(userDTo.username(), userDTo.email(),
                    userDTo.password(), userDTo.isAdmin(), userDTo.balance());
    }

    public static Horse horseFromDTO(HorseDTO dto) {
        return new Horse(dto.id(), dto.name(), dto.speedMin(), dto.speedMax());
    }

    public static RaceTrack raceTrackfromDTO(RaceTrackDTO dto) {
        return new RaceTrack(dto.name(), dto.length(), dto.location());
    }
}




