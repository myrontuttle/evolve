package com.myrontuttle.sci.evolve.api;

public interface SelectionStrategyService {

	/**
	 * @return The names of the selection strategies that are available from this service
	 */
	public String[] availableSelectionStrategies();
	
	/**
	 * @param strategyName provided in availableSelectionStrategies()
	 * @return A specific selection strategy
	 */
	public SelectionStrategy<?> getSelectionStrategy(String strategyName);
}
