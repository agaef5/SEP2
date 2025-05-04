package server.persistence.raceRepository.raceTrack;

import server.model.Race;
import server.model.RaceTrack;
import server.persistence.raceRepository.RaceRepositoryimpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaceTrackRepImpl implements RaceTrackRep {
    private static RaceTrackRepImpl instance;
    private RaceTrackRepImpl() throws SQLException{
        DriverManager.registerDriver(new org.postgresql.Driver());
    }
    public static synchronized RaceTrackRepImpl getInstance() throws SQLException{
        if (instance == null){
            instance = new RaceTrackRepImpl();
        }
        return instance;
    }
    private Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2",
                "postgres", "1234");
    }
    @Override
    public RaceTrack create(String name, int length, String location) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO raceTrack" + " (name, raceLength, location) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(
                    query,
                    PreparedStatement.RETURN_GENERATED_KEYS);
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

    @Override
    public List<RaceTrack> readByName(String name) throws SQLException {
        try ( Connection connection = getConnection() )
        {
            String query = "SELECT * FROM raceTrack" + " WHERE name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(
                    query);
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<RaceTrack> result = new ArrayList<>();
            while (resultSet!= null && resultSet.next())
            {
                int length = resultSet.getInt("raceLength");
                String resultSetString = resultSet.getString("name");
                String location = resultSet.getString("location");
                RaceTrack raceTrack = new RaceTrack(resultSetString, length, location); //wrong constructor
                result.add(raceTrack);
            }
            return result;
        }
    }

    @Override
    public RaceTrack readByLength(int length) throws SQLException {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM raceTrack WHERE length = ?");
            statement.setInt(1, length);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");
                return new RaceTrack(name, length, location);
            } else {
                return null;
            }
        }
    }

    @Override
    public RaceTrack readByLocation(String location) throws SQLException {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM raceTrack WHERE location LIKE ?");
            statement.setString(1, "%" + location + "%");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int length = resultSet.getInt("raceLength");
                RaceTrack raceTrack = new RaceTrack(name, length, location);
                return raceTrack;
            } else {
                return null;
            }
        }
    }

    @Override
    public void updateRacer(RaceTrack raceTrack) throws SQLException {
        try ( Connection connection = getConnection() )
        {
            String query = "UPDATE " + raceTrack.getClass().getName().toLowerCase()
                    + " name = ?, status = ?, startTime = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, raceTrack.getName());
            statement.setInt(2, raceTrack.getLength());
            statement.setString(3, raceTrack.getLocation());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(RaceTrack raceTrack) throws SQLException {
        try ( Connection connection = getConnection() )
        {
            String query = "DELETE FROM " + raceTrack.getClass().getName().toLowerCase()
                    + "WHERE name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(
                    query);
            statement.setString(1, "%" + raceTrack.getName() + "%");
            statement.executeUpdate();
        }
    }
}
