package com.mills.beggarmyneighbour.game;

import com.google.common.collect.Iterables;
import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mills.beggarmyneighbour.GameRunner.PLAYER_VALUES;

public class GamePlay {
    private Iterator<Player> playerIterator = Iterables.cycle(PLAYER_VALUES).iterator();

    private Boolean isPenalty = false;

    private Map<Player, Deque<CardValue>> playerHands;
    private int numberTricks = 0;

    private int numberCards = 0;

    GamePlay() {
        this(new HashMap<>());
    }

    GamePlay(Map<Player, Deque<CardValue>> playerHands) {
        this.playerHands = playerHands;
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

        return playCards(deck, hand, cardsToPlay);
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

    private GameStats playGame() {
        Deque<CardValue> deck = new ArrayDeque<>();
        for (Player player : Iterables.cycle(PLAYER_VALUES)) {
            if (!playTurn(player, deck)) {
                break;
            }
        }
        Player winner = playerIterator.next();

        return new GameStats(numberTricks, numberCards, winner);
    }

    private Boolean getPenaltyMode() {
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
        for (int i = 0; i < cardsToPlay; i++) {
            numberCards++;
            if (playerHand.isEmpty()) {
                return false;
            }
            CardValue card = playSingleCard(deck, playerHand);

            if (!card.equals(CardValue.NON_FACE)) {
                return true;
            }
        }
        return true;
    }
}
