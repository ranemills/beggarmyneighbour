package com.mills.beggarmyneighbour.ga;

import com.mills.beggarmyneighbour.models.SpecificDeckRepresentation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;

public interface MergeStrategy {

    Collection<SpecificDeckRepresentation> mergeDecks(SpecificDeckRepresentation deck1,
                                                      SpecificDeckRepresentation deck2);

}
