package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.Deck;
import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;

/**
 * Created by ryan on 12/02/17.
 */
public interface MutationStrategy {

    SpecificDeckRepresentation mutateDeck(SpecificDeckRepresentation deck);

}
