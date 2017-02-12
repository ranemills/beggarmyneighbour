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

    private final Deck deck;
    private volatile GameStats gameStats;

    public GamePlayThread(Deck deck)
    {
        this.deck = deck;
    }

    private static GameStats generateAndPlayGame(Deck deck)
    {
        Map<Player, Deque<CardValue>> playerHands = CardOperations.splitCards(deck);

        GameStats gameStats = GamePlay.playGame(playerHands);
        gameStats.setSpecificDeckRepresentation(SpecificDeckRepresentation.fromDeck(deck));
        return gameStats;
    }

    public GameStats getGameStats() {
        return gameStats;
    }

    @Override
    public void run() {
        Map<Player, Deque<CardValue>> playerHands = CardOperations.splitCards(deck);

        gameStats = GamePlay.playGame(playerHands);
        gameStats.setSpecificDeckRepresentation(SpecificDeckRepresentation.fromDeck(deck));
    }
}
