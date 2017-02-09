package com.mills.beggarmyneighbour.utils;

import com.google.common.collect.Iterables;
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
    public static List<CardValue> getShuffledDeck()
    {
        List<CardValue> deck = new ArrayList<>();

        for(int i=0; i<36; i++)
        {
            deck.add(CardValue.NON_FACE);
        }

        for (int i=0; i<4; i++) {
            deck.add(CardValue.ACE);
            deck.add(CardValue.KING);
            deck.add(CardValue.QUEEN);
            deck.add(CardValue.JACK);
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

    public static Boolean isValidDeck(List<CardValue> inDeck)
    {
        List<CardValue> deck = new ArrayList<>(inDeck);
        for(CardValue cardValue : EnumSet.allOf(CardValue.class))
        {
            for(int i=0; i<cardValue.getRequiredInDeck(); i++)
            {
                if(!deck.remove(cardValue)) { return false; }
            }
        }
        return true;
    }

    public static String deckToString(List<CardValue> deck)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (CardValue value : deck) {
            stringBuilder.append(value.getAsciiChar());
        }
        return stringBuilder.toString();
    }
}
