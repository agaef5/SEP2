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
    try{

      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      ArrayList<Horse> horseList = new ArrayList<>(horseRepository.readAll());
      return new HorseListResponse(horseList);
    }catch (SQLException sqlException){
      System.err.println("Database error when fetching horse list: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch horse list", sqlException);
    }
  }

  @Override public HorseResponse getHorse(int id)
  {
    if(id < 0) throw new IllegalArgumentException("Incorrect id");
    try{
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      Horse horse = horseRepository.readByID(id);
      return new HorseResponse(horseRepository.readByID(id));
    }catch (SQLException sqlException){
      System.err.println("Database error when fetching horse: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch horse", sqlException);
    }
  }

  @Override public Horse createHorse(String horseName, int speedMin, int speedMax)
  {
//  Data validation
    if(BaseVal.validate(horseName)) throw new  IllegalArgumentException("Cannot create new horse. Arguments are empty.");
    if(speedMin >= speedMax) throw new  IllegalArgumentException("SpeedMin cannot be bigger than speedMax");
    try
    {
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      return horseRepository.create(horseName, speedMin, speedMax);
    }
    catch (SQLException sqlException)
    {
      System.err.println("Database error when creating horse: " + sqlException.getMessage());
      throw new RuntimeException("Failed to create horse", sqlException);
    }
  }

  @Override public Horse updateHorse(Horse horse)
  {
    if (horse == null)
      throw new IllegalArgumentException("No horse to update");
    try
    {
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      horseRepository.updateHorse(horse);
      return horseRepository.readByID(horse.getId());
    }
    catch (SQLException sqlException)
    {
      System.err.println(
          "Database error when fetching horse: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch horse", sqlException);
    }
  }

  @Override public String removeHorse(Horse horse)
  {
    if (horse == null)
      throw new IllegalArgumentException("No horse to update");
    try{
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      horseRepository.delete(horse);
      return "success";
    }catch (SQLException sqlException){
      System.err.println("Database error when fetching horse: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch horse", sqlException);
    }
  }

}
