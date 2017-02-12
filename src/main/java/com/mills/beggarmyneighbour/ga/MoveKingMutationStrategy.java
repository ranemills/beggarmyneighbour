package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;

import java.util.Random;

public class MoveKingMutationStrategy
    implements MutationStrategy
{
    private static final Random RANDOM = new Random();

    public Deck mutateDeck(Deck deck) {
        int i = deck.indexOf(CardValue.KING);
        int j = RANDOM.nextInt(52);
        deck.add(j, deck.remove(i));
        return deck;
    }

    @Override
    public SpecificDeckRepresentation mutateDeck(SpecificDeckRepresentation deck) {
        return SpecificDeckRepresentation.fromDeck(mutateDeck(deck.toDeck()));
    }
}
