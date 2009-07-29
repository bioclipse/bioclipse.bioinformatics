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
                                         .getDefault().getBioJavaManager();
        
        List<IDNA> dnalist=new ArrayList<IDNA>();
        dnalist.add(biojava.DNAfromString( "ctcttcgg" ));
        dnalist.add(biojava.DNAfromString( "ctcaattcggaaa" ));
        
        List<IDNA> res = kalign.alignDNA( dnalist );
        assertEquals( 2, res.size() );
        
    }

    @Test
    public void testAlignProteins() throws BioclipseException{
        
        IKalignManager kalign = Activator.getDefault().getKalignManager();
        IBiojavaManager biojava = net.bioclipse.biojava.business.Activator
                                         .getDefault().getBioJavaManager();
        
        List<IProtein> proteinlist=new ArrayList<IProtein>();
        proteinlist.add(biojava.proteinFromString( "ASAMPLESEQ" ));
        proteinlist.add(biojava.proteinFromString( "ANOTHERSAMPLESEQ" ));

        List<IProtein> res = kalign.alignProteins( proteinlist );
        assertEquals( 2, res.size() );
        
    }

}
