package com.mills.beggarmyneighbour.ga2;

import com.mills.beggarmyneighbour.models.DeckOfGenes;

import java.util.Collection;

public interface MergeStrategy {

    Collection<DeckOfGenes> mergeDecks(DeckOfGenes deck1,
                                       DeckOfGenes deck2);

}
