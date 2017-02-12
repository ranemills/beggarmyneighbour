package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;

import java.util.Random;

/**
 * Created by ryan on 12/02/17.
 */
public class MoveKingMutationStrategy
    implements MutationStrategy
{
    private static final Random RANDOM = new Random();

    @Override
    public void mutateDeck(Deck deck) {
        int i = deck.indexOf(CardValue.KING);
        int j = RANDOM.nextInt(52);
        deck.add(j, deck.remove(i));
    }
}
