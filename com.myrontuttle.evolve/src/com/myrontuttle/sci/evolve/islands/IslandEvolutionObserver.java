//=============================================================================
// Copyright 2006-2010 Daniel W. Dyer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//=============================================================================
package com.myrontuttle.sci.evolve.islands;

import com.myrontuttle.sci.evolve.api.EvolutionObserver;
import com.myrontuttle.sci.evolve.api.PopulationStats;

/**
 * A specialisation of {@link com.myrontuttle.sci.evolve.EvolutionObserver} that, as well as
 * receiving global population updates (at the end of each epoch), can receive individual island
 * population updates (at the end of each generation on each island).
 * @param <T> The type of entity being evolved.
 * @author Daniel Dyer
 */
public interface IslandEvolutionObserver<T> extends EvolutionObserver<T>
{
    /**
     * Method called to notify the listener of the state of the population of an individual
     * island.  This will be called once for each generation on each island.
     * @param islandIndex Identifies which individual island the data comes from.
     * Indices start at zero and are sequential.
     * @param data The latest data from the evolution on the specified island.
     */
    void islandPopulationUpdate(int islandIndex, PopulationStats<? extends T> data);
}
