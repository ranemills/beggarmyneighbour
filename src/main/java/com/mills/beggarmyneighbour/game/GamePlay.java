package com.mills.beggarmyneighbour.game;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GamePlay {
    private static final Logger logger = LoggerFactory.getLogger(GamePlay.class);


    private static final Map<Integer, Integer> PENALTIES = ImmutableMap.<Integer, Integer>builder().put(1, 4)
            .put(11, 1)
            .put(12, 2)
            .put(13, 3)
            .build();

    private Boolean isPenalty = false;
    private Map<Player, Deque<CardValue>> playerHands;

    private int numberTricks = 0;
    private int numberCards = 0;

    private Iterator<Player> playerIterator;

    GamePlay() {
        this(new HashMap<Player, Deque<CardValue>>());
    }

    public GamePlay(Map<Player, Deque<CardValue>> playerHands) {
        this.playerHands = playerHands;
        this.playerIterator = Iterables.cycle(Player.values()).iterator();
    }

    public static GameStats playGame(Map<Player, Deque<CardValue>> playerHands) {
        return new GamePlay(playerHands).playGame();
    }

    boolean playTurn(Player player, Deque<CardValue> deck) {
        Deque<CardValue> hand = playerHands.get(player);

        if (!deck.isEmpty()) {
            setPenaltyMode(dealWithPenaltyMode(deck, hand));
        }

        int cardsToPlay = computeCardsToPlay(deck);

        if (!playCards(deck, hand, cardsToPlay)) {
            logger.trace(String.format("End of game. Player %s lost.", player));
            return false;
        }

        return true;
    }

    boolean dealWithPenaltyMode(Deque<CardValue> deck, Deque<CardValue> hand) {
        boolean topCardIsPenalty = !deck.peek().equals(CardValue.NON_FACE);

        if (!topCardIsPenalty && getPenaltyMode()) {
            pickUpDeck(deck, hand);
            return false;
        } else if (topCardIsPenalty) {
            return true;

        }

        return false;
    }

    void pickUpDeck(Deque<CardValue> deck, Deque<CardValue> hand) {
        while (!deck.isEmpty()) {
            hand.addLast(deck.removeLast());
        }
        numberTricks++;
    }

    public GameStats playGame() {
        Deque<CardValue> deck = new ArrayDeque<>();
        for (Player player : Iterables.cycle(Player.values())) {
            logger.trace(String.format("Turn of %s", player));
            if (!playTurn(player, deck)) {
                break;
            }
        }
        Player winner = playerIterator.next();

        return new GameStats(numberTricks, numberCards, winner);
    }

    public Boolean getPenaltyMode() {
        return isPenalty;
    }

    void setPenaltyMode(boolean penaltyMode) {
        isPenalty = penaltyMode;
    }

    Integer computeCardsToPlay(Deque<CardValue> deck) {
        if (deck.isEmpty()) {
            return 1;
        } else {
            return deck.peek().getPenalty();
        }
    }

    /**
     * Play the top card of the player's hand onto the deck
     *
     * @param deck
     * @param playerHand
     * @return The card that was played
     */
    CardValue playSingleCard(Deque<CardValue> deck, Deque<CardValue> playerHand) {
        CardValue card = playerHand.pop();
        deck.push(card);
        return card;
    }

    Boolean playCards(Deque<CardValue> deck, Deque<CardValue> playerHand, Integer cardsToPlay) {
        logger.trace(String.format("Needs to play %s cards", cardsToPlay));

        for (int i = 0; i < cardsToPlay; i++) {
            numberCards++;
            if (playerHand.isEmpty()) {
                logger.trace("Out of cards");
                return false;
            }
            CardValue card = playSingleCard(deck, playerHand);
            logger.trace(String.format("Plays card %s", card));

            if (!card.equals(CardValue.NON_FACE)) {
                return true;
            }
        }
        logger.trace("End of turn");
        return true;
    }
}
