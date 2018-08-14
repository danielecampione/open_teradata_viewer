/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2013, D. Campione
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.templates.ICodeTemplate;

/**
 * Manages "code templates".<p>
 *
 * All methods in this class are synchronized for thread safety, but as a best
 * practice, you should probably only modify the templates known to a
 * <code>CodeTemplateManager</code> on the EDT. Modifying a
 * <code>ICodeTemplate</code> retrieved from a <code>CodeTemplateManager</code>
 * while <em>not</em> on the EDT could cause problems.
 *
 * @author D. Campione
 * 
 */
public class CodeTemplateManager {

    private int maxTemplateIDLength;
    private List<ICodeTemplate> templates;

    private Segment s;
    private TemplateComparator comparator;
    private File directory;

    /** Ctor. */
    public CodeTemplateManager() {
        s = new Segment();
        comparator = new TemplateComparator();
        templates = new ArrayList<ICodeTemplate>();
    }

    /**
     * Registers the specified template with this template manager.
     *
     * @param template The template to register.
     * @throws IllegalArgumentException If <code>template</code> is
     *         <code>null</code>.
     * @see #removeTemplate(ICodeTemplate)
     * @see #removeTemplate(String)
     */
    public synchronized void addTemplate(ICodeTemplate template) {
        if (template == null) {
            throw new IllegalArgumentException("template cannot be null");
        }
        templates.add(template);
        sortTemplates();
    }

    /**
     * Returns the template that should be inserted at the current caret
     * position, assuming the trigger character was pressed.
     *
     * @param textArea The text area that's getting text inserted into it.
     * @return A template that should be inserted, if appropriate, or
     *         <code>null</code> if no template should be inserted.
     */
    public synchronized ICodeTemplate getTemplate(SyntaxTextArea textArea) {
        int caretPos = textArea.getCaretPosition();
        int charsToGet = Math.min(caretPos, maxTemplateIDLength);
        try {
            Document doc = textArea.getDocument();
            doc.getText(caretPos - charsToGet, charsToGet, s);
            int index = Collections.binarySearch(templates, s, comparator);
            return index >= 0 ? (ICodeTemplate) templates.get(index) : null;
        } catch (BadLocationException ble) {
            ExceptionDialog.notifyException(ble);
            throw new InternalError("Error in CodeTemplateManager");
        }
    }

    /**
     * Returns the number of templates this manager knows about.
     *
     * @return The template count.
     */
    public synchronized int getTemplateCount() {
        return templates.size();
    }

    /**
     * Returns the templates currently available.
     *
     * @return The templates available.
     */
    public synchronized ICodeTemplate[] getTemplates() {
        ICodeTemplate[] temp = new ICodeTemplate[templates.size()];
        return templates.toArray(temp);
    }

    /**
     * Returns whether the specified character is a valid character for a
     * <code>ICodeTemplate</code> id.
     *
     * @param ch The character to check.
     * @return Whether the character is a valid template character.
     */
    public static final boolean isValidChar(char ch) {
        return SyntaxUtilities.isLetterOrDigit(ch) || ch == '_';
    }

    /**
     * Returns the specified code template.
     *
     * @param template The template to remove.
     * @return <code>true</code> if the template was removed, <code>false</code>
     *         if the template was not in this template manager.
     * @throws IllegalArgumentException If <code>template</code> is
     *         <code>null</code>.
     * @see #removeTemplate(String)
     * @see #addTemplate(ICodeTemplate)
     */
    public synchronized boolean removeTemplate(ICodeTemplate template) {
        if (template == null) {
            throw new IllegalArgumentException("template cannot be null");
        }

        return templates.remove(template);
    }

    /**
     * Returns the code template with the specified id.
     *
     * @param id The id to check for.
     * @return The code template that was removed, or <code>null</code> if there
     *         was no template with the specified ID.
     * @throws IllegalArgumentException If <code>id</code> is <code>null</code>.
     * @see #removeTemplate(ICodeTemplate)
     * @see #addTemplate(ICodeTemplate)
     */
    public synchronized ICodeTemplate removeTemplate(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        for (Iterator<ICodeTemplate> i = templates.iterator(); i.hasNext();) {
            ICodeTemplate template = i.next();
            if (id.equals(template.getID())) {
                i.remove();
                return template;
            }
        }

        return null;
    }

    /**
     * Replaces the current set of available templates with the ones specified.
     *
     * @param newTemplates The new set of templates.  Note that we will
     *        be taking a shallow copy of these and sorting them.
     */
    public synchronized void replaceTemplates(ICodeTemplate[] newTemplates) {
        templates.clear();
        if (newTemplates != null) {
            for (int i = 0; i < newTemplates.length; i++) {
                templates.add(newTemplates[i]);
            }
        }
        sortTemplates(); // Also recomputes maxTemplateIDLength.
    }

    /**
     * Saves all templates as XML files in the current template directory.
     *
     * @return Whether or not the save was successful.
     */
    public synchronized boolean saveTemplates() {
        if (templates == null) {
            return true;
        }
        if (directory == null || !directory.isDirectory()) {
            return false;
        }

        // Blow away all old XML files to start anew, as some might be from
        // templates we're removed from the template manager
        File[] oldXMLFiles = directory.listFiles(new XMLFileFilter());
        if (oldXMLFiles == null) {
            return false; // Either an IOException or it isn't a directory
        }
        int count = oldXMLFiles.length;
        for (int i = 0; i < count; i++) {
            oldXMLFiles[i].delete();
        }

        // Save all current templates as XML
        boolean wasSuccessful = true;
        for (ICodeTemplate template : templates) {
            File xmlFile = new File(directory, template.getID() + ".xml");
            try {
                XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
                        new FileOutputStream(xmlFile)));
                e.writeObject(template);
                e.close();
            } catch (IOException ioe) {
                ExceptionDialog.notifyException(ioe);
                wasSuccessful = false;
            }
        }

        return wasSuccessful;
    }

    /**
     * Sets the directory in which to look for templates. Calling this method
     * adds any new templates found in the specified directory to the templates
     * already registered.
     *
     * @param dir The new directory in which to look for templates.
     * @return The new number of templates in this template manager, or
     *         <code>-1</code> if the specified directory does not exist.
     */
    public synchronized int setTemplateDirectory(File dir) {
        if (dir != null && dir.isDirectory()) {
            this.directory = dir;

            File[] files = dir.listFiles(new XMLFileFilter());
            int newCount = files == null ? 0 : files.length;
            int oldCount = templates.size();

            List<ICodeTemplate> temp = new ArrayList<ICodeTemplate>(oldCount
                    + newCount);
            temp.addAll(templates);

            for (int i = 0; i < newCount; i++) {
                try {
                    XMLDecoder d = new XMLDecoder(new BufferedInputStream(
                            new FileInputStream(files[i])));
                    Object obj = d.readObject();
                    if (!(obj instanceof ICodeTemplate)) {
                        d.close();
                        throw new IOException("Not a ICodeTemplate: "
                                + files[i].getAbsolutePath());
                    }
                    temp.add((ICodeTemplate) obj);
                    d.close();
                } catch (/*IO, NoSuchElement*/Exception e) {
                    // NoSuchElementException can be thrown when reading an XML
                    // file not in the format expected by XMLDecoder (e.g.
                    // CodeTemplates in an old format)
                    ExceptionDialog.notifyException(e);
                }
            }
            templates = temp;
            sortTemplates();

            return getTemplateCount();
        }

        return -1;
    }

    /**
     * Removes any null entries in the current set of templates (if any), sorts
     * the remaining templates, and computes the new maximum template ID length.
     */
    private synchronized void sortTemplates() {
        // Get the maximum length of a template ID.
        maxTemplateIDLength = 0;

        // Remove any null entries (should only happen because of IOExceptions,
        // etc. when loading from files), and sort the remaining list.
        for (Iterator<ICodeTemplate> i = templates.iterator(); i.hasNext();) {
            ICodeTemplate temp = i.next();
            if (temp == null || temp.getID() == null) {
                i.remove();
            } else {
                maxTemplateIDLength = Math.max(maxTemplateIDLength, temp
                        .getID().length());
            }
        }

        Collections.sort(templates);
    }

    /**
     * A comparator that takes a <code>ICodeTemplate</code> as its first
     * parameter and a <code>Segment</code> as its second, and knows to compare
     * the template's ID to the segment's text.
     * 
     * @author D. Campione
     * 
     */
    @SuppressWarnings("rawtypes")
    private static class TemplateComparator implements Comparator, Serializable {

        private static final long serialVersionUID = 8919403953778641908L;

        @Override
        public int compare(Object template, Object segment) {
            // Get template start index (0) and length.
            ICodeTemplate t = (ICodeTemplate) template;
            final char[] templateArray = t.getID().toCharArray();
            int i = 0;
            int len1 = templateArray.length;

            // Find "token" part of segment and get its offset and length.
            Segment s = (Segment) segment;
            char[] segArray = s.array;
            int len2 = s.count;
            int j = s.offset + len2 - 1;
            while (j >= s.offset && isValidChar(segArray[j])) {
                j--;
            }
            j++;
            int segShift = j - s.offset;
            len2 -= segShift;

            int n = Math.min(len1, len2);
            while (n-- != 0) {
                char c1 = templateArray[i++];
                char c2 = segArray[j++];
                if (c1 != c2) {
                    return c1 - c2;
                }
            }
            return len1 - len2;

        }

    }

    /**
     * A file filter that accepts only XML files.
     * 
     * @author D. Campione
     * 
     */
    private static class XMLFileFilter implements FileFilter {

        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".xml");
        }

    }
}