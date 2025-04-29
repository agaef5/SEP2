package client.ui.racerList.adminView;

import client.networking.SocketService;
import client.networking.racers.RacersClient;
import client.ui.racerList.adminView.horseList.CreateEditRacerVM;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Horse;
import server.model.Racer;
import shared.CreateRacerRequest;
import shared.RacerListResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEditRacerVMTest {

  private RacersClient racersClient;
  private SocketService socketService;
  private CreateEditRacerVM viewModel;

  @BeforeEach
  void setUp() {
    racersClient = mock(RacersClient.class);
    socketService = mock(SocketService.class);
    viewModel = new CreateEditRacerVM(racersClient, socketService);
  }

  @Test
  void testAddRacer() {
    viewModel.racerTypeProperty().setValue("Horse");
    viewModel.racerNameProperty().setValue("Lightning");
    viewModel.speedMinProperty().setValue(10);
    viewModel.speedMaxProperty().setValue(20);

    viewModel.addRacer();

    verify(racersClient, times(1)).createRacer(any(CreateRacerRequest.class));
    verify(racersClient, times(1)).getRacerList();
  }

  @Test
  void testUpdateRacer() {
    Racer racer = new Horse(1, "OldName", 5, 15);
    viewModel.setSelectedRacer(racer);

    viewModel.racerNameProperty().setValue("NewName");
    viewModel.speedMinProperty().setValue(10);
    viewModel.speedMaxProperty().setValue(30);
    viewModel.racerTypeProperty().setValue("Horse");

    viewModel.updateRacer();

    assertEquals("NewName", racer.getName());
    assertEquals(10, racer.getSpeedMin());
    assertEquals(30, racer.getSpeedMax());
    assertEquals("Horse", racer.getType());

    verify(racersClient, times(1)).updateRacer(racer);
  }

  @Test
  void testRemoveRacer() {
    Racer racer = new Horse(1, "ToDelete", 5, 15);
    viewModel.setSelectedRacer(racer);

    viewModel.removeRacer();

    verify(racersClient, times(1)).deleteRacer(racer);
  }

  @Test
  void testUpdateRacerList() {
    RacerListResponse response = new RacerListResponse(
        List.of(new Horse(1, "FastHorse", 10, 20))
    );

    Platform.runLater(() -> {
      viewModel.updateRacerList(response);

      ObservableList<Racer> racers = viewModel.getRacerList();
      assertEquals(1, racers.size());
      assertNotNull(viewModel.getRacerList());
    });
  }
}