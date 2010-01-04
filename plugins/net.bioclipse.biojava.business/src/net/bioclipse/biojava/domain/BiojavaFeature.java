package net.bioclipse.biojava.domain;

import org.biojava.bio.seq.Feature;

public class BiojavaFeature implements IFeature {
    Feature feature;
    
    public BiojavaFeature(Feature feature) {
        this.feature = feature;
    }
}
