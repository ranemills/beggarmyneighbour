package com.mills.beggarmyneighbour.game;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.mills.beggarmyneighbour.models.Card;
import com.mills.beggarmyneighbour.models.Player;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by ryan on 08/02/17.
 */
public class GamePlay {
    private static final Logger logger = Logger.getLogger("GamePlay");

    private static final Map<Integer, Integer> PENALTIES = ImmutableMap.<Integer, Integer>builder().put(1, 4)
            .put(11, 1)
            .put(12, 2)
            .put(13, 3)
            .build();

    private Boolean isPenalty = false;
    private Map<Player, Deque<Card>> playerHands;

    private Iterator<Player> playerIterator;

    GamePlay() {
        this(new HashMap<Player, Deque<Card>>());
    }

    public GamePlay(Map<Player, Deque<Card>> playerHands) {
        this.playerHands = playerHands;
        this.playerIterator = Iterables.cycle(Player.values()).iterator();
    }

    public static Player playGame(Map<Player, Deque<Card>> playerHands) {
        return new GamePlay(playerHands).playGame();
    }

    boolean playTurn(Player player, Deque<Card> deck) {
        Deque<Card> hand = playerHands.get(player);

        if (!deck.isEmpty()) {
            setPenaltyMode(dealWithPenaltyMode(deck, hand));
        }

        int cardsToPlay = computeCardsToPlay(deck);

        if (!playCards(deck, hand, cardsToPlay)) {
            logger.warning(String.format("End of game. Player %s lost.", player));
            return false;
        }

        return true;
    }

    boolean dealWithPenaltyMode(Deque<Card> deck, Deque<Card> hand) {
        boolean topCardIsPenalty = PENALTIES.keySet().contains(deck.peek().getValue());

        if (!topCardIsPenalty && getPenaltyMode()) {
            pickUpDeck(deck, hand);
            return false;
        } else if (topCardIsPenalty) {
            return true;

        }

        return false;
    }

    void pickUpDeck(Deque<Card> deck, Deque<Card> hand) {
        while (!deck.isEmpty()) {
            hand.addLast(deck.removeLast());
        }
    }

    public Player playGame() {
        Deque<Card> deck = new ArrayDeque<>();
        for (Player player : Iterables.cycle(Player.values())) {
            logger.warning(String.format("Turn of %s", player));
            if (!playTurn(player, deck)) {
                break;
            }
        }
        return playerIterator.next();
    }

    public Boolean getPenaltyMode() {
        return isPenalty;
    }

    void setPenaltyMode(boolean penaltyMode) {
        isPenalty = penaltyMode;
    }

    Integer computeCardsToPlay(Deque<Card> deck) {
        if (deck.isEmpty()) {
            return 1;
        } else {
            Card topCard = deck.peek();
            Integer cardsToPlay = PENALTIES.get(topCard.getValue());
            if (cardsToPlay == null) {
                cardsToPlay = 1;
            }
            return cardsToPlay;
        }
    }

    /**
     * Play the top card of the player's hand onto the deck
     *
     * @param deck
     * @param playerHand
     * @return The card that was played
     */
    Card playSingleCard(Deque<Card> deck, Deque<Card> playerHand) {
        Card card = playerHand.pop();
        deck.push(card);
        return card;
    }

    Boolean playCards(Deque<Card> deck, Deque<Card> playerHand, Integer cardsToPlay) {
        logger.warning(String.format("Needs to play %s cards", cardsToPlay));

        for (int i = 0; i < cardsToPlay; i++) {
            if (playerHand.isEmpty()) {
                logger.warning("Out of cards");
                return false;
            }
            Card card = playSingleCard(deck, playerHand);
            logger.warning(String.format("Plays card %s", card));

            if (PENALTIES.keySet().contains(card.getValue())) {
                return true;
            }
        }
        logger.warning("End of turn");
        return true;
    }
}
