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

import java.util.List;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IDNA;
import net.bioclipse.core.domain.IProtein;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(value = "Controls access to KAlign Webservice at EBI.")
/**
 * 
 * @author ola
 *
 */
public interface IKalignManager extends IBioclipseManager {

    /**
     * Accepts a list of DNA and delegates to generic alignment method.
     * Parses results and returns a list of aligned DNA.
     * This is a long running method.
     * @param dnaList List of DNA sequences to align
     * @param monitor a monitor for reporting progress
     * @return List of IDNA with aligned sequences
     * @throws BioclipseException if aligning fails or result is not list 
     * of DNA
     */
    @Recorded
    @PublishedMethod(params="List<IDNA> dnaList",
                     methodSummary="Align a list of DNA sequences using the " +
                     		"KAlign Web service at EBI.")
    public List<IDNA> alignDNA(List<IDNA> dnaList) throws BioclipseException;

    
    /**
     * Accepts a list of proteins and delegates to generic alignment method.
     * Parses results and returns a list of aligned proteins.
     * This is a long running method.
     * @param proteinList List of protein sequences to align
     * @param monitor a monitor for reporting progress
     * @return List of IDNA with aligned sequences
     * @throws BioclipseException if aligning fails or result is not list 
     * of proteins
     */
    @Recorded
    @PublishedMethod(params="List<IProtein> proteinList",
                     methodSummary="Align a list of protein sequences using " +
                     		"the KAlign Web service at EBI.")
    public List<IProtein> alignProteins(List<IProtein> proteinList) throws BioclipseException;
    
    
}
