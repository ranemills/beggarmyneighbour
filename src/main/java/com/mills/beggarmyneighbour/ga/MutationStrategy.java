package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.CardValue;
import com.mills.beggarmyneighbour.models.Deck;

import java.util.List;

/**
 * Created by ryan on 12/02/17.
 */
public interface MutationStrategy {

    void mutateDeck(Deck deck);

}
