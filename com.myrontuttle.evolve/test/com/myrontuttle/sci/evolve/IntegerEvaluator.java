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
package com.myrontuttle.sci.evolve;

import java.util.List;

import com.myrontuttle.sci.evolve.FitnessEvaluator;

/**
 * Trivial fitness evaluator for integers.  Used by unit tests.
 * @author Daniel Dyer
 */
final class IntegerEvaluator implements FitnessEvaluator<Integer>
{

    public double getFitness(Integer candidate,
                             List<? extends Integer> population)
    {
        return candidate;
    }

    public boolean isNatural()
    {
        return true;
    }
}