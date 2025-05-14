package server.model;

import java.util.Objects;

public class Bet {
    private Race race;
    private Horse horse;
    private User user;
    private int betAmount;
    private boolean isWinningBet;

    public Bet(Race race, Horse horse,User user, int betAmount)
    {
        this.race=race;
        this.horse=horse;
        this.user=user;
        this.betAmount=betAmount;
    }


    public Race getRace() {
        return race;
    }

    public Horse getHorse() {
        return horse;
    }

    public User getUser() {
        return user;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public boolean isWinning(Horse actualWinner) {
        return this.horse.equals(actualWinner);
    }








    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return betAmount == bet.betAmount && Objects.equals(race, bet.race) && Objects.equals(horse, bet.horse) && Objects.equals(user, bet.user);
    }

    @Override
    public String toString() {
        return "Bet{" +
                "race=" + race +
                ", horse=" + horse +
                ", user=" + user +
                ", betAmount=" + betAmount +
                '}';
    }

    public boolean isWinningBet() {
        return isWinningBet;
    }

    public void setWinningBet(boolean winningBet) {
        isWinningBet = winningBet;
    }
}
