package com.myrontuttle.sci.evolve.api;

public interface EvolutionEngineService {

	/**
	 * @return The names of the evolution engines that are available from this service
	 */
	public String[] availableEvolutionEngines();
	
	/**
	 * @param engineName provided in availableEvolutionEngines()
	 * @return A specific evolution engine
	 */
	public EvolutionEngine<?> getEvolutionEngine(String engineName);
}
