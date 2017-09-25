package at.fhooe.mc.ois;

import java.awt.*;

/**
 * Created by laureenschausberger on 21.04.17.
 */
public class PresentationSchema {
    /**
     * line color
      */
    private Color mLineColor = null;

    /**
     * fill color
     */
    private Color mFillColor = null;

    /**
     * line width
     */
    private float mLineWidth = -1.0f;

    public PresentationSchema(Color _lineColor, Color _fillcolor) {
        mLineColor = _lineColor;
        mFillColor = _fillcolor;
    }

    /**
     * returns line color
     * @return
     */
    public Color getLineColor() {
        return mLineColor;
    }

    /**
     * returns fill color
     * @return
     */
    public Color getFillColor() {
        return mFillColor;
    }
}
