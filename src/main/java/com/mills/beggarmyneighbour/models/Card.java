package com.mills.beggarmyneighbour.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(value, card.value) &&
                suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, suit);
    }
}
