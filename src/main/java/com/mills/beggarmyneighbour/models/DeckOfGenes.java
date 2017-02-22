package com.mills.beggarmyneighbour.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeckOfGenes
    extends ArrayList<Pair<CardValue, Double>>
    implements List<Pair<CardValue, Double>> {

    private static final Logger LOG = LoggerFactory.getLogger(DeckOfGenes.class);

    private int score;
    private Map<Deck, Integer> parents = new HashMap<>();

    private Deck cachedDeck;

    public DeckOfGenes()
    {
        super();
    }

    public DeckOfGenes(Collection<? extends Pair<CardValue, Double>> collection) {
        super(collection);
    }

    public static DeckOfGenes randomDeckOfGenes()
    {
        DeckOfGenes deck = new DeckOfGenes();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                deck.add(Pair.of(CardValue.NON_FACE, Math.random()));
            }
            for (CardValue cardValue : EnumSet.complementOf(EnumSet.of(CardValue.NON_FACE))) {
                deck.add(Pair.of(cardValue, Math.random()));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    public Map<Deck, Integer> getParents() {
        return parents;
    }

    public void addParent(Deck deck, int score)
    {
        parents.put(deck, score);
    }

    public int getScore() {
        return score;
    }

    public DeckOfGenes setScore(int score) {
        this.score = score;
        return this;
    }

    public Deck toDeck()
    {
        if (cachedDeck == null) {
            cachedDeck = new Deck(this.stream().map(Pair::getKey).collect(Collectors.toList()));
        }
        return cachedDeck;
    }

    public boolean isValid()
    {
        for (CardValue cardValue : EnumSet.allOf(CardValue.class)) {
            int frequency = Collections.frequency(toDeck(), cardValue);
            if (frequency > cardValue.getRequiredInDeck()) {
                return false;
            }
        }
        return true;
    }

    public void updateScore(int index, double scoreAdjustment) {
        set(index, Pair.of(get(index).getKey(), scoreAdjustment));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DeckOfGenes pairs = (DeckOfGenes) o;

        return new EqualsBuilder()
                   .append(toDeck(), pairs.toDeck())
                   .append(getScore(), pairs.getScore())
                   .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                   .append(toDeck())
                   .append(getScore())
                   .toHashCode();
    }

    @Override
    public boolean add(Pair<CardValue, Double> value)
    {
        cachedDeck = null;
        return super.add(value);
    }

    @Override
    public boolean addAll(Collection<? extends Pair<CardValue, Double>> collection) {
        cachedDeck = null;
        return super.addAll(collection);
    }
}
