package com.myrontuttle.sci.evolve.api;

public interface CandidateFactoryService {

	/**
	 * @return The names of the candidate factories that are available from this service
	 */
	public String[] availableCandidateFactories();
	
	/**
	 * @param factoryName provided in availableCandidateFactories()
	 * @return A specific candidate factory
	 */
	public CandidateFactory<?> getCandidateFactory(String factoryName);
}
