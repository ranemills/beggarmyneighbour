package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import com.mills.beggarmyneighbour.utils.CardOperations;

import java.util.Random;

public class RandomDeckMutationStrategy
    implements MutationStrategy
{
    @Override
    public SpecificDeckRepresentation mutateDeck(SpecificDeckRepresentation deck) {
        return CardOperations.getShuffledDeck();
    }
}
