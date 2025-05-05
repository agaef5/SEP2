package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Racer;
import server.services.horseList.HorseListService;
import shared.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RacerHandlerTest
{

  private HorseHandler racerHandler;
  private HorseListService mockRacerListService;
  private Gson gson = new Gson();

  @BeforeEach void setUp()
  {
    mockRacerListService = mock(HorseListService.class);

    racerHandler = new HorseHandler()
    {
      {
        // Inject mock into the handler instead of real RacerListServiceImpl
//        REMEMBER to remove "private final" for tests
        this.racerListService = mockRacerListService;
      }
    };
  }

  @Test void testHandleGetRacer()
  {
    HorseRequest request = new HorseRequest("horse", 1);
    Racer mockRacer = mock(Racer.class);

    when(mockRacerListService.getRacer("horse", 1)).thenReturn(
        new HorseResponse("horse", mockRacer));

    JsonElement payload = JsonParser.parseString(gson.toJson(request));
    Object result = racerHandler.handle("getRacer", payload);

    assertInstanceOf(HorseResponse.class, result);
    verify(mockRacerListService).getRacer("horse", 1);
  }

  @Test void testHandleCreateRacer()
  {
    CreateHorseRequest request = new CreateHorseRequest("horse", "Lightning",
        10, 20);
    Racer mockRacer = mock(Racer.class);

    when(mockRacerListService.createRacer("horse", "Lightning", 10,
        20)).thenReturn(mockRacer);

    JsonElement payload = JsonParser.parseString(gson.toJson(request));
    Object result = racerHandler.handle("createRacer", payload);

    assertInstanceOf(CreateHorseResponse.class, result);
    verify(mockRacerListService).createRacer("horse", "Lightning", 10, 20);
  }
}