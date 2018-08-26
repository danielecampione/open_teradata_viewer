/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser.statement.create.table;

import java.util.List;

import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.PlainSelect;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ColDataType {

    private String dataType;
    private List<String> argumentsStringList;
    private String characterSet;

    public List<String> getArgumentsStringList() {
        return argumentsStringList;
    }

    public String getDataType() {
        return dataType;
    }

    public void setArgumentsStringList(List<String> list) {
        argumentsStringList = list;
    }

    public void setDataType(String string) {
        dataType = string;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    @Override
    public String toString() {
        return dataType
                + (argumentsStringList != null ? " "
                        + PlainSelect.getStringList(argumentsStringList, true,
                                true) : "")
                + (characterSet != null ? " CHARACTER SET " + characterSet : "");
    }
}