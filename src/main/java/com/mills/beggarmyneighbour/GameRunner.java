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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

@Service
public class GameRunner implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(GameRunner.class);

    @Autowired
    private GameStatsRepository gameStatsRepository;

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        gameStatsRepository.deleteAll();

        List<GameStats> results = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            List<CardValue> deck = CardOperations.getDeck();
            results.add(generateAndPlayGame(deck));
        }

        gameStatsRepository.save(results);

        for (GameStats stats : results) {
            logger.info(stats.toString());
        }
    }

    private static GameStats generateAndPlayGame(List<CardValue> deck)
    {
        Map<Player, Deque<CardValue>> playerHands = CardOperations.splitCards(deck);

        GameStats gameStats = GamePlay.playGame(playerHands);
        gameStats.setInitialDeck(deck);
        return gameStats;
    }

}