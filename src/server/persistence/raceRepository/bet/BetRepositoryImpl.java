    package server.persistence.raceRepository.bet;

    import client.ui.util.ErrorHandler;
    import server.model.*;
    import server.persistence.raceRepository.raceTrack.RaceTrackRepImpl;
    import server.persistence.shared.ConnectionProviderImpl;
    import server.persistence.user.UserRepositoryImpl;
    import shared.DTO.BetDTO;

    import java.sql.*;

    /**
     * Repository implementation for handling bet-related persistence operations.
     * Provides methods to create, read, update, and delete bets from the database.
     */
    public class BetRepositoryImpl implements BetRepository
    {
        private static BetRepositoryImpl instance;
        private final ConnectionProviderImpl connectionProvider;
        private RaceTrackRepImpl raceTrackRepository;
        private UserRepositoryImpl userRepository;


        /**
         * Constructs a new BetRepositoryImpl with the provided ConnectionProvider.
         * Initializes the raceTrackRepository.
         *
         * @param connectionProvider the provider for database connections
         */
        public BetRepositoryImpl (ConnectionProviderImpl connectionProvider){
            this.connectionProvider = connectionProvider;
            try {
                raceTrackRepository = RaceTrackRepImpl.getInstance();
            } catch (SQLException e) {
                ErrorHandler.handleError(e, getClass().getName());
            }
        }

        /**
         * Returns the singleton instance of BetRepositoryImpl.
         *
         * @return the singleton instance
         */
        public static synchronized BetRepositoryImpl getInstance()
        {
            if (instance == null){
                instance = new BetRepositoryImpl(new ConnectionProviderImpl());
            }
            return instance;
        }

        /**
         * Gets a new database connection from the connection provider.
         *
         * @return a SQL Connection
         * @throws SQLException if connection cannot be established
         */
        private Connection getConnection() throws SQLException {
            return connectionProvider.getConnection();
        }

        /**
         * Creates a Bet object from the database if one matches the specified amount and status.
         *
         * @param betAmount the amount of the bet
         * @param status    the status of the bet
         * @return a Bet object if found, otherwise null
         * @throws SQLException if a database error occurs
         */
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


        /**
         * Saves a bet to the database.
         *
         * @param bet the Bet object to save
         * @return the saved Bet
         * @throws SQLException if a database error occurs
         */
        @Override
        public Bet save(Bet bet) throws SQLException
        {
            return null;
        };

        /**
         * Reads a bet by the username of the user.
         *
         * @param username the username to search by
         * @return a BetDTO if found
         * @throws SQLException if a database error occurs
         */
        @Override
        public BetDTO readByUsername(String username) throws SQLException
        {
            return null;
        }

        /**
         * Reads a bet by race name.
         *
         * @param raceName the name of the race
         * @return a BetDTO if found
         * @throws SQLException if a database error occurs
         */
        @Override
        public BetDTO readByRace(String raceName) throws SQLException
        {
            return null;
        }

        /**
         * Reads bets by their status (e.g., active/inactive).
         *
         * @param status the status of the bet
         * @return a BetDTO if found
         * @throws SQLException if a database error occurs
         */
        @Override
        public BetDTO readByStatus(boolean status) throws SQLException
        {
            return null;
        }

        /**
         * Reads bets by the name of the race track.
         *
         * @param rtName the name of the racetrack
         * @return a BetDTO if found
         * @throws SQLException if a database error occurs
         */
        @Override
        public BetDTO readByRaceTrack(String rtName) throws SQLException
        {
            return null;
        }

        /**
         * Updates the given bet in the database.
         *
         * @param bet the Bet to update
         * @throws SQLException if a database error occurs
         */
        @Override
        public void updateBet(Bet bet) throws SQLException
        {

        }

        /**
         * Deletes the given bet from the database.
         *
         * @param bet the Bet to delete
         * @throws SQLException if a database error occurs
         */
        @Override
        public void deleteBet(Bet bet) throws SQLException
        {

        }

        /**
         * Reads the horse participating in a race by race ID.
         *
         * @param race_id the race ID
         * @return the Horse participating in the race
         * @throws SQLException if a database error occurs
         */
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

        /**
         * Reads race data by race ID.
         *
         * @param race_id the race ID
         * @return the Race object associated with the given ID
         * @throws SQLException if a database error occurs
         */
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


        /**
         * Reads the user who placed a bet in a race by race ID.
         *
         * @param race_id the race ID
         * @return the User object or null if not found
         * @throws SQLException if a database error occurs
         */
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
