package hu.palferi.mergetool.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DocumentSizeFilter extends DocumentFilter {

	private int size;

	public DocumentSizeFilter(int size) {
		super();
		this.size = size;
	}

	@Override
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
			AttributeSet attrs) throws BadLocationException {

		String string = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
		if (string.length() <= this.size) {
			super.replace(fb, offset, length, text.toUpperCase(), attrs);
		}
	}
}
