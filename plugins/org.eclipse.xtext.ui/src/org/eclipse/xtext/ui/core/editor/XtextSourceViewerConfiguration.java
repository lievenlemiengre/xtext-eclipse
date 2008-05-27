/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.xtext.ui.core.editor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

/**
 * @author Dennis H�bner - Initial contribution and API
 * 
 */
public class XtextSourceViewerConfiguration extends TextSourceViewerConfiguration {
	private final XtextModelManager modelManager;
	private final IEditorPart editor;

	/**
	 * @param file
	 * @param languageDescriptor
	 * @param langDescr
	 */
	public XtextSourceViewerConfiguration(XtextModelManager manager, IPreferenceStore preferenceStore,
			IEditorPart editor) {
		super(preferenceStore);
		this.editor = editor;
		this.modelManager = manager;
	}

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		return new MonoReconciler(new XtextReconcilingStrategy(modelManager, editor), false);
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = (PresentationReconciler) super.getPresentationReconciler(sourceViewer);
		DefaultDamagerRepairer defDR = new DefaultDamagerRepairer(new XtextTokenScanner(modelManager, fPreferenceStore));
		reconciler.setRepairer(defDR, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setDamager(defDR, IDocument.DEFAULT_CONTENT_TYPE);
		return reconciler;
	}

}
