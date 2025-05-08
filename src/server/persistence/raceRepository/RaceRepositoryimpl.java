package server.persistence.raceRepository;

import server.model.Race;
import server.model.RaceTrack;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class implements the {@link RaceRepository} interface and provides methods for interacting with the race data in the database.
 * It handles CRUD operations for races, including creating, reading, updating, and deleting race records.
 */
public class RaceRepositoryimpl implements RaceRepository {

    private static RaceRepositoryimpl instance;

    /**
     * Private constructor to prevent direct instantiation.
     * Registers the PostgreSQL JDBC driver.
     *
     * @throws SQLException if a database access error occurs
     */
    private RaceRepositoryimpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    /**
     * Provides a singleton instance of the {@link RaceRepositoryimpl} class.
     * Ensures that only one instance of this repository exists.
     *
     * @return the singleton instance of {@link RaceRepositoryimpl}
     * @throws SQLException if a database access error occurs
     */
    public static synchronized RaceRepositoryimpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new RaceRepositoryimpl();
        }
        return instance;
    }

    /**
     * Establishes a connection to the PostgreSQL database.
     *
     * @return a {@link Connection} object to interact with the database
     * @throws SQLException if a database access error occurs
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2",
                "postgres", "1234");
    }

    /**
     * Creates a new race record in the database.
     *
     * @param name     the name of the race
     * @param time     the start time of the race
     * @param raceTrack the race track associated with the race
     * @return a new {@link Race} object with the generated ID
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Race create(String name, Date time, RaceTrack raceTrack) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO race (name, startTime, race) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(
                    query,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setDate(2, new java.sql.Date(time.getTime()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new Race(resultSet.getString(3), null, null);
            } else {
                throw new SQLException("No keys generated");
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
    public Race readByID(int id) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT r.id, r.name, r.status, r.startTime, " +
                    "rt.name as track_name, rt.raceLength, rt.location " +
                    "FROM race r " +
                    "LEFT JOIN raceTrack rt ON r.race_id = rt.race_id " +
                    "WHERE r.id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                Integer raceCapacity = resultSet.getInt("raceCapacity");

                String trackName = resultSet.getString("track_name");
                int trackLength = resultSet.getInt("raceLength");
                String trackLocation = resultSet.getString("location");

                RaceTrack raceTrack = null;
                if(trackName!=null){
                    raceTrack = new RaceTrack(trackName, trackLength, trackLocation);
                }


                return new Race(name, raceTrack, raceCapacity);
            } else {
                return null;
            }
        }
    }

    /**
     * Reads races from the database that match the given name.
     *
     * @param searchName the name (or part of the name) to search for
     * @return a list of {@link Race} objects that match the given name
     * @throws SQLException if a database access error occurs
     */
    @Override
    public List<Race> readByName(String searchName) throws SQLException
    {
        try (Connection connection = getConnection())
        {
            String query = "SELECT r.id, r.name, r.status, r.startTime, " +
                    "rt.name as track_name, rt.raceLength, rt.location " +
                    "FROM race r " +
                    "LEFT JOIN raceTrack rt ON r.race_id = rt.race_id " +
                    "WHERE r.name LIKE ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchName + "%");
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Race> result = new ArrayList<>();
            while (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                Integer raceCapacity = resultSet.getInt("raceCapacity");

                String trackName = resultSet.getString("track_name");
                int trackLength = resultSet.getInt("raceLength");
                String trackLocation = resultSet.getString("location");

                RaceTrack raceTrack = null;
                if(trackName!=null){
                    raceTrack = new RaceTrack(trackName, trackLength, trackLocation);
                }

                Race race = new Race(name, raceTrack, raceCapacity);

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
    public List<Race> getAll() throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT r.id, r.name, r.status, r.startTime, " +
                    "rt.name as track_name, rt.raceLength, rt.location " +
                    "FROM race r " +
                    "LEFT JOIN raceTrack rt ON r.race_id = rt.race_id";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Race> result = new ArrayList<>();
            while (resultSet != null && resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                Integer raceCapacity = resultSet.getInt("raceCapacity");

                String trackName = resultSet.getString("track_name");
                int trackLength = resultSet.getInt("raceLength");
                String trackLocation = resultSet.getString("location");

                RaceTrack raceTrack = null;
                if(trackName!=null){
                    raceTrack = new RaceTrack(trackName, trackLength, trackLocation);
                }

                Race race = new Race(name, raceTrack, raceCapacity);

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
    public Race readByTime(Date time) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT r.id, r.name, r.status, r.startTime, " +
                    "rt.name as track_name, rt.raceLength, rt.location " +
                    "FROM race r " +
                    "LEFT JOIN raceTrack rt ON r.race_id = rt.race_id " +
                    "WHERE r.startTime = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, new java.sql.Date(time.getTime()));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                Integer raceCapacity = resultSet.getInt("raceCapacity");

                String trackName = resultSet.getString("track_name");
                int trackLength = resultSet.getInt("raceLength");
                String trackLocation = resultSet.getString("location");

                RaceTrack raceTrack = null;
                if(trackName!=null){
                    raceTrack = new RaceTrack(trackName, trackLength, trackLocation);
                }

                Race race = new Race(name, raceTrack, raceCapacity);

             return race;
            } else {
                return null;
            }
        }
    }

    /**
     * Reads a race from the database by its status.
     *
     * @param status the status of the race to search for
     * @return a {@link Race} object if found, or {@code null} if no race exists with the given status
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Race readByStatus(String status) throws SQLException {
        try (Connection connection = getConnection()) {

            String query = "SELECT r.id, r.name, r.status, r.startTime, " +
                    "rt.name as track_name, rt.raceLength, rt.location " +
                    "FROM race r " +
                    "LEFT JOIN raceTrack rt ON r.race_id = rt.race_id " +
                    "WHERE r.status LIKE ?";

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM race WHERE status LIKE ?");
            statement.setString(1, "%" + status + "%");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String resultStatus = resultSet.getString("status");
                Integer raceCapacity = resultSet.getInt("raceCapacity");

                String trackName = resultSet.getString("track_name");
                int trackLength = resultSet.getInt("raceLength");
                String trackLocation = resultSet.getString("location");

                RaceTrack raceTrack = null;
                if(trackName!=null){
                    raceTrack = new RaceTrack(trackName, trackLength, trackLocation);
                }


                Race race = new Race(name, raceTrack, raceCapacity);

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
            statement.setString(3, null); // Placeholder for start time
            statement.executeUpdate();
        }
    }

    /**
     * Deletes a race from the database.
     *
     * @param race the race to delete
     * @throws SQLException if a database access error occurs
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
}
