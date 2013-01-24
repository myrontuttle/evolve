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
package com.myrontuttle.evolve.util.id;

import org.testng.annotations.Test;

/**
 * Unit test for 64-bit ID sequence.
 * @author Daniel Dyer
 */
public class LongSequenceIDSourceTest
{
    @Test
    public void testSequence()
    {
        LongSequenceIDSource idSource = new LongSequenceIDSource();
        long firstID = idSource.nextID();
        long secondID = idSource.nextID();
        long thirdID = idSource.nextID();
        assert firstID == 0 : "First ID should be 0.";
        assert secondID == firstID + 1 : "Second ID should be 1 more than first ID.";
        assert thirdID == secondID + 1 : "Third ID should be 1 more than second ID.";
    }


    @Test(expectedExceptions = IDSourceExhaustedException.class)
    public void testExhaustion()
    {
        IDSource<Long> idSource = new LongSequenceIDSource(Long.MAX_VALUE);
        // Should be able to get one ID from this.
        long id = idSource.nextID();
        assert id == Long.MAX_VALUE : "Incorrect initial value: " + id;
        // But the next invocation should result in an exception.
        idSource.nextID();
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidInitialValue()
    {
        new LongSequenceIDSource(-1);
    }
}
