/*
 * Open Teradata Viewer ( editor language support js engine )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.engine;

import java.util.HashMap;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JavaScriptEngineFactory {

    public static final String DEFAULT = EMCAJavaScriptEngine.EMCA_ENGINE;

    private HashMap<String, JavaScriptEngine> supportedEngines = new HashMap<String, JavaScriptEngine>();

    private static JavaScriptEngineFactory Instance = new JavaScriptEngineFactory();

    static {
        Instance().addEngine(EMCAJavaScriptEngine.EMCA_ENGINE,
                new EMCAJavaScriptEngine());
        Instance().addEngine(JSR223JavaScriptEngine.JSR223_ENGINE,
                new JSR223JavaScriptEngine());
        Instance().addEngine(RhinoJavaScriptEngine.RHINO_ENGINE,
                new RhinoJavaScriptEngine());
    }

    private JavaScriptEngineFactory() {
    }

    public static JavaScriptEngineFactory Instance() {
        return Instance;
    }

    public JavaScriptEngine getEngineFromCache(String name) {
        if (name == null) {
            name = DEFAULT;
        }
        return supportedEngines.get(name);
    }

    public void addEngine(String name, JavaScriptEngine engine) {
        supportedEngines.put(name, engine);
    }

    public void removeEngine(String name) {
        supportedEngines.remove(name);
    }
}