package com.mills.beggarmyneighbour.utils;

import com.google.common.collect.Lists;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardOperations {
    public static List<CardValue> getDeck()
    {
        List<CardValue> deck = new ArrayList<>();

        for (int i=0; i<4; i++) {
            for (int value = 2; value <= 10; value++) {
                deck.add(CardValue.NON_FACE);
            }
            for(CardValue cardValue : EnumSet.of(CardValue.ACE, CardValue.KING, CardValue.QUEEN, CardValue.JACK))
            {
                deck.add(cardValue);
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    public static Map<Player, Deque<CardValue>> splitCards(List<CardValue> deck)
    {
        int numberPlayers = Player.values().length;
        List<Player> players = Arrays.asList(Player.values());
        List<List<CardValue>> partitions = Lists.partition(deck, deck.size()/numberPlayers);

        Map<Player, Deque<CardValue>> playerHands = new HashMap<>();
        for (int i = 0; i < numberPlayers; i++) {
            playerHands.put(players.get(i), new ArrayDeque<>(partitions.get(i)));
        }

        return playerHands;
    }
}
