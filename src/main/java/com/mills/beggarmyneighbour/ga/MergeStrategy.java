package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import org.apache.commons.lang3.tuple.Pair;

public interface MergeStrategy {

    Pair<SpecificDeckRepresentation, SpecificDeckRepresentation> mergeDecks(SpecificDeckRepresentation deck1,
                                                                            SpecificDeckRepresentation deck2);

}
