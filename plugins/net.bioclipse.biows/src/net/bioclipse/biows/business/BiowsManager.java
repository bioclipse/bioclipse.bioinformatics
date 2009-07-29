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

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;

import net.bioclipse.biojava.business.IBiojavaManager;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.webservices.Activator;
import net.bioclipse.webservices.business.IWebservicesManager;


/**
 * This manager makes use of net.bioclipse.webservices WSDbfetch service @ EBI 
 * via SOAP to query and retrieve sequences.
 * 
 * Website: http://www.ebi.ac.uk/Tools/webservices/services/dbfetch
 * WSDL: http://www.ebi.ac.uk/Tools/webservices/wsdl/WSDbfetch.wsdl
 * 
 * @author ola
 *
 */
public class BiowsManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(BiowsManager.class);

    public String getManagerName() {
        return "biows";
    }
    
    public List<IDNA> queryEMBL(String seqid) throws BioclipseException{

        IBiojavaManager biojava = net.bioclipse.biojava.business.Activator
        .getDefault().getBioJavaManager();

        String fasta=download( "embl", seqid );

        return biojava.DNAsFromString( fasta );

    }

    public List<IProtein> queryUniProtKB(String seqid) throws BioclipseException{

        IBiojavaManager biojava = net.bioclipse.biojava.business.Activator
        .getDefault().getBioJavaManager();

        String fasta=download( "uniprotkb", seqid );

        return biojava.proteinsFromString( fasta );

    }

    public List<IDNA> queryRefseq(String seqid) throws BioclipseException{

        IBiojavaManager biojava = net.bioclipse.biojava.business.Activator
        .getDefault().getBioJavaManager();

        String fasta=download( "refseq", seqid );

        return biojava.DNAsfromString( fasta );

    }



    public String download(String database, String seqid) 
                                                      throws BioclipseException{

        IWebservicesManager ws = Activator.getDefault().getWebservicesManager();
        try {
            String dl=ws.downloadDbEntry( database, seqid, "fasta" );
            logger.debug("Biows (" + database+"," + seqid 
                         + ",fasta) returned:\n"+dl);
            return dl;
        } catch ( CoreException e ) {
            throw new BioclipseException("Error downloading from EBI WS: " 
                                         + e.getMessage());
        }
    }


}
