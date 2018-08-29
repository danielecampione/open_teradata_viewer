import javax.swing.*
import java.awt.Desktop
import java.net.URL
import java.io.File

def fileName = app.getSelectedText()

if (fileName==null || fileName.length()==0) {
    JOptionPane.showMessageDialog(app,
            "Couldn't open the file: No selection.\n" +
            "A path (or a URL) must be selected in the active editor to open a file (or a Web site).",
            "Error", JOptionPane.ERROR_MESSAGE);
} else {
    def isUrl = fileName.startsWith("http://");

    def file = new File(fileName);

    // If this is a URL, open it in a browser
    if (isUrl) {
        Desktop.getDesktop().browse(new URL(fileName).toURI());
    }
    else if (file.isFile()) {
            app.openFile(file.absolutePath)
    }
    else {
        JOptionPane.showMessageDialog(app,
                "File does not exist:\n" + file.absolutePath, "Error",
                JOptionPane.ERROR_MESSAGE)
    }
}