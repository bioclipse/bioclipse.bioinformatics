/* *****************************************************************************
* Copyright (c) 2009  Jonathan Alvarsson <jonalv@users.sf.net>
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contact: http://www.bioclipse.net/
******************************************************************************/
package net.bioclipse.biojava.business;

import net.bioclipse.biojava.business.IBiojavaManager;
import net.bioclipse.biojava.business.IJavaBiojavaManager;
import net.bioclipse.biojava.business.IJavaScriptBiojavaManager;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The shared instance
    private static Activator plugin;

    // Trackers for getting the managers
    private ServiceTracker javaFinderTracker;
    private ServiceTracker jsFinderTracker;

    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        javaFinderTracker
            = new ServiceTracker( context,
                                  IJavaBiojavaManager.class.getName(),
                                  null );

        javaFinderTracker.open();
        jsFinderTracker
            = new ServiceTracker( context,
                                  IJavaScriptBiojavaManager.class.getName(),
                                  null );

        jsFinderTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public IBiojavaManager getJavaBiojavaManager() {
        IBiojavaManager manager = null;
        try {
            manager = (IBiojavaManager)
                      javaFinderTracker.waitForService(1000*10);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the Java BiojavaManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the Java BiojavaManager");
        }
        return manager;
    }

    public IJavaScriptBiojavaManager getJavaScriptBiojavaManager() {
        IJavaScriptBiojavaManager manager = null;
        try {
            manager = (IJavaScriptBiojavaManager)
                      jsFinderTracker.waitForService(1000*10);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the JavaScript BiojavaManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the JavaScript BiojavaManager");
        }
        return manager;
    }
}
