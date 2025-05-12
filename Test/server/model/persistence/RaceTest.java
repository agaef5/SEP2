//package persistence;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import server.model.Race;
//import server.model.RaceTrack;
//import server.persistence.raceRepository.RaceRepository;
//import server.persistence.raceRepository.RaceRepositoryImpl;
//import server.persistence.raceRepository.raceTrack.RaceTrackRep;
//
//import java.sql.*;
//import java.util.Date;
//import java.util.List;
//
//public class RaceTest
//{
//    @Mock
//    Race race;
//    @Mock
//    private Connection connection;
//    @Mock
//    private RaceTrackRep raceTrackRepository;
//    @Mock
//    private RaceRepository raceRepository;
//
//    @BeforeEach
//    public void setUp() throws SQLException {
//        MockitoAnnotations.openMocks(this);
//        when(connection.prepareStatement(anyString())).thenReturn(mock(PreparedStatement.class));
//        raceTrackRepository = mock(RaceTrackRep.class);
//        raceRepository = mock(RaceRepositoryImpl.class);
//    }
//    @Test
//    public void testCreateRace() throws SQLException {
//        int raceID = 1;
//        int raceTrackID = 1;
//        String nameRace = "Name test";
//        String nameRaceTrack = "Midtown Madness";
//        String status = "Status test";
//        Date date = mock(Date.class);
//        when(date.toString()).thenReturn("09-05-2025");
//        RaceTrack raceTrack = new RaceTrack("Midtown Madness", 1000, "Aarhus");
//        when(raceTrackRepository.readByName(nameRaceTrack)).thenReturn(raceTrack);
//
//        RaceTrack resultTrack = raceTrackRepository.readByName("Midtown Madness");
//        Race result = raceRepository.create("Name test", "Status test", date, raceTrack);
//        assertNotNull(resultTrack);
//        assertEquals("Name test", result.getName());
//        assertEquals("Status test", result.getStatus());
//        assertEquals(raceTrack, result.getRaceTrack());
//        assertEquals("Midtown Madness", resultTrack.getName());
//        assertEquals(1000, resultTrack.getLength());
//        assertEquals("Aarhus", resultTrack.getLocation());
//
//    }
////    @Test
////    public void testRetrieveRace() throws SQLException{
////        int raceID = 1;
////        int raceTrackID = 1;
////        String nameRace = "Race Test";
////        String nameRaceTrack = "Midtown Madness";
////        int raceCap = 7;
////        String status = "Status test";
////        Date date = mock(Date.class);
////        when(date.toString()).thenReturn("09-05-2025");
////
////        RaceTrack raceTrack = new RaceTrack("Steelball Run", 10000, "U.S.A.");
////        when(raceTrackRepository.readByName(nameRaceTrack)).thenReturn(raceTrack);
////        Race race = new Race(nameRace, raceTrack, raceCap);
////
////        when(raceRepository.readByName("Race")).thenReturn(List.of(race));
////        List<Race> result = raceRepository.readByName("Race");
////
////    }
////    @Test
////    public void editRace() throws SQLException{
////        Date mockedDate = mock(Date.class);
////        when(mockedDate.getTime()).thenReturn(1622499000000L);
////        when(race.getDateTime()).thenReturn((Timestamp) mockedDate);
////        int raceID = 1;
////        int raceTrackID = 1;
////        String nameRace = "Steelball Run";
////        String nameRaceTrack = "Midtown Madness";
////        int raceCap = 7;
////        String status = "Status test";
////        Date date = mock(Date.class);
////        when(date.toString()).thenReturn("09-05-2025");
////
////        assertEquals("Steelball Run", race.getName());
////
////    }
//}
