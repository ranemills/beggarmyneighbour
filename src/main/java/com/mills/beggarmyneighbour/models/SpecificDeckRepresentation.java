package com.mills.beggarmyneighbour.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mills.beggarmyneighbour.models.CardValue.*;

public class SpecificDeckRepresentation {

    private Set<Integer> aces;
    private Set<Integer> kings;
    private Set<Integer> queens;

    public Set<Integer> getAces() {
        return aces;
    }

    public Set<Integer> getKings() {
        return kings;
    }

    public Set<Integer> getQueens() {
        return queens;
    }

    public Set<Integer> getJacks() {
        return jacks;
    }

    private Set<Integer> jacks;

    private SpecificDeckRepresentation() {
        aces = new HashSet<>();
        kings = new HashSet<>();
        queens = new HashSet<>();
        jacks = new HashSet<>();
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
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                   .append(getAces())
                   .append(getKings())
                   .append(getQueens())
                   .append(getJacks())
                   .toHashCode();
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

    public static SpecificDeckRepresentation fromOrderedList(List<Integer> orderedList)
    {
        SpecificDeckRepresentation deckRepresentation = new SpecificDeckRepresentation();
        deckRepresentation.aces.addAll(orderedList.subList(0,4));
        deckRepresentation.kings.addAll(orderedList.subList(4,8));
        deckRepresentation.queens.addAll(orderedList.subList(8,12));
        deckRepresentation.jacks.addAll(orderedList.subList(12,16));
        return deckRepresentation;
    }

    public static SpecificDeckRepresentation fromDeck(Deck deck)
    {
        SpecificDeckRepresentation deckRepresentation = new SpecificDeckRepresentation();
        for(int i=0; i<deck.size(); i++)
        {
            CardValue value = deck.get(i);
            switch(value)
            {
                case ACE:
                    deckRepresentation.aces.add(i);
                    break;
                case KING:
                    deckRepresentation.kings.add(i);
                    break;
                case QUEEN:
                    deckRepresentation.queens.add(i);
                    break;
                case JACK:
                    deckRepresentation.jacks.add(i);
                    break;
            }
        }
        return deckRepresentation;
    }

    public Deck toDeck()
    {
        Deck deck = new Deck();
        for(int i=0;i<52;i++)
        {
            deck.add(NON_FACE);
        }
        for(Integer i : aces)
        {
            deck.set(i, ACE);
        }
        for(Integer i : kings)
        {
            deck.set(i, KING);
        }
        for(Integer i : queens)
        {
            deck.set(i, QUEEN);
        }
        for(Integer i : jacks)
        {
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

    public static SpecificDeckRepresentation randomDeck()
    {
        Set<Integer> numbers = new HashSet<>();
        while(numbers.size() != 16)
        {
            numbers.add((int) (Math.random()*52));
        }
        return SpecificDeckRepresentation.fromOrderedList(new ArrayList<>(numbers));
    }

}
