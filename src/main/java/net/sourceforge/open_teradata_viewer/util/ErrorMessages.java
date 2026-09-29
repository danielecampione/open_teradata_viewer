/*
 * Open Teradata Viewer ( util )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer.util;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class ErrorMessages {

    private String prefix;

    public ErrorMessages() {
        this(ErrorMessages.class, "UTL");
    }

    public ErrorMessages(String prefix) {
        this(ErrorMessages.class, prefix);
    }

    public ErrorMessages(Class<?> clazz, String prefix) {
        super();
        this.prefix = prefix;
    }

    public ErrorMessages(String resName, String prefix) {
        super();
        this.prefix = prefix;
    }

    public String getKey(int code) {
        return prefix + "-" + String.format("%05d", new Object[]{code});
    }

    private String postMessages(String msg) {
        return StringUtil.evl(msg, "Can't find message for code.");
    }

    public String getString(String code) {
        return code + ": " + postMessages(code);
    }

    public String getString(String code, Object[] argCode) {
        if (argCode == null) {
            return code;
        }
        String msg = "";
        for (int i = 0; i < argCode.length; i++) {
            msg += argCode[i] + "\n";
        }
        return code + ": " + postMessages(code) + "\n" + postMessages(msg);
    }

    public String getPrefix() {
        return prefix;
    }
}