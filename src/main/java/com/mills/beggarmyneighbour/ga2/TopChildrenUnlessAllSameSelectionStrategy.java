package com.mills.beggarmyneighbour.ga2;

import com.mills.beggarmyneighbour.models.DeckOfGenes;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.mills.beggarmyneighbour.run.GameRunner.INITIAL_DECKS;

public class TopChildrenUnlessAllSameSelectionStrategy
    implements SelectionStrategy {
    private static final int NUMBER_TO_REPLACE = 50;
    private static final int SHAKE_UP_REQUIRED_THRESHOLD = 20;

    private static final Logger LOGGER = LoggerFactory.getLogger(TopChildrenUnlessAllSameSelectionStrategy.class);

    private MutablePair<Integer, Integer> tricksToCount = MutablePair.of(0, 0);

    @Override
    public List<DeckOfGenes> selectFromResults(List<DeckOfGenes> results) {
        List<DeckOfGenes> minimisedResults = results.subList(0, INITIAL_DECKS);

        LOGGER.info("Top of top 100 was:    {}: {}", minimisedResults.get(0).getScore(), minimisedResults.get(0).toDeck());
        LOGGER.info("Bottom of top 100 was: {}: {}", minimisedResults.get(minimisedResults.size() - 1).getScore(), minimisedResults.get(minimisedResults.size() - 1).toDeck());

        if (tricksToCount.getLeft().equals(minimisedResults.get(0).getScore())) {
            tricksToCount.setRight(tricksToCount.getRight() + 1);
        } else {
            tricksToCount = MutablePair.of(minimisedResults.get(0).getScore(), 1);
        }

        boolean shakeUpRequired = tricksToCount.getRight() > SHAKE_UP_REQUIRED_THRESHOLD;

        List<DeckOfGenes> decks = minimisedResults;
        if (minimisedResults.get(0).getScore() - minimisedResults.get(minimisedResults.size() - 1).getScore() == 0 ||
            shakeUpRequired)
        {
            LOGGER.info("Shaking up");
            decks = minimisedResults.stream()
                                    .limit(INITIAL_DECKS - NUMBER_TO_REPLACE)
                                    .collect(Collectors.toList());
            while (decks.size() < INITIAL_DECKS) {
                decks.add(DeckOfGenes.randomDeckOfGenes());
            }
        }
        return decks;
    }
}
