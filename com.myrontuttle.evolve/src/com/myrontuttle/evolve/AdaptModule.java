package com.myrontuttle.evolve;


import com.myrontuttle.evolve.util.log.Slf4jTypeListener;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class AdaptModule extends AbstractModule {

	@Override
	protected void configure() {

		//@InjectLogger Logger logger;
		bindListener(Matchers.any(), new Slf4jTypeListener());

	}

}
