package com.mills.beggarmyneighbour;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class BeggarMyNeighbourApplication {

    private static final Logger logger = Logger.getLogger("BeggarMyNeighbourApplication");

    private static final Map<Integer, Integer> PENALTIES = ImmutableMap.<Integer, Integer>builder().put(1, 4)
                                                                                                   .put(11, 1)
                                                                                                   .put(12, 2)
                                                                                                   .put(13, 3)
                                                                                                   .build();

    public static void main(String[] args) {
        List<Player> results = new ArrayList<>();

        for(int i=0; i<10; i++)
        {
            Deque<Card> deck = getDeck();
            Map<Player, Deque<Card>> playerHands = dealCards(deck);
            Player loser = playGame(deck, playerHands);
            EnumSet<Player> allPlayers = EnumSet.allOf(Player.class);
            allPlayers.remove(loser);
            Player winner =  allPlayers.iterator().next();
            results.add(winner);
        }
        logger.warning(results.toString());

    }

    private static Player playGame(Deque<Card> deck, Map<Player, Deque<Card>> playerHands) {

        boolean isPenalty = false;

        for(Player player : Iterables.cycle(Player.values()))
        {
            int cardsToPlay = 1;

            logger.warning(String.format("Turn of player %s", player));

            Deque<Card> hand = playerHands.get(player);

            if(!deck.isEmpty())
            {
                Card topCard = deck.peek();
                logger.warning(String.format("Top card is %s", topCard));

                if (PENALTIES.keySet().contains(topCard.getValue())) {
                    isPenalty = true;
                    cardsToPlay = PENALTIES.get(topCard.getValue());
                    logger.warning(String.format("Player %s is paying a penalty of %s", player, cardsToPlay));
                } else if (isPenalty) {
                    logger.warning(String.format("Player %s is picking up the deck", player));
                    while (!deck.isEmpty()) {
                        hand.addLast(deck.pop());
                    }
                    isPenalty = false;
                }
            }

            playCards(deck, hand, cardsToPlay);

            if(hand.isEmpty())
            {
                logger.warning(String.format("End of game. Player %s lost.", player));
                return player;
            }
        }

        return null;
    }

    private static Map<Player, Deque<Card>> dealCards(Deque<Card> deck) {
        Map<Player, Deque<Card>> playerHands = new HashMap<>();
        for(Player player : Player.values())
        {
            playerHands.put(player, new ArrayDeque<Card>());
        }

        Iterator<Player> playersDealIterator = Iterables.cycle(Player.values()).iterator();
        while(!deck.isEmpty())
        {
            Player player = playersDealIterator.next();
            playerHands.get(player).add(deck.pop());
        }

        for(Map.Entry<Player, Deque<Card>> entry : playerHands.entrySet()) {
            logger.warning(String.format("Player %s: %s", entry.getKey(), entry.getValue()));
        }

        return playerHands;
    }

    enum Player {
        PLAYER_ONE,
        PLAYER_TWO
    }


    private static void playCards(Deque<Card> deck, Deque<Card> playerHand, Integer cardsToPlay)
    {
        for (int i = 0; i < cardsToPlay; i++) {
            if(playerHand.isEmpty())
            {
                logger.warning("Out of cards");
                break;
            }
            Card card = playerHand.pop();
            logger.warning(String.format("Plays card %s", card));
            deck.push(card);

            if (PENALTIES.keySet().contains(card.getValue())) {
                break;
            }
        }
        logger.warning("End of turn");
    }

    private static Deque<Card> getDeck()
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

    enum Suit {
        HEART,
        CLUB,
        SPADE,
        DIAMOND
    }

    static class Card {
        private final Integer value;
        private final Suit suit;

        public Card(Suit suit, Integer value) {
            this.value = value;
            this.suit = suit;
        }

        public Integer getValue() {
            return value;
        }

        public Suit getSuit() {
            return suit;
        }

        @Override
        public String toString() {
            return String.format("%s%s", value, suit.toString().charAt(0));
        }
    }

}
