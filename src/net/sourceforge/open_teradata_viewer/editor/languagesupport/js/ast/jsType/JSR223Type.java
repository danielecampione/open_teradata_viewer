/*
 * Open Teradata Viewer ( editor language support js ast jsType )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType;

import java.util.HashSet;
import java.util.Map;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.Logger;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.IJSCompletion;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JSR223Type extends JavaScriptType {

    public JSR223Type(TypeDeclaration type) {
        super(type);
    }

    /**
     * @return IJSCompletion using lookup name.
     * @see IJSCompletion
     */
    @Override
    protected IJSCompletion _getCompletion(String completionLookup,
            SourceCompletionProvider provider) {
        IJSCompletion completion = methodFieldCompletions.get(completionLookup);
        if (completion != null) {
            return completion;
        }
        // Else
        if (completionLookup.indexOf('(') != -1) {
            boolean isJavaScriptType = provider.getTypesFactory()
                    .isJavaScriptType(getType());
            // Must be a function, so compare function strings get a list of
            // best fit methods
            Logger.log("Completion Lookup : " + completionLookup);
            JavaScriptFunctionType javaScriptFunctionType = JavaScriptFunctionType
                    .parseFunction(completionLookup, provider);

            IJSCompletion[] matches = getPotentialLookupList(javaScriptFunctionType
                    .getName());

            // Iterate through types and check best fit parameters
            int bestFitIndex = -1;
            int bestFitWeight = -1;
            Logger.log("Potential matches : " + matches.length);
            for (int i = 0; i < matches.length; i++) {
                Logger.log("Potential match : " + matches[i].getLookupName());
                JavaScriptFunctionType matchFunctionType = JavaScriptFunctionType
                        .parseFunction(matches[i].getLookupName(), provider);
                Logger.log("Matching against completion: " + completionLookup);
                int weight = matchFunctionType.compare(javaScriptFunctionType,
                        provider, isJavaScriptType);
                Logger.log("Weight: " + weight);
                if (weight < JavaScriptFunctionType.CONVERSION_NONE
                        && (weight < bestFitWeight || bestFitIndex == -1)) {
                    bestFitIndex = i;
                    bestFitWeight = weight;
                }
            }
            if (bestFitIndex > -1) {
                Logger.log("BEST FIT: " + matches[bestFitIndex].getLookupName());
                return matches[bestFitIndex];
            }
        }

        return null;
    }

    private IJSCompletion[] getPotentialLookupList(String name) {
        // Get a list of all potential matches, including extended
        HashSet<IJSCompletion> completionMatches = new HashSet<IJSCompletion>();
        getPotentialLookupList(name, completionMatches, this);
        return completionMatches.toArray(new IJSCompletion[completionMatches
                .size()]);
    }

    // Get a list of all potential method matches.
    private void getPotentialLookupList(String name,
            HashSet<IJSCompletion> completionMatches, JavaScriptType type) {
        Map<String, IJSCompletion> typeCompletions = type.methodFieldCompletions;

        for (String key : typeCompletions.keySet()) {
            if (key.startsWith(name)) {
                IJSCompletion completion = typeCompletions.get(key);
                if (completion instanceof FunctionCompletion) {
                    completionMatches.add(completion);
                }
            }
        }

        // Loop through extended and add it's methods too recursively
        for (JavaScriptType extendedType : type.getExtendedClasses()) {
            getPotentialLookupList(name, completionMatches, extendedType);
        }
    }
}