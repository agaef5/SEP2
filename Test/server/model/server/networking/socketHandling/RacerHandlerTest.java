package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Racer;
import server.services.racerList.RacerListService;
import server.services.racerList.RacerListServiceImpl;
import shared.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RacerHandlerTest
{

  private RacerHandler racerHandler;
  private RacerListService mockRacerListService;
  private Gson gson = new Gson();

  @BeforeEach void setUp()
  {
    mockRacerListService = mock(RacerListService.class);

    racerHandler = new RacerHandler()
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
    RacerRequest request = new RacerRequest("horse", 1);
    Racer mockRacer = mock(Racer.class);

    when(mockRacerListService.getRacer("horse", 1)).thenReturn(
        new RacerResponse("horse", mockRacer));

    JsonElement payload = JsonParser.parseString(gson.toJson(request));
    Object result = racerHandler.handle("getRacer", payload);

    assertInstanceOf(RacerResponse.class, result);
    verify(mockRacerListService).getRacer("horse", 1);
  }

  @Test void testHandleCreateRacer()
  {
    CreateRacerRequest request = new CreateRacerRequest("horse", "Lightning",
        10, 20);
    Racer mockRacer = mock(Racer.class);

    when(mockRacerListService.createRacer("horse", "Lightning", 10,
        20)).thenReturn(mockRacer);

    JsonElement payload = JsonParser.parseString(gson.toJson(request));
    Object result = racerHandler.handle("createRacer", payload);

    assertInstanceOf(CreateRacerResponse.class, result);
    verify(mockRacerListService).createRacer("horse", "Lightning", 10, 20);
  }
}