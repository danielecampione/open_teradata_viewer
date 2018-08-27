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
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.EnumDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.NormalClassDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.NormalInterfaceDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;

/**
 * Tree node for type declarations.
 *
 * @author D. Campione
 * 
 */
class TypeDeclarationTreeNode extends JavaTreeNode {

    private static final long serialVersionUID = -3128847312176425251L;

    public TypeDeclarationTreeNode(ITypeDeclaration typeDec) {
        super(typeDec);
        String iconName = null;
        int priority = PRIORITY_TYPE;

        if (typeDec instanceof NormalClassDeclaration) {
            NormalClassDeclaration ncd = (NormalClassDeclaration) typeDec;
            if (ncd.getModifiers() != null) {
                if (ncd.getModifiers().isPublic()) {
                    iconName = IconFactory.CLASS_ICON;
                } else if (ncd.getModifiers().isProtected()) {
                    iconName = IconFactory.INNER_CLASS_PROTECTED_ICON;
                } else if (ncd.getModifiers().isPrivate()) {
                    iconName = IconFactory.INNER_CLASS_PRIVATE_ICON;
                } else {
                    iconName = IconFactory.INNER_CLASS_DEFAULT_ICON;
                }
            } else {
                iconName = IconFactory.DEFAULT_CLASS_ICON;
            }
        } else if (typeDec instanceof NormalInterfaceDeclaration) {
            NormalInterfaceDeclaration nid = (NormalInterfaceDeclaration) typeDec;
            if (nid.getModifiers() != null && nid.getModifiers().isPublic()) {
                iconName = IconFactory.INTERFACE_ICON;
            } else {
                iconName = IconFactory.DEFAULT_INTERFACE_ICON;
            }
        } else if (typeDec instanceof EnumDeclaration) {
            EnumDeclaration ed = (EnumDeclaration) typeDec;
            if (ed.getModifiers() != null) {
                if (ed.getModifiers().isPublic()) {
                    iconName = IconFactory.ENUM_ICON;
                } else if (ed.getModifiers().isProtected()) {
                    iconName = IconFactory.ENUM_PROTECTED_ICON;
                    ;
                } else if (ed.getModifiers().isPrivate()) {
                    iconName = IconFactory.ENUM_PRIVATE_ICON;
                } else {
                    iconName = IconFactory.ENUM_DEFAULT_ICON;
                }
            } else {
                iconName = IconFactory.ENUM_DEFAULT_ICON;
            }
        }

        IconFactory fact = IconFactory.get();
        Icon mainIcon = fact.getIcon(iconName);

        if (mainIcon == null) {
            System.out.println("*** " + typeDec);
        } else {
            DecoratableIcon di = new DecoratableIcon(mainIcon);
            di.setDeprecated(typeDec.isDeprecated());
            Modifiers mods = typeDec.getModifiers();
            if (mods != null) {
                if (mods.isAbstract()) {
                    di.addDecorationIcon(fact
                            .getIcon(IconFactory.ABSTRACT_ICON));
                } else if (mods.isFinal()) {
                    di.addDecorationIcon(fact.getIcon(IconFactory.FINAL_ICON));
                }
                if (mods.isStatic()) {
                    di.addDecorationIcon(fact.getIcon(IconFactory.STATIC_ICON));
                    priority = PRIORITY_BOOST_STATIC;
                }
            }
            setIcon(di);
        }

        setSortPriority(priority);
    }

    @Override
    public String getText(boolean selected) {
        ITypeDeclaration typeDec = (ITypeDeclaration) getUserObject();
        return typeDec != null ? typeDec.getName() : null;
    }
}