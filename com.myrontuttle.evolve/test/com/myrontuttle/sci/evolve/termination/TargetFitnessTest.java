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
package com.myrontuttle.sci.evolve.termination;

import org.testng.annotations.Test;

import com.myrontuttle.sci.evolve.api.TerminationCondition;
import com.myrontuttle.sci.evolve.api.PopulationStats;
import com.myrontuttle.sci.evolve.termination.TargetFitness;

/**
 * Unit test for termination condition that checks the best fitness attained so far
 * against a pre-determined target.
 * @author Daniel Dyer
 */
public class TargetFitnessTest
{
    @Test
    public void testNaturalFitness()
    {
        TerminationCondition condition = new TargetFitness(10.0d, true);
        PopulationStats<Object> data = new PopulationStats<Object>(0, new Object(), 5.0d, 4.0d, 0, true, 2, 0, 0, 100);
        assert !condition.shouldTerminate(data) : "Should not terminate before target fitness is reached.";
        data = new PopulationStats<Object>(0, new Object(), 10.0d, 8.0d, 0, true, 2, 0, 0, 100);
        assert condition.shouldTerminate(data) : "Should terminate once target fitness is reached.";
    }


    @Test
    public void testNonNaturalFitness()
    {
        TerminationCondition condition = new TargetFitness(1.0d, false);
        PopulationStats<Object> data = new PopulationStats<Object>(0, new Object(), 5.0d, 4.0d, 0, true, 2, 0, 0, 100);
        assert !condition.shouldTerminate(data) : "Should not terminate before target fitness is reached.";
        data = new PopulationStats<Object>(0, new Object(), 1.0d, 3.1d, 0, true, 2, 0, 0, 100);
        assert condition.shouldTerminate(data) : "Should terminate once target fitness is reached.";
    }
}
