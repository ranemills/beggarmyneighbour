package com.mills.beggarmyneighbour.models;

public enum CardValue {
    NON_FACE(1, '-', 36),
    ACE(4, 'A', 4),
    JACK(1, 'J', 4),
    QUEEN(2, 'Q', 4),
    KING(3, 'K', 4);

    private int penalty;
    private char asciiChar;
    private int requiredInDeck;

    CardValue(int penalty, char asciiChar, int requiredInDeck)
    {
        this.penalty = penalty;
        this.asciiChar = asciiChar;
        this.requiredInDeck = requiredInDeck;
    }

    public int getPenalty() {
        return penalty;
    }

    public char getAsciiChar() {
        return asciiChar;
    }

    public int getRequiredInDeck() {
        return requiredInDeck;
    }
}
