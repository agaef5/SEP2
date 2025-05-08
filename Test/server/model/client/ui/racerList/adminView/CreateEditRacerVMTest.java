package client.ui.racerList.adminView;

import client.networking.SocketService;
import client.networking.horses.HorsesClient;
import client.ui.adminView.horseList.CreateEditHorseVM;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Horse;

import shared.CreateHorseRequest;
import shared.HorseListResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEditRacerVMTest {

  private HorsesClient horsesClient;
  private SocketService socketService;
  private CreateEditHorseVM viewModel;

  @BeforeEach
  void setUp() {
    horsesClient = mock(HorsesClient.class);
    socketService = mock(SocketService.class);
    viewModel = new CreateEditHorseVM(horsesClient, socketService);
  }

  @Test
  void testAddRacer() {
    viewModel.horseNameProperty().setValue("Lightning");
    viewModel.speedMinProperty().setValue(10);
    viewModel.speedMaxProperty().setValue(20);

    viewModel.addHorse();

    verify(horsesClient, times(1)).createHorse(any(CreateHorseRequest.class));
    verify(horsesClient, times(1)).getHorseList();
  }

  @Test
  void testUpdateRacer() {
    Horse horse = new Horse(1, "OldName", 5, 15);
    viewModel.setSelectedHorse(horse);

    viewModel.horseNameProperty().setValue("NewName");
    viewModel.speedMinProperty().setValue(10);
    viewModel.speedMaxProperty().setValue(30);

    viewModel.updateHorse();

    assertEquals("NewName", horse.getName());
    assertEquals(10, horse.getSpeedMin());
    assertEquals(30, horse.getSpeedMax());
    assertEquals("Horse", horse.getType());

    verify(horsesClient, times(1)).updateHorse(horse);
  }

  @Test
  void testRemoveRacer() {
    Horse horse = new Horse(1, "ToDelete", 5, 15);
    viewModel.setSelectedHorse(horse);

    viewModel.removeHorse();

    verify(horsesClient, times(1)).deleteHorse(horse);
  }

  @Test
  void testUpdateRacerList() {
    HorseListResponse response = new HorseListResponse(
        List.of(new Horse(1, "FastHorse", 10, 20))
    );

    Platform.runLater(() -> {
      viewModel.updateHorseList(response);

      ObservableList<Horse> horses = viewModel.getHorseList();
      assertEquals(1, horses.size());
      assertNotNull(viewModel.getHorseList());
    });
  }
}