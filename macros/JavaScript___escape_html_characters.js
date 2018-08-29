function replaceMultipleSpaces(text) {
	var p = java.util.regex.Pattern.compile("  +");
	var m = p.matcher(text);
	var sb = new java.lang.StringBuffer();
	while (m.find()) {
		var spaces = m.group();
		m.appendReplacement(sb, spaces.replace(" ", "&nbsp;"));
	}
	m.appendTail(sb);
	return sb.toString();
}

textArea.beginAtomicEdit();
try {
	var text = textArea.selectedText;
	if (text==null || text.length()==0) {
		javax.swing.JOptionPane.showMessageDialog(app,
				"Error:  No selection.\n" +
				"Text must be selected to HTML-ify.",
				"Error", javax.swing.JOptionPane.ERROR_MESSAGE);
	}
	else {
		text = text.replace("&", "&amp;").replace("\"", "&quot;").
				replace("<", "&lt;").replace(">", "&gt;").
				replace("\t", "&#009;").replace("\n", "<br />\n");
		if (text.contains("  ")) { // Replace multiple spaces with &nbsp; sequences
			text = replaceMultipleSpaces(text);
		}
		var start = textArea.getSelectionStart();
		textArea.replaceSelection(text);
		textArea.setSelectionStart(start);
		textArea.setSelectionEnd(start+text.length());
	}
} finally {
	textArea.endAtomicEdit();
}