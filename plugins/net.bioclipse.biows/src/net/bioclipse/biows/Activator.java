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
package net.bioclipse.biows;

import net.bioclipse.biows.business.IBiowsJSManager;
import net.bioclipse.biows.business.IBiowsManager;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "net.bioclipse.biows";

    // The shared instance
    private static Activator plugin;

  private ServiceTracker finderTracker;
  private ServiceTracker jsFinderTracker;

  private static final Logger logger = Logger.getLogger(Activator.class);

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    
            finderTracker = new ServiceTracker( context,
                                                IBiowsManager.class.getName(),
                                                null );
            finderTracker.open();
            
            jsFinderTracker = new ServiceTracker(
                context,
                IBiowsJSManager.class.getName(),
                null
            );
            jsFinderTracker.open();
        }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
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

  public IBiowsManager getBiowsManager() {
      IBiowsManager biowsManager;
      
      try {
          biowsManager
              = (IBiowsManager) finderTracker.waitForService(1000*30);
      }
      catch (InterruptedException e) {
          throw
            new IllegalStateException("Could not get biows manager", e);
      }
      if (biowsManager == null) {
          throw new IllegalStateException("Could not get biows manager");
      }
      return biowsManager;
  }
  
  public IBiowsManager getBiowsJSManager() {
      IBiowsJSManager biowsJSManager;
      
      try {
          biowsJSManager
              = (IBiowsJSManager) jsFinderTracker.waitForService(1000*30);
      }
      catch (InterruptedException e) {
          throw new IllegalStateException("Could not get biows JS manager");
      }
      if (biowsJSManager == null) {
          throw new IllegalStateException("Could not get biows JS manager");
      }
      return biowsJSManager;
  }
}
