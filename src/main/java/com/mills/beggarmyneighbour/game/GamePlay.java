package com.mills.beggarmyneighbour.game;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.mills.beggarmyneighbour.models.Card;
import com.mills.beggarmyneighbour.models.Player;

import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
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
    private Deque<Card> deck;

    private Iterator<Player> playerIterator;

    public GamePlay(Deque<Card> deck, Map<Player, Deque<Card>> playerHands)
    {
        this.playerHands = playerHands;
        this.deck = deck;
        this.playerIterator = Iterables.cycle(Player.values()).iterator();
    }

    public boolean playNextTurn()
    {
        Player player = playerIterator.next();
        int cardsToPlay = 1;

        logger.warning(String.format("Turn of player %s", player));

        Deque<Card> hand = playerHands.get(player);

        if (!deck.isEmpty()) {
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

        if (hand.isEmpty()) {
            logger.warning(String.format("End of game. Player %s lost.", player));
            return false;
        }

        return true;
    }

    public Player playGame()
    {
        while(playNextTurn()) {}
        return playerIterator.next();
    }

    public static Player playGame(Deque<Card> deck, Map<Player, Deque<Card>> playerHands) {
        return new GamePlay(deck, playerHands).playGame();
    }

    private static void playCards(Deque<Card> deck, Deque<Card> playerHand, Integer cardsToPlay)
    {
        for (int i = 0; i < cardsToPlay; i++) {
            if (playerHand.isEmpty()) {
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
}
