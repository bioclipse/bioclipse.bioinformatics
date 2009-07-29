/*******************************************************************************
 * Copyright (c) 2009 Ola Spjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth - initial API and implementation
 ******************************************************************************/
package net.bioclipse.biows.business;

import net.bioclipse.biows.Activator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;

/**
 * 
 * @author ola
 *
 */
public class BiowsManagerFactory implements IExecutableExtension, 
                                                IExecutableExtensionFactory {

    private Object biowsManager;
    
    public void setInitializationData( IConfigurationElement config,
                                       String propertyName, 
                                       Object data) throws CoreException {
    
        biowsManager = Activator.getDefault().getBiowsJSManager();
    }
    
    public Object create() throws CoreException {
        return biowsManager;
    }
}
