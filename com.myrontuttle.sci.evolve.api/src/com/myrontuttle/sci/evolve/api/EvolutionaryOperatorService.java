package com.myrontuttle.sci.evolve.api;

public interface EvolutionaryOperatorService {

	/**
	 * @return The names of the evolutionary operators that are available from this service
	 */
	public String[] availableEvolutionaryOperators();
	
	/**
	 * @param operatorName provided in availableEvolutionaryOperators()
	 * @return A specific evolutionary operator
	 */
	public EvolutionaryOperator<?> getEvolutionaryOperator(String operatorName);
}
