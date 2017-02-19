package com.mills.beggarmyneighbour.game;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import com.mills.beggarmyneighbour.utils.CardOperations;

import java.util.Deque;
import java.util.Map;

public class GamePlayThread
    implements Runnable {

    private final SpecificDeckRepresentation deck;
    private volatile GameStats gameStats;

    public GamePlayThread(SpecificDeckRepresentation deck)
    {
        this.deck = deck;
    }

    public GameStats getGameStats() {
        return gameStats;
    }

    @Override
    public void run() {
        Map<Player, Deque<CardValue>> playerHands = CardOperations.splitCards(deck.toDeck());

        gameStats = GamePlay.playGame(playerHands);

        deck.setScore(gameStats.getTricks());
        gameStats.setSpecificDeckRepresentation(deck);
    }
}
