package server.model;

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

public class BettingManager implements RaceListener {

    private static volatile BettingManager instance;

    // use the singleton repositories directly
//    private final BetRepository betRepository  = BetRepositoryImpl.getInstance();
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();

    private final List<Bet>        openBets       = new ArrayList<>();
    private Race                   currentRace;
    private boolean                bettingOpen    = false;

    // private ctor so nobody else can new-up
    private BettingManager() { }

    /**
     * Double-checked locking for a thread-safe singleton
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

    @Override
    public void bettingOpen(Race race) {
        this.currentRace = Objects.requireNonNull(race);
        this.bettingOpen = true;
        openBets.clear();
//
//        BettingOpenUpdate payload = new BettingOpenUpdate(race.getName());
//        Server.broadcast("bettingOpen", payload);
    }

    @Override
    public void onHorseFinished(Horse horse, int position) {
        // no-op; we just settle in onRaceFinished
    }

    @Override
    public void onRaceStarted(Race race) {
        if (!race.equals(currentRace)) return;
        this.bettingOpen = false;
        System.out.println("Race " + race.getName() + "has started — no more bets!");

//        OnRaceStarted payload = new OnRaceStarted(race.getName());
//        Server.broadcast("onRaceStarted",payload);
    }

    @Override
    public void onRaceFinished(Race race, HorseList finalPosition) {
        if (!race.equals(currentRace)) return;

        // winner is the first in the finalPositions list
        Horse winner = finalPosition.getList().get(0);

        for (Bet bet : openBets) {
            boolean won = bet.isWinning(winner);
            bet.setWinningBet(won);

            if (won) {
                int payout = bet.getBetAmount() * 2;
                User u = bet.getUser();
                int newBal = u.getBalance().getAmount() + payout;
                u.getBalance().setAmount(newBal);
                try {
                    userRepository.updateBalance(u.getUsername(), newBal);
                } catch (SQLException e) {
                    throw new RuntimeException(
                            "Failed to update balance for user " + u.getUsername(), e);
                }
            }

//            try {
//                betRepository.save(bet);
//            } catch (SQLException e) {
//                throw new RuntimeException(
//                        "Failed to save bet for user " + bet.getUser().getUsername(), e);
//            }
        }

        openBets.clear();
        currentRace = null;
        System.out.println("Race " + race.getName() + " settled; manager reset.");
//
//        List<HorseDTO> finalPositionsDTO = DTOMapper.horseListToDTO(finalPosition);
//        OnRaceFinished payload = new OnRaceFinished(race.getName(),finalPositionsDTO);
//        Server.broadcast("onRaceFinished",payload);
    }

    /**
     * Called by your application / service layer when a user places a bet.
     */
    public Bet placeBet(User user, Horse horse, int amount) {
        if (!bettingOpen) {
            throw new IllegalStateException("Betting is closed");
        }

        // debit immediately
        int newBal = user.getBalance().getAmount() - amount;
        user.getBalance().setAmount(newBal);
        try {
            userRepository.updateBalance(user.getUsername(), newBal);
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update balance for user " + user.getUsername(), e);
        }

        Bet bet = new Bet(currentRace, horse, user, amount);
        openBets.add(bet);

//        try {
//            betRepository.save(bet);
//        } catch (SQLException e) {
//            throw new RuntimeException(
//                    "Failed to save new bet for user " + user.getUsername(), e);
//        }
        return bet;
    }

    /**
     * @return the Race currently open for betting, or null if none
     */
    public Race getCurrentRace() {
        return bettingOpen ? currentRace : null;
    }
}
