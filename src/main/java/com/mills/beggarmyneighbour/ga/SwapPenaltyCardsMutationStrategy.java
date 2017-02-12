package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;

import java.util.List;

public class SwapPenaltyCardsMutationStrategy implements MutationStrategy {
    @Override
    public SpecificDeckRepresentation mutateDeck(SpecificDeckRepresentation deck) {
        int random = (int) (Math.random()*4);
        int i = (int) (Math.random()*4);
        int j = (int) (Math.random()*4);

        List<Integer> listRep = deck.toList();

        Integer first = listRep.get(random*i);
        Integer second = listRep.get(random*j);

        listRep.set(random*i, second);
        listRep.set(random*j, first);

        return SpecificDeckRepresentation.fromOrderedList(listRep);
    }
}
