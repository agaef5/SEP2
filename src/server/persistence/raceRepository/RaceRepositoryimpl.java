package server.persistence.raceRepository;

import server.model.Race;
import server.model.Horse;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RaceRepositoryimpl implements RaceRepository{
    private static RaceRepositoryimpl instance;
    private RaceRepositoryimpl() throws SQLException{
        DriverManager.registerDriver(new org.postgresql.Driver());
    }
    public static synchronized RaceRepositoryimpl getInstance() throws SQLException{
        if (instance == null){
            instance = new RaceRepositoryimpl();
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
    public Race create(String name, Date time, String status) throws SQLException {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO race" + " (name, startTime, status) VALUES (?, ?, ?)"; //admin id?
            PreparedStatement statement = connection.prepareStatement(
                    query,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setDate(2, (java.sql.Date) time);
            statement.setString(3, status);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
//                return new Race(resultSet.getInt(1), name); needs id in constructor
            } else {
                throw new SQLException("No keys generated");
            }
        }
        return null; //placeholder
    }

    @Override
    public Race readByID(int id) throws SQLException {
        try ( Connection connection = getConnection() )
        {
            String query = "SELECT * FROM race"+ " WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(
                    query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet.next() )
            {
                String race = resultSet.getString("name");
                return new Race(race, 3); //3 is placeholder to temporary match constructor
            }
            else
            {
                return null;
            }
        }
    }

    @Override
    public List<Race> readByName(String searchName) throws SQLException {
        try ( Connection connection = getConnection() )
        {
            String query = "SELECT * FROM race" + " WHERE name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(
                    query);
            statement.setString(1, "%" + searchName + "%");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Race> result = new ArrayList<>();
            while (resultSet!= null && resultSet.next())
            {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Race race = new Race(name, 3); //wrong constructor
                result.add(race);
            }
            return result;
        }
    }

    @Override
    public Race readByTime(Date time) throws SQLException {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM race" +
                    " WHERE  = ?"); //!!
            statement.setDate(1, (java.sql.Date) time);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");

                Race race = new Race(name, 3); //!!
                return race;
            } else {
                return null;
            }
        }
    }

    @Override
    public Race readByStatus(String status) throws SQLException {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM race" +
                    " WHERE status LIKE ?"); //!!
            statement.setString(1, "%" + status + "%");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Race race = new Race(name, 3); //!!
                return race;
            } else {
                return null;
            }
        }
    }

    @Override
    public void updateRacer(Race race) throws SQLException {
        try ( Connection connection = getConnection() )
        {
            String query = "UPDATE " + race.getClass().getName().toLowerCase()
                    + " name = ?, status = ?, startTime = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, race.getName());
            statement.setString(2, race.getStatus().toString());
            statement.setString(3, null); //just placeholder
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Race race) throws SQLException {
        try ( Connection connection = getConnection() )
        {
            String query = "DELETE FROM " + race.getClass().getName().toLowerCase()
                    + "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(
                    query);
            statement.setInt(1, 3); //pkaceholder
            statement.executeUpdate();
        }
    }
}
