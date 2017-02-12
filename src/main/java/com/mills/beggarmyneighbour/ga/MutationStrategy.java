package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.Deck;

/**
 * Created by ryan on 12/02/17.
 */
public interface MutationStrategy {

    Deck mutateDeck(Deck deck);

}
