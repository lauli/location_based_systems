package at.fhooe.mc.ois;

import java.awt.*;

/**
 * Created by laureenschausberger on 19.04.17.
 */
public class GeoObject {
    /**
     * id
     */
    protected String mId = null;

    /**
     * typ
     */
    protected int mType = 0;

    /**
     * polygon
     */
    protected Polygon mPolygon = null;

    /**
     * Konstruktor
     *
     * @param _id Die Id des Objektes
     * @param _type Der Typ des Objektes
     * @param _poly Die Geometrie des Objektes
     */
    public GeoObject(String _id, int _type, Polygon _poly) {
        mId = _id;
        mType = _type;
        mPolygon = _poly;
    }

    /**
     * Liefert die Id des Geo-Objektes
     * @return Die Id des Objektes
     * @see java.lang.String
     */
    public String getId()   { return mId; }

    /**
     * Liefert den Typ des Geo-Objektes
     * @return Der Typ des Objektes
     */
    public int getType() { return mType; }

    /**
     * Liefert die Geometrie des Geo-Objektes
     * @return Die Geometrie des Objektes
     * @see java.awt.Polygon
     */
    public Polygon getPoly() { return mPolygon; }

    /**
     * Liefert die Bounding Box der Geometrie
     * @return die Boundin Box der Geometrie als Rechteckobjekt
     * @see java.awt.Rectangle
     */
    public Rectangle getBounds() {
        if (mPolygon != null) {
            return mPolygon.getBoundingBox();
        }
        return null;
    }

    /**
     * Gibt die internen Informationen des Geo-Objektes als
     * String zurueck
     * @return Der Inhalt des Objektes in Form eines Strings
     */
    public String toString() {
        return "GeoObject: [ " + mId + " ;\t " + mType + "\t ]";
    }

    /** *
     Das Geo-Objekt verwendet den uebergebenen Graphik-Kontext, * um sich zu zeichnen
     *
     *
     *
     * @see java.awt.Graphics
     * @see at.fhooe.mc.ois.Matrix
     */
    public void paint(Graphics _g, Matrix _m) {
        if (mPolygon != null && _g != null && _m != null) {
            Polygon temp = _m.multiply(mPolygon);
            for (int i = 0 ; i < temp.npoints ; i++) {
                System.out.println(i + ". point (" + temp.xpoints[i] + ";" + temp.ypoints[i] + ")");
            }
            _g.drawPolygon(temp);
        }
    }

}