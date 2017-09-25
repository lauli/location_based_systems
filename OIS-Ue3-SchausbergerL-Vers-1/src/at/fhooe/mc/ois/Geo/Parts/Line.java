package at.fhooe.mc.ois.Geo.Parts;

import at.fhooe.mc.ois.Geo.GeoObjectPart;
import at.fhooe.mc.ois.Matrix;
import at.fhooe.mc.ois.PresentationSchema;

import java.awt.*;

/**
 * Created by laureenschausberger on 02.05.17.
 */
public class Line extends GeoObjectPart {

    java.awt.Point[] mGeometry = null;

    public Line(java.awt.Point[] _geometry) {
        mGeometry = _geometry;
    }

    @Override
    public void draw(Graphics _graphics, Matrix _matrix, PresentationSchema _schema) {
        if (mGeometry != null && _graphics != null && _matrix != null) {
            for(int i = 0; i < mGeometry.length-1; i++) {
                _graphics.setColor(_schema.getLineColor());
                java.awt.Point point = _matrix.multiply(mGeometry[i]);
                java.awt.Point point2 = _matrix.multiply(mGeometry[i+1]);
                _graphics.drawLine((int) point.getX(), (int) point.getY(), (int) point2.getX(), (int) point2.getY());
            }
        }
    }


    @Override
    public Rectangle getBounds() {
        Rectangle rect = new Rectangle(mGeometry[0]);
        for (int i = 0 ; i < mGeometry.length ; i++) {
            rect.add(mGeometry[i]);
        }
        return rect;
    }

    @Override
    public boolean contains(java.awt.Point _point) {
        for (int i = 0; i < mGeometry.length-1; i++) {
            if (_point.getX() == mGeometry[i].getX() && _point.getY() == mGeometry[i].getY())
                return true;
        }
        return false;
    }
}