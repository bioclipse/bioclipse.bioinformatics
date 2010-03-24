/* ***************************************************************************
 * Copyright (c) 2008 Bioclipse Project
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *****************************************************************************/

package net.bioclipse.biojava.business;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern NEWLINE
      = Pattern.compile( "[\\n\\r]", Pattern.DOTALL );

    /**
     * Used to remove the first line (until line break) in a string, e.g. to
     * remove header line in a FASTA string
     * @param content
     * @return
     */
    public static String withoutFirstLine(String content) {

        Matcher matcher = NEWLINE.matcher(content);
        if ( matcher.find() )
            return content.substring(matcher.end());

        throw new IllegalArgumentException(
            "The string does not appear to be FASTA"
        );
    }
}
