package com.mills.beggarmyneighbour.game;

import com.mills.beggarmyneighbour.models.Card;
import com.mills.beggarmyneighbour.models.Player;
import com.mills.beggarmyneighbour.models.Suit;
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

        Deque<Card> deck = new ArrayDeque<>();
        Deque<Card> hand = new ArrayDeque<>();
        hand.push(new Card(Suit.HEART, 13));
        hand.push(new Card(Suit.HEART, 8));

        Boolean result = gamePlaySpy.playCards(deck, hand, 4);

        assertTrue(result);
        verify(gamePlaySpy, times(2)).playSingleCard(any(Deque.class), any(Deque.class));
    }

    @Test
    public void playsAllCardsWhenPenaltyCardIsNotPlayed() {
        GamePlay gamePlaySpy = Mockito.spy(new GamePlay());

        Deque<Card> deck = new ArrayDeque<>();
        Deque<Card> hand = new ArrayDeque<>();
        hand.push(new Card(Suit.HEART, 2));
        hand.push(new Card(Suit.HEART, 3));
        hand.push(new Card(Suit.HEART, 4));
        hand.push(new Card(Suit.HEART, 5));

        Boolean result = gamePlaySpy.playCards(deck, hand, 4);

        assertTrue(result);
        verify(gamePlaySpy, times(4)).playSingleCard(any(Deque.class), any(Deque.class));
    }

    @Test
    public void returnsFalseWhenRunsOutOfCards() {
        GamePlay gamePlaySpy = Mockito.spy(new GamePlay());

        Deque<Card> deck = new ArrayDeque<>();
        Deque<Card> hand = new ArrayDeque<>();
        hand.push(new Card(Suit.HEART, 2));

        Boolean result = gamePlaySpy.playCards(deck, hand, 2);

        assertFalse(result);
        verify(gamePlaySpy, times(1)).playSingleCard(any(Deque.class), any(Deque.class));
    }

    @Test
    public void cardsToPlayIsOneIfTopCardIsNotPenaltyCard()
    {
        GamePlay gamePlay = new GamePlay();

        Deque<Card> deck = new ArrayDeque<>();
        deck.push(new Card(Suit.HEART, 2));

        int result = gamePlay.computeCardsToPlay(deck);

        assertEquals(result, 1);
    }

    @Test
    public void cardsToPlayIsTwoForTopCardIsQueen()
    {
        GamePlay gamePlay = new GamePlay();

        Deque<Card> deck = new ArrayDeque<>();
        deck.push(new Card(Suit.HEART, 12));

        int result = gamePlay.computeCardsToPlay(deck);

        assertEquals(result, 2);
    }

    @Test
    public void cardsToPlayIsOneForEmptyDeck()
    {
        GamePlay gamePlay = new GamePlay();

        Deque<Card> deck = new ArrayDeque<>();

        int result = gamePlay.computeCardsToPlay(deck);

        assertEquals(result, 1);
    }

    @Test
    public void canPickUpDeckInRightOrder()
    {
        GamePlay gamePlay = new GamePlay();

        Deque<Card> deck = new ArrayDeque<>();
        deck.push(new Card(Suit.CLUB, 2));
        deck.push(new Card(Suit.CLUB, 3));
        Deque<Card> hand = new ArrayDeque<>();
        hand.push(new Card(Suit.CLUB, 5));

        gamePlay.pickUpDeck(deck, hand);

        assertEquals(hand.pop(), new Card(Suit.CLUB, 5));
        assertEquals(hand.pop(), new Card(Suit.CLUB, 2));
        assertEquals(hand.pop(), new Card(Suit.CLUB, 3));
    }

    @Test
    public void deckIsPickedUpAndPenaltyModeIsResetWhenTopCardIsNotPenaltyAndInPenaltyMode()
    {
        GamePlay gamePlay = spy(new GamePlay());
        gamePlay.setPenaltyMode(true);

        Deque<Card> deck = new ArrayDeque<>();
        deck.push(new Card(Suit.CLUB, 2));
        Deque<Card> hand = new ArrayDeque<>();
        hand.push(new Card(Suit.CLUB, 5));

        boolean penaltyMode = gamePlay.dealWithPenaltyMode(deck, hand);

        assertFalse(penaltyMode);
        verify(gamePlay).pickUpDeck(deck, hand);
    }

    @Test
    public void returnsTrueIfTopCardIsPenaltyCardWhenInPenaltyMode()
    {
        GamePlay gamePlay = spy(new GamePlay());
        gamePlay.setPenaltyMode(true);

        Deque<Card> deck = new ArrayDeque<>();
        deck.push(new Card(Suit.CLUB, 13));
        Deque<Card> hand = new ArrayDeque<>();
        hand.push(new Card(Suit.CLUB, 5));

        boolean penaltyMode = gamePlay.dealWithPenaltyMode(deck, hand);

        assertTrue(penaltyMode);
        verify(gamePlay, never()).pickUpDeck(deck, hand);
    }

    @Test
    public void returnsTrueIfTopCardIsPenaltyCardWhenNotInPenaltyMode()
    {
        GamePlay gamePlay = spy(new GamePlay());
        gamePlay.setPenaltyMode(false);

        Deque<Card> deck = new ArrayDeque<>();
        deck.push(new Card(Suit.CLUB, 13));
        Deque<Card> hand = new ArrayDeque<>();
        hand.push(new Card(Suit.CLUB, 5));

        boolean penaltyMode = gamePlay.dealWithPenaltyMode(deck, hand);

        assertTrue(penaltyMode);
        verify(gamePlay, never()).pickUpDeck(deck, hand);
    }

    @Test
    public void returnsFalseIfTopCardIsNotPenaltyCardAndNotInPenaltyMode()
    {
        GamePlay gamePlay = spy(new GamePlay());
        gamePlay.setPenaltyMode(false);

        Deque<Card> deck = new ArrayDeque<>();
        deck.push(new Card(Suit.CLUB, 3));
        Deque<Card> hand = new ArrayDeque<>();
        hand.push(new Card(Suit.CLUB, 5));

        boolean penaltyMode = gamePlay.dealWithPenaltyMode(deck, hand);

        assertFalse(penaltyMode);
        verify(gamePlay, never()).pickUpDeck(deck, hand);
    }

    @Test
    public void firstTurnSimplyPlaysACard()
    {
        Player player = Player.PLAYER_ONE;
        Deque<Card> playerHand = new ArrayDeque<>();
        playerHand.push(new Card(Suit.CLUB, 2));
        playerHand.push(new Card(Suit.CLUB, 3));

        Map<Player, Deque<Card>> playerHands = new HashMap<>();
        playerHands.put(player, playerHand);

        Deque<Card> deck = new ArrayDeque<>();

        GamePlay gamePlay = new GamePlay(playerHands);

        gamePlay.playTurn(player, deck);

        assertEquals(deck.size(), 1);
        assertEquals(deck.peek(), new Card(Suit.CLUB, 3));
        assertEquals(playerHand.peek(), new Card(Suit.CLUB, 2));
    }

}
