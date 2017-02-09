package com.mills.beggarmyneighbour.game;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class GamePlayTest {

    @Test
    public void stopsPlayingCardsWhenPenaltyCardIsPlayed() {
        GamePlay gamePlaySpy = Mockito.spy(new GamePlay());

        Deque<CardValue> deck = new ArrayDeque<>();
        Deque<CardValue> hand = new ArrayDeque<>();
        hand.push(CardValue.KING);
        hand.push(CardValue.NON_FACE);

        Boolean result = gamePlaySpy.playCards(deck, hand, 4);

        assertTrue(result);
        verify(gamePlaySpy, times(2)).playSingleCard(any(Deque.class), any(Deque.class));
    }

    @Test
    public void playsAllCardsWhenPenaltyCardIsNotPlayed() {
        GamePlay gamePlaySpy = Mockito.spy(new GamePlay());

        Deque<CardValue> deck = new ArrayDeque<>();
        Deque<CardValue> hand = new ArrayDeque<>();
        hand.push(CardValue.NON_FACE);
        hand.push(CardValue.NON_FACE);
        hand.push(CardValue.NON_FACE);
        hand.push(CardValue.NON_FACE);

        Boolean result = gamePlaySpy.playCards(deck, hand, 4);

        assertTrue(result);
        verify(gamePlaySpy, times(4)).playSingleCard(any(Deque.class), any(Deque.class));
    }

    @Test
    public void returnsFalseWhenRunsOutOfCards() {
        GamePlay gamePlaySpy = Mockito.spy(new GamePlay());

        Deque<CardValue> deck = new ArrayDeque<>();
        Deque<CardValue> hand = new ArrayDeque<>();
        hand.push(CardValue.NON_FACE);

        Boolean result = gamePlaySpy.playCards(deck, hand, 2);

        assertFalse(result);
        verify(gamePlaySpy, times(1)).playSingleCard(any(Deque.class), any(Deque.class));
    }

    @Test
    public void cardsToPlayIsOneIfTopCardIsNotPenaltyCard()
    {
        GamePlay gamePlay = new GamePlay();

        Deque<CardValue> deck = new ArrayDeque<>();
        deck.push(CardValue.NON_FACE);

        int result = gamePlay.computeCardsToPlay(deck);

        assertEquals(result, 1);
    }

    @Test
    public void cardsToPlayIsTwoForTopCardIsQueen()
    {
        GamePlay gamePlay = new GamePlay();

        Deque<CardValue> deck = new ArrayDeque<>();
        deck.push(CardValue.QUEEN);

        int result = gamePlay.computeCardsToPlay(deck);

        assertEquals(result, 2);
    }

    @Test
    public void cardsToPlayIsOneForEmptyDeck()
    {
        GamePlay gamePlay = new GamePlay();

        Deque<CardValue> deck = new ArrayDeque<>();

        int result = gamePlay.computeCardsToPlay(deck);

        assertEquals(result, 1);
    }

    @Test
    public void canPickUpDeckInRightOrder()
    {
        GamePlay gamePlay = new GamePlay();

        Deque<CardValue> deck = new ArrayDeque<>();
        deck.push(CardValue.JACK);
        deck.push(CardValue.NON_FACE);
        Deque<CardValue> hand = new ArrayDeque<>();
        hand.push(CardValue.NON_FACE);

        gamePlay.pickUpDeck(deck, hand);

        assertEquals(hand.pop(), CardValue.NON_FACE);
        assertEquals(hand.pop(), CardValue.JACK);
        assertEquals(hand.pop(), CardValue.NON_FACE);
    }

    @Test
    public void deckIsPickedUpAndPenaltyModeIsResetWhenTopCardIsNotPenaltyAndInPenaltyMode()
    {
        GamePlay gamePlay = spy(new GamePlay());
        gamePlay.setPenaltyMode(true);

        Deque<CardValue> deck = new ArrayDeque<>();
        deck.push(CardValue.NON_FACE);
        Deque<CardValue> hand = new ArrayDeque<>();
        hand.push(CardValue.NON_FACE);

        boolean penaltyMode = gamePlay.dealWithPenaltyMode(deck, hand);

        assertFalse(penaltyMode);
        verify(gamePlay).pickUpDeck(deck, hand);
    }

    @Test
    public void returnsTrueIfTopCardIsPenaltyCardWhenInPenaltyMode()
    {
        GamePlay gamePlay = spy(new GamePlay());
        gamePlay.setPenaltyMode(true);

        Deque<CardValue> deck = new ArrayDeque<>();
        deck.push(CardValue.KING);
        Deque<CardValue> hand = new ArrayDeque<>();
        hand.push(CardValue.NON_FACE);

        boolean penaltyMode = gamePlay.dealWithPenaltyMode(deck, hand);

        assertTrue(penaltyMode);
        verify(gamePlay, never()).pickUpDeck(deck, hand);
    }

    @Test
    public void returnsTrueIfTopCardIsPenaltyCardWhenNotInPenaltyMode()
    {
        GamePlay gamePlay = spy(new GamePlay());
        gamePlay.setPenaltyMode(false);

        Deque<CardValue> deck = new ArrayDeque<>();
        deck.push(CardValue.KING);
        Deque<CardValue> hand = new ArrayDeque<>();
        hand.push(CardValue.NON_FACE);

        boolean penaltyMode = gamePlay.dealWithPenaltyMode(deck, hand);

        assertTrue(penaltyMode);
        verify(gamePlay, never()).pickUpDeck(deck, hand);
    }

    @Test
    public void returnsFalseIfTopCardIsNotPenaltyCardAndNotInPenaltyMode()
    {
        GamePlay gamePlay = spy(new GamePlay());
        gamePlay.setPenaltyMode(false);

        Deque<CardValue> deck = new ArrayDeque<>();
        deck.push(CardValue.NON_FACE);
        Deque<CardValue> hand = new ArrayDeque<>();
        hand.push(CardValue.NON_FACE);

        boolean penaltyMode = gamePlay.dealWithPenaltyMode(deck, hand);

        assertFalse(penaltyMode);
        verify(gamePlay, never()).pickUpDeck(deck, hand);
    }

    @Test
    public void firstTurnSimplyPlaysACard()
    {
        Player player = Player.PLAYER_ONE;
        Deque<CardValue> playerHand = new ArrayDeque<>();
        playerHand.push(CardValue.NON_FACE);
        playerHand.push(CardValue.NON_FACE);

        Map<Player, Deque<CardValue>> playerHands = new HashMap<>();
        playerHands.put(player, playerHand);

        Deque<CardValue> deck = new ArrayDeque<>();

        GamePlay gamePlay = new GamePlay(playerHands);

        gamePlay.playTurn(player, deck);

        assertEquals(deck.size(), 1);
        assertEquals(deck.peek(), CardValue.NON_FACE);
        assertEquals(playerHand.peek(), CardValue.NON_FACE);
    }

}
