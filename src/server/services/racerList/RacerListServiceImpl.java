package server.services.racerList;

import server.model.Horse;
import server.model.Racer;
import server.persistence.racer.RacerRepository;
import server.persistence.racer.RacerRepositoryImpl;
import server.validation.baseValidation.BaseVal;
import shared.RacerListResponse;
import shared.RacerResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RacerListServiceImpl implements RacerListService
{

  @Override public RacerListResponse getRacerList(String racerType)
  {
    validateRacerType(racerType);
    try{
      RacerRepository racerRepository = RacerRepositoryImpl.getInstance();
      ArrayList<Racer> racerList = new ArrayList<>(racerRepository.readAll(racerType));
      return new RacerListResponse(racerList);
    }catch (SQLException sqlException){
      System.err.println("Database error when fetching racer list: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch racer list", sqlException);
    }
  }

  @Override public RacerResponse getRacer(String racerType, int id)
  {
    validateRacerType(racerType);
    if(id < 0) throw new IllegalArgumentException("Incorrect id");
    try{
      RacerRepository racerRepository = RacerRepositoryImpl.getInstance();
      Racer racer = racerRepository.readByID(racerType, id);
      return new RacerResponse(racer.getClass().getName(), racerRepository.readByID(racerType, id));
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
      RacerRepository racerRepository = RacerRepositoryImpl.getInstance();
      return racerRepository.create(racerType.toLowerCase(),racerName, speedMin, speedMax);
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
