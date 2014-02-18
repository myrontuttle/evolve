package com.myrontuttle.sci.evolve.termination;

import com.myrontuttle.sci.evolve.api.TerminationCondition;
import com.myrontuttle.sci.evolve.api.TerminationConditionService;

public class ConditionService implements TerminationConditionService {

	// 1. Define strategies
	private ElapsedTime elapsedTime;
	private GenerationCount generationCount;
	private Stagnation stagnation;
	private TargetFitness targetFitness;
	private UserAbort userAbort;

	// 2. Add the name of the strategies
	public static String[] availableConditions = new String[]{
		ElapsedTime.class.getName(),
		GenerationCount.class.getName(),
		Stagnation.class.getName(),
		TargetFitness.class.getName(),
		UserAbort.class.getName()
	};

	// 3. Add required arguments/services to the constructor and construct the strategies
	// 4. Make sure to add any service references/arguments to the blueprint config
	
	
	
	@Override
	public String[] availableTerminationConditions() {
		return availableConditions;
	}

	@Override
	public TerminationCondition getTerminationCondition(String conditionName) {
		if (conditionName.equals(ElapsedTime.class.getName())) {
			return elapsedTime;
		}
		if (conditionName.equals(GenerationCount.class.getName())) {
			return generationCount;
		}
		if (conditionName.equals(Stagnation.class.getName())) {
			return stagnation;
		}
		if (conditionName.equals(TargetFitness.class.getName())) {
			return targetFitness;
		}
		if (conditionName.equals(UserAbort.class.getName())) {
			return userAbort;
		}
		return null;
	}

}
