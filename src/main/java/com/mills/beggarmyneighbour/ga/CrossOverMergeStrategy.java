package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;

public class CrossOverMergeStrategy implements MergeStrategy {
    private static final Random RANDOM = new Random();

    @Override
    public Pair<Deck, Deck> mergeDecks(Deck deck1, Deck deck2) {
        if (deck1.equals(deck2)) {
            return Pair.of(deck1, deck2);
        }
        Deck deck1Clone = new Deck(deck1);
        Deck deck2Clone = new Deck(deck2);

        int i = RANDOM.nextInt(52);
        while (deck1Clone.get(i) == CardValue.NON_FACE &&
               deck2Clone.get(i) == CardValue.NON_FACE) {
            i = RANDOM.nextInt(52);
        }

        crossOverLists(deck1Clone, deck2Clone, i);

        while (!deck1Clone.isValidDeck()) {
            i++;
            if (i > 51) {
                i = 0;
            }
            crossOverLists(deck1Clone, deck2Clone, i);
        }

        return Pair.of(deck1Clone, deck2Clone);
    }

    private void crossOverLists(Deck deck1, Deck deck2, int index)
    {
        CardValue deck1Value = deck1.remove(index);
        CardValue deck2Value = deck2.remove(index);

        deck1.add(index, deck2Value);
        deck2.add(index, deck1Value);
    }
}
