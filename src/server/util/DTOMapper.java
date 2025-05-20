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

    public static List<HorseDTO> horseListToDTO(HorseList horseList)
    {
        List<HorseDTO> finalPositionDTOs = new ArrayList<>();
        for (Horse horse : horseList.getList()) {
            finalPositionDTOs.add(new HorseDTO(horse.getId(), horse.getName(), horse.getSpeedMin(), horse.getSpeedMax()));
        }
        return finalPositionDTOs;
    }

    //-----------From DTO-----------//
    public static BetResponseDTO betToResponseDTO(Bet bet) {
        return new BetResponseDTO(
                bet.getRace().getName(),
                horseToDTO(bet.getHorse()),  // Reuse your existing horseToDTO method
                bet.getUser().getUsername(),
                bet.getBetAmount()
        );
    }
}




