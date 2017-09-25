package at.fhooe.mc.ois.Geo.Parts;

import at.fhooe.mc.ois.Geo.GeoObjectPart;
import at.fhooe.mc.ois.Matrix;
import at.fhooe.mc.ois.PresentationSchema;

import java.awt.*;

/**
 * Created by laureenschausberger on 02.05.17.
 */
public class Point extends GeoObjectPart {

    java.awt.Point mGeometry = null;

    public Point(java.awt.Point _geometry) {
        mGeometry = _geometry;
    }

    @Override
    public void draw(Graphics _graphics, Matrix _matrix, PresentationSchema _schema) {
        if (mGeometry != null && _graphics != null && _matrix != null) {
            _graphics.setColor(_schema.getFillColor());
            java.awt.Point point = _matrix.multiply(mGeometry);
            _graphics.fillRect((int)point.getX(), (int)point.getY(), 1, 1);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)mGeometry.getX(), (int)mGeometry.getY(), 1, 1);
    }

    @Override
    public boolean contains(java.awt.Point _point) {
        if(_point.getX() == mGeometry.getX() && _point.getY() == mGeometry.getY())
            return true;
        return false;
    }
}
