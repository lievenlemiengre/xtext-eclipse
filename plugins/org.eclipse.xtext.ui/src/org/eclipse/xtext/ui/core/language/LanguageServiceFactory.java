/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.xtext.ui.core.language;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.ui.core.internal.CoreLog;
import org.eclipse.xtext.ui.core.service.ISyntaxColorer;


/**
 * @author Peter Friese - Initial contribution and API
 * 
 */
public class LanguageServiceFactory {

	private static final String LANGUAGE_ID = "languageId";
	private static final String CLASS = "class";

	private static final String SYNTAXCOLORER = "syntaxColorer";

	private static LanguageServiceFactory instance;

	private LanguageServiceFactory() {
	}

	public static final LanguageServiceFactory getInstance() {
		if (instance == null) {
			instance = new LanguageServiceFactory();
		}
		return instance;
	}

	public ISyntaxColorer getSyntaxColorer(LanguageDescriptor language) {
		return (ISyntaxColorer) loadService(language, SYNTAXCOLORER);
	}
	
	private ILanguageService loadService(
			LanguageDescriptor languageDescriptor,
			String serviceName) {
		if (languageDescriptor == null) {
			throw new IllegalArgumentException("languageDescriptor must not be null");
		}
		return createServiceExtension(languageDescriptor, serviceName);
	}
	
	private ILanguageService createServiceExtension(
			LanguageDescriptor languageDescriptor,
			String serviceName) {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint("org.eclipse.xtext.ui.core", serviceName);
		if (extensionPoint == null) {
			return null;
		}
		IConfigurationElement[] configurationElements = extensionPoint
				.getConfigurationElements();
		for (IConfigurationElement configurationElement : configurationElements) {
			try {
				String languageId = configurationElement
						.getAttribute(LANGUAGE_ID);
				if (languageDescriptor.getId().equals(languageId)) {
					return (org.eclipse.xtext.ui.core.language.ILanguageService) configurationElement
							.createExecutableExtension(CLASS);
				}
			} catch (CoreException e) {
				CoreLog.log(e.getStatus());
			}
		}
		return null;
	}


	

}
