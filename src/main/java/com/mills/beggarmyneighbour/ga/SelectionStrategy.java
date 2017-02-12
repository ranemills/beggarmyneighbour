package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.Deck;

import java.util.List;

public interface SelectionStrategy {

    List<Deck> selectFromResults(List<GameStats> results);

}
