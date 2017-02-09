package com.mills.beggarmyneighbour.models;

public enum CardValue {
    NON_FACE(1),
    ACE(4),
    JACK(1),
    QUEEN(2),
    KING(3);

    private int penalty;

    CardValue(int penalty)
    {
        this.penalty = penalty;
    }

    public int getPenalty() {
        return penalty;
    }
}
