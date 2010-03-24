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
package net.bioclipse.align.kalign.ws.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.align.kalign.ws.Activator;
import net.bioclipse.align.kalign.ws.business.IKalignManager;
import net.bioclipse.biojava.business.IBiojavaManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;

import org.junit.Test;

/**
 * 
 * @author ola
 *
 */
public class PluginTestKalignwsManager {


    @Test
    public void testAlignDNA() throws BioclipseException{
        
        IKalignManager kalign = Activator.getDefault().getKalignManager();
        IBiojavaManager biojava = net.bioclipse.biojava.business.Activator
                                     .getDefault().getJavaBiojavaManager();
        
        List<IDNA> dnalist=new ArrayList<IDNA>();
        dnalist.add(biojava.DNAfromPlainSequence( "ctcttcgg" ));
        dnalist.add(biojava.DNAfromPlainSequence( "ctcaattcggaaa" ));
        
        List<IDNA> res = kalign.alignDNA( dnalist );
        assertEquals( 2, res.size() );
        
        assertEquals( "ctc--ttcgg~~~", res.get( 0 ).getPlainSequence() );
        assertEquals( "ctcaattcggaaa", res.get( 1 ).getPlainSequence() );
        
    }

    @Test
    public void testAlignProteins() throws BioclipseException{
        
        IKalignManager kalign = Activator.getDefault().getKalignManager();
        IBiojavaManager biojava = net.bioclipse.biojava.business.Activator
                                         .getDefault().getJavaBiojavaManager();
        
        List<IProtein> proteinlist=new ArrayList<IProtein>();
        proteinlist.add(biojava.proteinFromPlainSequence( "ASAMPLESEQ" ));
        proteinlist.add(biojava.proteinFromPlainSequence( "ANOTHERSAMPLESEQ" ));

        List<IProtein> res = kalign.alignProteins( proteinlist );
        assertEquals( 2, res.size() );
        
        assertEquals( "A------SAMPLESEQ", res.get( 0 ).getPlainSequence() );
        assertEquals( "ANOTHERSAMPLESEQ", res.get( 1 ).getPlainSequence() );

    }

}
