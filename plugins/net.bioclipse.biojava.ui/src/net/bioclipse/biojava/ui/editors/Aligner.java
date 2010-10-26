/* ***************************************************************************
 * Copyright (c) 2008 Bioclipse Project
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *****************************************************************************/

package net.bioclipse.biojava.ui.editors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.bioclipse.biojava.business.Activator;
import net.bioclipse.biojava.business.IBiojavaManager;
import net.bioclipse.core.api.domain.ISequence;
import net.bioclipse.ui.editors.ColorManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class Aligner extends EditorPart {

    private int squareSize = 20;
    private final static int MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS = 8,
                             NAME_CANVAS_WIDTH_IN_SQUARES = 8;
    private int canvasWidthInSquares, numberOfSequences;

    static final Display display = Display.getCurrent();
    static final ColorManager colorManager = new ColorManager();

    static public final Color
        normalAAColor   = display.getSystemColor( SWT.COLOR_WHITE ),
        polarAAColor    = colorManager.getColor( new RGB(0xD0, 0xFF, 0xD0) ),
        nonpolarAAColor = colorManager.getColor( new RGB(0xFF, 0xFF, 0xD0) ),
        acidicAAColor   = colorManager.getColor( new RGB(0xFF, 0xD0, 0xA0) ),
        basicAAColor    = colorManager.getColor( new RGB(0xD0, 0xFF, 0xFF) ),
        smallAAColor    = colorManager.getColor( new RGB(0xFF, 0xD0, 0xD0) ),
        cysteineColor   = colorManager.getColor( new RGB(0xFF, 0xFF, 0xD0) ),
        adenineColor    = colorManager.getColor( new RGB(0xB0, 0xFF, 0xB0) ),
        cytosineColor   = colorManager.getColor( new RGB(0xB0, 0xB0, 0xFF) ),
        guanineColor    = colorManager.getColor( new RGB(0xFF, 0xFF, 0xD0) ),
        thymineColor    = colorManager.getColor( new RGB(0xFF, 0xB0, 0xB0) ),
        textColor       = display.getSystemColor( SWT.COLOR_BLACK ),
        nameColor       = display.getSystemColor( SWT.COLOR_WHITE ),
        buttonColor     = colorManager.getColor( new RGB(0x66, 0x66, 0x66) ),
        consensusColor  = colorManager.getColor( new RGB(0xAA, 0xAA, 0xAA) ),
        selectionColor1 = display.getSystemColor( SWT.COLOR_BLACK ),
        selectionColor2 = display.getSystemColor( SWT.COLOR_BLACK ),
        backgroundColor = display.getSystemColor( SWT.COLOR_WHITE );

    static public final Color[] consensusColors
        = generateColorList( new int[] {
                0xFFFFDD, // only one type; total consensus
                0xEEEEBE, // two different types
                0xDDDDB0, // three...
                0xCCCCA3, // ...
                0xBBBB95,
                0xAAAA88,
                0x99997A,
                0x88886C,
                0x77775F
        } );

    //          seqname, sequence
    private Map<String,  String> sequences;
    private int consensusRow;

    private static Point np() { return new Point(0, 0); }

    private Point selectionStart                = np(),
                  selectionEnd                  = np(),
                  dragStart                     = np(),
                  dragEnd                       = np(),
                  selectionTopLeftInSquares     = np(),
                  selectionBottomRightInSquares = np();

    private boolean currentlySelecting         = false,
                    currentlyDraggingSelection = false,
                    selectionVisible           = false;

    private GridData data;
    private Composite parent;
    private Composite c;
    private IBiojavaManager biojava
        = Activator.getDefault().getJavaBiojavaManager();
    private char fastas[][];
    private Canvas nameCanvas;
    private PaintListener nameCanvasPaintListener;
    private Canvas sequenceCanvas;
    private PaintListener sequenceCanvasPaintListener;
    private ScrolledComposite sc;

    private boolean wrapMode = false;

    @Override
    public void doSave( IProgressMonitor monitor ) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void init( IEditorSite site, IEditorInput input )
        throws PartInitException {

        if (!(input instanceof IFileEditorInput))
            throw new PartInitException(
                "Invalid Input: Must be IFileEditorInput");

        setSite(site);
        setInput(input);
    }

    @Override
    public void setInput( IEditorInput input ) {
        super.setInput(input);

        // Turn the editor input into an IFile.
        IFile file = (IFile) input.getAdapter( IFile.class );
        if (file == null)
            return;

        List<ISequence> sequences;
        try {
            sequences = biojava.sequencesFromFile(file);
        } catch (FileNotFoundException e1) {
            return; // No exception handling at all! We just give up! Gasp!
        }
        this.setSequences(sequences);
    }

    public void setSequences(List<ISequence> seqs) {
        sequences = new LinkedHashMap<String, String>();

        // Add the sequences one by one to the Map. Do minor cosmetics
        // on the name by removing everything up to and including to
        // the last '|', if any.
        for (ISequence seq : seqs) {
            String name = seq.getName().replaceFirst( ".*\\|", "" );
            sequences.put( name, seq.getPlainSequence() );
        }

        // We only show a consensus sequence if there is more than one
        // sequence already.
        if (sequences.size() > 1) {
            sequences.put(
                "Consensus",
                consensusSequence( sequences.values() )
            );
        }

        numberOfSequences = sequences.size();
        canvasWidthInSquares = maxLength( sequences.values() );

        fastas = new char[ sequences.size() ][];

        {
            int i = 0;
            for ( String sequence : sequences.values() )
                fastas[i++] = sequence.toCharArray();
        }

        if (parent != null) {
            parent.layout();
            parent.redraw();
        }
    }

    private static String consensusSequence( final Collection<String>
                                                   sequences ) {

        final StringBuilder consensus = new StringBuilder();
        for ( int i = 0, n = maxLength(sequences); i < n; ++i ) {
            consensus.append( consensusChar(sequences, i) );
        }

        return consensus.toString();
    }

    private static int maxLength( final Collection<String> strings ) {

        int maxLength = 0;
        for ( String s : strings )
            if ( maxLength < s.length() )
                maxLength = s.length();

        return maxLength;
    }

    private static char consensusChar( final Collection<String> sequences,
                                       final int index ) {

        Map<Character, Boolean> columnChars
            = new HashMap<Character, Boolean>();

        for ( String seq : sequences )
            columnChars.put( seq.length() > index
                               ? seq.charAt(index)
                               : '\0',
                             true );

        return columnChars.size() == 1
               ? columnChars.keySet().iterator().next()
               : Character.forDigit( Math.min(columnChars.size(), 9), 10 );
    }

    static private Color[] generateColorList( int[] rgbList ) {
        List<Color> colors = new ArrayList<Color>();
        for ( int rgb : rgbList ) {
            colors.add( colorManager.getColor( new RGB( (rgb >> 16) & 0xFF,
                                                        (rgb >>  8) & 0xFF,
                                                        (rgb >>  0) & 0xFF )));
        }
        return colors.toArray(new Color[0]);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    private void selectionBounds() {
        int xLeft   = Math.min( selectionStart.x, selectionEnd.x ),
            xRight  = Math.max( selectionStart.x, selectionEnd.x ),
            yTop    = Math.min( selectionStart.y, selectionEnd.y ) - squareSize,
            yBottom = Math.max( selectionStart.y, selectionEnd.y ) - squareSize;

        // clip
        xLeft   = Math.max( xLeft, 0 );
        xRight  = Math.min( xRight, canvasWidthInSquares * squareSize );
        yTop    = Math.max( yTop, 0 );
        yBottom = Math.min( yBottom, (numberOfSequences-1) * squareSize );

        // round down
        xLeft  =                   xLeft / squareSize;
        yTop   =                    yTop / squareSize;

        // round up
        xRight  =  (xRight+squareSize-1) / squareSize;
        yBottom = (yBottom+squareSize-1) / squareSize;

        // make sure a selection always has positive area
        if ( xRight <= xLeft )
            xRight = xLeft + 1;
        if ( yBottom <= yTop && yTop < numberOfSequences-1 )
            yBottom = yTop + 1;

        // special case: mark along the consensus row
        if ( yTop == yBottom )
            yTop = 0;

        selectionTopLeftInSquares.x     = xLeft;
        selectionTopLeftInSquares.y     = yTop;
        selectionBottomRightInSquares.x = xRight;
        selectionBottomRightInSquares.y = yBottom;
    }

    // Returns the four boundary coordinates, in pixels, of the current
    // selection, with the current dragged distance taken into account.
    private int[] boundaries() {
        int dragXDistance = dragEnd.x - dragStart.x,
            dragYDistance = dragEnd.y - dragStart.y,

        xLeft   = selectionTopLeftInSquares.x     * squareSize + dragXDistance,
        yTop    = (selectionTopLeftInSquares.y + 1)
                                                  * squareSize + dragYDistance,
        xRight  = selectionBottomRightInSquares.x * squareSize + dragXDistance,
        yBottom = (selectionBottomRightInSquares.y + 1)
                                                  * squareSize + dragYDistance;

        return new int[] { xLeft, yTop, xRight, yBottom };
    }

    @Override
    public void createPartControl( Composite parent ) {

        this.parent = parent;

        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        parent.setLayout( layout );

        nameCanvas = new Canvas( parent, SWT.NONE );
        data = new GridData(GridData.FILL_VERTICAL);
        nameCanvas.setLayoutData( data );

        sc = new ScrolledComposite( parent, SWT.H_SCROLL | SWT.V_SCROLL );
        GridData sc_data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
                                        | GridData.FILL_BOTH);
        sc.setLayoutData( sc_data );

        c = new Composite(sc, SWT.NONE);
        c.setLayout( new FillLayout() );

        sequenceCanvas = new Canvas( c, SWT.NONE );
        sequenceCanvas.setLocation( 0, 0 );
        setCanvasSizes();
        sc.setContent( c );

        equipCanvasesForNonwrapMode();
    }

    @Override
    public void setFocus() {
    }

    private void setCanvasSizes() {

        int nameWidth = squareSize >= MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS
                        ? NAME_CANVAS_WIDTH_IN_SQUARES * squareSize : 0;
        if (wrapMode) {
            data.widthHint = 0;
            final int columns = 30;
            c.setSize( columns * squareSize + nameWidth,
                       ( 1 + (numberOfSequences + 2)
                             * (fastas[0].length / columns) )
                       * squareSize );
        }
        else {
            data.widthHint = nameWidth;
            c.setSize( canvasWidthInSquares * squareSize,
                       (numberOfSequences + 1) * squareSize );
        }
    }

    public void zoomIn() {
        squareSize++;
        setCanvasSizes();
        parent.layout();
        parent.redraw();
    }

    public void zoomOut() {
        if ( squareSize > 1 )
            squareSize--;
        setCanvasSizes();
        parent.layout();
        parent.redraw();
    }

    public List<ISequence> getSequences() {

        List<ISequence> seqs = new ArrayList<ISequence>();

        for (String name : sequences.keySet()){
            if (!("Consensus".equals( name ))){
                String plainSequence = sequences.get( name );
                seqs.add(biojava.proteinFromPlainSequence(plainSequence, name));
            }
        }
        return seqs;
    }

    enum Type {
        DNA,
        RNA,
        PROTEIN
    }

    public Type getSequenceType() {
        return Type.PROTEIN;
    }

    public void toggleWrapMode() {
        nameCanvas.removePaintListener(nameCanvasPaintListener);
        sequenceCanvas.removePaintListener(sequenceCanvasPaintListener);

        wrapMode = !wrapMode;

        if (wrapMode) {
            equipCanvasesForWrapMode();
        }
        else {
            equipCanvasesForNonwrapMode();
        }

        nameCanvas.addPaintListener( nameCanvasPaintListener );
        sequenceCanvas.addPaintListener( sequenceCanvasPaintListener);

        setCanvasSizes();
        parent.layout();
        parent.redraw();
    }

    private void equipCanvasesForNonwrapMode() {
        nameCanvasPaintListener = new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (squareSize < MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS)
                    return;

                GC gc = e.gc;
                gc.setTextAntialias( SWT.OFF );
                gc.setBackground( buttonColor );

                gc.setFont( new Font(gc.getDevice(),
                                     "Arial",
                                     (int)(.7 * squareSize),
                                     SWT.NONE) );
                gc.setForeground( nameColor );

                int index = 0;
                for ( String name : sequences.keySet() ) {
                    if ( index == consensusRow )
                        gc.setBackground( consensusColor );
                    gc.fillRectangle(0, (1 + index) * squareSize,
                                     8 * squareSize, squareSize);
                    Point extent = gc.stringExtent(name);
                    gc.drawString(
                        name,
                        5,
                        (int)((1.5 + index) * squareSize) - extent.y/2
                    );
                    ++index;
                }
            }
        };
        nameCanvas.addPaintListener( nameCanvasPaintListener );

        sequenceCanvasPaintListener = new PaintListener() {
            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                gc.setTextAntialias( SWT.OFF );

                gc.setForeground( textColor );

                int firstVisibleColumn
                        = sc.getHorizontalBar().getSelection() / squareSize,
                    lastVisibleColumn
                        = firstVisibleColumn
                          + sc.getBounds().width / squareSize
                          + 2; // compensate for 2 possible round-downs

                drawTickMarks(firstVisibleColumn, lastVisibleColumn, gc);
                drawSequences(fastas,
                              firstVisibleColumn, lastVisibleColumn,
                              gc);
                drawSelection( gc );
                drawConsensusSequence(
                    fastas[numberOfSequences-1],
                    firstVisibleColumn, lastVisibleColumn, gc);
            }

            private void drawTickMarks(int firstVisibleColumn,
                                       int lastVisibleColumn, GC gc) {

                gc.setForeground( textColor );
                gc.setBackground( backgroundColor );

                for ( int column = firstVisibleColumn;
                      column < lastVisibleColumn; ++column ) {

                    if (column % 10 != 9) // It's an off-by one situation:
                        continue;         // A zero-based 9 is a one-based 10

                    int xCoord = column * squareSize + squareSize/2,
                            y1 = (int)(squareSize * .7),
                            y2 = (int)(squareSize * .9);

                    gc.drawLine(xCoord, y1, xCoord, y2);
                }

                if ( squareSize
                        >= MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS * 2 ) {

                    gc.setFont( new Font(gc.getDevice(),
                               "Arial",
                               (int)(.35 * squareSize),
                               SWT.NONE) );

                    for ( int column = firstVisibleColumn;
                          column < lastVisibleColumn; ++column ) {

                        if (column % 10 != 9)  // Same off-by-one again
                            continue;

                        int xCoord = column * squareSize + squareSize/2;

                        String text = "" + (column + 1);
                        Point extent = gc.stringExtent(text);
                        gc.drawString( text,
                                       xCoord - extent.x/2,
                                       (int)(squareSize * .6) - extent.y );
                    }
                }
            }

            private void drawSequences( final char[][] fasta,
                                        int firstVisibleColumn,
                                        int lastVisibleColumn, GC gc ) {

                if ( squareSize >= MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS ) {
                    gc.setFont( new Font(gc.getDevice(),
                                         "Arial",
                                         (int)(.7 * squareSize),
                                         SWT.NONE) );
                }

                int n = numberOfSequences == 1 ? 1 : numberOfSequences - 1;
                for ( int column = firstVisibleColumn;
                      column < lastVisibleColumn; ++column ) {

                    int xCoord = column * squareSize;

                    for ( int row = 0; row < n; ++row ) {

                        char c = fasta[row].length > column
                                 ? fasta[row][column] : ' ';
                        String cc = c + "";

                        gc.setBackground(
                                "HKR".contains( cc ) ? basicAAColor
                             :   "DE".contains( cc ) ? acidicAAColor
                             : "TQSN".contains( cc ) ? polarAAColor
                             :  "FYW".contains( cc ) ? nonpolarAAColor
                             :   "GP".contains( cc ) ? smallAAColor
                             :    'C' == c           ? cysteineColor
                             :    'a' == c           ? adenineColor
                             :    'c' == c           ? cytosineColor
                             :    'g' == c           ? guanineColor
                             :    't' == c           ? thymineColor
                                                     : normalAAColor );

                        int yCoord = (1 + row) * squareSize;

                        gc.fillRectangle(xCoord, yCoord,
                                         squareSize, squareSize);

                        if ( squareSize
                                  >= MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS ) {
                            String text = cc.toUpperCase();
                            Point extent = gc.stringExtent(text);
                            gc.drawString( text,
                                           xCoord + squareSize/2 - extent.x/2,
                                           yCoord + squareSize/2 - extent.y/2 );
                        }
                    }
                }
            }

            private void drawConsensusSequence( final char[] sequence,
                                                int firstVisibleColumn,
                                                int lastVisibleColumn, GC gc ) {

                if ( numberOfSequences < 2 )
                    return;

                int yCoord = numberOfSequences * squareSize;

                for ( int column = firstVisibleColumn;
                      column < lastVisibleColumn; ++column ) {

                    char c = sequence.length > column ? sequence[column] : ' ';
                    int consensusDegree = Character.isDigit(c) ? c - '0' : 1;

                    gc.setBackground(consensusColors[ consensusDegree-1 ]);

                    int xCoord = column * squareSize;

                    gc.fillRectangle(xCoord, yCoord, squareSize, squareSize);

                    if ( Character.isUpperCase( c )
                            && squareSize
                                 >= MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS ) {
                        String text = "" + c;
                        Point extent = gc.stringExtent(text);
                        gc.drawString( text,
                                       xCoord + squareSize/2 - extent.x/2,
                                       yCoord + squareSize/2 - extent.y/2 );
                    }
                }
            }

            private void drawSelection( GC gc ) {

                if (!selectionVisible)
                    return;

                int boundaries[] = boundaries(),
                    xLeft   = boundaries[0],
                    yTop    = boundaries[1],
                    xRight  = boundaries[2],
                    yBottom = boundaries[3];

                gc.setForeground( selectionColor1 );
                gc.drawRectangle( xLeft,
                                  yTop,
                                  xRight - xLeft - 1,
                                  yBottom - yTop - 1 );

                gc.setBackground( selectionColor2 );
                gc.setAlpha( 64 ); // 25%
                gc.fillRectangle( xLeft           + 1,
                                  yTop            + 1,
                                  xRight - xLeft  - 1,
                                  yBottom - yTop  - 1 );
                gc.setAlpha( 255 ); // opaque again
            }
        };
        sequenceCanvas.addPaintListener( sequenceCanvasPaintListener);

        sequenceCanvas.addMouseListener( new MouseListener() {

            public void mouseDoubleClick( MouseEvent e ) {
                // we're not interested in double clicks
            }

            public void mouseDown( MouseEvent e ) {
                if (e.button != 1)
                    return;

                int boundaries[] = boundaries(),
                    xLeft   = boundaries[0],
                    yTop    = boundaries[1],
                    xRight  = boundaries[2],
                    yBottom = boundaries[3];

                if ( selectionVisible
                     && xLeft <= e.x && e.x <= xRight
                     && yTop  <= e.y && e.y <= yBottom ) {

                    currentlyDraggingSelection = true;
                    dragStart.x = dragEnd.x = e.x;
                    dragStart.y = dragEnd.y = e.y;
                }
                else {
                    currentlySelecting = true;
                    selectionVisible = false;
                    selectionStart.x = selectionEnd.x = e.x;
                    selectionStart.y = selectionEnd.y = e.y;
                    sequenceCanvas.redraw();
                }
            }

            public void mouseUp( MouseEvent e ) {

                if (currentlyDraggingSelection) {
                    // The expressions do three things:
                    //
                    // 1. Calculate the distance dragged (end minus start)
                    // 2. Add half a square in that direction
                    // 3. Round towards zero to the closest squareSize point
                    //
                    // The second step is required precisely because the third
                    // step rounds towards zero.
                    int xDelta
                          = (dragEnd.x - dragStart.x                       // 1
                             + squareSize/2 * (dragEnd.x<dragStart.x?-1:1) // 2
                            ) / squareSize,                                // 3
                        yDelta
                          = (dragEnd.y - dragStart.y                       // 1
                             + squareSize/2 * (dragEnd.y<dragStart.y?-1:1) // 2
                            ) / squareSize;                                // 3

                    selectionTopLeftInSquares.x       += xDelta;
                    selectionBottomRightInSquares.x   += xDelta;

                    selectionTopLeftInSquares.y       += yDelta;
                    selectionBottomRightInSquares.y   += yDelta;

                    sequenceCanvas.redraw();
                }

                dragEnd = new Point(dragStart.x, dragStart.y);
                currentlySelecting = currentlyDraggingSelection = false;
            }

        });

        sequenceCanvas.addMouseMoveListener( new MouseMoveListener() {

            public void mouseMove( MouseEvent e ) {

                // e.stateMask contains info on shift keys
                if (currentlySelecting) {
                  selectionEnd.x = e.x;
                  selectionEnd.y = e.y;

                  selectionVisible = true;

                  int viewPortLeft  = -c.getLocation().x,
                      viewPortRight = viewPortLeft + sc.getBounds().width,
                      viewPortTop   = -c.getLocation().y,
                      maximumLeft   = sc.getHorizontalBar().getMaximum();

                  if ( e.x > viewPortRight ) {
                      viewPortLeft += e.x - viewPortRight;
                      if (viewPortRight >= maximumLeft )
                          viewPortLeft = maximumLeft - sc.getBounds().width;
                  }
                  else if ( e.x < viewPortLeft ) {
                      viewPortLeft -= viewPortLeft - e.x;
                      if (viewPortLeft < 0)
                          viewPortLeft = 0;
                  }

                  if ( viewPortLeft != -c.getLocation().x ) {
                      sc.getHorizontalBar().setSelection( viewPortLeft );
                      c.setLocation( -viewPortLeft, -viewPortTop );
                  }

                  sequenceCanvas.redraw();

                  selectionBounds();
                }

                if (currentlyDraggingSelection) {
                    dragEnd.x = e.x;
                    dragEnd.y = e.y;

                    sequenceCanvas.redraw();
                }
            }

        });
    }

    private void equipCanvasesForWrapMode() {
        final int columns = 30;

        sequenceCanvasPaintListener = new PaintListener() {
            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                gc.setTextAntialias( SWT.OFF );

                int offset = 0;
                if ( squareSize >= MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS ) {
                    offset = NAME_CANVAS_WIDTH_IN_SQUARES * squareSize;

                    gc.setFont( new Font(gc.getDevice(),
                                         "Arial",
                                         (int)(.7 * squareSize),
                                         SWT.NONE) );
                }

                drawNames(gc);
                drawTickMarks(offset, gc);
                drawSequences(fastas, offset, gc);
                drawConsensusSequence(fastas[numberOfSequences-1],
                                      offset, gc);
            }

            private void drawNames(GC gc) {
                gc.setBackground( buttonColor );
                gc.setForeground( nameColor );

                gc.setFont( new Font(gc.getDevice(),
                                     "Arial",
                                     (int)(.7 * squareSize),
                                     SWT.NONE) );

                int index = 0;
                for ( String name : sequences.keySet() ) {
                    if ( index == consensusRow )
                        gc.setBackground( consensusColor );
                    int groups = fastas[0].length / columns;
                    for (int group = 0; group < groups; group++) {
                        Point extent = gc.stringExtent(name);
                        int y = (1 + index + (sequences.size() + 2) * group)
                                * squareSize;
                        gc.fillRectangle(0, y, 8 * squareSize, squareSize);
                        gc.drawString(
                            name,
                            5,
                            y  + squareSize/2 - extent.y/2
                        );
                    }
                    ++index;
                }
            }

            private void drawTickMarks(int offset, GC gc) {

                gc.setForeground( textColor );
                gc.setBackground( backgroundColor );

                for (int column = 9; column < fastas[0].length; column += 10) {

                    int xCoord = offset
                                 + (column % columns)
                                   * squareSize
                                 + squareSize/2,
                        yCoord = (numberOfSequences + 2)
                                 * (column / columns) * squareSize,
                            y1 = yCoord + (int)(squareSize * .7),
                            y2 = yCoord + (int)(squareSize * .9);

                    gc.drawLine(xCoord, y1, xCoord, y2);
                }

                if ( squareSize
                     >= MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS * 2 ) {

                    gc.setFont( new Font(gc.getDevice(),
                                "Arial",
                                (int)(.35 * squareSize),
                                SWT.NONE) );

                    for ( int column = 9;
                          column < fastas[0].length;
                          column += 10 ) {

                        int xCoord
                                 = offset
                                   + (column % columns) * squareSize
                                   + squareSize/2,
                          yCoord = (numberOfSequences + 2)
                                   * (column / columns) * squareSize
                                   + (int)(squareSize * .6);

                        String text = "" + (column + 1);
                        Point extent = gc.stringExtent(text);
                        gc.drawString( text,
                                       xCoord - extent.x/2,
                                       yCoord - extent.y );
                    }
                }
            }

            private void drawSequences( final char[][] fasta,
                                        int offset,
                                        GC gc ) {

                gc.setForeground( textColor );
                gc.setFont( new Font(gc.getDevice(),
                                     "Arial",
                                     (int)(.7 * squareSize),
                                     SWT.NONE) );

                int n = numberOfSequences == 1 ? 1 : numberOfSequences - 1;
                for ( int column = 0; column < fasta[0].length; ++column ) {

                    int xCoord = (column % columns) * squareSize;

                    for ( int row = 0; row < n; ++row ) {

                        char c = fasta[row].length > column
                                 ? fasta[row][column] : ' ';
                        String cc = c + "";

                        gc.setBackground(
                             "HKR".contains( cc ) ? basicAAColor
                          :   "DE".contains( cc ) ? acidicAAColor
                          : "TQSN".contains( cc ) ? polarAAColor
                          :  "FYW".contains( cc ) ? nonpolarAAColor
                          :   "GP".contains( cc ) ? smallAAColor
                          :    'C' == c           ? cysteineColor
                          :    'a' == c           ? adenineColor
                          :    'c' == c           ? cytosineColor
                          :    'g' == c           ? guanineColor
                          :    't' == c           ? thymineColor
                                                  : normalAAColor );

                        int yCoord
                            = (1 + row
                               + (numberOfSequences + 2)
                                 * (column / columns))
                              * squareSize;

                        gc.fillRectangle(offset + xCoord, yCoord,
                                         squareSize, squareSize);

                        if ( squareSize
                                >= MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS ) {
                            String text = cc.toUpperCase();
                            Point extent = gc.stringExtent(text);
                            gc.drawString(
                                text,
                                offset + xCoord + squareSize/2 - extent.x/2,
                                yCoord + squareSize/2 - extent.y/2
                            );
                        }
                    }
                }
            }

            private void drawConsensusSequence( final char[] sequence,
                                                int offset,
                                                GC gc ) {

                if ( numberOfSequences < 2 )
                    return;

                for ( int column = 0; column < sequence.length; ++column ) {

                    char c = sequence.length > column ? sequence[column] : ' ';
                    int consensusDegree = Character.isDigit(c) ? c - '0' : 1;

                    gc.setBackground(consensusColors[ consensusDegree-1 ]);

                    int xCoord = (column % columns) * squareSize;
                    int yCoord
                        = (numberOfSequences
                           + (numberOfSequences + 2)
                             * (column / columns))
                           * squareSize;

                    gc.fillRectangle(offset + xCoord, yCoord,
                                     squareSize, squareSize);

                    if ( Character.isUpperCase( c )
                         && squareSize
                            >= MINIMUM_SQUARE_SIZE_FOR_TEXT_IN_PIXELS ) {
                        String text = "" + c;
                        Point extent = gc.stringExtent(text);
                        gc.drawString(
                            text,
                            offset + xCoord + squareSize/2 - extent.x/2,
                            yCoord + squareSize/2 - extent.y/2
                        );
                    }
                }
            }
        };
    }
}
