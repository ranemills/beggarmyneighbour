package com.mills.beggarmyneighbour;

import com.mills.beggarmyneighbour.game.GamePlay;
import com.mills.beggarmyneighbour.models.Card;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.utils.CardOperations;

import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class BeggarMyNeighbourApplication {

    private static final Logger logger = Logger.getLogger("BeggarMyNeighbourApplication");

    public static void main(String[] args) {
        List<Player> results = new ArrayList<>();

        for(int i=0; i<10; i++)
        {
            Deque<Card> deck = CardOperations.getDeck();
            Map<Player, Deque<Card>> playerHands = CardOperations.dealCards(deck);

            Player winner = new GamePlay(deck, playerHands).playGame();
            results.add(winner);
        }
        logger.warning(results.toString());

    }


}
