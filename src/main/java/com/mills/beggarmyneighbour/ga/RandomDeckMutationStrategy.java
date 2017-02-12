package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.utils.CardOperations;

import java.util.Random;

/**
 * Created by ryan on 12/02/17.
 */
public class RandomDeckMutationStrategy
    implements MutationStrategy
{
    private static final Random RANDOM = new Random();

    @Override
    public Deck mutateDeck(Deck deck) {
        return CardOperations.getShuffledDeck();
    }
}
