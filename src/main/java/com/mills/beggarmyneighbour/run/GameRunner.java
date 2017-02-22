package com.mills.beggarmyneighbour.run;

import com.mills.beggarmyneighbour.ga2.MergeStrategy;
import com.mills.beggarmyneighbour.ga2.NaiveGeneFitnessMergeStrategy;
import com.mills.beggarmyneighbour.ga2.SelectionStrategy;
import com.mills.beggarmyneighbour.ga2.TopChildrenUnlessAllSameSelectionStrategy;
import com.mills.beggarmyneighbour.game.GamePlayThread;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.DeckOfGenes;
import com.mills.beggarmyneighbour.models.Player;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GameRunner {

    // Constants
    public static final Player[] PLAYER_VALUES = Player.values();
    public static final Integer INITIAL_DECKS = 100;
    private static final Logger logger = LoggerFactory.getLogger(GameRunner.class);
    private static final Integer ITERATIONS = 200;

    // Strategies
    private MergeStrategy mergeStrategy = new NaiveGeneFitnessMergeStrategy();
    private SelectionStrategy selectionStrategy = new TopChildrenUnlessAllSameSelectionStrategy();

    private List<Pair<Deck, Integer>> topScores = new ArrayList<>();

    public void run() {
        List<DeckOfGenes> decks = new ArrayList<>() ;
        for (int i = 0; i < ITERATIONS; i++) {
            if (decks.isEmpty()) {
                decks = getInitialDecks();
            }

            runGamesForDecks(decks);

            List<DeckOfGenes> sortedDeckResults = decks.stream()
                                                       .sorted(Comparator.comparing(DeckOfGenes::getScore).reversed())
                                                       .collect(Collectors.toList());

            topScores.add(Pair.of(sortedDeckResults.get(0).toDeck(), sortedDeckResults.get(0).getScore()));

            updateGeneScores(decks);

            logger.info("Iteration {}", i);
            logger.info("Number of results {}", sortedDeckResults.size());

            decks = mergeDecks(selectionStrategy.selectFromResults(sortedDeckResults));
        }
        logger.info("Iteration, Deck, Tricks, Cards");
        for (int i = 0; i < topScores.size(); i++) {
            logger.info("{}, {}, {}", i, topScores.get(i).getKey(), topScores.get(i).getValue());
        }
    }

    private void updateGeneScores(List<DeckOfGenes> decks)
    {
        for (DeckOfGenes deck : decks) {
            for (Map.Entry<Deck, Integer> parentDeck : deck.getParents().entrySet()) {
                if (parentDeck.getValue() > deck.getScore()) {
                    double scoreAdjustment = (deck.getScore() - parentDeck.getValue()) / 100;
                    updateGeneScore(deck, parentDeck.getKey(), scoreAdjustment);
                }
            }
        }
    }

    private void updateGeneScore(DeckOfGenes deck, Deck parentDeck, double scoreAdjustment)
    {
        for (int i = 0; i < deck.size(); i++) {
            if (deck.get(i).getKey() == parentDeck.get(i)) {
                deck.updateScore(i, scoreAdjustment);
            }
        }
    }

    private List<DeckOfGenes> mergeDecks(List<DeckOfGenes> decks)
    {
        Set<DeckOfGenes> newDecks = new HashSet<>();
        for (DeckOfGenes deck1 : decks) {
            for (DeckOfGenes deck2 : decks) {
                if (deck1.equals(deck2)) {
                    continue;
                }

                for (DeckOfGenes newDeck : mergeStrategy.mergeDecks(deck1, deck2)) {
                    newDeck.addParent(deck1.toDeck(), deck1.getScore());
                    newDeck.addParent(deck2.toDeck(), deck2.getScore());

                    if(Math.random() > 0.95)
                    {
                        int i = (int) (Math.random() * 51);
                        int j = (int) (Math.random() * 51);
                        Collections.swap(newDeck, i, j);
//                        logger.warn("Swap! ({} and {})", i, j);
                    }

                    newDecks.add(newDeck);
                }
            }
        }

        return new ArrayList<>(newDecks);
    }

    private List<DeckOfGenes> getInitialDecks()
    {
        List<DeckOfGenes> decks = new ArrayList<>();
        for (int i = 0; i < INITIAL_DECKS; i++) {
            decks.add(DeckOfGenes.randomDeckOfGenes());
        }
        return decks;
    }

    private void runGamesForDecks(List<DeckOfGenes> decks)
    {
        List<Thread> threads = new ArrayList<>();
        List<GamePlayThread> gamePlayThreads = new ArrayList<>();

        for (DeckOfGenes deck : decks) {
            GamePlayThread gamePlayThread = new GamePlayThread(deck.toDeck());
            Thread thread = new Thread(gamePlayThread);

            thread.start();

            threads.add(thread);
            gamePlayThreads.add(gamePlayThread);
        }

        logger.info("Spun up {} threads", threads.size());

        for (int i = 0; i < threads.size(); i++) {
            try {
                threads.get(i).join();
                decks.get(i).setScore(gamePlayThreads.get(i).getScore());
            } catch (InterruptedException e) {
                logger.warn("Thread {} was interrupted. Deck was {}", i, decks.get(i));
            }
        }
    }

}