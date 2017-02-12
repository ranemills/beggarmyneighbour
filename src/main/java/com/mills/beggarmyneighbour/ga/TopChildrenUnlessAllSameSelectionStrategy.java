package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import com.mills.beggarmyneighbour.utils.CardOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopChildrenUnlessAllSameSelectionStrategy
    implements SelectionStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopChildrenUnlessAllSameSelectionStrategy.class);

    private static final int INITIAL_DECKS = 100;

    @Override
    public List<SpecificDeckRepresentation> selectFromResults(List<GameStats> results) {


        List<GameStats> minimisedResults = results.stream()
                                                  .sorted(Comparator.comparing(GameStats::getTricks).reversed())
                                                  .limit(INITIAL_DECKS)
                                                  .peek(result -> LOGGER.info("{}: {}",
                                                                              result.getDeckRepresentation(),
                                                                              result.getTricks()))
                                                  .collect(Collectors.toList());

        List<SpecificDeckRepresentation> decks;
        if (minimisedResults.stream()
                            .map(GameStats::getTricks)
                            .distinct()
                            .collect(Collectors.toList()).size() <= 3)
        {
            decks = minimisedResults.stream().limit(INITIAL_DECKS / 2)
                                    .map(GameStats::getSpecificDeckRepresentation)
                                    .collect(Collectors.toList());
            for (int j = 0; j < INITIAL_DECKS / 2; j++) {
                decks.add(CardOperations.getShuffledDeck());
            }
        } else {

            decks = minimisedResults.stream()
                                    .map(GameStats::getSpecificDeckRepresentation)
                                    .collect(Collectors.toList());
        }
        return decks;
    }
}
