package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import server.model.Horse;
import server.services.horseList.HorseListServiceImpl;
import server.services.horseList.HorseListService;
import shared.*;

public class HorseHandler extends BaseRequestHandler
{
  private final HorseListService horseListService;
  private final Gson gson = new Gson();

  public HorseHandler(){
    this.horseListService = new HorseListServiceImpl();
  }

  @Override public Object safeHandle(String action, JsonElement payload)
  {
      switch (action)
      {
        case "getHorseList" ->
        {
          HorseListRequest request = parsePayload(payload, HorseListRequest.class);
          return handleHorseListRequest(request);
        }
        case "getHorse" ->
        {
          HorseRequest request = parsePayload(payload, HorseRequest.class);
          return handleGetHorseRequest(request);
        }
        case "createHorse" ->
        {
          CreateHorseRequest request = parsePayload(payload, CreateHorseRequest.class);
          return createHorserRequest(request);
        }
        case "updateHorse" ->
        {
          Horse horseToUpdate = parsePayload(payload, Horse.class);
          return handleUpdateHorse(horseToUpdate);
        }
        case "deleteHorse" -> {
          Horse horseToRemove = parsePayload(payload, Horse.class);
          return handleRemoveHorse(horseToRemove);
        }
        default ->
            throw new IllegalArgumentException("Invalid action: " + action);
      }
  }

  private HorseListResponse handleHorseListRequest(
      HorseListRequest horseListRequest){
    return horseListService.getHorseList();
  }

  private HorseResponse handleGetHorseRequest(HorseRequest horseRequest){
    return horseListService.getHorse(horseRequest.id());
  }

private Horse handleUpdateHorse(Horse horse){
    return horseListService.updateHorse(horse);
}

private String handleRemoveHorse(Horse horse){
    horseListService.removeHorse(horse);
    return "success";
}

  private CreateHorseResponse createHorserRequest(
      CreateHorseRequest createHorseRequest){
    Horse createdHorse = horseListService.createHorse(
        createHorseRequest.name(), createHorseRequest.speedMin(), createHorseRequest.speedMax() );
    return new CreateHorseResponse(createdHorse);
  }
}
