package com.myrontuttle.sci.evolve.api;

public interface TerminationConditionService {

	/**
	 * @return The names of the termination conditions that are available from this service
	 */
	public String[] availableTerminationConditions();
	
	/**
	 * @param conditionName provided in availableTerminationConditions()
	 * @return A specific termination condition
	 */
	public TerminationCondition getTerminationCondition(String conditionName);
}
