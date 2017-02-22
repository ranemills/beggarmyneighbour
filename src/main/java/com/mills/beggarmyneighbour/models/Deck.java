package com.mills.beggarmyneighbour.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

public class Deck
    extends ArrayList<CardValue>
    implements List<CardValue>
{
    public Deck(Deck deck) {
        super(deck);
    }

    public Deck(List<CardValue> list)
    {
        super(list);
    }

    public Deck() {
        super();
    }

    public Boolean isValidDeck()
    {
        for (CardValue cardValue : EnumSet.allOf(CardValue.class)) {
            int frequency = Collections.frequency(this, cardValue);
            if (frequency > cardValue.getRequiredInDeck()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (CardValue value : this) {
            stringBuilder.append(value.getAsciiChar());
        }
        return stringBuilder.toString();
    }
}
