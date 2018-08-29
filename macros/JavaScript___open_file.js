var fileName = app.getSelectedText();
if (fileName==null || fileName.length()==0) {
    javax.swing.JOptionPane.showMessageDialog(app,
            "Couldn't open the file: No selection.\n" +
            "A path (or a URL) must be selected in the active editor to open a file (or a Web site).",
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
}
else {
    var isUrl = fileName.startsWith("http://");

    var file = new java.io.File(fileName);

    // If this is a URL, open it in a browser
    if (isUrl) {
        java.awt.Desktop.getDesktop().browse(new java.net.URL(fileName).toURI());
    }
    else if (file.isFile()) {
        app.openFile(file.absolutePath);
    }
    else {
        javax.swing.JOptionPane.showMessageDialog(app,
                "File does not exist:\n" + file.absolutePath, "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}