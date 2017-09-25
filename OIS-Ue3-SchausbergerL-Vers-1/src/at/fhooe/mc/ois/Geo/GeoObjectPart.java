package at.fhooe.mc.ois.Geo;

import at.fhooe.mc.ois.Matrix;
import at.fhooe.mc.ois.PresentationSchema;

import java.awt.*;

/**
 * Created by laureenschausberger on 02.05.17.
 */
public abstract class GeoObjectPart {

    /**
     * draws object
     * @param _graphics
     * @param _matrix
     * @param _schema
     */
    public abstract void draw(Graphics _graphics, Matrix _matrix, PresentationSchema _schema);

    /**
     * return Rectangle which represents bounds of line
     * @return
     */
    public  abstract Rectangle getBounds();

    /**
     * checks if Line contains _point
     * @param _point
     * @return
     */
    public  abstract boolean contains(Point _point);
}
