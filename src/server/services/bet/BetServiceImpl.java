package server.services.bet;

import server.persistence.horses.HorseRepository;
import server.persistence.horses.HorseRepositoryImpl;
import server.persistence.user.UserRepository;
import server.persistence.user.UserRepositoryImpl;
import shared.DTO.*;
import server.model.Bet;
import server.model.BettingManager;
import server.model.Horse;
import server.model.Race;
import server.model.User;


import java.sql.SQLException;
import java.util.List;

public class BetServiceImpl implements BetService {
    private static volatile BetServiceImpl instance;

    private final UserRepository userRepo        = UserRepositoryImpl.getInstance();
    private final HorseRepository horseRepo       = HorseRepositoryImpl.getInstance();
    private final BetRepository     betRepo         = BetRepositoryImpl.getInstance();
    private final BettingManager    bettingManager  = BettingManager.getInstance();

    private BetServiceImpl() throws SQLException { /* singleton */ }

    public static BetServiceImpl getInstance() throws SQLException {
        if (instance == null) {
            synchronized (BetServiceImpl.class) {
                if (instance == null) {
                    instance = new BetServiceImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Place a new bet. SQLExceptions from the repos are caught
     * and rethrown as RuntimeExceptions.
     */
    public Bet createBet(String username, HorseDTO horseDto, int amount) {
        try {
            User user = userRepo.readByUsername(username);
            if (user == null) {
                throw new IllegalArgumentException("Unknown user: " + username);
            }

            Horse horse = horseRepo.readByID(horseDto.id());
            if (horse == null) {
                throw new IllegalArgumentException("Unknown horse ID: " + horseDto.id());
            }

            Race race = bettingManager.getCurrentRace();
            if (race == null) {
                throw new IllegalStateException("No race currently open for betting");
            }

            // Delegate to the domain manager (which itself persists the Bet)
            return bettingManager.placeBet(user, horse, amount);

        } catch (SQLException e) {
            throw new RuntimeException("Database error while creating bet for user " + username, e);
        }
    }

    /**
     * Fetch all bets ever placed by a given user. Wraps SQLExceptions similarly.
     */
    public List<BetDTO> getBetsByUser(String username) {
        try {
            User user = userRepo.readByUsername(username);
            if (user == null) {
                throw new IllegalArgumentException("Unknown user: " + username);
            }
            return betRepo.readByUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching bets for user " + username, e);
        }
    }

    /**
     * Persist any Bet (e.g. if you settle it outside of createBet).
     */
    public void saveBet(Bet bet) {
        try {
            betRepo.save(bet);
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database error while saving bet for user " + bet.getUser().getUsername(), e);
        }
    }
}
