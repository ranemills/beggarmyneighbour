package com.mills.beggarmyneighbour.models;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mills.beggarmyneighbour.models.CardValue.ACE;
import static com.mills.beggarmyneighbour.models.CardValue.JACK;
import static com.mills.beggarmyneighbour.models.CardValue.KING;
import static com.mills.beggarmyneighbour.models.CardValue.NON_FACE;
import static com.mills.beggarmyneighbour.models.CardValue.QUEEN;

public class SpecificDeckRepresentation {

    private ImmutableList<Integer> aces;
    private ImmutableList<Integer> kings;
    private ImmutableList<Integer> queens;
    private ImmutableList<Integer> jacks;

    private SpecificDeckRepresentation() {
    }

    public static SpecificDeckRepresentation fromOrderedList(List<Integer> orderedList)
    {
        SpecificDeckRepresentation deckRepresentation = new SpecificDeckRepresentation();
        deckRepresentation.aces = ImmutableList.<Integer>builder().addAll(orderedList.subList(0, 4)).build();
        deckRepresentation.kings = ImmutableList.<Integer>builder().addAll(orderedList.subList(4, 8)).build();
        deckRepresentation.queens = ImmutableList.<Integer>builder().addAll(orderedList.subList(8, 12)).build();
        deckRepresentation.jacks = ImmutableList.<Integer>builder().addAll(orderedList.subList(12, 16)).build();
        return deckRepresentation;
    }

    static SpecificDeckRepresentation fromDeck(Deck deck)
    {
        Map<CardValue, List<Integer>> cards = new HashMap<>();
        cards.put(ACE, new ArrayList<>());
        cards.put(KING, new ArrayList<>());
        cards.put(QUEEN, new ArrayList<>());
        cards.put(JACK, new ArrayList<>());

        for (int i = 0; i < deck.size(); i++) {
            CardValue value = deck.get(i);
            if(value == NON_FACE) { continue; }
            cards.get(value).add(i);
        }

        SpecificDeckRepresentation deckRepresentation = new SpecificDeckRepresentation();
        deckRepresentation.aces = ImmutableList.copyOf(cards.get(ACE));
        deckRepresentation.kings = ImmutableList.copyOf(cards.get(KING));
        deckRepresentation.queens = ImmutableList.copyOf(cards.get(QUEEN));
        deckRepresentation.jacks = ImmutableList.copyOf(cards.get(JACK));

        return deckRepresentation;
    }

    public static SpecificDeckRepresentation randomDeck()
    {
        Set<Integer> numbers = new HashSet<>();
        while (numbers.size() != 16) {
            numbers.add((int) (Math.random() * 52));
        }
        return SpecificDeckRepresentation.fromOrderedList(new ArrayList<>(numbers));
    }

    public ImmutableList<Integer> getAces() {
        return aces;
    }

    public ImmutableList<Integer> getKings() {
        return kings;
    }

    public ImmutableList<Integer> getQueens() {
        return queens;
    }

    public ImmutableList<Integer> getJacks() {
        return jacks;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                   .append(getAces())
                   .append(getKings())
                   .append(getQueens())
                   .append(getJacks())
                   .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SpecificDeckRepresentation that = (SpecificDeckRepresentation) o;

        return new EqualsBuilder()
                   .append(getAces(), that.getAces())
                   .append(getKings(), that.getKings())
                   .append(getQueens(), that.getQueens())
                   .append(getJacks(), that.getJacks())
                   .isEquals();
    }

    @Override
    public String toString() {
        return toDeck().toString();
    }

    public List<Integer> toList()
    {
        List<Integer> orderedSet = new ArrayList<>();
        orderedSet.addAll(aces);
        orderedSet.addAll(kings);
        orderedSet.addAll(queens);
        orderedSet.addAll(jacks);

        return orderedSet;
    }

    public Deck toDeck()
    {
        Deck deck = new Deck();
        for (int i = 0; i < 52; i++) {
            deck.add(NON_FACE);
        }
        for (Integer i : aces) {
            deck.set(i, ACE);
        }
        for (Integer i : kings) {
            deck.set(i, KING);
        }
        for (Integer i : queens) {
            deck.set(i, QUEEN);
        }
        for (Integer i : jacks) {
            deck.set(i, JACK);
        }
        return deck;
    }

    public Boolean isValid()
    {
        Set<Integer> test = new HashSet<>();
        test.addAll(aces);
        test.addAll(kings);
        test.addAll(queens);
        test.addAll(jacks);
        return test.size() == 16;
    }

}
