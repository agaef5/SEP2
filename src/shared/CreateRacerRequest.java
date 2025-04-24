package shared;

public record CreateRacerRequest(String racerType, String name, int speedMin, int speedMax)
{
}
