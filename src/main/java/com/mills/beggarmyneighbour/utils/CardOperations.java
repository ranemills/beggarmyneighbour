package com.mills.beggarmyneighbour.utils;

import com.google.common.collect.ImmutableMap;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.Player;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mills.beggarmyneighbour.run.GameRunner.PLAYER_VALUES;

public class CardOperations {
    public static Map<Player, Deque<CardValue>> splitCards(Deck deck)
    {
        return ImmutableMap.of(Player.PLAYER_ONE, new ArrayDeque<>(deck.subList(0, 26)),
                               Player.PLAYER_TWO, new ArrayDeque<>(deck.subList(26, 52)));
    }
}
