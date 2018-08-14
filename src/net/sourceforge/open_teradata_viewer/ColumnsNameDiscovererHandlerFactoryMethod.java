/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

/**
 * One of the partecipants belonging to the factory method that has been adopted
 * to implement the initialization of the columns name discoverer statement; the
 * class represents the Creator of the framework belonging to the specified
 * design pattern.<p/>
 * 
 * The class <code>ColumnsNameDiscovererHandlerFactoryMethod</code> provides the
 * common operations for manipulating the columns name discoverers.<p/>
 * 
 * Notice that the class is able to determine when a particular columns name
 * discoverer is to be created, but doesn't know the particular type of the
 * columns name discoverer to create. In other words, the framework needs to
 * create instances of classes, but relates solely with abstract classes, which
 * cannot be instantiated.<p/>
 * 
 * It declares the factory method (method <code>newElement</code>) that returns
 * an object of Product type.<br/>
 * It invokes the factory method to create the Product (the particular columns
 * name discoverer).
 * 
 * @author D. Campione
 *
 */
public abstract class ColumnsNameDiscovererHandlerFactoryMethod {

    public IColumnsNameDiscovererElement createElement(String relationName) {
        IColumnsNameDiscovererElement element = newElement();
        element.setSQLQuery(relationName);
        return element;
    }

    public abstract IColumnsNameDiscovererElement newElement();

    public String returnElement(IColumnsNameDiscovererElement element) {
        return element.getSQLQuery();
    }
}