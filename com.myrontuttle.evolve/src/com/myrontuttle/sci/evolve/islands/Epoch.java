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

import java.util.List;
import java.util.concurrent.Callable;

import com.myrontuttle.sci.evolve.api.EvolutionEngine;
import com.myrontuttle.sci.evolve.api.TerminationCondition;
import com.myrontuttle.sci.evolve.api.EvaluatedCandidate;

/**
 * @author Daniel Dyer
 */
class Epoch<T> implements Callable<List<EvaluatedCandidate<T>>>
{
    private final EvolutionEngine<T> island;
    private final long populationId;
    private final int populationSize;
    private final int eliteCount;
    private final List<T> seedCandidates;
    private final TerminationCondition[] terminationConditions;

    Epoch(EvolutionEngine<T> island,
    	  long populationId,
          int populationSize,
          int eliteCount,
          List<T> seedCandidates,
          TerminationCondition... terminationConditions)
    {
        this.island = island;
        this.populationId = populationId;
        this.populationSize = populationSize;
        this.eliteCount = eliteCount;
        this.seedCandidates = seedCandidates;
        this.terminationConditions = terminationConditions;
    }


    public List<EvaluatedCandidate<T>> call() throws Exception
    {
        return island.evolvePopulation(populationId, populationSize, eliteCount, seedCandidates, terminationConditions);
    }
}
