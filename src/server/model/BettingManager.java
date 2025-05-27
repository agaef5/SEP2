package server.model;

import client.ui.util.ErrorHandler;
import server.networking.Server;
import server.persistence.raceRepository.bet.BetRepository;
import server.persistence.raceRepository.bet.BetRepositoryImpl;
import server.persistence.user.UserRepository;
import server.persistence.user.UserRepositoryImpl;
import server.util.DTOMapper;
import shared.DTO.HorseDTO;
import shared.updates.BettingOpenUpdate;
import shared.updates.OnRaceFinished;
import shared.updates.OnRaceStarted;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Singleton manager class for handling bets during a race.
 * Responsible for accepting bets, processing payouts when a race finishes,
 * and managing user balances.
 *
 * Implements {@link RaceListener} to receive callbacks from the race lifecycle.
 */
public class BettingManager implements RaceListener {

    private static volatile BettingManager instance;

    private final BetRepository betRepository = BetRepositoryImpl.getInstance();
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();

    private final List<Bet> openBets = new ArrayList<>();
    private Race currentRace;
    private boolean bettingOpen = false;

    /** Private constructor to enforce singleton pattern. */
    private BettingManager() { }

    /**
     * Returns the singleton instance of the BettingManager using
     * double-checked locking for thread safety.
     *
     * @return the global BettingManager instance
     */
    public static BettingManager getInstance() {
        if (instance == null) {
            synchronized (BettingManager.class) {
                if (instance == null) {
                    instance = new BettingManager();
                }
            }
        }
        return instance;
    }

    /**
     * Called when a race is ready to accept bets.
     *
     * @param race the race that has opened for betting
     */
    @Override
    public void bettingOpen(Race race) {
        this.currentRace = Objects.requireNonNull(race);
        this.bettingOpen = true;
        openBets.clear();
    }

    /**
     * Called when a horse finishes a race.
     * This implementation does nothing, as settlement happens when the race ends.
     */
    @Override
    public void onHorseFinished(Horse horse, int position) {
        // no-op
    }

    /**
     * Called when the race starts. Betting is closed at this point.
     *
     * @param race the race that has started
     */
    @Override
    public void onRaceStarted(Race race) {
        if (!race.equals(currentRace)) return;
        this.bettingOpen = false;
        System.out.println("Race " + race.getName() + " has started — no more bets!");
    }

    /**
     * Called when the race finishes.
     * Processes all open bets, pays out winners, and resets manager state.
     *
     * @param race           the completed race
     * @param finalPosition  list of horses in their final placement order
     */
    @Override
    public void onRaceFinished(Race race, HorseList finalPosition) {
        if (!race.equals(currentRace)) return;

        Horse winner = finalPosition.getList().get(0);

        for (Bet bet : openBets) {
            boolean won = bet.isWinning(winner);
            bet.setWinningBet(won);

            if (won) {
                int payout = bet.getBetAmount() * 2;
                User user = bet.getUser();
                int newBal = user.getBalance().getAmount() + payout;
                user.getBalance().setAmount(newBal);
                try {
                    userRepository.updateBalance(user.getUsername(), newBal);
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to update balance for user " + user.getUsername(), e);
                }
            }

            try {
                betRepository.save(bet);
            } catch (SQLException e) {
                ErrorHandler.handleError(new RuntimeException(
                        "Failed to save bet for user " + bet.getUser().getUsername(), e), getClass().getName());
            }
        }

        openBets.clear();
        currentRace = null;
        System.out.println("Race " + race.getName() + " settled; manager reset.");
    }

    /**
     * Places a new bet for a user.
     * Deducts the amount from user's balance and stores the bet.
     *
     * @param user   the user placing the bet
     * @param horse  the horse being bet on
     * @param amount the amount wagered
     * @return the created Bet object
     * @throws IllegalStateException if betting is closed
     */
    public Bet placeBet(User user, Horse horse, int amount) {
        if (!bettingOpen) {
            throw new IllegalStateException("Betting is closed");
        }

        int newBal = user.getBalance().getAmount() - amount;
        user.getBalance().setAmount(newBal);
        try {
            userRepository.updateBalance(user.getUsername(), newBal);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update balance for user " + user.getUsername(), e);
        }

        Bet bet = new Bet(currentRace, horse, user, amount);
        openBets.add(bet);

        try {
            betRepository.save(bet);
        } catch (SQLException e) {
            ErrorHandler.handleError(new RuntimeException(
                    "Failed to save bet for user " + bet.getUser().getUsername(), e), getClass().getName());
        }

        return bet;
    }

    /**
     * Returns the race currently open for betting.
     *
     * @return the current race if betting is open; otherwise null
     */
    public Race getCurrentRace() {
        return bettingOpen ? currentRace : null;
    }
}
