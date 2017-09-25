package at.fhooe.mc.ois;

import de.intergis.JavaClient.comm.*;
import de.intergis.JavaClient.gui.IgcConnection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import static oracle.jrockit.jfr.events.Bits.intValue;

/**
 * Created by laureenschausberger on 05.04.17.
 * manages the logical process
 */
public class GISModel {

    /**
     * saves all observers that we have included (but in this case we only use one ^^)
     */
    public ArrayList<DataOberserver> mDataOberservers = new ArrayList<>();

    /**
     * width of drawingpanel
     */
    public int mWidth = 800;

    /**
     * height of drawingpanel
     */
    public int mHeight = 480;

    /**
     * transformed poly
     */
    public Vector<GeoObject> mPolygons;


    /**
     * transformed matrix
     */
    public Matrix mMatrix;

    /**
     * last image that we created
     */
    public BufferedImage mImage;

    // die Verbindung zum Geo-Server
    CgGeoConnection m_geoConnection  = null;
    // das Anfrage-Interface des Geo-Servers
    CgGeoInterface m_geoInterface   = null;

    public boolean init() {
        try {
            // der Geo-Server wird initialisiert
            m_geoConnection = new IgcConnection(new CgConnection("admin",
                            "admin",
                            "T:10.29.18.166:4949",
                            null));
            // das Anfrage-Interface des Servers wird abgeholt
            m_geoInterface = m_geoConnection.getInterface();
            return true;
        } catch (Exception _e) {_e.printStackTrace();}
        return false;
    }

    /**
     * Extrahiert einige Geoobjekte aus dem Server
     */
    protected Vector extractData(String _stmt) {
        try {
            CgStatement stmt            = m_geoInterface.Execute(_stmt);
            CgResultSet cursor          = stmt.getCursor();
            Vector      objectContainer = new Vector();
            while (cursor.next()) {
                CgIGeoObject obj = cursor.getObject();
                System.out.println("NAME --> " + obj.getName());
                System.out.println("TYP  --> " + obj.getCategory());
                CgIGeoPart[] parts = obj.getParts();
                for (int i = 0 ; i < parts.length ; i++){
                    System.out.println("PART " + i);
                    int   pointCount = parts[i].getPointCount();
                    int[] xArray     = parts[i].getX();
                    int[] yArray     = parts[i].getY();
                    Polygon poly = new Polygon(xArray, yArray, pointCount);
                    for (int j = 0 ; j < pointCount ; j++) {
                        System.out.println("[" + xArray[j] + " ; " + yArray[j] + "]");
                    } // for j
                    GeoObject geo = new GeoObject(obj.getName(), obj.getCategory(), poly);
                    objectContainer.addElement(geo);
                } // for i
                System.out.println();
            } // while cursor
            return objectContainer;
        } catch (Exception _e) { _e.printStackTrace(); }
        return null;
    }


    public static void main (String[] _argv) {
        GISModel      m = new GISModel();
        GISController c = new GISController(m);
        GISView       v = new GISView(c);
        m.addObserver(v);
    }

    /**
     * adds an observer to our mDataObserver Arraylist
     * @param _v
     */
    private void addObserver(DataOberserver _v) {
        mDataOberservers.add(_v);
    }


    public GISModel() {
        mMatrix = new Matrix();
    }
    /**
     * Stellt intern eine Transformationsmatrix zur Verfuegung, die so
     * skaliert, verschiebt und spiegelt, dass die zu zeichnenden Polygone
     * komplett in den Anzeigebereich passen
     */
    public void zoomToFit() {
        Rectangle window = new Rectangle(0, 0, mWidth, mHeight);
        Rectangle world = getMapBounds(mPolygons);
        mMatrix = Matrix.zoomToFit(world,window);
        paint();
    }

    /**
     * Stellt intern eine Transformationsmatrix zur Verfuegung, die so
     * rotiert ist um 180 grad
     */
    public void rotate(double _alpha) {
        Rectangle window = new Rectangle(0, 0, mWidth, mHeight);
        Rectangle world = getMapBounds(mPolygons);

        Matrix translate    = Matrix.translate(-window.getCenterX(), -window.getCenterY());

        Matrix rotate        = Matrix.rotate(_alpha);

        Matrix mirrorX      = Matrix.mirrorX();
        Matrix trans        = Matrix.translate(window.getCenterX(), window.getCenterY());

        mMatrix = trans.multiply(mirrorX.multiply(rotate.multiply(translate))).multiply(mMatrix);

        paint();
    }


    /**
     * Veraendert die interne Transformationsmatrix so, dass in das
     * Zentrum des Anzeigebereiches herein- bzw. herausgezoomt wird *
     * @param _factor Der Faktor um den herein- bzw. herausgezoomt wird */
    public void zoom(double _factor) {
        mMatrix = Matrix.zoomPoint(mMatrix, new Point(mWidth/2, mHeight/2), _factor);
        paint();
    }

    /**
     * Veraendert die interne Transformationsmatrix so, dass an dem
     * uebergebenen Punkt herein- bzw. herausgezoomt wird *
     * @param _pt Der Punkt an dem herein- bzw. herausgezoomt wird
     * @param _factor Der Faktor um den herein- bzw. herausgezoomt wird */
    public void zoom(Point _pt, double _factor) {
        mMatrix = Matrix.zoomPoint(mMatrix, _pt, _factor);
        paint();
    }

    /** * * * *
     Ermittelt die gemeinsame BoundingBox der uebergebenen Polygone
     @param _poly Die Polygone, fuer die die BoundingBox berechnet werden soll
      * @return Die BoundingBox
     */
    public Rectangle getMapBounds(Vector<GeoObject> _poly) {
        Rectangle bounds = null;
        if (_poly != null) {
            for (int i = 0 ; i < _poly.size() ; i++) {
                if (bounds == null) {
                    bounds = (_poly.elementAt(i)).getBounds();
                } else {
                    bounds = bounds.union((_poly.elementAt(i)).getBounds());
                }
            }
        }

        return bounds;
    }

    /**
     * Veraendert die interne Transformationsmatrix so, dass
     * die zu zeichnenden Objekt horizontal verschoben werden. *
     * @param _delta Die Strecke, um die verschoben werden soll */
    public void scrollHorizontal(int _delta) {
        Point p = new Point(_delta, 0);
        mMatrix = Matrix.translate(p).multiply(mMatrix);
        paint();
    }
    /**
     * Veraendert die interne Transformationsmatrix so, dass
     * die zu zeichnenden Objekt horizontal verschoben werden. *
     * @param _delta Die Strecke, um die verschoben werden soll */
    public void scrollVertical(int _delta) {
        Point p = new Point(0, _delta);
        mMatrix = Matrix.translate(p).multiply(mMatrix);
        paint();
    }

    /**
     * created image that will be shown in drawingpanel
     * @return image
     */
    public void paint() {
        BufferedImage image = new BufferedImage(this.mWidth, this.mHeight, BufferedImage.TYPE_INT_RGB);

        Graphics graphics   = image.getGraphics();
        graphics.setColor(Color.gray);
        graphics.fillRect(0,0, this.mWidth, this.mHeight);
        graphics.setColor(Color.black);

        for(int i = 0; i < mPolygons.size(); i++){
            graphics.drawPolygon(mMatrix.multiply(mPolygons.elementAt(i).getPoly()));
        }

        graphics.dispose();
        mImage = image;

        for(DataOberserver observer : mDataOberservers){
            observer.update(mImage);
        }
    }

    /**
     * loads given data into vector
     */
    public void loadData() {
        if (init()) {
            mPolygons = extractData("SELECT * FROM data WHERE type in (233, 931, 932, 933, 934, 1101)");
        }
        zoomToFit();
    }
}

