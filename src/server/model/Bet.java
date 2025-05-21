package server.model;

import java.util.Objects;

/**
 * Represents a bet placed by a user on a specific horse in a race.
 */
public class Bet {
    private Race race;
    private Horse horse;
    private User user;
    private int betAmount;
    private boolean isWinningBet;

    /**
     * Constructs a Bet instance.
     *
     * @param race      the race the bet is placed on
     * @param horse     the horse the user is betting on
     * @param user      the user placing the bet
     * @param betAmount the amount of the bet
     */
    public Bet(Race race, Horse horse,User user, int betAmount)
    {
        this.race=race;
        this.horse=horse;
        this.user=user;
        this.betAmount=betAmount;
    }

    /**
     * Returns the race associated with this bet.
     *
     * @return the race
     */
    public Race getRace() {
        return race;
    }

    /**
     * Returns the horse the user bet on.
     *
     * @return the horse
     */
    public Horse getHorse() {
        return horse;
    }

    /**
     * Returns the user who placed the bet.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the amount of money placed on the bet.
     *
     * @return the bet amount
     */
    public int getBetAmount() {
        return betAmount;
    }


    /**
     * Checks if this bet is winning by comparing the chosen horse to the actual winner.
     *
     * @param actualWinner the horse that actually won the race
     * @return true if this bet is on the winning horse, false otherwise
     */
    public boolean isWinning(Horse actualWinner) {
        return this.horse.equals(actualWinner);
    }

    /**
     * Returns whether this bet was marked as a winning bet.
     * (Used after race finishes and results are processed)
     *
     * @return true if the bet is marked as a winning bet
     */
    public boolean isWinningBet() {
        return isWinningBet;
    }

    /**
     * Marks this bet as a winning or losing bet.
     * Should be called after race result is known.
     *
     * @param winningBet true to mark as winning, false otherwise
     */
    public void setWinningBet(boolean winningBet) {
        isWinningBet = winningBet;
    }

    /**
     * Compares this bet to another object for equality.
     * Two bets are equal if they have the same race, horse, user, and bet amount.
     *
     * @param o the object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return betAmount == bet.betAmount && Objects.equals(race, bet.race) && Objects.equals(horse, bet.horse) && Objects.equals(user, bet.user);
    }

    /**
     * Returns a string representation of this bet.
     *
     * @return string representation of the bet
     */
    @Override
    public String toString() {
        return "Bet{" +
                "race=" + race +
                ", horse=" + horse +
                ", user=" + user +
                ", betAmount=" + betAmount +
                '}';
    }
}
