package server.persistence.raceRepository.raceTrack;

import server.model.RaceTrack;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link RaceTrackRep} that manages {@code RaceTrack} entries using JDBC and PostgreSQL.
 */
public class RaceTrackRepImpl implements RaceTrackRep {
    private static RaceTrackRepImpl instance;

    /**
     * Private constructor to register the PostgreSQL driver.
     *
     * @throws SQLException if registering the driver fails
     */
    private RaceTrackRepImpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    /**
     * Retrieves the singleton instance of {@code RaceTrackRepImpl}.
     *
     * @return the singleton instance
     * @throws SQLException if the instance creation fails
     */
    public static synchronized RaceTrackRepImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new RaceTrackRepImpl();
        }
        return instance;
    }

    /**
     * Establishes a database connection.
     *
     * @return a new {@code Connection} object
     * @throws SQLException if connection fails
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2",
                "postgres", "1234");
    }

    /**
     * Inserts a new race track into the database.
     *
     * @param name     the name of the track
     * @param length   the length of the track
     * @param location the location of the track
     * @return the created {@code RaceTrack} object
     * @throws SQLException if insertion fails
     */
    @Override
    public RaceTrack create(String name, int length, String location) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO raceTrack (name, raceLength, location) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setInt(2, length);
            statement.setString(3, location);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new RaceTrack(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3));
            } else {
                throw new SQLException("No keys generated");
            }
        }
    }

    /**
     * Retrieves all race tracks from the database.
     *
     * @return a list of all {@code RaceTrack} entries
     * @throws SQLException if the query fails
     */
    @Override
    public List<RaceTrack> getAll() throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM raceTrack";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<RaceTrack> result = new ArrayList<>();
            while (resultSet != null && resultSet.next()) {
                int length = resultSet.getInt("raceLength");
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");
                result.add(new RaceTrack(name, length, location));
            }
            return result;
        }
    }

    /**
     * Retrieves race tracks that match a given name (case-insensitive).
     *
     * @param name the name or partial name to match
     * @return a list of matching {@code RaceTrack} entries
     * @throws SQLException if the query fails
     */
    @Override
    public RaceTrack readByName(String name) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM raceTrack WHERE name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                int length = resultSet.getInt("length");
                String location = resultSet.getString("location");
                return new RaceTrack(name, length, location);
            }
            return null;
        }
    }

    /**
     * Retrieves a race track by its exact length.
     *
     * @param length the length to match
     * @return the matching {@code RaceTrack}, or {@code null} if not found
     * @throws SQLException if the query fails
     */
    @Override
    public RaceTrack readByLength(int length) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM raceTrack WHERE raceLength = ?");
            statement.setInt(1, length);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");
                return new RaceTrack(name, length, location);
            }
            return null;
        }
    }

    /**
     * Retrieves a race track by its location.
     *
     * @param location the location to match (partial match allowed)
     * @return the matching {@code RaceTrack}, or {@code null} if not found
     * @throws SQLException if the query fails
     */
    @Override
    public RaceTrack readByLocation(String location) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM raceTrack WHERE location ILIKE ?");
            statement.setString(1, "%" + location + "%");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int length = resultSet.getInt("raceLength");
                return new RaceTrack(name, length, location);
            }
            return null;
        }
    }



    /**
     * Updates a race track in the database.
     *
     * @param raceTrack the race track to update
     * @throws SQLException if the update fails
     */
    @Override
    public void updateRaceTrack(RaceTrack raceTrack) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "UPDATE raceTrack SET name = ?, raceLength = ?, location = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, raceTrack.getName());
            statement.setInt(2, raceTrack.getLength());
            statement.setString(3, raceTrack.getLocation());
            statement.setString(4, raceTrack.getName()); // assuming name is the unique key
            statement.executeUpdate();
        }
    }

    /**
     * Deletes a race track from the database.
     *
     * @param raceTrack the race track to delete
     * @throws SQLException if the deletion fails
     */
    @Override
    public void delete(RaceTrack raceTrack) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "DELETE FROM raceTrack WHERE name ILIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + raceTrack.getName() + "%");
            statement.executeUpdate();
        }
    }
}
