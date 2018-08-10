/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Dimension;

/**
 * 
 * @author D. Campione
 * 
 */
public interface XDialogs {
    void showErrorMessage(String message);

    void showInfoMessage(String message);

    void showInfoMessage(String message, String title);

    void showExtendedInfo(String title, String description, String content,
            Dimension size);

    boolean confirm(String question, String title);

    Boolean confirmOrCancel(String question, String title);

    int yesYesToAllOrNo(String question, String title);

    String prompt(String question, String title, String value);

    String prompt(String question, String title);

    Object prompt(String question, String title, Object[] objects);

    Object prompt(String question, String title, Object[] objects, String value);

    char[] promptPassword(String question, String title);

    boolean confirmExtendedInfo(String title, String description,
            String content, Dimension size);

    Boolean confirmOrCancleExtendedInfo(String title, String description,
            String content, Dimension size);

    String selectXPath(String title, String info, String xml, String xpath);
}
