//package server.model;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import shared.DTO.RaceState;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RaceTest {
//    private Race race;
//    private MockRaceListener mockListener;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        // Create a short track
//        RaceTrack track = new RaceTrack("TestTrack", 500, "London");
//
//        // Use custom constructor to avoid DB
//        HorseList horseList = new HorseList(3);
//        horseList.addToList(new Horse(1,"Flash",5,10));
//        horseList.addToList(new Horse(2,"Bolt",4,11));
//        horseList.addToList(new Horse(3,"Zoom",3,12));
//
//        // Initialize race with predefined horses
//        race = new Race("TestRace", new Timestamp(System.currentTimeMillis()), new HorseList(3), track);
//        race.getHorseList().getList().addAll(horseList.getList());
//
//        mockListener = new MockRaceListener();
//        race.addListener(mockListener);
//
//        // Optional: patch sleep time or use a test-specific Race subclass to avoid long wait
//    }
//
//    @Test
//    void testRunMethodCompletesRace() throws InterruptedException {
//        Thread thread = new Thread(() -> {
//            race.run();
//        });
//
//        thread.start();
//
//        try {
//            thread.join(5000); // Wait max 5 seconds
//        } catch (InterruptedException e) {
//            fail("Thread was interrupted");
//        }
//
//        assertEquals(RaceState.FINISHED, race.getStatus());
//        assertEquals(3, race.getFinalPositionlist().getList().size());
//
//        // Check that listener was triggered
//        assertTrue(mockListener.started);
//        assertTrue(mockListener.finished);
//        assertEquals(3, mockListener.finishedHorses.size());
//    }
//
//    static class MockRaceListener implements RaceListener {
//        boolean started = false;
//        boolean finished = false;
//        List<Horse> finishedHorses = new ArrayList<>();
//
//        @Override
//        public void onRaceStarted(String raceName) {
//            started = true;
//            System.out.println("MockListener: Race started");
//        }
//
//        @Override
//        public void onHorseFinished(Horse horse, int position) {
//            finishedHorses.add(horse);
//            System.out.println("MockListener: Horse finished - " + horse.getName() + " at position " + position);
//        }
//
//        @Override
//        public void onRaceFinished(String raceName, HorseList finalPositionList) {
//            finished = true;
//            System.out.println("MockListener: Race finished");
//        }
//
//    }
//}
