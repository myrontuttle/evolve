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
import com.myrontuttle.sci.evolve.termination.GenerationCount;

/**
 * Unit test for termination condition that checks the number of evolved generations.
 * @author Daniel Dyer
 */
public class GenerationCountTest
{
    @Test
    public void testGenerationCounts()
    {
        TerminationCondition condition = new GenerationCount(5);
        PopulationStats<Object> data = new PopulationStats<Object>(null, new Object(), 0, 0, 0, true, 2, 0, 3, 100);
        // Generation number 3 is the 4th generation (generation numbers are zero-based).
        assert !condition.shouldTerminate(data) : "Should not terminate after 4th generation.";
        data = new PopulationStats<Object>(null, new Object(), 0, 0, 0, true, 2, 0, 4, 100);
        // Generation number 4 is the 5th generation (generation numbers are zero-based).
        assert condition.shouldTerminate(data) : "Should terminate after 5th generation.";
    }


    /**
     * The generation count must be greater than zero to be useful.  This test
     * ensures that an appropriate exception is thrown if the count is not positive.
     * Not throwing an exception is an error because it permits undetected bugs in
     * evolutionary programs.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZeroRatio()
    {
        new GenerationCount(0);
    }
}
