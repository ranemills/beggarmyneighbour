package com.mills.beggarmyneighbour;

import com.mills.beggarmyneighbour.ga.CrossOverMergeStrategy;
import com.mills.beggarmyneighbour.ga.MergeStrategy;
import com.mills.beggarmyneighbour.ga.MoveKingMutationStrategy;
import com.mills.beggarmyneighbour.ga.MutationStrategy;
import com.mills.beggarmyneighbour.game.GamePlay;
import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.repositories.GameStatsRepository;
import com.mills.beggarmyneighbour.utils.CardOperations;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Service
public class GameRunner implements ApplicationListener<ApplicationReadyEvent> {

    // Constants
    private static final Logger logger = LoggerFactory.getLogger(GameRunner.class);
    private static final Integer INITIAL_DECKS = 100;
    private static final Integer ITERATIONS = 500;
    private static final Random RANDOM = new Random();

    // Store the decks we've dealt with
    private Set<Deck> processedDecks = new HashSet<>();

    // Strategies
    private MergeStrategy mergeStrategy = new CrossOverMergeStrategy();
    private MutationStrategy mutationStrategy = new MoveKingMutationStrategy();

    // Fields to autowire
    private final GameStatsRepository gameStatsRepository;

    @Autowired
    public GameRunner(GameStatsRepository gameStatsRepository) {
        this.gameStatsRepository = gameStatsRepository;
    }

    private static GameStats generateAndPlayGame(Deck deck)
    {
        Map<Player, Deque<CardValue>> playerHands = CardOperations.splitCards(deck);

        GameStats gameStats = GamePlay.playGame(playerHands);
        gameStats.setInitialDeck(deck);
        return gameStats;
    }

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        for (int i = 0; i < ITERATIONS; i++) {

            int page = 1;
            int pageRandom = RANDOM.nextInt(5);
            if (pageRandom == 0) {
                page = 2;
            }
            if (pageRandom == 1) {
                page = 3;
            }

            Pageable pageRequest = new PageRequest(page, 10, Sort.Direction.DESC, "tricks");
            Page<GameStats> response = gameStatsRepository.findAll(pageRequest);

            Set<Deck> winningDecks = new HashSet<>();

            for (GameStats stats : response) {
                winningDecks.add(stats.getInitialDeck());
            }

            Set<Deck> decks = mergeDecks(winningDecks);

            if (decks.isEmpty()) {
                decks = getInitialDecks();
                processedDecks.addAll(decks);
            }

            runGamesForDecks(decks);
        }

        for (GameStats stats : gameStatsRepository.findAll(new PageRequest(0, 1, Sort.Direction.DESC, "tricks"))) {
            logger.info("Best deck was {} with {} tricks", stats.getDeckRepresentation(), stats.getTricks());
        }
    }

    private Set<Deck> mergeDecks(Set<Deck> decks)
    {
        Set<Deck> newDecks = new HashSet<>();
        for (Deck deck1 : decks) {
            if (Math.random() > 0.9) {
                mutationStrategy.mutateDeck(deck1);
                logger.info("Mutation");
            }
            for (Deck deck2 : decks) {
                Pair<Deck, Deck> mergedDecks = mergeStrategy.mergeDecks(deck1, deck2);

                if (!processedDecks.contains(mergedDecks.getLeft()) && !newDecks.contains(mergedDecks.getLeft())) {
                    newDecks.add(mergedDecks.getLeft());
                }
                if (!processedDecks.contains(mergedDecks.getRight()) && !newDecks.contains(mergedDecks.getRight())) {
                    newDecks.add(mergedDecks.getRight());
                }
            }
        }

        for (Deck deck : newDecks) {
            logger.info("Generated new deck {}", deck);

        }

        processedDecks.addAll(decks);
        return newDecks;
    }

    private Set<Deck> getInitialDecks()
    {
        Set<Deck> decks = new HashSet<>();
        for (int i = 0; i < INITIAL_DECKS; i++) {
            decks.add(CardOperations.getShuffledDeck());
        }
        return decks;
    }

    private void runGamesForDecks(Set<Deck> decks)
    {
        List<GameStats> results = new ArrayList<>();

        for (Deck deck : decks) {
            results.add(generateAndPlayGame(deck));
        }

        gameStatsRepository.save(results);
    }

}