package at.fhooe.mc.ois.Geo;

import at.fhooe.mc.ois.Geo.Parts.*;
import at.fhooe.mc.ois.Matrix;
import at.fhooe.mc.ois.PresentationSchema;

import java.awt.*;
import java.awt.Point;

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
    protected GeoObjectPart[] mGeoObjectPart = null;

    /**
     * Konstruktor
     *
     */
    public GeoObject() {
    }

    /**
     * Konstruktor
     *
     * @param _id Die Id des Objektes
     * @param _type Der Typ des Objektes
     * @param _parts Die Geometrie des Objektes
     */
    public GeoObject(String _id, int _type, GeoObjectPart[] _parts) {
        mId = _id;
        mType = _type;
        mGeoObjectPart = _parts;
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
    public GeoObjectPart[] getGeoObject() { return mGeoObjectPart; }

    /**
     * Liefert die Bounding Box der Geometrie
     * @return die Boundin Box der Geometrie als Rechteckobjekt
     * @see java.awt.Rectangle
     */
    public Rectangle getBounds() {
        Rectangle bounds = null;
        //if (mGeoObjectPart != null) {
            for(GeoObjectPart part : mGeoObjectPart){
                if (bounds == null) {
                    bounds = part.getBounds();
                }
                else {
                    bounds = bounds.union(part.getBounds());
                }
            }

            return bounds;
        //}
        //return null;
    }

    /**
     * Gibt die internen Informationen des Geo-Objektes als
     * String zurueck
     * @return Der Inhalt des Objektes in Form eines Strings
     */
    public String toString() {
        return "GeoObject:   id: " + mId + " ;\n \t\t\t\t\t\t\t\t\ttype: " + mType + " ";
    }

    /** *
     Das Geo-Objekt verwendet den uebergebenen Graphik-Kontext, * um sich zu zeichnen
     *
     *
     *
     * @see java.awt.Graphics
     * @see at.fhooe.mc.ois.Matrix
     */
    public void paint(Graphics _g, Matrix _m, PresentationSchema _schema) {
        if (mGeoObjectPart != null && _g != null && _m != null) {
            for(GeoObjectPart part : mGeoObjectPart)
                part.draw(_g, _m, _schema);
        }
    }

    /**
     * schaut ob point innerhalb exisitiert
     * @param _pt
     * @return
     */
    public boolean contains(Point _pt) {

        if (mGeoObjectPart != null) {
            for(GeoObjectPart part : mGeoObjectPart)
                if (part.contains(_pt))
                    return true;
        }
        return false;
    }

    /**
     * return String of GeoObject
     * @return
     */
    public String getGeoObjectString() {
        return "I dnt knw";
    }

}