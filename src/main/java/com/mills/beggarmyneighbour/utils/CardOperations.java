package com.mills.beggarmyneighbour.utils;

import com.google.common.collect.Iterables;
import com.mills.beggarmyneighbour.models.Card;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.models.Suit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CardOperations {
    private static final Logger logger = LoggerFactory.getLogger(CardOperations.class);

    public static List<Card> getDeck()
    {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (int value = 1; value <= 13; value++) {
                deck.add(new Card(suit, value));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    public static Map<Player, Deque<Card>> dealCards(List<Card> deck)
    {
        List<Player> players = Arrays.asList(Player.values());
        int numberPlayers = Player.values().length;
        int deckSize = deck.size();

        int cardsPerPlayer = deckSize/numberPlayers;

        Map<Player, Deque<Card>> playerHands = new HashMap<>();

        for(int i=0; i<numberPlayers; i++)
        {
            playerHands.put(players.get(i), new ArrayDeque<>(deck.subList(cardsPerPlayer*i, cardsPerPlayer*(i+1))));
        }

        return playerHands;
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
            logger.info("Player %s: %s", entry.getKey(), entry.getValue());
        }

        return playerHands;
    }
}
