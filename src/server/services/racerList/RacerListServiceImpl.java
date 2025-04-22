package server.services.racerList;

import server.model.Horse;
import server.model.Racer;
import server.persistence.racer.RacerRepository;
import server.persistence.racer.RacerRepositoryImpl;
import shared.HorseListResponse;
import shared.RacerResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class RacerListServiceImpl implements RacerListService
{
  @Override public HorseListResponse getRacerList()
  {
    try{
      RacerRepository racerRepository = RacerRepositoryImpl.getInstance();
      ArrayList<Horse> horseList= new ArrayList<>(racerRepository.readAll());
      return new HorseListResponse(horseList);
    }catch (SQLException sqlException){
      sqlException.getMessage();
      return new HorseListResponse(null);
    }
  }

  @Override public RacerResponse getRacer(String type, int id)
  {
    try{
      RacerRepository racerRepository = RacerRepositoryImpl.getInstance();
      if(type.equals("horse")){
        Racer racer = racerRepository.readByID(id);
        return new RacerResponse(racer.getClass().getName(), racerRepository.readByID(id));
      }else {
        return new RacerResponse("Error. No such racer type found.", null);
      }
    }catch (SQLException sqlException){
      sqlException.getMessage();
      return new RacerResponse("Error. Problems with SQL Server", null);
    }
  }
}
