package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import com.mills.beggarmyneighbour.utils.CardOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mills.beggarmyneighbour.GameRunner.INITIAL_DECKS;

public class TopChildrenUnlessAllSameSelectionStrategy
    implements SelectionStrategy {
    private static final int NUMBER_TO_REPLACE = 50;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopChildrenUnlessAllSameSelectionStrategy.class);

    @Override
    public List<SpecificDeckRepresentation> selectFromResults(List<GameStats> results) {
        List<GameStats> minimisedResults = results.subList(0, INITIAL_DECKS);

        LOGGER.info("Top of top 100 was:    {}: {}", minimisedResults.get(0).getDeckRepresentation(), minimisedResults.get(0).getTricks());
        LOGGER.info("Bottom of top 100 was: {}: {}", minimisedResults.get(minimisedResults.size()-1).getDeckRepresentation(), minimisedResults.get(minimisedResults.size()-1).getTricks());

        List<SpecificDeckRepresentation> decks;
        if (minimisedResults.get(0).getTricks() - minimisedResults.get(minimisedResults.size()-1).getTricks() == 0)
        {
            decks = minimisedResults.stream()
                                    .limit(INITIAL_DECKS - NUMBER_TO_REPLACE)
                                    .map(GameStats::getSpecificDeckRepresentation)
                                    .collect(Collectors.toList());
            while(decks.size() < INITIAL_DECKS) {
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
