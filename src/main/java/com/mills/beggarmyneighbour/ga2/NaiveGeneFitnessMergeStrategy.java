package com.mills.beggarmyneighbour.ga2;

import com.google.common.collect.ImmutableList;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.DeckOfGenes;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Random;

public class NaiveGeneFitnessMergeStrategy implements MergeStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveGeneFitnessMergeStrategy.class);

    @Override
    public Collection<DeckOfGenes> mergeDecks(DeckOfGenes deck1, DeckOfGenes deck2) {
        DeckOfGenes newDeck = new DeckOfGenes();
        for (int i = 0; i < deck1.size(); i++) {
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
            newDeck.add(cardToAdd);
            if(!newDeck.isValid())
            {
                newDeck.remove(cardToAdd);
//                LOG.warn("new deck became invalid at i={}. deck is {}", i, newDeck.toDeck());

                for(CardValue cardValue : EnumSet.allOf(CardValue.class))
                {
                    for (; i < deck1.size(); i++) {
//                        LOG.warn("adding {} at i={}", cardValue, i);
                        Pair<CardValue, Double> newCard = Pair.of(cardValue, Math.random());
                        newDeck.add(newCard);
                        if(!newDeck.isValid())
                        {
//                            LOG.warn("{} made it invalid, deck is {}", cardValue, newDeck.toDeck());
//                            LOG.warn("{} made it invalid, removing", cardValue);
                            newDeck.remove(newCard);
                            break;
                        }
                    }
                    if(i == deck1.size())
                    {
                        break;
                    }
                }

//                LOG.warn("Finished. Size is {}. Deck is {}", newDeck.size(), newDeck.toDeck());
            }
        }
        return ImmutableList.of(newDeck);
    }
}
