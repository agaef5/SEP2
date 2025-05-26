package client.networking.race;

import client.networking.SocketService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shared.DTO.RaceTrackDTO;
import shared.Request;
import shared.race.CreateRaceRequest;
import shared.race.GetRaceListRequest;
import shared.race.GetRaceTracksRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocketRaceClientTest {

    @Mock
    private SocketService mockSocketService;

    private SocketRaceClient raceClient;
    private Gson gson;

    @BeforeEach
    void setUp() {
        raceClient = new SocketRaceClient(mockSocketService);
        gson = new Gson();
    }

    @Test
    void constructor_WithValidSocketService_CreatesInstance() {
        // Act
        SocketRaceClient newClient = new SocketRaceClient(mockSocketService);

        // Assert
        assertNotNull(newClient);
    }

    @Test
    void constructor_WithNullSocketService_ShouldNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> new SocketRaceClient(null));
    }

    @Test
    void getRaceList_SendsCorrectRequest() {
        // Arrange
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        // Act
        raceClient.getRaceList();

        // Assert
        verify(mockSocketService).sendRequest(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();
        assertEquals("race", capturedRequest.handler());
        assertEquals("getRaceList", capturedRequest.action());
        assertNotNull(capturedRequest.payload());

        // Verify payload is a serialized GetRaceListRequest
        GetRaceListRequest expectedRequest = new GetRaceListRequest();
        JsonElement expectedPayload = gson.toJsonTree(expectedRequest);
        assertEquals(expectedPayload, capturedRequest.payload());
    }

    @Test
    void createRace_WithValidRequest_SendsCorrectRequest() {
        // Arrange
        RaceTrackDTO raceTrack = new RaceTrackDTO("Test Track", 1000, "Test Location");
        CreateRaceRequest createRaceRequest = new CreateRaceRequest("Test Race", raceTrack, 5);
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        // Act
        raceClient.createRace(createRaceRequest);

        // Assert
        verify(mockSocketService).sendRequest(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();
        assertEquals("race", capturedRequest.handler());
        assertEquals("createRace", capturedRequest.action());
        assertNotNull(capturedRequest.payload());

        // Verify payload matches the input request
        JsonElement expectedPayload = gson.toJsonTree(createRaceRequest);
        assertEquals(expectedPayload, capturedRequest.payload());
    }

    @Test
    void createRace_WithNullRequest_SendsRequestWithNullPayload() {
        // Arrange
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        // Act
        raceClient.createRace(null);

        // Assert
        verify(mockSocketService).sendRequest(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();
        assertEquals("race", capturedRequest.handler());
        assertEquals("createRace", capturedRequest.action());

        // Gson.toJsonTree(null) returns JsonNull
        JsonElement expectedPayload = gson.toJsonTree(null);
        assertEquals(expectedPayload, capturedRequest.payload());
    }

    @Test
    void getRaceTracks_WithValidRequest_SendsCorrectRequest() {
        // Arrange
        GetRaceTracksRequest getRaceTracksRequest = new GetRaceTracksRequest();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        // Act
        raceClient.getRaceTracks(getRaceTracksRequest);

        // Assert
        verify(mockSocketService).sendRequest(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();
        assertEquals("race", capturedRequest.handler());
        assertEquals("getRaceTracks", capturedRequest.action());
        assertNotNull(capturedRequest.payload());

        // Verify payload matches the input request
        JsonElement expectedPayload = gson.toJsonTree(getRaceTracksRequest);
        assertEquals(expectedPayload, capturedRequest.payload());
    }

    @Test
    void getRaceTracks_WithNullRequest_SendsRequestWithNullPayload() {
        // Arrange
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        // Act
        raceClient.getRaceTracks(null);

        // Assert
        verify(mockSocketService).sendRequest(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();
        assertEquals("race", capturedRequest.handler());
        assertEquals("getRaceTracks", capturedRequest.action());

        // Gson.toJsonTree(null) returns JsonNull
        JsonElement expectedPayload = gson.toJsonTree(null);
        assertEquals(expectedPayload, capturedRequest.payload());
    }

    @Test
    void allMethods_CallSocketServiceExactlyOnce() {
        // Arrange
        RaceTrackDTO raceTrack = new RaceTrackDTO("Test Track", 1000, "Test Location");
        CreateRaceRequest createRaceRequest = new CreateRaceRequest("Test Race", raceTrack, 5);
        GetRaceTracksRequest getRaceTracksRequest = new GetRaceTracksRequest();

        // Act
        raceClient.getRaceList();
        raceClient.createRace(createRaceRequest);
        raceClient.getRaceTracks(getRaceTracksRequest);

        // Assert
        verify(mockSocketService, times(3)).sendRequest(any(Request.class));
    }

    @Test
    void createRace_WithComplexRequest_SerializesCorrectly() {
        // Arrange
        RaceTrackDTO raceTrack = new RaceTrackDTO("Complex Track Name", 2500, "Complex Location");
        CreateRaceRequest createRaceRequest = new CreateRaceRequest("Complex Race Name", raceTrack, 10);
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        // Act
        raceClient.createRace(createRaceRequest);

        // Assert
        verify(mockSocketService).sendRequest(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();

        // Deserialize the payload back to verify it contains the correct data
        CreateRaceRequest deserializedRequest = gson.fromJson(capturedRequest.payload(), CreateRaceRequest.class);

        assertEquals(createRaceRequest.name(), deserializedRequest.name());
        assertEquals(createRaceRequest.capacity(), deserializedRequest.capacity());
        assertEquals(createRaceRequest.raceTrack().name(), deserializedRequest.raceTrack().name());
        assertEquals(createRaceRequest.raceTrack().length(), deserializedRequest.raceTrack().length());
        assertEquals(createRaceRequest.raceTrack().location(), deserializedRequest.raceTrack().location());
    }

    @Test
    void socketServiceFailure_DoesNotThrowException() {
        // Arrange
        doThrow(new RuntimeException("Socket error")).when(mockSocketService).sendRequest(any(Request.class));

        // Act & Assert
        assertDoesNotThrow(() -> raceClient.getRaceList());
        assertDoesNotThrow(() -> raceClient.createRace(null));
        assertDoesNotThrow(() -> raceClient.getRaceTracks(null));
    }

    @Test
    void requestStructure_HasCorrectFormat() {
        // Arrange
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        // Act
        raceClient.getRaceList();

        // Assert
        verify(mockSocketService).sendRequest(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();

        // Verify the request has all required fields
        assertNotNull(capturedRequest.handler());
        assertNotNull(capturedRequest.action());
        assertNotNull(capturedRequest.payload());

        // Verify the request follows the expected pattern
        assertTrue(capturedRequest.handler() instanceof String);
        assertTrue(capturedRequest.action() instanceof String);
        assertTrue(capturedRequest.payload() instanceof JsonElement);
    }

    @Test
    void multipleRequests_EachHasUniquePayload() {
        // Arrange
        RaceTrackDTO raceTrack1 = new RaceTrackDTO("Track 1", 1000, "Location 1");
        RaceTrackDTO raceTrack2 = new RaceTrackDTO("Track 2", 2000, "Location 2");
        CreateRaceRequest request1 = new CreateRaceRequest("Race 1", raceTrack1, 5);
        CreateRaceRequest request2 = new CreateRaceRequest("Race 2", raceTrack2, 8);

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        // Act
        raceClient.createRace(request1);
        raceClient.createRace(request2);

        // Assert
        verify(mockSocketService, times(2)).sendRequest(requestCaptor.capture());

        // Verify both requests are different
        var capturedRequests = requestCaptor.getAllValues();
        assertNotEquals(capturedRequests.get(0).payload(), capturedRequests.get(1).payload());

        // Verify each payload corresponds to the correct request
        CreateRaceRequest deserializedRequest1 = gson.fromJson(capturedRequests.get(0).payload(), CreateRaceRequest.class);
        CreateRaceRequest deserializedRequest2 = gson.fromJson(capturedRequests.get(1).payload(), CreateRaceRequest.class);

        assertEquals("Race 1", deserializedRequest1.name());
        assertEquals("Race 2", deserializedRequest2.name());
    }
}