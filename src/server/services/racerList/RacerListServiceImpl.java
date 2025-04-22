package server.services.racerList;

import server.model.Horse;
import server.persistence.racer.RacerRepository;
import server.persistence.racer.RacerRepositoryImpl;
import shared.HorseListResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class RacerListServiceImpl implements RacerListService
{
  @Override public HorseListResponse getHorsesList()
  {
    try{
      RacerRepository racerRepository = RacerRepositoryImpl.getInstance();
      ArrayList<Horse> horseList= new ArrayList<>(racerRepository.readAll());
      return new HorseListResponse(horseList);
    }catch (SQLException sqlException){
      sqlException.getMessage();
      return new HorseListResponse(null);
    }
  };
}
