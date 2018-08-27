/*
 * Open Teradata Viewer ( editor language support js ast type )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TypeDeclaration {

    private String pkg;
    private String apiName;
    private String jsName;
    private boolean staticsOnly;
    private boolean supportsBeanProperties;

    public TypeDeclaration(String pkg, String apiName, String jsName,
            boolean staticsOnly, boolean supportsBeanProperties) {
        this.staticsOnly = staticsOnly;
        this.pkg = pkg;
        this.apiName = apiName;
        this.jsName = jsName;
        this.supportsBeanProperties = supportsBeanProperties;
    }

    public TypeDeclaration(String pkg, String apiName, String jsName,
            boolean staticsOnly) {
        this(pkg, apiName, jsName, staticsOnly, true);
    }

    public TypeDeclaration(String pkg, String apiName, String jsName) {
        this(pkg, apiName, jsName, false, true);
    }

    public String getPackageName() {
        return pkg;
    }

    public String getAPITypeName() {
        return apiName;
    }

    public String getJSName() {
        return jsName;
    }

    public String getQualifiedName() {
        return pkg != null && pkg.length() > 0 ? (pkg + '.' + apiName)
                : apiName;
    }

    public boolean isQualified() {
        return getQualifiedName().indexOf('.') != -1;
    }

    public boolean isStaticsOnly() {
        return staticsOnly;
    }

    public void setStaticsOnly(boolean staticsOnly) {
        this.staticsOnly = staticsOnly;
    }

    public void setSupportsBeanProperties(boolean supportsBeanProperties) {
        this.supportsBeanProperties = supportsBeanProperties;
    }

    public boolean supportsBeanProperties() {
        return supportsBeanProperties;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        if (obj instanceof TypeDeclaration) {
            TypeDeclaration dec = (TypeDeclaration) obj;
            return getQualifiedName().equals(dec.getQualifiedName())
                    && isStaticsOnly() == dec.isStaticsOnly();
        }

        return super.equals(obj);
    }

    /**
     * Overridden since {@link #equals(Object)} is overridden.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * new Boolean(staticsOnly).hashCode();
        hash = 31 * hash + getQualifiedName().hashCode();
        return hash;
    }
}