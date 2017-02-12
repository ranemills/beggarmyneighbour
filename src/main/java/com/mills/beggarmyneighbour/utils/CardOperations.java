package com.mills.beggarmyneighbour.utils;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mills.beggarmyneighbour.GameRunner.PLAYER_VALUES;

public class CardOperations {
    private static final List<Player> PLAYERS = Arrays.asList(PLAYER_VALUES);

    public static SpecificDeckRepresentation getShuffledDeck()
    {
        return SpecificDeckRepresentation.randomDeck();
    }

    public static Map<Player, Deque<CardValue>> splitCards(Deck deck)
    {
        Map<Player, Deque<CardValue>> playerHands = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            playerHands.put(PLAYERS.get(i), new ArrayDeque<>(deck.subList(i, i + 26)));
        }

        return playerHands;
    }
//    public static Map<Player, Deque<CardValue>> splitCards(Deck deck)
//    {
//        int numberPlayers = PLAYER_VALUES.length;
//        int partitionSize = deck.size() / numberPlayers;
//
//        Map<Player, Deque<CardValue>> playerHands = new HashMap<>();
//        for (int i = 0; i < numberPlayers; i++) {
//            playerHands.put(PLAYERS.get(i), new ArrayDeque<>(deck.subList(i, i + partitionSize)));
//        }
//
//        return playerHands;
//    }
}
