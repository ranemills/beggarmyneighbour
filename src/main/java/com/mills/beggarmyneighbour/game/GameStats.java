package com.mills.beggarmyneighbour.game;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Player;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document
public class GameStats {

    @Indexed
    @Field
    private final int tricks;
    @Indexed
    @Field
    private final int cards;
    @Field
    private final Player winner;
    @Field
    private List<CardValue> initialDeck;
    @Field("deck_representation")
    private String deckRepresentation;

    public GameStats(int tricks, int cards, Player winner) {
        this.tricks = tricks;
        this.cards = cards;
        this.winner = winner;
    }

    public void setInitialDeck(List<CardValue> initialDeck)
    {
        this.initialDeck = initialDeck;
        this.deckRepresentation = initialDeckToString();
    }

    private String initialDeckToString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(CardValue value : initialDeck)
        {
            stringBuilder.append(value.getAsciiChar());
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tricks", tricks)
                .append("cards", cards)
                .append("winner", winner)
                .append("initialDeck", initialDeckToString())
                .toString();
    }


}
