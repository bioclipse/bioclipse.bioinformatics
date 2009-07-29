package net.bioclipse.biows.tests;

import static org.junit.Assert.*;
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
        IDNA dna=biows.queryEMBL( "J00231" );
        assertNotNull( dna );

//        J00231,HSFOS,ROD894,LOP242600
        
    }

    @Test
    public void testQueryUniprot() throws BioclipseException{
        
        IBiowsManager biows = Activator.getDefault().getBiowsManager();
        IProtein protein=biows.queryUniProtKB( "Q9Y2H6" );
        assertNotNull( protein );
        
        //Q9Y2H6,O95273,P08246
        
    }

}
