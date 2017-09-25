package at.fhooe.mc.ois;

import at.fhooe.mc.ois.Geo.GeoObject;

import java.awt.*;

/**
 * Created by laureenschausberger on 01.05.17.
 */
public class POIObject extends GeoObject {

    /**
     * objects geometry
     */
    private Image m_image = null;

    /**
     * objects start point
     */
    private Point m_point = null;

    public POIObject(String _id, int _type, Image _image, Point _pt) {
        mId = _id;
        mType = _type;

        m_image = _image;
        m_point = _pt;
    }

    /**
     * returned m_image
     */
    public Image getImage() { return m_image; }

    /**
     * draws object
     */
    public void paint(Graphics _g, Matrix _matrix) {
        if (_g != null && _matrix != null) {
            Point pt = _matrix.multiply(m_point);
            _g.drawImage(m_image, pt.x, pt.y, new Panel());
        }
    }
}