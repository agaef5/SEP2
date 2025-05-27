package server.util;

import server.model.*;
import shared.DTO.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for converting domain model objects to their corresponding DTOs.
 * Helps isolate the server-side logic from data sent to the client.
 */
public class DTOMapper {

    //-----------To DTO-----------//

    /**
     * Converts a {@link User} object to a {@link UserDTO}.
     *
     * @param user the user to convert
     * @return the corresponding UserDTO
     */
    public static UserDTO UserToDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isAdmin(),
                user.getBalance().getAmount()
        );
    }

    /**
     * Converts a {@link Horse} object to a {@link HorseDTO}.
     *
     * @param horse the horse to convert
     * @return the corresponding HorseDTO
     */
    public static HorseDTO horseToDTO(Horse horse) {
        return new HorseDTO(
                horse.getId(),
                horse.getName(),
                horse.getSpeedMin(),
                horse.getSpeedMax()
        );
    }

    /**
     * Converts a {@link HorseList} (list of Horse domain objects)
     * to a list of {@link HorseDTO}s.
     *
     * @param horseList the list of horses to convert
     * @return a list of HorseDTOs
     */
    public static List<HorseDTO> horseListToDTO(HorseList horseList) {
        List<HorseDTO> finalPositionDTOs = new ArrayList<>();
        for (Horse horse : horseList.getList()) {
            finalPositionDTOs.add(horseToDTO(horse));
        }
        return finalPositionDTOs;
    }

    /**
     * Converts a {@link Bet} object to a {@link BetResponseDTO}.
     *
     * @param bet the bet to convert
     * @return the corresponding BetResponseDTO
     */
    public static BetResponseDTO betToResponseDTO(Bet bet) {
        return new BetResponseDTO(
                bet.getRace().getName(),
                horseToDTO(bet.getHorse()),
                bet.getUser().getUsername(),
                bet.getBetAmount()
        );
    }
}
