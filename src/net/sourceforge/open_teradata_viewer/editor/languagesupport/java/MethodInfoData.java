/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.IMemberCompletion.IData;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ISourceLocation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.MethodInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.FormalParameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.IMember;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;

/**
 * Metadata about a method as read from a class file. This class is used by
 * instances of {@link MethodCompletion}.
 *
 * @author D. Campione
 * 
 */
class MethodInfoData implements IData {

    /** The parent completion provider. */
    private SourceCompletionProvider provider;

    /** The actual metadata. */
    private MethodInfo info;

    /** Cached method parameter names. */
    private List<String> paramNames;

    /** Ctor. */
    public MethodInfoData(MethodInfo info, SourceCompletionProvider provider) {
        this.info = info;
        this.provider = provider;
    }

    /** {@inheritDoc} */
    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        return info.getClassFile().getClassName(fullyQualified);
    }

    /** {@inheritDoc} */
    @Override
    public String getIcon() {
        String key = null;
        int flags = info.getAccessFlags();

        if (Util.isDefault(flags)) {
            key = IconFactory.METHOD_DEFAULT_ICON;
        } else if (Util.isPrivate(flags)) {
            key = IconFactory.METHOD_PRIVATE_ICON;
        } else if (Util.isProtected(flags)) {
            key = IconFactory.METHOD_PROTECTED_ICON;
        } else if (Util.isPublic(flags)) {
            key = IconFactory.METHOD_PUBLIC_ICON;
        } else {
            key = IconFactory.METHOD_DEFAULT_ICON;
        }

        return key;
    }

    /**
     * Scours the source in a location (zip file, directory), looking for a
     * particular class's source. If it is found, it is parsed and the {@link
     * Method} for this method (if any) is returned.
     *
     * @param loc The zip file, jar file or directory to look in.
     * @param cf The {@link ClassFile} representing the class of this method.
     * @return The method or <code>null</code> if it cannot be found or an
     *         IO error occurred.
     */
    private Method getMethodFromSourceLoc(ISourceLocation loc, ClassFile cf) {
        Method res = null;
        CompilationUnit cu = net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util
                .getCompilationUnitFromDisk(loc, cf);

        // If the class' source was found and successfully parsed, look for
        // this method
        if (cu != null) {
            Iterator<ITypeDeclaration> i = cu.getTypeDeclarationIterator();
            while (i.hasNext()) {
                ITypeDeclaration td = i.next();
                String typeName = td.getName();

                // Avoid inner classes, etc..
                if (typeName.equals(cf.getClassName(false))) {
                    // Get all overloads of this method with the number of
                    // parameters we're looking for. 99% of the time, there will
                    // only be 1, the method we're looking for
                    List<Method> contenders = null;
                    for (int j = 0; j < td.getMemberCount(); j++) {
                        IMember member = td.getMember(j);
                        if (member instanceof Method
                                && member.getName().equals(info.getName())) {
                            Method m2 = (Method) member;
                            if (m2.getParameterCount() == info
                                    .getParameterCount()) {
                                if (contenders == null) {
                                    contenders = new ArrayList<Method>(1); // Usually just 1
                                }
                                contenders.add(m2);
                            }
                        }
                    }

                    // We found some matches
                    if (contenders != null) {
                        // Common case - only 1 overload with the desired
                        // number of parameters => it must be our method
                        if (contenders.size() == 1) {
                            res = contenders.get(0);
                        }
                        // More than 1 overload with the same number of
                        // parameters.. we decide which contender is the one
                        // we're looking for by checking each of its parameters'
                        // types and making sure they're correct
                        else {
                            for (Method method : contenders) {
                                boolean match = true;
                                for (int p = 0; p < info.getParameterCount(); p++) {
                                    String type1 = info.getParameterType(p,
                                            false);
                                    FormalParameter fp = method.getParameter(p);
                                    String type2 = fp.getType().toString();
                                    if (!type1.equals(type2)) {
                                        match = false;
                                        break;
                                    }
                                }
                                if (match) {
                                    res = method;
                                    break;
                                }
                            }
                        }
                    }

                    break;
                } // if (typeName.equals(cf.getClassName(false)))
            } // for (Iterator i=cu.getTypeDeclarationIterator(); i.hasNext(); )
        } // if (cu!=null)

        return res;
    }

    /**
     * Returns the name of the specified parameter to this method or
     * <code>null</code> if it cannot be determined.
     *
     * @param index The index of the parameter.
     * @return The name of the parameter or <code>null</code>.
     */
    public String getParameterName(int index) {
        // First, check whether the debugging attribute was enabled at
        // compilation and the parameter name is embedded in the class file.
        // This method takes priority because it *likely* matches a name
        // specified in Javadoc and is much faster for us to fetch (it's
        // already parsed)
        String name = info.getParameterName(index);

        // Otherwise..
        if (name == null) {
            // Next, check the attached source, if any (lazily parsed)
            if (paramNames == null) {
                paramNames = new ArrayList<String>(1);
                int offs = 0;
                String rawSummary = getSummary();

                // If there's attached source with Javadoc for this method..
                if (rawSummary != null && rawSummary.startsWith("/**")) {
                    int nextParam = 0;
                    int summaryLen = rawSummary.length();

                    while ((nextParam = rawSummary.indexOf("@param", offs)) > -1) {
                        int temp = nextParam + "@param".length() + 1;
                        while (temp < summaryLen
                                && Character.isWhitespace(rawSummary
                                        .charAt(temp))) {
                            temp++;
                        }
                        if (temp < summaryLen) {
                            int start = temp;
                            int end = start + 1;
                            while (end < summaryLen
                                    && Character
                                            .isJavaIdentifierPart(rawSummary
                                                    .charAt(end))) {
                                end++;
                            }
                            paramNames.add(rawSummary.substring(start, end));
                            offs = end;
                        } else {
                            break;
                        }
                    }
                }
            }

            if (index < paramNames.size()) {
                name = paramNames.get(index);
            }
        }

        // Use a default name
        if (name == null) {
            name = "arg" + index;
        }

        return name;
    }

    @Override
    public String getSignature() {
        // Don't call MethodInfo's implementation, as it is unaware of param
        // names
        //return info.getNameAndParameters();

        StringBuilder sb = new StringBuilder(info.getName());

        sb.append('(');
        int paramCount = info.getParameterCount();
        for (int i = 0; i < paramCount; i++) {
            sb.append(info.getParameterType(i, false));
            sb.append(' ');
            sb.append(getParameterName(i));
            if (i < paramCount - 1) {
                sb.append(", ");
            }
        }
        sb.append(')');

        return sb.toString();
    }

    @Override
    public String getSummary() {
        ClassFile cf = info.getClassFile();
        ISourceLocation loc = provider.getSourceLocForClass(cf
                .getClassName(true));
        String summary = null;

        // First, try to parse the Javadoc for this method from the attached
        // source
        if (loc != null) {
            summary = getSummaryFromSourceLoc(loc, cf);
        }

        // Default to the method signature
        if (summary == null) {
            summary = info.getSignature();
        }
        return summary;
    }

    /**
     * Scours the source in a location (zip file, directory), looking for a
     * particular class's source. If it is found, it is parsed, and the Javadoc
     * for this method (if any) is returned.
     *
     * @param loc The zip file, jar file, or directory to look in.
     * @param cf The {@link ClassFile} representing the class of this method.
     * @return The summary or <code>null</code> if the method has no javadoc,
     *         the class's source was not found or an IO error occurred.
     */
    private String getSummaryFromSourceLoc(ISourceLocation loc, ClassFile cf) {
        Method method = getMethodFromSourceLoc(loc, cf);
        return method != null ? method.getDocComment() : null;
    }

    /** {@inheritDoc} */
    @Override
    public String getType() {
        return info.getReturnTypeString(false);
    }

    @Override
    public boolean isAbstract() {
        return info.isAbstract();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isConstructor() {
        return info.isConstructor();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDeprecated() {
        return info.isDeprecated();
    }

    @Override
    public boolean isFinal() {
        return info.isFinal();
    }

    @Override
    public boolean isStatic() {
        return info.isStatic();
    }
}