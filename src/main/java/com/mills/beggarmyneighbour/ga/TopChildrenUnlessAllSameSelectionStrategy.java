package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import com.mills.beggarmyneighbour.utils.CardOperations;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.mills.beggarmyneighbour.GameRunner.INITIAL_DECKS;

public class TopChildrenUnlessAllSameSelectionStrategy
    implements SelectionStrategy {
    private static final int NUMBER_TO_REPLACE = 80;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopChildrenUnlessAllSameSelectionStrategy.class);

    private MutablePair<Integer, Integer> tricksToCount = MutablePair.of(0, 0);

    @Override
    public List<SpecificDeckRepresentation> selectFromResults(List<SpecificDeckRepresentation> results) {
        List<SpecificDeckRepresentation> minimisedResults = results.subList(0, INITIAL_DECKS);

        LOGGER.info("Top of top 100 was:    {}", minimisedResults.get(0).toString());
        LOGGER.info("Bottom of top 100 was: {}", minimisedResults.get(minimisedResults.size() - 1).toString());

        if (tricksToCount.getLeft().equals(minimisedResults.get(0).getScore())) {
            tricksToCount.setRight(tricksToCount.getRight() + 1);
        } else {
            tricksToCount = MutablePair.of(minimisedResults.get(0).getScore(), 1);
        }

        boolean shakeUpRequired = tricksToCount.getRight() > 5;

        List<SpecificDeckRepresentation> decks = minimisedResults;
        if (minimisedResults.get(0).getScore() - minimisedResults.get(minimisedResults.size() - 1).getScore() == 0 ||
            shakeUpRequired)
        {
            decks = minimisedResults.stream()
                                    .limit(INITIAL_DECKS - NUMBER_TO_REPLACE)
                                    .collect(Collectors.toList());
            while (decks.size() < INITIAL_DECKS) {
                decks.add(CardOperations.getShuffledDeck());
            }
        }
        return decks;
    }
}
