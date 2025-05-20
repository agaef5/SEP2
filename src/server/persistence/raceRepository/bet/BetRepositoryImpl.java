package server.persistence.raceRepository.bet;

import client.ui.util.ErrorHandler;
import server.model.*;
import server.persistence.raceRepository.RaceRepositoryImpl;
import server.persistence.raceRepository.raceTrack.RaceTrackRepImpl;
import server.persistence.shared.ConnectionProviderImpl;
import server.persistence.user.UserRepository;
import server.persistence.user.UserRepositoryImpl;
import shared.DTO.BetDTO;

import java.sql.*;

public class BetRepositoryImpl implements BetRepository
{
    private static BetRepositoryImpl instance;
    private final ConnectionProviderImpl connectionProvider;
    private RaceTrackRepImpl raceTrackRepository;
    private UserRepositoryImpl userRepository;

    public BetRepositoryImpl (ConnectionProviderImpl connectionProvider){
        this.connectionProvider = connectionProvider;
        try {
            raceTrackRepository = RaceTrackRepImpl.getInstance();
        } catch (SQLException e) {
            ErrorHandler.handleError(e, getClass().getName());
        }
    }
    public static synchronized BetRepositoryImpl getInstance()
    {
        if (instance == null){
            instance = new BetRepositoryImpl(new ConnectionProviderImpl());
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return connectionProvider.getConnection();
    }

    @Override
    public Bet create(int betAmount, boolean status) throws SQLException
    {
        try (Connection connection = getConnection()){
               String trackQuery = "SELECT race_id FROM bet " +
                       "WHERE betAmount = ? AND status = ?";
            PreparedStatement trackStatement = connection.prepareStatement(trackQuery);
            trackStatement.setInt(1, betAmount);
            trackStatement.setBoolean(2, status);

//            Execute query
            ResultSet resultSet = trackStatement.executeQuery();

//            If results, return Bet
            if (resultSet.next()) {
                int id = resultSet.getInt("race_id");
                return new Bet(readRace(id), readHorses(id), readUser(id), betAmount);
            }
            return null;
        }
    }

    @Override
    public Bet save(Bet bet) throws SQLException
    {
        return null;
    }

    @Override
    public BetDTO readByUsername(String username) throws SQLException
    {
        return null;
    }

    @Override
    public BetDTO readByRace(String raceName) throws SQLException
    {
        return null;
    }

    @Override
    public BetDTO readByStatus(boolean status) throws SQLException
    {
        return null;
    }

    @Override
    public BetDTO readByRaceTrack(String rtName) throws SQLException
    {
        return null;
    }

    @Override
    public void updateBet(Bet bet) throws SQLException
    {

    }

    @Override
    public void deleteBet(Bet bet) throws SQLException
    {

    }

    private Horse readHorses (int race_id) throws SQLException{
        try (Connection connection = getConnection())
        {
            String horseQuery = "SELECT h.id, h.name, h.speedMin, h.speedMax " +
                    "FROM participant p " +
                    "JOIN sep2.horse h ON h.id = p.horse_id " +
                    "WHERE p.race_id = ?";
            PreparedStatement horseStatement = connection.prepareStatement(horseQuery);
            horseStatement.setInt(1, race_id);
            ResultSet hrs = horseStatement.executeQuery();

            int horseID = hrs.getInt("id");
            String name = hrs.getString("name");
            int speedMin = hrs.getInt("speedMin");
            int speedMax = hrs.getInt("speedMax");

            return new Horse(horseID, name, speedMin, speedMax);
        }
    }

    private Race readRace(int race_id) throws SQLException{
        try (Connection connection = getConnection())
        {
            String raceQuery = "SELECT r.name AS race_name, rt.name AS track_name " +
                    "FROM race r " +
                    "JOIN raceTrack rt ON r.racetrack_id = rt.id " +
                    "WHERE r.id = ?";
            PreparedStatement statement = connection.prepareStatement(raceQuery);
            statement.setInt(1, race_id);
            ResultSet resultSet = statement.executeQuery();

            String raceName = resultSet.getString("race_name");
            int racetrackId = resultSet.getInt("racetrack_id");
            RaceTrack raceTrackEntity = raceTrackRepository.readById(racetrackId);

            return new Race(raceName, raceTrackEntity, null);
        }
    }

    private User readUser(int race_id) throws SQLException{
        try (Connection connection = getConnection())
        {
            String query = "SELECT gu.username " +
                    "FROM raceObserver ro " +
                    "JOIN game_user gu ON gu.username = ro.player_username " +
                    "WHERE ro.race_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, race_id);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                return null;
            }

            String username = resultSet.getString("username");
            return userRepository.readByUsername(username);
        }
    }
}
