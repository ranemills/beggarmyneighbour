package com.mills.beggarmyneighbour.models;

import java.util.ArrayList;
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

    public Deck() {
        super();
    }

    public Boolean isValidDeck()
    {
        Deck deck = new Deck(this);
        for(CardValue cardValue : EnumSet.allOf(CardValue.class))
        {
            for(int i=0; i<cardValue.getRequiredInDeck(); i++)
            {
                if(!deck.remove(cardValue)) { return false; }
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