package com.mills.beggarmyneighbour.models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SpecificDeckRepresentationTest {

    private static Deck generateOrderedDeck()
    {
        Deck deck = new Deck();
        for (int i = 0; i < 36; i++) {
            deck.add(CardValue.NON_FACE);
        }
        for (int i = 0; i < 4; i++) {
            deck.add(CardValue.ACE);
            deck.add(CardValue.KING);
            deck.add(CardValue.QUEEN);
            deck.add(CardValue.JACK);
        }
        return deck;
    }

    @Test
    public void canCreateFromDeck()
    {
        Deck deck = generateOrderedDeck();

        SpecificDeckRepresentation deckRepresentation = SpecificDeckRepresentation.fromDeck(deck);

        assertThat(deckRepresentation.getAces(), hasItems(36, 40, 44, 48));
        assertThat(deckRepresentation.getKings(), hasItems(37, 41, 45, 49));
        assertThat(deckRepresentation.getQueens(), hasItems(38, 42, 46, 50));
        assertThat(deckRepresentation.getJacks(), hasItems(39, 43, 47, 51));

    }

    @Test
    public void canConvertFromDeckAndBackAgain()
    {
        Deck deck = generateOrderedDeck();
        Deck otherDeck = SpecificDeckRepresentation.fromDeck(deck).toDeck();
        assertEquals(deck, otherDeck);
    }

    @Test
    public void canCreateListRepresentation()
    {
        Deck deck = generateOrderedDeck();
        SpecificDeckRepresentation deckRepresentation = SpecificDeckRepresentation.fromDeck(deck);

        List<Integer> list = deckRepresentation.toList();
        assertThat(list.subList(0,4), hasItems(40, 44, 48, 36));
        assertThat(list.subList(4,8), hasItems(37, 41, 45, 49));
        assertThat(list.subList(8,12), hasItems(38, 42, 46, 50));
        assertThat(list.subList(12,16), hasItems(39, 43, 47, 51));
    }

    @Test
    public void canCreateFromOrderedList()
    {
        ArrayList<Integer> input = new ArrayList<>();
        input.addAll(Arrays.asList(40, 44, 48, 36));
        input.addAll(Arrays.asList(37, 41, 45, 49));
        input.addAll(Arrays.asList(38, 42, 46, 50));
        input.addAll(Arrays.asList(39, 43, 47, 51));

        assertEquals(generateOrderedDeck(), SpecificDeckRepresentation.fromOrderedList(input).toDeck());
    }

    @Test
    public void equality()
    {
        ArrayList<Integer> leftInput = new ArrayList<>();
        leftInput.addAll(Arrays.asList(44, 40, 48, 36));
        leftInput.addAll(Arrays.asList(41, 37, 45, 49));
        leftInput.addAll(Arrays.asList(42, 38, 46, 50));
        leftInput.addAll(Arrays.asList(43, 39, 47, 51));

        SpecificDeckRepresentation left = SpecificDeckRepresentation.fromOrderedList(leftInput);
        ArrayList<Integer> rightInput = new ArrayList<>();
        leftInput.addAll(Arrays.asList(40, 44, 48, 36));
        leftInput.addAll(Arrays.asList(37, 41, 45, 49));
        leftInput.addAll(Arrays.asList(38, 42, 46, 50));
        leftInput.addAll(Arrays.asList(39, 43, 47, 51));

        SpecificDeckRepresentation right = SpecificDeckRepresentation.fromOrderedList(leftInput);

        assertEquals(left, right);
    }

}