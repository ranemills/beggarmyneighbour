package com.mills.beggarmyneighbour;

import com.google.common.collect.Sets;
import com.mills.beggarmyneighbour.ga.CrossOverMergeStrategy;
import com.mills.beggarmyneighbour.ga.MergeStrategy;
import com.mills.beggarmyneighbour.ga.MutationStrategy;
import com.mills.beggarmyneighbour.ga.RandomDeckMutationStrategy;
import com.mills.beggarmyneighbour.ga.SelectionStrategy;
import com.mills.beggarmyneighbour.ga.SwapPenaltyCardsMutationStrategy;
import com.mills.beggarmyneighbour.ga.TopChildrenUnlessAllSameSelectionStrategy;
import com.mills.beggarmyneighbour.game.GamePlayThread;
import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import com.mills.beggarmyneighbour.utils.CardOperations;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameRunner implements ApplicationListener<ApplicationReadyEvent> {

    // Constants
    public static final Player[] PLAYER_VALUES = Player.values();
    public static final Integer INITIAL_DECKS = 100;
    private static final Logger logger = LoggerFactory.getLogger(GameRunner.class);
    private static final Integer ITERATIONS = 200;

    // Store the decks we've dealt with
    private Set<SpecificDeckRepresentation> processedDecks = new HashSet<>();

    // Strategies
    private MergeStrategy mergeStrategy = new CrossOverMergeStrategy();
    private MutationStrategy mutationStrategy1 = new SwapPenaltyCardsMutationStrategy();
    private MutationStrategy mutationStrategy2 = new RandomDeckMutationStrategy();
    private SelectionStrategy selectionStrategy = new TopChildrenUnlessAllSameSelectionStrategy();

    private List<GameStats> topScores = new ArrayList<>();

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

            List<GameStats> sortedResults = results.stream()
                                                   .sorted(Comparator.comparing(GameStats::getTricks).reversed())
                                                   .collect(Collectors.toList());

            topScores.add(sortedResults.get(0));

            logger.info("Iteration {}", i);
            logger.info("Number of results {}", results.size());

            decks = mergeDecks(selectionStrategy.selectFromResults(sortedResults));
        }
        logger.info("Iteration, Deck, Tricks, Cards");
        for (int i = 0; i < topScores.size(); i++) {
            logger.info("{}, {}, {}, {}", i, topScores.get(i).getDeckRepresentation(), topScores.get(i).getTricks(),
                        topScores.get(i).getCards());
        }
    }

    private List<SpecificDeckRepresentation> mergeDecks(List<SpecificDeckRepresentation> decks)
    {
        Set<SpecificDeckRepresentation> newDecks = new HashSet<>();
        for (SpecificDeckRepresentation deck1 : decks) {
            for (SpecificDeckRepresentation deck2 : decks) {
                if (deck1.equals(deck2)) {
                    continue;
                }

                Pair<SpecificDeckRepresentation, SpecificDeckRepresentation> mergedSpecificDeckRepresentations =
                    mergeStrategy.mergeDecks(deck1, deck2);

                SpecificDeckRepresentation newDeck1 = mergedSpecificDeckRepresentations.getLeft();
                SpecificDeckRepresentation newDeck2 = mergedSpecificDeckRepresentations.getRight();

                if (Math.random() > 0.9) {
                    newDeck1 = mutationStrategy1.mutateDeck(newDeck1);
                }
                if (Math.random() > 0.9) {
                    newDeck2 = mutationStrategy1.mutateDeck(newDeck2);
                }

                newDecks.add(newDeck1);
                newDecks.add(newDeck2);
            }
        }

        newDecks = Sets.difference(newDecks, processedDecks);

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

        List<Thread> threads = new ArrayList<>();
        List<GamePlayThread> gamePlayThreads = new ArrayList<>();

        for (SpecificDeckRepresentation deck : decks) {
            GamePlayThread gamePlayThread = new GamePlayThread(deck);
            Thread thread = new Thread(gamePlayThread);

            thread.start();

            threads.add(thread);
            gamePlayThreads.add(gamePlayThread);
        }

        logger.info("Spun up {} threads", threads.size());

        for (int i = 0; i < threads.size(); i++) {
            try {
                threads.get(i).join();
                results.add(gamePlayThreads.get(i).getGameStats());
            } catch (InterruptedException e) {
                logger.warn("Thread {} was interrupted. Deck was {}", i, decks.get(i));
            }
        }

        return results;
    }

}