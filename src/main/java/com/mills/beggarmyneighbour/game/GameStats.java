package com.mills.beggarmyneighbour.game;

import com.mills.beggarmyneighbour.models.Player;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GameStats {

    private final int tricks;
    private final int cards;
    private final Player winner;

    public GameStats(int tricks, int cards, Player winner) {
        this.tricks = tricks;
        this.cards = cards;
        this.winner = winner;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tricks", tricks)
                .append("cards", cards)
                .append("winner", winner)
                .toString();
    }
}
