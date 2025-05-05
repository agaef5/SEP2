package server.services.horseList;

import server.model.Horse;
import server.persistence.horses.HorseRepository;
import server.persistence.horses.HorseRepositoryImpl;
import server.validation.baseValidation.BaseVal;
import shared.HorseListResponse;
import shared.HorseResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HorseListServiceImpl implements HorseListService
{

  @Override public HorseListResponse getHorseList()
  {
    validateRacerType(racerType);
    try{

      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      ArrayList<Horse> horseList = new ArrayList<>(horseRepository.readAll());
      return new HorseListResponse(horseList);
    }catch (SQLException sqlException){
      System.err.println("Database error when fetching racer list: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch racer list", sqlException);
    }
  }

  @Override public HorseResponse getHorse(int id)
  {
    validateRacerType(racerType);
    if(id < 0) throw new IllegalArgumentException("Incorrect id");
    try{
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      Horse horse = horseRepository.readByID(id);
      return new HorseResponse(horseRepository.readByID(id));
    }catch (SQLException sqlException){
      System.err.println("Database error when fetching racer: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch racer", sqlException);
    }
  }

  @Override public Racer createRacer(String racerType, String racerName, int speedMin, int speedMax)
  {
//  Data validation
    if(BaseVal.validate(racerName) || BaseVal.validate(racerType)) throw new  IllegalArgumentException("Cannot create new racer. Arguments are empty.");
    if(speedMin >= speedMax) throw new  IllegalArgumentException("SpeedMin cannot be bigger than speedMax");
    validateRacerType(racerType);
    try
    {
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      return horseRepository.create(horseName, speedMin, speedMax);
    }
    catch (SQLException sqlException)
    {
      System.err.println("Database error when creating racer: " + sqlException.getMessage());
      throw new RuntimeException("Failed to create racer", sqlException);
    }
  }

  private void validateRacerType(String racerType){
    List<String> allowedTypes = List.of("horse");
    if (!allowedTypes.contains(racerType.toLowerCase()))
      throw new IllegalArgumentException("No such type of racer exists");
  }
}
