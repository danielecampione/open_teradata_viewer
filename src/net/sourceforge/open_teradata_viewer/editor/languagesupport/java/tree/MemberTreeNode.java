/*
 * Open Teradata Viewer ( editor language support java tree )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.tree;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.DecoratableIcon;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.FormalParameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * Tree node for a field or method.
 *
 * @author D. Campione
 * 
 */
class MemberTreeNode extends JavaTreeNode {

    private static final long serialVersionUID = -4370284166443607610L;

    private String text;

    public MemberTreeNode(CodeBlock cb) {
        super(cb);
        text = "<html>" + cb.getName();
        IconFactory fact = IconFactory.get();
        Icon base = fact.getIcon(IconFactory.METHOD_PRIVATE_ICON);
        DecoratableIcon di = new DecoratableIcon(base);
        int priority = PRIORITY_METHOD;
        if (cb.isStatic()) {
            di.addDecorationIcon(fact.getIcon(IconFactory.STATIC_ICON));
            priority += PRIORITY_BOOST_STATIC;
        }
        setIcon(di);
        setSortPriority(priority);
    }

    public MemberTreeNode(Field field) {
        super(field);

        Modifiers mods = field.getModifiers();
        String icon = null;

        if (mods == null) {
            icon = IconFactory.FIELD_DEFAULT_ICON;
        } else if (mods.isPrivate()) {
            icon = IconFactory.FIELD_PRIVATE_ICON;
        } else if (mods.isProtected()) {
            icon = IconFactory.FIELD_PROTECTED_ICON;
        } else if (mods.isPublic()) {
            icon = IconFactory.FIELD_PUBLIC_ICON;
        } else {
            icon = IconFactory.FIELD_DEFAULT_ICON;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(field.getName());
        sb.append(" : ");
        sb.append("<font color='#888888'>");

        appendType(field.getType(), sb);
        text = sb.toString();
        int priority = PRIORITY_FIELD;

        IconFactory fact = IconFactory.get();
        Icon base = fact.getIcon(icon);
        DecoratableIcon di = new DecoratableIcon(base);
        di.setDeprecated(field.isDeprecated());
        if (mods != null) {
            if (mods.isStatic()) {
                di.addDecorationIcon(fact.getIcon(IconFactory.STATIC_ICON));
                priority += PRIORITY_BOOST_STATIC;
            }
            if (mods.isFinal()) {
                di.addDecorationIcon(fact.getIcon(IconFactory.FINAL_ICON));
            }
        }
        setIcon(di);

        setSortPriority(priority);
    }

    public MemberTreeNode(Method method) {
        super(method);

        String icon = null;
        int priority = PRIORITY_METHOD;

        Modifiers mods = method.getModifiers();
        if (mods == null) {
            icon = IconFactory.METHOD_DEFAULT_ICON;
        } else if (mods.isPrivate()) {
            icon = IconFactory.METHOD_PRIVATE_ICON;
        } else if (mods.isProtected()) {
            icon = IconFactory.METHOD_PROTECTED_ICON;
        } else if (mods.isPublic()) {
            icon = IconFactory.METHOD_PUBLIC_ICON;
        } else {
            icon = IconFactory.METHOD_DEFAULT_ICON;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(method.getName());
        sb.append('(');
        int paramCount = method.getParameterCount();
        for (int i = 0; i < paramCount; i++) {
            FormalParameter param = method.getParameter(i);
            appendType(param.getType(), sb);
            if (i < paramCount - 1) {
                sb.append(", ");
            }
        }
        sb.append(')');
        if (method.getType() != null) {
            sb.append(" : ");
            sb.append("<font color='#888888'>");
            appendType(method.getType(), sb);
        }

        text = sb.toString();

        IconFactory fact = IconFactory.get();
        Icon base = fact.getIcon(icon);
        DecoratableIcon di = new DecoratableIcon(base);
        di.setDeprecated(method.isDeprecated());
        if (mods != null) {
            if (mods.isAbstract()) {
                di.addDecorationIcon(fact.getIcon(IconFactory.ABSTRACT_ICON));
            }
            if (method.isConstructor()) {
                di.addDecorationIcon(fact.getIcon(IconFactory.CONSTRUCTOR_ICON));
                priority = PRIORITY_CONSTRUCTOR; // Overrides previous value
            }
            if (mods.isStatic()) {
                di.addDecorationIcon(fact.getIcon(IconFactory.STATIC_ICON));
                priority += PRIORITY_BOOST_STATIC;
            }
            if (mods.isFinal()) {
                di.addDecorationIcon(fact.getIcon(IconFactory.FINAL_ICON));
            }
        }
        setIcon(di);

        setSortPriority(priority);
    }

    static void appendType(Type type, StringBuilder sb) {
        if (type != null) {
            String t = type.toString();
            t = t.replaceAll("<", "&lt;");
            t = t.replaceAll(">", "&gt;");
            sb.append(t);
        }
    }

    @Override
    public String getText(boolean selected) {
        // Strip out HTML tags
        return selected ? Utilities.stripHtml(text).replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">") : text;
    }
}