/*****************************************************************************
 * Copyright (c) 2008 Bioclipse Project
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *****************************************************************************/

package net.bioclipse.biojava.domain;

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
    public static String removeUntilFirstNewline(String content) {

        Matcher matcher = NEWLINE.matcher(content);
        if ( matcher.find() )
            return content.substring(matcher.end());

        // masak 2009-06-25: I've only refactored this method to be shorter
        // and more efficient, but it seems a bit doubtful to me that we
        // want to return the string unaffected if we cannot find a newline.
        // Seems more reasonable to return an empty string, or to throw
        // an exception.
        // I'll leave it as it is, but might submit a bug ticket about it.
        return content;
    }
}
