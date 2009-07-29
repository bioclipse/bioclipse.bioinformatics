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
package net.bioclipse.align.kalign.ws.business;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;

/**
 * 
 * @author ola
 *
 */
public class KalignManagerFactory implements IExecutableExtension, 
                                                IExecutableExtensionFactory {

    private Object kalignManager;
    
    public void setInitializationData( IConfigurationElement config,
                                       String propertyName, 
                                       Object data) throws CoreException {
    
        kalignManager = net.bioclipse.align.kalign.ws.Activator
                                    .getDefault().getKalignJSManager();
    }
    
    public Object create() throws CoreException {
        return kalignManager;
    }
}
