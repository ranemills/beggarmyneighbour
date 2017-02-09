package com.mills.beggarmyneighbour.models;

public enum CardValue {
    NON_FACE(1, '-'),
    ACE(4, 'A'),
    JACK(1, 'J'),
    QUEEN(2, 'Q'),
    KING(3, 'K');

    private int penalty;
    private char asciiChar;

    CardValue(int penalty, char asciiChar)
    {
        this.penalty = penalty;
        this.asciiChar = asciiChar;
    }

    public int getPenalty() {
        return penalty;
    }

    public char getAsciiChar() {
        return asciiChar;
    }
}
