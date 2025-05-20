package shared.DTO;

public record BetResponseDTO(String raceName, HorseDTO horseDTO, String username, int betAmount)
{
    // Static factory method to convert from a full BetDTO
    public static BetResponseDTO fromBetDTO(BetDTO betDTO)
    {
        return new BetResponseDTO(
                betDTO.raceDTO().name(),
                betDTO.horseDTO(),
                betDTO.userDTO().username(),
                betDTO.betAmount()
        );
    }
}