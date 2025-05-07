//package client.ui.racerList.adminView;
//
//import client.networking.SocketService;
//import client.networking.horses.HorsesClient;
//import client.ui.adminView.horseList.CreateEditHorseVM;
//import javafx.application.Platform;
//import javafx.collections.ObservableList;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import server.model.Horse;
//
//import shared.CreateHorseRequest;
//import shared.HorseListResponse;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CreateEditRacerVMTest {
//
//  private HorsesClient racersClient;
//  private SocketService socketService;
//  private CreateEditHorseVM viewModel;
//
//  @BeforeEach
//  void setUp() {
//    racersClient = mock(HorsesClient.class);
//    socketService = mock(SocketService.class);
//    viewModel = new CreateEditHorseVM(racersClient);
//  }
//
//  @Test
//  void testAddRacer() {
//    viewModel.racerTypeProperty().setValue("Horse");
//    viewModel.horseNameProperty().setValue("Lightning");
//    viewModel.speedMinProperty().setValue(10);
//    viewModel.speedMaxProperty().setValue(20);
//
//    viewModel.addHorse();
//
//    verify(racersClient, times(1)).createHorse(any(CreateHorseRequest.class));
//    verify(racersClient, times(1)).getHorseList();
//  }
//
//  @Test
//  void testUpdateRacer() {
//    Racer racer = new Horse(1, "OldName", 5, 15);
//    viewModel.setSelectedHorse(racer);
//
//    viewModel.horseNameProperty().setValue("NewName");
//    viewModel.speedMinProperty().setValue(10);
//    viewModel.speedMaxProperty().setValue(30);
//    viewModel.racerTypeProperty().setValue("Horse");
//
//    viewModel.updateHorse();
//
//    assertEquals("NewName", racer.getName());
//    assertEquals(10, racer.getSpeedMin());
//    assertEquals(30, racer.getSpeedMax());
//    assertEquals("Horse", racer.getType());
//
//    verify(racersClient, times(1)).updateHorse(racer);
//  }
//
//  @Test
//  void testRemoveRacer() {
//    Racer racer = new Horse(1, "ToDelete", 5, 15);
//    viewModel.setSelectedHorse(racer);
//
//    viewModel.removeHorse();
//
//    verify(racersClient, times(1)).deleteHorse(racer);
//  }
//
//  @Test
//  void testUpdateRacerList() {
//    HorseListResponse response = new HorseListResponse(
//        List.of(new Horse(1, "FastHorse", 10, 20))
//    );
//
//    Platform.runLater(() -> {
//      viewModel.updateHorseList(response);
//
//      ObservableList<Racer> racers = viewModel.getHorseList();
//      assertEquals(1, racers.size());
//      assertNotNull(viewModel.getHorseList());
//    });
//  }
//}