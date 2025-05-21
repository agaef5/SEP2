package server.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RaceTest {
    public static void main(String[] args) {
        // Create some sample horses
        RaceTrack raceTrack = new RaceTrack("testRaceTrack",5000,"TestLocation");


        // Create a race object (you may need to add race track info, etc.)
        Race race = null;
        try {
            race = new Race("TestRace",raceTrack,9);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Run the race (this might start a thread or run synchronously depending on your code)
        Thread t = new Thread(race);
        t.start();
    }
}

