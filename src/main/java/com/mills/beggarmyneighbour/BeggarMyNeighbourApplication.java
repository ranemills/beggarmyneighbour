package com.mills.beggarmyneighbour;

import com.mills.beggarmyneighbour.game.GamePlay;
import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.utils.CardOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class BeggarMyNeighbourApplication {

    private static final Logger logger = LoggerFactory.getLogger(BeggarMyNeighbourApplication.class);

    public static void main(String[] args) {
        List<GameStats> results = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            List<CardValue> deck = CardOperations.getDeck();
            results.add(generateAndPlayGame(deck));
        }

        for (GameStats stats : results) {
            logger.error(stats.toString());
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
