package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CrossOverMergeStrategy implements MergeStrategy {
    private static final Random RANDOM = new Random();

    private static final Logger LOGGER = LoggerFactory.getLogger(CrossOverMergeStrategy.class);

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

    @Override
    public Pair<SpecificDeckRepresentation, SpecificDeckRepresentation> mergeDecks(SpecificDeckRepresentation deck1,
                                                                                   SpecificDeckRepresentation deck2)
    {
        List<Integer> deck1Rep = deck1.toList();
        List<Integer> deck2Rep = deck2.toList();

        List<Integer> deck1Clone = new ArrayList<>(deck1Rep);
        List<Integer> deck2Clone = new ArrayList<>(deck2Rep);


        boolean isValid = false;
        while(!isValid || deck1Clone == deck1Rep || deck2Clone == deck2Rep) {
            int i = RANDOM.nextInt(16);
            Integer deck1int = deck1Rep.get(i);
            Integer deck2int = deck2Rep.get(i);

            deck1Rep.set(i, deck2int);
            deck2Rep.set(i, deck1int);

            isValid = (new HashSet<>(deck1Rep).size() == 16 && new HashSet<>(deck2Rep).size() == 16);
        }

        SpecificDeckRepresentation newDeck1 = SpecificDeckRepresentation.fromOrderedList(deck1Rep);
        SpecificDeckRepresentation newDeck2 = SpecificDeckRepresentation.fromOrderedList(deck2Rep);


        return Pair.of(newDeck1, newDeck2);
    }

    private void crossOverLists(Deck deck1, Deck deck2, int index)
    {
        CardValue deck1Value = deck1.remove(index);
        CardValue deck2Value = deck2.remove(index);

        deck1.add(index, deck2Value);
        deck2.add(index, deck1Value);
    }
}
