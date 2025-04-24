package server.services.racerList;

import server.model.Horse;
import server.model.Racer;
import server.persistence.racer.RacerRepository;
import server.persistence.racer.RacerRepositoryImpl;
import shared.RacerListResponse;
import shared.RacerResponse;

import java.sql.SQLException;
import java.util.ArrayList;

public class RacerListServiceImpl implements RacerListService
{
  @Override public RacerListResponse getRacerList(String racerType)
  {
    try{
      RacerRepository racerRepository = RacerRepositoryImpl.getInstance();
      ArrayList<Racer> racerList = new ArrayList<>(racerRepository.readAll(racerType));
      return new RacerListResponse(racerList);
    }catch (SQLException sqlException){
      sqlException.getMessage();
      return new RacerListResponse(null);
    }
  }

  @Override public RacerResponse getRacer(String type, int id)
  {
    try{
      RacerRepository racerRepository = RacerRepositoryImpl.getInstance();
      if(type.equals("horse")){
        Racer racer = racerRepository.readByID(type, id);
        return new RacerResponse(racer.getClass().getName(), racerRepository.readByID(type, id));
      }else {
        return new RacerResponse("", null);
      }
    }catch (SQLException sqlException){
      sqlException.getMessage();
      return new RacerResponse("", null);
    }
  }

  @Override public Racer createRacer(Racer racer)
  {
    // TODO maybe make this method better
    try
    {
      RacerRepository racerRepository = RacerRepositoryImpl.getInstance();
      return racerRepository.create("horse",racer.getName(),racer.getSpeedMin(),racer.getSpeedMax());

    }
    catch (SQLException sqlException)
    {
      sqlException.getMessage();
      return null;
    }
  }
}
