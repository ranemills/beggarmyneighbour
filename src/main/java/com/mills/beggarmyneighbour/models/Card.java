package com.mills.beggarmyneighbour.models;

/**
 * Created by ryan on 08/02/17.
 */
public class Card {
    private final Integer value;
    private final Suit suit;

    public Card(Suit suit, Integer value) {
        this.value = value;
        this.suit = suit;
    }

    public Integer getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return String.format("%s%s", value, suit.toString().charAt(0));
    }
}
