package com.mills.beggarmyneighbour.game;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    private Deck initialDeck;
    @Id
    private String deckRepresentation;
    @Transient
    private SpecificDeckRepresentation specificDeckRepresentation;

    public GameStats(int tricks, int cards, Player winner) {
        this.tricks = tricks;
        this.cards = cards;
        this.winner = winner;
    }

    public SpecificDeckRepresentation getSpecificDeckRepresentation() {
        return specificDeckRepresentation;
    }

    public GameStats setSpecificDeckRepresentation(SpecificDeckRepresentation specificDeckRepresentation) {
        setInitialDeck(specificDeckRepresentation.toDeck());
        this.specificDeckRepresentation = specificDeckRepresentation;
        return this;
    }

    private String initialDeckToString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (CardValue value : initialDeck) {
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
                   .append("initialDeck", initialDeck.toString())
                   .toString();
    }

    public int getTricks() {
        return tricks;
    }

    public int getCards() {
        return cards;
    }

    public Player getWinner() {
        return winner;
    }

    public Deck getInitialDeck() {
        return initialDeck;
    }

    public void setInitialDeck(Deck initialDeck)
    {
        this.initialDeck = initialDeck;
        this.deckRepresentation = initialDeckToString();
    }

    public String getDeckRepresentation() {
        return deckRepresentation;
    }
}
