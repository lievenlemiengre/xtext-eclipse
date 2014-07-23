/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.xtext.xbase.ui.templates;

import org.eclipse.xtext.ui.editor.templates.XtextTemplateContextType;

import com.google.inject.Inject;

/**
 * Provides a convenience base type for <code>XbaseTemplateContextType's</code> preconfigured with several handy Xbase
 * <code>TemplateVariableResolver</code> .
 *
 * @author Dennis H�bner (dhubner) - Initial contribution and API
 * 
 * @since 2.7
 */
public class XbaseTemplateContextType extends XtextTemplateContextType {

	/**
	 * @since 2.7
	 */
	@Inject
	public void setImportsResolver(ImportsVariableResolver resolver) {
		addResolver(resolver);
	}
}
