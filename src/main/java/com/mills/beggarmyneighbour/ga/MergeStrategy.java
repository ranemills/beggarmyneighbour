package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.Deck;
import org.apache.commons.lang3.tuple.Pair;

public interface MergeStrategy {

    Pair<Deck, Deck> mergeDecks(Deck deck1, Deck deck2);

}
