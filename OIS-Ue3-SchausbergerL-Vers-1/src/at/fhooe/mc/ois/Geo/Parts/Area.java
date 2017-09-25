package at.fhooe.mc.ois.Geo.Parts;

import at.fhooe.mc.ois.Geo.GeoObjectPart;
import at.fhooe.mc.ois.Matrix;
import at.fhooe.mc.ois.PresentationSchema;

import java.awt.*;
import java.awt.Point;

/**
 * Created by laureenschausberger on 02.05.17.
 */
public class Area extends GeoObjectPart {

    Polygon mGeometry = null;

    public Area(Polygon _geometry) {
        mGeometry = _geometry;
    }

    @Override
    public void draw(Graphics _graphics, Matrix _matrix, PresentationSchema _schema) {
        if (mGeometry != null && _graphics != null && _matrix != null) {

            _graphics.setColor(_schema.getFillColor());
            _graphics.fillPolygon(_matrix.multiply(mGeometry));
            _graphics.setColor(_schema.getLineColor());
            _graphics.drawPolygon(_matrix.multiply(mGeometry));
        }
    }

    @Override
    public Rectangle getBounds() {
        return mGeometry.getBounds();
    }

    @Override
    public boolean contains(Point _point) {
        return mGeometry.contains(_point);
    }
}