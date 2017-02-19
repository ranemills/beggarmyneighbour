package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;

import java.util.List;

public interface SelectionStrategy {

    List<SpecificDeckRepresentation> selectFromResults(List<SpecificDeckRepresentation> results);

}
