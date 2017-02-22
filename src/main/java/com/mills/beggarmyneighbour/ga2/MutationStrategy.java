package com.mills.beggarmyneighbour.ga2;

import com.mills.beggarmyneighbour.models.DeckOfGenes;

public interface MutationStrategy {

    DeckOfGenes mutateDeck(DeckOfGenes deck);

}
