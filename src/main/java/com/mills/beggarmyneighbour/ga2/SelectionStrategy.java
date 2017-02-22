package com.mills.beggarmyneighbour.ga2;

import com.mills.beggarmyneighbour.models.DeckOfGenes;

import java.util.List;

public interface SelectionStrategy {

    List<DeckOfGenes> selectFromResults(List<DeckOfGenes> results);

}
