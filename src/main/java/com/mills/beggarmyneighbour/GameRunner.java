package com.mills.beggarmyneighbour;

import com.mills.beggarmyneighbour.game.GamePlay;
import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.repositories.GameStatsRepository;
import com.mills.beggarmyneighbour.utils.CardOperations;
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
import java.util.Set;

@Service
public class GameRunner implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(GameRunner.class);
    private static final Integer INITIAL_DECKS = 100;
    private static final Integer ITERATIONS = 50000;

    private Set<List<CardValue>> processedDecks = new HashSet<>();

    @Autowired
    private GameStatsRepository gameStatsRepository;

    private static GameStats generateAndPlayGame(List<CardValue> deck)
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
        gameStatsRepository.deleteAll();

        Set<List<CardValue>> decks = getInitialDecks();

        for (int i = 0; i < ITERATIONS; i++) {
            runGamesForDecks(decks);

            Pageable pageRequest = new PageRequest(0, 10, Sort.Direction.DESC, "tricks");
            Page<GameStats> response = gameStatsRepository.findAll(pageRequest);

            Set<List<CardValue>> winningDecks = new HashSet<>();

            for (GameStats stats : response) {
                winningDecks.add(stats.getInitialDeck());
            }

            decks = mergeDecks(winningDecks);

            if (decks.isEmpty()) {
                decks = getInitialDecks();
                processedDecks.addAll(decks);
            }
        }

        for (GameStats stats : gameStatsRepository.findAll(new PageRequest(0, 1, Sort.Direction.DESC, "tricks"))) {
            logger.info("Best deck was {} with {} tricks", stats.getDeckRepresentation(), stats.getTricks());
        }


    }

    private Set<List<CardValue>> mergeDecks(Set<List<CardValue>> decks)
    {
        Set<List<CardValue>> newDecks = new HashSet<>();
        for (List<CardValue> deck1 : decks) {
            if (Math.random() > 0.9) {
                deck1.add(deck1.remove(0));
                logger.info("Mutation");
            }
            for (List<CardValue> deck2 : decks) {
                if (deck1.equals(deck2)) {
                    continue;
                }
                List<CardValue> deck1Clone = new ArrayList<>(deck1);
                List<CardValue> deck2Clone = new ArrayList<>(deck2);

                int i = (int) (Math.random() * 52);

                crossOverLists(deck1Clone, deck2Clone, i);

                while (!CardOperations.isValidDeck(deck1Clone)) {
                    i++;
                    if (i > 51) {
                        i = 0;
                    }
                    crossOverLists(deck1Clone, deck2Clone, i);
                }

                if (!processedDecks.contains(deck1Clone) && !newDecks.contains(deck1Clone)) {
                    newDecks.add(deck1Clone);
                }
                if (!processedDecks.contains(deck2Clone) && !newDecks.contains(deck2Clone)) {
                    newDecks.add(deck2Clone);
                }
            }
        }

        for (List<CardValue> deck : newDecks) {
            logger.info("Generated new deck {}", CardOperations.deckToString(deck));

        }

        processedDecks.addAll(decks);
        return newDecks;
    }

    private void crossOverLists(List<CardValue> deck1, List<CardValue> deck2, int index)
    {
        CardValue deck1Value = deck1.remove(index);
        CardValue deck2Value = deck2.remove(index);

        deck1.add(index, deck2Value);
        deck2.add(index, deck1Value);
    }

    private Set<List<CardValue>> getInitialDecks()
    {
        Set<List<CardValue>> decks = new HashSet<>();
        for (int i = 0; i < INITIAL_DECKS; i++) {
            decks.add(CardOperations.getShuffledDeck());
        }
        return decks;
    }

    private void runGamesForDecks(Set<List<CardValue>> decks)
    {
        List<GameStats> results = new ArrayList<>();

        for (List<CardValue> deck : decks) {
            results.add(generateAndPlayGame(deck));
        }

        gameStatsRepository.save(results);
    }

}