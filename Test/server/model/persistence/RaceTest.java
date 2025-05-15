package persistence;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Horse;
import server.persistence.horses.HorseRepositoryImpl;
import server.persistence.raceRepository.RaceRepositoryImpl;
import server.persistence.shared.ConnectionProvider;
import server.persistence.shared.ConnectionProviderImpl;
import shared.DTO.RaceDTO;

import java.sql.*;
import java.util.List;

public class RaceTest
{
    @Test
    void testReadByIdWithMockedConnection() throws Exception
    {
        ConnectionProviderImpl mockProvider = mock(ConnectionProviderImpl.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockResult = mock(ResultSet.class);

        when(mockProvider.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResult);

        when(mockResult.next()).thenReturn(true);
        when(mockResult.getString("name")).thenReturn("Mocked Race");
        when(mockResult.getTimestamp("startTime")).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResult.getString("track_name")).thenReturn("Mock Track");
        when(mockResult.getInt("raceLength")).thenReturn(1200);
        when(mockResult.getString("location")).thenReturn("Mock City");
        when(mockResult.getString("horse_name")).thenReturn("Mock Horse");

        HorseRepositoryImpl horseRepo = mock(HorseRepositoryImpl.class);
        when(horseRepo.readByName(anyString())).thenReturn(List.of(
                new Horse(1, "Mock Horse", 10, 20)
        ));

        RaceRepositoryImpl repo = new RaceRepositoryImpl(mockProvider);
        RaceDTO race = repo.readByID(1);

        assertNotNull(race);
        assertEquals("Mocked Race", race.name());
    }

//    @Test
//    public void testRetrieveRace() throws SQLException{
//        int raceID = 1;
//        int raceTrackID = 1;
//        String nameRace = "Race Test";
//        String nameRaceTrack = "Midtown Madness";
//        int raceCap = 7;
//        String status = "Status test";
//        Date date = mock(Date.class);
//        when(date.toString()).thenReturn("09-05-2025");
//
//        RaceTrack raceTrack = new RaceTrack("Steelball Run", 10000, "U.S.A.");
//        when(raceTrackRepository.readByName(nameRaceTrack)).thenReturn(raceTrack);
//        Race race = new Race(nameRace, raceTrack, raceCap);
//
//        when(raceRepository.readByName("Race")).thenReturn(List.of(race));
//        List<Race> result = raceRepository.readByName("Race");
//
//    }
//    @Test
//    public void editRace() throws SQLException{
//        Date mockedDate = mock(Date.class);
//        when(mockedDate.getTime()).thenReturn(1622499000000L);
//        when(race.getDateTime()).thenReturn((Timestamp) mockedDate);
//        int raceID = 1;
//        int raceTrackID = 1;
//        String nameRace = "Steelball Run";
//        String nameRaceTrack = "Midtown Madness";
//        int raceCap = 7;
//        String status = "Status test";
//        Date date = mock(Date.class);
//        when(date.toString()).thenReturn("09-05-2025");
//
//        assertEquals("Steelball Run", race.getName());
//
//    }
}
