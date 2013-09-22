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
import org.testng.annotations.Test;
import com.myrontuttle.sci.evolve.FitnessEvaluator;
import com.myrontuttle.sci.evolve.FrameworkTestUtils;
import com.myrontuttle.sci.evolve.PopulationStats;
import com.myrontuttle.sci.evolve.factories.StubIntegerFactory;
import com.myrontuttle.sci.evolve.islands.IslandEvolution;
import com.myrontuttle.sci.evolve.islands.IslandEvolutionObserver;
import com.myrontuttle.sci.evolve.islands.RingMigration;
import com.myrontuttle.sci.evolve.operators.IntegerAdjuster;
import com.myrontuttle.sci.evolve.selection.RouletteWheelSelection;
import com.myrontuttle.sci.evolve.termination.ElapsedTime;
import com.myrontuttle.sci.evolve.termination.GenerationCount;

/**
 * Unit test for the {@link IslandEvolution} class.
 * @author Daniel Dyer
 */
public class IslandEvolutionTest
{
    /**
     * This test makes sure that the evolution observer global method only gets invoked at
     * the end of each epoch, and that the island method gets invoked for each generation on each
     * island.
     */
    @Test
    public void testListeners()
    {
        final int islandCount = 3;
        final int epochCount = 2;
        final int generationCount = 5;

        IslandEvolution<Integer> islandEvolution = new IslandEvolution<Integer>(islandCount,
                                                                                new RingMigration(),
                                                                                new StubIntegerFactory(),
                                                                                new IntegerAdjuster(2),
                                                                                new DummyFitnessEvaluator(),
                                                                                new RouletteWheelSelection(),
                                                                                FrameworkTestUtils.getRNG());
        final int[] observedEpochCount = new int[1];
        final int[] observedGenerationCounts = new int[islandCount];

        islandEvolution.addEvolutionObserver(new IslandEvolutionObserver<Integer>()
        {
            public void populationUpdate(PopulationStats<? extends Integer> PopulationStats)
            {
                observedEpochCount[0]++;
            }


            public void islandPopulationUpdate(int islandIndex, PopulationStats<? extends Integer> PopulationStats)
            {
                observedGenerationCounts[islandIndex]++;
            }
        });
        islandEvolution.evolve(null, 5, 0, 5, 0, new GenerationCount(2));
        assert observedEpochCount[0] == 2 : "Listener should have been notified twice, was " + observedEpochCount[0];
        for (int i = 0; i < islandCount; i++)
        {
            int expected = epochCount * generationCount;
            assert observedGenerationCounts[i] == expected
                : "Genertion count for island " + i + " should be " + expected + ", is " + observedGenerationCounts[i];
        }
    }


    @Test
    public void testInterrupt()
    {
        IslandEvolution<Integer> islandEvolution = new IslandEvolution<Integer>(2,
                                                                                new RingMigration(),
                                                                                new StubIntegerFactory(),
                                                                                new IntegerAdjuster(2),
                                                                                new DummyFitnessEvaluator(),
                                                                                new RouletteWheelSelection(),
                                                                                FrameworkTestUtils.getRNG());
        final long timeout = 1000L;
        final Thread requestThread = Thread.currentThread();
        islandEvolution.addEvolutionObserver(new IslandEvolutionObserver<Integer>()
        {
            public void populationUpdate(PopulationStats<? extends Integer> PopulationStats)
            {
                if (PopulationStats.getElapsedTime() > timeout / 2)
                {
                    requestThread.interrupt();
                }
            }


            public void islandPopulationUpdate(int islandIndex, PopulationStats<? extends Integer> PopulationStats){}
        });
        long startTime = System.currentTimeMillis();
        islandEvolution.evolve(null, 10, 0, 10, 0, new ElapsedTime(timeout));
        long elapsedTime = System.currentTimeMillis() - startTime;
        assert Thread.interrupted() : "Thread was not interrupted before timeout.";
        assert elapsedTime < timeout : "Engine did not respond to interrupt before timeout.";
        assert islandEvolution.getSatisfiedTerminationConditions().isEmpty()
            : "Interrupted islands should have no satisfied termination conditions.";
    }


    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetSatisfiedTerminationConditionsBeforeStart()
    {
        IslandEvolution<Integer> islandEvolution = new IslandEvolution<Integer>(3,
                                                                                new RingMigration(),
                                                                                new StubIntegerFactory(),
                                                                                new IntegerAdjuster(2),
                                                                                new DummyFitnessEvaluator(),
                                                                                new RouletteWheelSelection(),
                                                                                FrameworkTestUtils.getRNG());
        // Should throw an IllegalStateException because evolution has started, let alone terminated.
        islandEvolution.getSatisfiedTerminationConditions();
    }


    private static class DummyFitnessEvaluator implements FitnessEvaluator<Integer>
    {
        public double getFitness(Integer candidate, List<? extends Integer> population)
        {
            return 0;
        }

        public boolean isNatural()
        {
            return true;
        }
    }
}
