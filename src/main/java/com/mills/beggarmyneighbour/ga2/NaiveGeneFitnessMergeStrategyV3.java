package com.mills.beggarmyneighbour.ga2;

import com.google.common.collect.ImmutableList;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.DeckOfGenes;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NaiveGeneFitnessMergeStrategyV3 implements MergeStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveGeneFitnessMergeStrategyV3.class);
    private static List<CardValue> allCardValueAsList = new ArrayList<>(EnumSet.allOf(CardValue.class));

    @Override
    public Collection<DeckOfGenes> mergeDecks(DeckOfGenes deck1, DeckOfGenes deck2) {
        Map<Integer, Pair<CardValue, Double>> locationOfCards = new HashMap<>();
        List<CardValue> cardValuesInList = new ArrayList<>();

        for(Integer i : random52())
        {
            Pair<CardValue, Double> cardFromDeck1 = deck1.get(i);
            Pair<CardValue, Double> cardFromDeck2 = deck2.get(i);

            Pair<CardValue, Double> cardToAdd;

            double random = Math.random();

            if (random > 0.95) {
                cardToAdd = cardFromDeck1;
            } else if (random > 0.9) {
                cardToAdd = cardFromDeck2;
            } else {
                cardToAdd = cardFromDeck1.getValue() > cardFromDeck2.getValue() ?
                                                       cardFromDeck1 : cardFromDeck2;
            }
            locationOfCards.put(i, cardToAdd);
            cardValuesInList.add(cardToAdd.getKey());

            if(!checkValidity(cardValuesInList))
            {
                locationOfCards.remove(i);
                cardValuesInList.remove(cardToAdd.getKey());

                Collections.shuffle(allCardValueAsList);

                for(CardValue cardValue :  allCardValueAsList)
                {
                    cardValuesInList.add(cardValue);
                    if(checkValidity(cardValuesInList))
                    {
                        Pair<CardValue, Double> newCard = Pair.of(cardValue, Math.random());
                        locationOfCards.put(i, newCard);
                        break;
                    }
                    else
                    {
                        cardValuesInList.remove(cardValue);
                    }
                }
            }
        }

        DeckOfGenes newDeck = new DeckOfGenes(locationOfCards.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue).collect(Collectors.toList()));

        return ImmutableList.of(newDeck);
    }

    private boolean checkValidity(List<CardValue> cardValueList)
    {
        return new Deck(cardValueList).isValidDeck();
    }

    private List<Integer> random52()
    {
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<52; i++)
        {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }
}
