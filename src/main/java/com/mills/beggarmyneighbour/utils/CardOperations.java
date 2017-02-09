package com.mills.beggarmyneighbour.utils;

import com.google.common.collect.Lists;
import com.mills.beggarmyneighbour.models.Card;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.models.Suit;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardOperations {
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

    public static Map<Player, Deque<Card>> splitCards(List<Card> deck)
    {
        int numberPlayers = Player.values().length;
        List<Player> players = Arrays.asList(Player.values());
        List<List<Card>> partitions = Lists.partition(deck, deck.size()/numberPlayers);

        Map<Player, Deque<Card>> playerHands = new HashMap<>();
        for (int i = 0; i < numberPlayers; i++) {
            playerHands.put(players.get(i), new ArrayDeque<>(partitions.get(i)));
        }

        return playerHands;
    }
}
