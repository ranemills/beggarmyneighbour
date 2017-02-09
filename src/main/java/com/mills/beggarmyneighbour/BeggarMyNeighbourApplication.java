package com.mills.beggarmyneighbour;

import com.mills.beggarmyneighbour.game.GamePlay;
import com.mills.beggarmyneighbour.game.GameStats;
import com.mills.beggarmyneighbour.models.Card;
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

        for(int i=0; i<10; i++)
        {
            List<Card> deck = CardOperations.getDeck();
            Map<Player, Deque<Card>> playerHands = CardOperations.splitCards(deck);

            GameStats gameStats = GamePlay.playGame(playerHands);
            results.add(gameStats);
        }

        for(GameStats stats : results) {
            logger.info(stats.toString());
        }
    }


}
