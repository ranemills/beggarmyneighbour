package com.mills.beggarmyneighbour.models;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mills.beggarmyneighbour.models.CardValue.ACE;
import static com.mills.beggarmyneighbour.models.CardValue.JACK;
import static com.mills.beggarmyneighbour.models.CardValue.KING;
import static com.mills.beggarmyneighbour.models.CardValue.NON_FACE;
import static com.mills.beggarmyneighbour.models.CardValue.QUEEN;

public class SpecificDeckRepresentation {

    private SpecificDeckRepresentation leftParent;
    private SpecificDeckRepresentation rightParent;

    private Map<Integer, Double> aces = new HashMap<>();
    private Map<Integer, Double> kings = new HashMap<>();
    private Map<Integer, Double> queens = new HashMap<>();
    private Map<Integer, Double> jacks = new HashMap<>();

    private int score;

    private SpecificDeckRepresentation() {
    }

    public void updateGeneScore(CardValue cardValue, Integer location, double newGeneScore)
    {
        switch(cardValue)
        {
            case ACE:
                aces.put(location, newGeneScore);
                break;
            case KING:
                kings.put(location, newGeneScore);
                break;
            case QUEEN:
                queens.put(location, newGeneScore);
                break;
            case JACK:
                jacks.put(location, newGeneScore);
                break;
        }
    }

    public Double getGeneScore(CardValue cardValue, Integer location)
    {
        switch(cardValue)
        {
            case ACE:
                return aces.get(location);
            case KING:
                return kings.get(location);
            case QUEEN:
                return queens.get(location);
            case JACK:
                return jacks.get(location);
            default:
                return null;

        }
    }

    public static SpecificDeckRepresentation fromOrderedList(List<Integer> orderedList)
    {
        SpecificDeckRepresentation deckRepresentation = new SpecificDeckRepresentation();

        deckRepresentation.aces = toMapWithRandomFitness(orderedList.subList(0, 4));
        deckRepresentation.kings = toMapWithRandomFitness(orderedList.subList(4, 8));
        deckRepresentation.queens = toMapWithRandomFitness(orderedList.subList(8, 12));
        deckRepresentation.jacks = toMapWithRandomFitness(orderedList.subList(12, 16));
        return deckRepresentation;
    }

    private static Map<Integer, Double> toMapWithRandomFitness(List<Integer> list)
    {
        return list.stream().collect(Collectors.toMap(x -> x, x -> Math.random()));
    }

    static SpecificDeckRepresentation fromDeck(Deck deck)
    {
        SpecificDeckRepresentation deckRepresentation = new SpecificDeckRepresentation();

        for (int i = 0; i < deck.size(); i++) {
            CardValue value = deck.get(i);

            switch (value) {
                case KING:
                    deckRepresentation.kings.put(i, Math.random());
                    break;
                case QUEEN:
                    deckRepresentation.queens.put(i, Math.random());
                    break;
                case JACK:
                    deckRepresentation.jacks.put(i, Math.random());
                    break;
                case ACE:
                    deckRepresentation.aces.put(i, Math.random());
                    break;
            }

        }

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

    public int getScore() {
        return score;
    }

    public SpecificDeckRepresentation setScore(int score) {
        this.score = score;
        return this;
    }

    public SpecificDeckRepresentation getLeftParent() {

        return leftParent;
    }

    public SpecificDeckRepresentation setLeftParent(SpecificDeckRepresentation leftParent) {
        this.leftParent = leftParent;
        return this;
    }

    public SpecificDeckRepresentation getRightParent() {
        return rightParent;
    }

    public SpecificDeckRepresentation setRightParent(SpecificDeckRepresentation rightParent) {
        this.rightParent = rightParent;
        return this;
    }

    public List<Integer> getAces() {
        return ImmutableList.copyOf(aces.keySet());
    }

    public List<Integer> getKings() {
        return ImmutableList.copyOf(kings.keySet());
    }

    public List<Integer> getQueens() {
        return ImmutableList.copyOf(queens.keySet());
    }

    public List<Integer> getJacks() {
        return ImmutableList.copyOf(jacks.keySet());
    }

    public List<Integer> get(CardValue value)
    {
        switch(value)
        {
            case ACE: return getAces();
            case KING: return getKings();
            case QUEEN: return getQueens();
            case JACK: return getJacks();
            default: return null;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                   .append(ImmutableList.sortedCopyOf(getAces()))
                   .append(ImmutableList.sortedCopyOf(getKings()))
                   .append(ImmutableList.sortedCopyOf(getQueens()))
                   .append(ImmutableList.sortedCopyOf(getJacks()))
                   .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SpecificDeckRepresentation that = (SpecificDeckRepresentation) o;

        return new EqualsBuilder()
                   .append(ImmutableList.sortedCopyOf(getAces()), ImmutableList.sortedCopyOf(that.getAces()))
                   .append(ImmutableList.sortedCopyOf(getKings()), ImmutableList.sortedCopyOf(that.getKings()))
                   .append(ImmutableList.sortedCopyOf(getQueens()), ImmutableList.sortedCopyOf(that.getQueens()))
                   .append(ImmutableList.sortedCopyOf(getJacks()), ImmutableList.sortedCopyOf(that.getJacks()))
                   .isEquals();
    }

    @Override
    public String toString() {
        return toDeck().toString() + ":" + getScore();
    }

    public List<Integer> toList()
    {
        List<Integer> orderedSet = new ArrayList<>();
        orderedSet.addAll(aces.keySet());
        orderedSet.addAll(kings.keySet());
        orderedSet.addAll(queens.keySet());
        orderedSet.addAll(jacks.keySet());

        return orderedSet;
    }

    public Deck toDeck()
    {
        Deck deck = new Deck();
        for (int i = 0; i < 52; i++) {
            if (aces.keySet().contains(i)) {
                deck.add(ACE);
            } else if (kings.keySet().contains(i)) {
                deck.add(KING);
            } else if (queens.keySet().contains(i)) {
                deck.add(QUEEN);
            } else if (jacks.keySet().contains(i)) {
                deck.add(JACK);
            } else {
                deck.add(NON_FACE);
            }
        }
        return deck;
    }

    public Boolean isValid()
    {
        Set<Integer> test = new HashSet<>();
        test.addAll(aces.keySet());
        test.addAll(kings.keySet());
        test.addAll(queens.keySet());
        test.addAll(jacks.keySet());
        return test.size() == 16;
    }

    public Map<Integer, Double> getCards(CardValue cardValue)
    {
        switch(cardValue)
        {
            case ACE: return aces;
            case KING: return kings;
            case QUEEN: return queens;
            case JACK: return jacks;
            default: return Collections.emptyMap();
        }
    }


    public List<Pair<Integer, Double>> getAll()
    {
        List<Pair<Integer, Double>> all = new ArrayList<>();
        for(CardValue cardValue : EnumSet.complementOf(EnumSet.of(NON_FACE)))
        {
            for(Map.Entry<Integer, Double> entry : getCards(cardValue).entrySet())
            {
                all.add(Pair.of(entry.getKey(), entry.getValue()));
            }
        }
        return all;
    }

    public static SpecificDeckRepresentation fromPairList(List<Pair<Integer, Double>> pairList)
    {

    }

}
