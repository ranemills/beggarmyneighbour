package com.mills.beggarmyneighbour.models;

import java.util.Objects;

public class Card {
    private final CardValue value;

    public Card(CardValue value) {
        this.value = value;
    }

    public CardValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(value, card.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
