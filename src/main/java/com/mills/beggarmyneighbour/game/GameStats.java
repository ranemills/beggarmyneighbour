package com.mills.beggarmyneighbour.game;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Player;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class GameStats {

    private final int tricks;
    private final int cards;
    private final Player winner;
    private List<CardValue> initialDeck;

    public GameStats(int tricks, int cards, Player winner) {
        this.tricks = tricks;
        this.cards = cards;
        this.winner = winner;
    }

    public void setInitialDeck(List<CardValue> initialDeck)
    {
        this.initialDeck = initialDeck;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tricks", tricks)
                .append("cards", cards)
                .append("winner", winner)
                .append("initialDeck", initialDeck)
                .toString();
    }
}
