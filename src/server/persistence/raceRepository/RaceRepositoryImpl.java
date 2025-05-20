package server.persistence.raceRepository;

import server.model.Race;
import server.model.RaceTrack;
import server.persistence.shared.ConnectionProviderImpl;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
import shared.DTO.RaceTrackDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the {@link RaceRepository} interface and provides methods for interacting
 * with the race data in the database. It includes creation, saving, reading, updating, and deletion of races.
 */
public class RaceRepositoryImpl implements RaceRepository {
    private static RaceRepositoryImpl instance;
    private final ConnectionProviderImpl connectionProvider;
    private int defaultCapacity = 7;

    public RaceRepositoryImpl(ConnectionProviderImpl connectionProvider)
    {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Provides a singleton instance of the {@link RaceRepositoryImpl}.
     *
     * @return singleton instance
     * @throws SQLException if connection cannot be established
     */
    public static synchronized RaceRepositoryImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new RaceRepositoryImpl(new ConnectionProviderImpl());
        }
        return instance;
    }

    /**
     * Gets a new connection from the connection provider.
     */
    private Connection getConnection() throws SQLException {
        return connectionProvider.getConnection();
    }

    /**
     * Creates a new Race in the database.
     *
     * @param name the race name
     * @param time the scheduled time
     * @param raceTrack the track info
     * @return created {@link Race} object
     * @throws SQLException if insert fails or track doesn't exist
     */
    @Override
    public Race create(String name, Timestamp time, RaceTrack raceTrack) throws SQLException {
        try (Connection connection = getConnection()) {
            // Find the raceTrack ID
            String trackQuery = "SELECT id FROM raceTrack WHERE name = ? AND raceLength = ? AND location = ?";
            PreparedStatement trackStatement = connection.prepareStatement(trackQuery);
            trackStatement.setString(1, raceTrack.getName());
            trackStatement.setInt(2, raceTrack.getLength());
            trackStatement.setString(3, raceTrack.getLocation());
            ResultSet trackResult = trackStatement.executeQuery();

            int raceTrackId;
            if (trackResult.next()) {
                raceTrackId = trackResult.getInt("id");
            } else {
                throw new SQLException("RaceTrack not found in database.");
            }

            // Insert new race
            String query = "INSERT INTO race (name, startTime, racetrack_id) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(
                    query,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setTimestamp(2, time);
            statement.setInt(3, raceTrackId);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int generatedId = resultSet.getInt(1);
                return new Race(name, raceTrack, defaultCapacity);
            } else {
                throw new SQLException("No keys generated");
            }
        }
    }

    /**
     * Saves a Race instance into the database.
     *
     * @param race the {@link Race} object to store
     * @return the stored {@link Race}, or null if something goes wrong
     * @throws SQLException on database error
     */
    @Override
    public Race save(Race race) throws SQLException
    {
        try (Connection connection = getConnection()) {
            RaceTrack raceTrack = race.getRaceTrack();

            // Ensure RaceTrack exists
            String trackQuery = "SELECT id FROM raceTrack WHERE name = ? " +
                    "AND raceLength = ? AND location = ?";
            PreparedStatement trackStatement = connection.prepareStatement(trackQuery);
            trackStatement.setString(1, raceTrack.getName());
            trackStatement.setInt(2, raceTrack.getLength());
            trackStatement.setString(3, raceTrack.getLocation());
            ResultSet resultSetTrack = trackStatement.executeQuery();

            int raceTrackId;
            if (resultSetTrack.next()) {
                raceTrackId = resultSetTrack.getInt("id");
            } else {
                throw new SQLException("RaceTrack not found in database.");
            }

            // Insert race record
            String insertQuery = "INSERT INTO race (name, startTime, racetrack_id) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, race.getName());
            statement.setTimestamp(2, race.getDateTime());
            statement.setInt(3, raceTrackId);
            if (resultSetTrack.next()) {
                String name = resultSetTrack.getString("name");
                Timestamp startTime = resultSetTrack.getTimestamp("startTime");
                String trackName = resultSetTrack.getString("track_name");
                int trackLength = resultSetTrack.getInt("raceLength");
                String trackLocation = resultSetTrack.getString("location");

                RaceTrack raceTrack_Result = new RaceTrack(trackName, trackLength, trackLocation);

                return new Race(name, raceTrack_Result, defaultCapacity);
            } else {
                return null;
            }
        }
    }

    /**
     * Reads a race from the database by its ID.
     *
     * @param id the ID of the race to read
     * @return a {@link Race} object if found, or {@code null} if no race exists with the given ID
     * @throws SQLException if a database access error occurs
     */
    @Override
    public RaceDTO readByID(int id) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT name, startTime " +
                    "FROM race r " +
                    "WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Timestamp startTime = resultSet.getTimestamp("startTime");

                return new RaceDTO(name, startTime, readParticipantsList(id), readRaceTrack(id), RaceState.FINISHED);
            }
            return null;
        }
    }

    /**
     * Reads races matching a given name pattern.
     */
    @Override
    public List<RaceDTO> readByName(String searchName) throws SQLException
    {
        try (Connection connection = getConnection())
        {
            String query = "SELECT r.id, r.name, r.status, r.startTime, " +
                    "rt.name as track_name, rt.raceLength, rt.location " +
                    "FROM race r " +
                    "LEFT JOIN raceTrack rt ON r.race_id = rt.id " +
                    "WHERE r.name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchName + "%");

            ResultSet resultSet = statement.executeQuery();

            ArrayList<RaceDTO> result = new ArrayList<>();
            while (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Timestamp startTime = resultSet.getTimestamp("startTime");

                String trackName = resultSet.getString("track_name");
                int trackLength = resultSet.getInt("raceLength");
                String trackLocation = resultSet.getString("location");

                RaceTrackDTO raceTrack = null;
                if(trackName!=null){
                    raceTrack = new RaceTrackDTO(trackName, trackLength, trackLocation);
                }

                RaceDTO race = new RaceDTO(name, startTime, readParticipantsList(id), raceTrack, RaceState.FINISHED);

                result.add(race);
            }
            return result;
        }
    }

    /**
     * Retrieves all races from the database.
     *
     * @return a list of all {@link Race} objects
     * @throws SQLException if a database access error occurs
     */
    @Override
    public List<RaceDTO> getAll() throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT  name, startTime, " +
                    "FROM race ";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<RaceDTO> result = new ArrayList<>();
            while (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Timestamp startTime = resultSet.getTimestamp("startTime");

                RaceDTO race = new RaceDTO(name, startTime, readParticipantsList(id), readRaceTrack(id), RaceState.FINISHED);
                result.add(race);
            }
            return result;
        }
    }

    /**
     * Reads a race from the database by its start time.
     *
     * @param time the start time of the race
     * @return a {@link Race} object if found, or {@code null} if no race exists at the given time
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Race readByTime(Timestamp time) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT r.id, r.name, r.status, r.startTime, " +
                    "rt.name as track_name, rt.raceLength, rt.location " +
                    "h.id AS horse_id, h.name AS horse_name, h.speedMin, h.speedMax"+
                    "FROM race r " +
                    "LEFT JOIN raceTrack rt ON r.race_id = rt.id " +
                    "LEFT JOIN horse h ON r.horse_id = h.id"+
                    "WHERE r.startTime = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1,time);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");

                String trackName = resultSet.getString("track_name");
                int trackLength = resultSet.getInt("raceLength");
                String trackLocation = resultSet.getString("location");

                String horseName = resultSet.getString("horse_name");
                int speedMin = resultSet.getInt("speedMin");
                int speedMax = resultSet.getInt("speedMax");

                RaceTrack raceTrack = null;
                if(trackName!=null){
                    raceTrack = new RaceTrack(trackName, trackLength, trackLocation);
                }

                Race race = new Race(name, raceTrack, defaultCapacity);

             return race;
            } else {
                return null;
            }
        }
    }

    /**
     * Updates the details of a race in the database.
     *
     * @param race the race object containing updated data
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void updateRace(Race race) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "UPDATE race SET name = ?, status = ?, startTime = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, race.getName());
            statement.setString(2, race.getStatus().toString());
            statement.setTimestamp(3, race.getDateTime());
            statement.executeUpdate();
        }
    }

    /**
     * Deletes a race from the database.
     */
    @Override
    public void delete(Race race) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "DELETE FROM race WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 3); // Placeholder for race ID
            statement.executeUpdate();
        }
    }

    /**
     * Reads the list of horse participants for a given race ID.
     */
    private List<HorseDTO> readParticipantsList(int id) throws SQLException {
        try (Connection connection = getConnection()) {
                String horseQuery = "SELECT h.id, h.name, h.speedMin, h.speedMax " +
                        "FROM participant p " +
                        "JOIN sep2.horse h ON h.id = p.horse_id " +
                        "WHERE p.race_id = ?";
                List<HorseDTO> horseDTOS = new ArrayList<>();
                try (PreparedStatement horseStatement = connection.prepareStatement(horseQuery))
                {
                    horseStatement.setInt(1, id);
                    try (ResultSet hrs = horseStatement.executeQuery())
                    {
                        while (hrs.next())
                        {
                            horseDTOS.add(new HorseDTO(
                                    hrs.getInt("id"),
                                    hrs.getString("name"),
                                    hrs.getInt("speedMin"),
                                    hrs.getInt("speedMax")));
                        }
                    }
                }
                return horseDTOS;
        }
    }

    /**
     * Reads the RaceTrack associated with a given race ID.
     */
    private RaceTrackDTO readRaceTrack(int id) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT name, raceLength, location " +
                    "FROM raceTrack rt " +
                    "JOIN race r ON rt.race_id = r.id " +
                    "WHERE race_id = ? ";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<RaceDTO> result = new ArrayList<>();
            while (resultSet != null && resultSet.next()) {

                String trackName = resultSet.getString("track_name");
                int trackLength = resultSet.getInt("raceLength");
                String trackLocation = resultSet.getString("location");

                RaceTrackDTO raceTrack = null;
                if(trackName!=null){
                    raceTrack = new RaceTrackDTO(trackName, trackLength, trackLocation);
                }
                return raceTrack;
            }
        }
        return null;
    }
}
