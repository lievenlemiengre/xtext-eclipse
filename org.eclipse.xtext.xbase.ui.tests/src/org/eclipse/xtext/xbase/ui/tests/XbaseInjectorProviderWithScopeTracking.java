/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.ui.tests;

import org.eclipse.xtext.xbase.XbaseStandaloneSetup;
import org.eclipse.xtext.xbase.typesystem.internal.OptimizingFeatureScopeTrackerProvider;
import org.eclipse.xtext.xbase.typesystem.internal.IFeatureScopeTracker;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * An injector provider for plain Xbase tests that track feature scopes.
 * 
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class XbaseInjectorProviderWithScopeTracking extends XbaseInjectorProvider {
	@Override
	protected Injector internalCreateInjector() {
		return new CustomStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	public static class CustomStandaloneSetup extends XbaseStandaloneSetup {
		@Override
		public Injector createInjector() {
			return Guice.createInjector(new XbaseTestRuntimeModule() {
				@Override
				public void configure(com.google.inject.Binder binder) {
					super.configure(binder);
					binder.bind(IFeatureScopeTracker.Provider.class).to(OptimizingFeatureScopeTrackerProvider.class);
				}
				
			});
		}
	}

}