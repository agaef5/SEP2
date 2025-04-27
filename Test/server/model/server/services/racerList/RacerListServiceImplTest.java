package server.services.racerList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.model.Horse;
import server.model.Racer;
import server.persistence.racer.RacerRepository;
import org.mockito.Mockito;
import shared.RacerListResponse;
import shared.RacerResponse;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RacerListServiceImplTest
{
  private RacerRepository mockRepo;
  private RacerListServiceImpl service;

  @BeforeEach
  public void setUp() {
    mockRepo = Mockito.mock(RacerRepository.class);
    service = new RacerListServiceImpl();
  }

  @Test
  public void testGetRacerList_returnsList() throws SQLException
  {
    Racer horse = new Horse(1,"Spirit", 10, 15);
    Mockito.when(mockRepo.readAll("horse")).thenReturn(Arrays.asList(horse));

    RacerListResponse response = service.getRacerList("horse");

    assertNotNull(response.racerList());
  }

  @Test
  public void testGetRacer_validHorse_returnsRacer() throws SQLException {
    Racer horse = new Horse(1, "Moba", 8, 12);
    Mockito.when(mockRepo.readByID("horse", 1)).thenReturn(horse);

    RacerResponse response = service.getRacer("horse", 1);

    assertNotNull(response.racer());
    assertEquals("Moba", response.racer().getName());
  }

  @Test
  public void testGetRacer_invalidType_returnsNull() {
    RacerResponse response = service.getRacer("dog", 1);

    assertNull(response.racer());
  }

  @Test
  public void testCreateRacer_success() throws SQLException {
    Racer newHorse = new Horse(3, "Bolt", 7, 14);
    Mockito.when(mockRepo.create("horse", "Bolt", 7, 14)).thenReturn(newHorse);

    Racer result = service.createRacer("horse", "Bolt", 7, 14);

    assertNotNull(result);
    assertEquals("Bolt", result.getName());
  }
}