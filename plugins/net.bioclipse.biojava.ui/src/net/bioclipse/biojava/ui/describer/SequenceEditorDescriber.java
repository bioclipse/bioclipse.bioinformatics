package net.bioclipse.biojava.ui.describer;

import net.bioclipse.biojava.ui.editors.SequenceEditor;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IBioObject;
import net.bioclipse.core.domain.ISequence;
import net.bioclipse.core.domain.RecordableList;
import net.bioclipse.ui.business.describer.IBioObjectDescriber;


public class SequenceEditorDescriber implements IBioObjectDescriber {

    public SequenceEditorDescriber() {

    }

    @SuppressWarnings("unchecked")
    public String getPreferredEditorID( IBioObject object ) throws BioclipseException {

        //Valid for sequences
        if ( object instanceof ISequence ) {
            return SequenceEditor.SEQUENCE_EDITOR_ID;
        }
        
        //Valid for list of sequences
        if ( object instanceof RecordableList ) {
            RecordableList<IBioObject> biolist = (RecordableList<IBioObject>) object;
            if (biolist.isEmpty())
                throw new BioclipseException("BioList is empty");
            
            //Make sure first object is IMolecule
            if ( biolist.get( 0 ) instanceof ISequence ) {
                return SequenceEditor.SEQUENCE_EDITOR_ID;
            }
        }

        return null;
    }

}
