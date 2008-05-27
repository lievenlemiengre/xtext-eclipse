/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.xtext.ui.core.editor.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.services.IDisposable;

/**
 * Here different stylistic component are produced, kept and made available
 * 
 * @author Dennis H�bner
 */
public class CustomResourceLibrary implements ISharedTextColors, IDisposable {

	private Display display;

	public CustomResourceLibrary(Display display) {
		this.display = display;
	}

	private Map<String, Color> colors = new HashMap<String, Color>();

	private Map<Font, Font> fonts = new HashMap<Font, Font>();

	public void dispose() {
		for (Color col : colors.values()) {
			if ((col != null) && !col.isDisposed()) {
				col.dispose();
			}
		}
		for (Font font : fonts.values()) {
			if ((font != null) && !font.isDisposed()) {
				font.dispose();
			}
		}
	}

	public Font getFontAsItalic(Font font) {
		Font italicFont = fonts.get(font);
		if (italicFont == null) {
			FontData[] fontData = font.getFontData();
			for (FontData fontDataItem : fontData) {
				fontDataItem.setStyle(SWT.ITALIC);
			}
			italicFont = new Font(display, fontData);
			fonts.put(font, italicFont);
		}
		return italicFont;
	}

	public Color getColor(RGB rgb) {
		Color retVal = colors.get(rgb.toString());
		if (retVal == null) {
			retVal = new Color(display, rgb);
			colors.put(rgb.toString(), retVal);
		}
		return retVal;
	}

	public Font getFont(String fontName) {
		return null;
	}
}
