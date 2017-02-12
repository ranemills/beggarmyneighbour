package com.mills.beggarmyneighbour;

import com.mills.beggarmyneighbour.ga.CrossOverMergeStrategy;
import com.mills.beggarmyneighbour.ga.MergeStrategy;
import com.mills.beggarmyneighbour.ga.MutationStrategy;
import com.mills.beggarmyneighbour.ga.SelectionStrategy;
import com.mills.beggarmyneighbour.ga.SwapPenaltyCardsMutationStrategy;
import com.mills.beggarmyneighbour.ga.TopChildrenUnlessAllSameSelectionStrategy;
import com.mills.beggarmyneighbour.game.GamePlay;
import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import com.mills.beggarmyneighbour.repositories.GameStatsRepository;
import com.mills.beggarmyneighbour.utils.CardOperations;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GameRunner implements ApplicationListener<ApplicationReadyEvent> {

    // Constants
    private static final Logger logger = LoggerFactory.getLogger(GameRunner.class);
    private static final Integer INITIAL_DECKS = 100;
    private static final Integer ITERATIONS = 5000;
    // Fields to autowire
    private final GameStatsRepository gameStatsRepository;
    // Store the decks we've dealt with
    private Set<SpecificDeckRepresentation> processedDecks = new HashSet<>();
    // Strategies
    private MergeStrategy mergeStrategy = new CrossOverMergeStrategy();
    private MutationStrategy mutationStrategy = new SwapPenaltyCardsMutationStrategy();
    private SelectionStrategy selectionStrategy = new TopChildrenUnlessAllSameSelectionStrategy();

    @Autowired
    public GameRunner(GameStatsRepository gameStatsRepository) {
        this.gameStatsRepository = gameStatsRepository;
    }

    private static GameStats generateAndPlayGame(Deck deck)
    {
        Map<Player, Deque<CardValue>> playerHands = CardOperations.splitCards(deck);

        GameStats gameStats = GamePlay.playGame(playerHands);
        gameStats.setSpecificDeckRepresentation(SpecificDeckRepresentation.fromDeck(deck));
        return gameStats;
    }

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        List<SpecificDeckRepresentation> decks = new ArrayList<>();
        for (int i = 0; i < ITERATIONS; i++) {
            if (decks.isEmpty()) {
                decks = getInitialDecks();
            }

            processedDecks.addAll(decks);

            List<GameStats> results = runGamesForDecks(decks);

            logger.info("Iteration {}", i);
            logger.info("Number of results {}", results.size());

            decks = mergeDecks(selectionStrategy.selectFromResults(results));

        }

        for (GameStats stats : gameStatsRepository.findAll(new PageRequest(0, 1, Sort.Direction.DESC, "tricks"))) {
            logger.info("Best deck was {} with {} tricks", stats.getDeckRepresentation(), stats.getTricks());
        }
    }

    private List<SpecificDeckRepresentation> mergeDecks(List<SpecificDeckRepresentation> decks)
    {
        Set<SpecificDeckRepresentation> newDecks = new HashSet<>();
        for (SpecificDeckRepresentation deck1 : decks) {
            if (Math.random() > 0.9) {
                deck1 = mutationStrategy.mutateDeck(deck1);
                logger.debug("Mutation");
            }
            for (SpecificDeckRepresentation deck2 : decks) {
                Pair<SpecificDeckRepresentation, SpecificDeckRepresentation> mergedSpecificDeckRepresentations =
                    mergeStrategy.mergeDecks(deck1, deck2);

                if (!processedDecks.contains(mergedSpecificDeckRepresentations.getLeft())) {
                    newDecks.add(mergedSpecificDeckRepresentations.getLeft());
                }
                if (!processedDecks.contains(mergedSpecificDeckRepresentations.getRight())) {
                    newDecks.add(mergedSpecificDeckRepresentations.getRight());
                }
            }
        }

        for (SpecificDeckRepresentation deck : newDecks) {
            logger.debug("Generated new deck {}", deck);

        }

        return new ArrayList<>(newDecks);
    }

    private List<SpecificDeckRepresentation> getInitialDecks()
    {
        List<SpecificDeckRepresentation> decks = new ArrayList<>();
        for (int i = 0; i < INITIAL_DECKS; i++) {
            decks.add(CardOperations.getShuffledDeck());
        }
        return decks;
    }

    private List<GameStats> runGamesForDecks(List<SpecificDeckRepresentation> decks)
    {
        List<GameStats> results = new ArrayList<>();

        for (SpecificDeckRepresentation deck : decks) {
            results.add(generateAndPlayGame(deck.toDeck()));
        }

        gameStatsRepository.save(results);

        return results;
    }

}