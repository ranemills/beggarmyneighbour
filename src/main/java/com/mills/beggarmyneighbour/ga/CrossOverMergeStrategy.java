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
}
