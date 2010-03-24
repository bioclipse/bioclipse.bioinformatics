/* *****************************************************************************
 * Copyright (c) 2009 Ola Spjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth - initial API and implementation
 ******************************************************************************/
package net.bioclipse.biows.tests;

import static org.junit.Assert.*;

import java.util.List;

import net.bioclipse.biows.Activator;
import net.bioclipse.biows.business.IBiowsManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;

import org.junit.Test;


public class PluginTestBiowsManager {

    @Test
    public void testQueryEMBL() throws BioclipseException{
        
        IBiowsManager biows = Activator.getDefault().getBiowsManager();
        List<IDNA> dna=biows.queryEMBL( "J00231" );
        assertEquals( 1, dna.size());

//        J00231,HSFOS,ROD894,LOP242600
        
    }

    @Test
    public void testQueryUniprot() throws BioclipseException{
        
        IBiowsManager biows = Activator.getDefault().getBiowsManager();
        List<IProtein> proteins=biows.queryUniProtKB( "Q9Y2H6" );
        assertEquals( 1, proteins.size());
        
        //Q9Y2H6,O95273,P08246
        
    }

}
