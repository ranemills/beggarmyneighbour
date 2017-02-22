package com.mills.beggarmyneighbour.game;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.utils.CardOperations;

import java.util.Deque;
import java.util.Map;

public class GamePlayThread
    implements Runnable {

    private final Deck deck;
    private volatile int score;

    public GamePlayThread(Deck deck)
    {
        this.deck = deck;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void run() {
        Map<Player, Deque<CardValue>> playerHands = CardOperations.splitCards(deck);

        GameStats gameStats = GamePlay.playGame(playerHands);

        score = gameStats.getTricks();
    }
}
