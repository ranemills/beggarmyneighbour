package com.mills.beggarmyneighbour.utils;

import com.google.common.collect.Iterables;
import com.mills.beggarmyneighbour.models.Card;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.models.Suit;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by ryan on 08/02/17.
 */
public class CardOperations {
    private static final Logger logger = Logger.getLogger("CardOperations");

    public static Deque<Card> getDeck()
    {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (int value = 1; value <= 13; value++) {
                deck.add(new Card(suit, value));
            }
        }
        Collections.shuffle(deck);
        return new ArrayDeque<>(deck);
    }

    public static Map<Player, Deque<Card>> dealCards(Deque<Card> deck) {
        Map<Player, Deque<Card>> playerHands = new HashMap<>();
        for (Player player : Player.values()) {
            playerHands.put(player, new ArrayDeque<Card>());
        }

        Iterator<Player> playersDealIterator = Iterables.cycle(Player.values()).iterator();
        while (!deck.isEmpty()) {
            Player player = playersDealIterator.next();
            playerHands.get(player).add(deck.pop());
        }

        for (Map.Entry<Player, Deque<Card>> entry : playerHands.entrySet()) {
            logger.warning(String.format("Player %s: %s", entry.getKey(), entry.getValue()));
        }

        return playerHands;
    }
}
