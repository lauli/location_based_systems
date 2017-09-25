package at.fhooe.mc.ois;

import at.fhooe.mc.ois.Geo.GeoDoublePoint;
import at.fhooe.mc.ois.Geo.GeoObject;
import at.fhooe.mc.ois.Geo.GeoObjectPart;
import at.fhooe.mc.ois.Geo.Parts.*;
import de.intergis.JavaClient.comm.*;
import de.intergis.JavaClient.gui.IgcConnection;
import org.postgis.Geometry;
import org.postgis.LinearRing;
import org.postgis.PGgeometry;
import org.postgresql.PGConnection;
import org.postgresql.util.PGobject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by laureenschausberger on 05.04.17.
 * manages the logical process
 */
public class GISModel extends Panel {

    /**
     * saves all observers that we have included (but in this case we only use one ^^)
     */
    public ArrayList<DataOberserver> mDataOberservers = new ArrayList<>();

    /**
     * width of drawingpanel
     */
    public int mWidth = 640;

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

    /**
     * die Verbindung zum Geo-Server
      */
    CgGeoConnection m_geoConnection  = null;

    /**
     * das Anfrage-Interface des Geo-Servers
      */
    CgGeoInterface m_geoInterface   = null;

    /**
     * Rect das von Mouse gedragged wird -> zoomfenster
     */
     public Rectangle mRectangle;

    /**
     * Verschiebung X Achse
     */
    int mDeltaX = 0;

    /**
     * Verschiebung Y Achse
     */
    int mDeltaY = 0;

    /**
     * die zu zeichnenden POIs
      */
    private Vector<POIObject> mPois = null;

    /**
     * are POIS enabled?
     */
    private boolean mPoisEnabled = true;

    /**
     * mSticky command
     */
    String mSticky = "";

    /**
     * String command for server
     */
    String mMap = "";

    /**
     * Ermittelt die Geo-Objekte, die den Punkt (in Bildschirmkoordinaten) * enthalten
     * @param _pt Ein Selektionspunkt im Bildschirmkoordinatensystem
     * @return Ein Vektor von Geo-Objekte, die den Punkt enthalten
     * @see java.awt.Point
     * @see GeoObject
     */
    public Vector<GeoObject> initSelection(Point _pt) {
        Point pt  = mMatrix.invers().multiply(_pt);

        Vector<GeoObject> result = new Vector<>();
        for (int i = 0 ; i < mPolygons.size() ; i++) {
            GeoObject obj = mPolygons.elementAt(i);
            if (obj.contains(pt))
                result.addElement(obj);
        }
        return result;

    }


    /**
     * Stellt intern eine Transformationsmatrix zur Verfuegung, die so
     * skaliert, verschiebt und spiegelt, dass die zu zeichnenden Polygone
     * innerhalb eines definierten Rechtecks (_mapBounds) komplett in den
     * Anzeigebereich (die Zeichenflaeche) passen
     * @param _mapBounds Der darzustellende Bereich in Welt-Koordinaten
     */
    public void zoomRect(Rectangle _mapBounds) {
        Rectangle window = new Rectangle(0, 0, mWidth, mHeight);
        Rectangle world = mMatrix.invers().multiply(_mapBounds);
        mMatrix = Matrix.zoomToFit(world,window);
        paint();
    }


    /**
     * Liefert zu einem Punkt im Bildschirmkoordinatensystem den passenden
     * Punkt im Kartenkoordinatensystem
     * @param _pt Der umzuwandelnde Punkt im Bildschirmkoordinatensystem
     * @return Der gleiche Punkt im Weltkoordinatensystem
     * @see java.awt.Point
     */
    public Point getMapPoint(Point _pt) {
        return mMatrix.multiply(_pt);
    }


    /**
     * init die verbindung zum server
     * @return obs geklappt hat oder nicht
     */
    public boolean init() {
        try {
            // der Geo-Server wird initialisiert
            m_geoConnection = new IgcConnection(new CgConnection("admin",
                            "admin",
                            "T:54.229.208.75:4949",
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
                    GeoObject geo = new GeoObject(obj.getName(), obj.getCategory(), new GeoObjectPart[] {new Area(poly)});
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
        c.addView(v);
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
        //if (_poly != null) {
            for (GeoObject geoObject : _poly) {
                if (bounds == null) {
                    bounds = geoObject.getBounds();
                } else {
                    bounds.add(geoObject.getBounds());
                }
            }
        //}


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
     * Veraendert die interne Transformationsmatrix so, dass
     * die zu zeichnenden Objekt verschoben werden. *
     */
    public void scroll(int _dx, int _dy) {
        mDeltaX = _dx;
        mDeltaY = _dy;

        BufferedImage image = mImage;

        Graphics graphics   = image.getGraphics();


        graphics.copyArea(0,0, mWidth, mHeight, mDeltaX, mDeltaY);

        if (mDeltaX < 0) {
            graphics.clearRect(mWidth + mDeltaX, 0, -mDeltaX, mHeight);
        } else {
            graphics.clearRect(0, 0, mDeltaX, mHeight);
        }

        if (mDeltaY < 0) {
            graphics.clearRect(0, mHeight+ mDeltaY, mWidth, -mDeltaY);
        } else {
            graphics.clearRect(0, 0, mWidth, mDeltaY);
        }

        graphics.dispose();

        for(DataOberserver observer : mDataOberservers){
            observer.update(image);
            observer.changeScaleTo(calculateScale());
        }
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

        for(int i = 0; i < mPolygons.size(); i++){
            DrawingContext drawingContext = new DrawingContext();
            // fill
            graphics.setColor(drawingContext.getSchema(mPolygons.elementAt(i).getType()).getFillColor());
            mPolygons.elementAt(i).paint(graphics, mMatrix, drawingContext.getSchema(mPolygons.elementAt(i).getType()));
            // line
            //graphics.setColor(drawingContext.getSchema(mPolygons.elementAt(i).getType()).getLineColor());
           // graphics.drawPolygon(mMatrix.multiply(mPolygons.elementAt(i).getGeoObject()));
        }

        if( mPois != null ) {
            for (int i = 0; i < mPois.size() - 1; i++) {
                mPois.elementAt(i).paint(graphics, mMatrix);
            }
        }

        // Zoom Rect
        if(mRectangle != null){
            graphics.setXORMode(Color.blue);
            graphics.fillRect((int) mRectangle.getX(), (int) mRectangle.getY(), (int) mRectangle.getWidth(), (int) mRectangle.getHeight());
            graphics.setPaintMode();
        }

        graphics.dispose();
        mImage = image;

        for(DataOberserver observer : mDataOberservers){
            observer.update(mImage);
            observer.changeScaleTo(calculateScale());
        }
    }

    /**
     * zeichnet Rectangle wenn Maus gedrückt gehalten wird zum zoomen
     */
    public void drawRect(Rectangle _rectangle){
        mRectangle = _rectangle;
        paint();
    }

    /**
     * mRectangle = null
     */
    public void setRectToNull(){
        mRectangle = null;
        paint();
    }

    /**
     * loads given data into vector
     */
    public void connectDummyServer () {
        if (init()) {
            mPolygons = extractData("SELECT * FROM data WHERE type in (233, 931, 932, 933, 934, 1101)");
        }
    }

    /**
     * loads given data into vector
     */
    public void loadData() {
        if ( mPolygons == null )
            connectDummyServer();

        if(mPoisEnabled)
            setupPOI();
        else
            mPois = null;

        zoomToFit();
    }

    /**
     * sets mPoisEnabled
     */
    public void setPoisTo(boolean _set) {
        mPoisEnabled = _set;
        loadData();
    }

    /**
     * gets mPoisEnabled
     */
    public boolean getCurrentPOIBool() {
        return mPoisEnabled;
    }

    /**
     * setup Clipboard with _text
     * @param _text
     */
    public void setClipboard(String _text) {
        try {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable t = cb.getContents(this);
            String clip = (String) t.getTransferData(DataFlavor.stringFlavor);
            clip += "\n";

            addToClipboard(clip + _text);

        } catch (Exception _e) {
            System.out.println("FEHLER bei setClipboard.");
            _e.printStackTrace();
        }
    }

    /**
     * adds _text to Clipboard
     * @param _text
     */
    public void addToClipboard(String _text) {
        try {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(new StringSelection(_text), new StringSelection( _text));
        } catch (java.security.AccessControlException sec_e) {
            System.out.println("FEHLER bei addToClipboard.");
        } catch (Exception _e) {
            System.out.println("FEHLER bei copyToClipboard.");
            _e.printStackTrace();
        }

    }

    /**
     * returns points as string
     * @param _pt
     * @return
     */
    public String getClipboardString(Point _pt) {
        return _pt.x + " " + _pt.y;
    }

    /**
     * shows Dialog at _clickedPoint
     * @param _clickedPoint
     */
    public void showDialogIn(Point _clickedPoint) {
        Vector<GeoObject> objects = this.initSelection(_clickedPoint);
        for(DataOberserver observer : mDataOberservers){
            observer.showDialogwithInformation(objects, _clickedPoint);
        }
    }

    /**
     * Berechnet den gerade sichtbaren Massstab der Karte
     * @return der Darstellungsmassstab
     * @see Matrix
     */
    protected int calculateScale() {
        GeoDoublePoint vector             = new GeoDoublePoint(0,1.0);
        GeoDoublePoint vector_transformed = mMatrix.multiply(vector);

        double cmPerInch	= 2.54;
        double dotPerInch   = 72;
        double dotPerCm 	= dotPerInch / cmPerInch;

        double length = 1.0 / vector_transformed.length();
        double newCanvasScale = length * dotPerCm;

        System.out.println("Maßstab	--> " + newCanvasScale);
        return (int)newCanvasScale;

    }

    /**
     * setups POI Objects
     */
    public void setupPOI() {
        if (mPois == null) {
            mPois = new Vector();
            Image img = null;
            MediaTracker tracker = new MediaTracker(this);
            InputStream in = getClass().getResourceAsStream("groot2.png");
            if (in == null) {
                System.err.println("groot2.png nt fnd");
            } else {
                byte[] buffer = null;
                try {
                    buffer = new byte[in.available()];
                    in.read(buffer);
                    img = Toolkit.getDefaultToolkit().createImage(buffer);
                    tracker.addImage(img,1);
                    tracker.waitForAll();
                } catch (Throwable _e) {
                    _e.printStackTrace();
                }
            }
            mPois.addElement(new POIObject("001", 666, img, new Point(54037055,580450444)));
            mPois.addElement(new POIObject("002", 666, img, new Point(54038387,580448390)));
            mPois.addElement(new POIObject("003", 666, img, new Point(54036052,580450000)));
            mPois.addElement(new POIObject("004", 666, img, new Point(54039065,580450022)));
            mPois.addElement(new POIObject("005", 666, img, new Point(54040001,580450144)));
        } else {
            mPois = null;
        }
    }

    /**
     * loads data from server which name is _name
     * @param _name
     * @return
     */
    public void connectTo(String _name) {
        Connection connection = getConnection("/osm" + _name);
        mPolygons = null;
        if(_name == "") {
            //Austria
            mPolygons = createQuery(connection, "SELECT * FROM bundeslaender " + mSticky);
            mPolygons.addAll(createQuery(connection, "SELECT * FROM gemeinden " + mSticky));
        }
        else {

            mPolygons = createQuery(connection, "SELECT * FROM osm_natural " + mSticky);
            Vector<GeoObject> landuse = createQuery(connection, "SELECT * FROM osm_landuse " + mSticky);
            Vector<GeoObject> highway = createQuery(connection, "SELECT * FROM osm_highway " + mSticky);
            Vector<GeoObject> amenity = createQuery(connection, "SELECT * FROM osm_amenity " + mSticky);
            Vector<GeoObject> boundary = createQuery(connection, "SELECT * FROM osm_boundary " + mSticky);
            Vector<GeoObject> building = createQuery(connection, "SELECT * FROM osm_building " + mSticky);
            Vector<GeoObject> leisure = createQuery(connection, "SELECT * FROM osm_leisure " + mSticky);
            Vector<GeoObject> place = createQuery(connection, "SELECT * FROM osm_place " + mSticky);
            Vector<GeoObject> waterway = createQuery(connection, "SELECT * FROM osm_waterway " + mSticky);
            if(boundary != null)
                mPolygons.addAll(boundary);
            if(place != null)
                mPolygons.addAll(place);
            if(landuse != null)
                mPolygons.addAll(landuse);
            if(leisure != null)
                mPolygons.addAll(leisure);
            if(highway != null)
                mPolygons.addAll(highway);
            if(amenity != null)
                mPolygons.addAll(amenity);
            if(waterway != null)
                mPolygons.addAll(waterway);
            if(building != null)
                mPolygons.addAll(building);


        }

        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * connects to server which name is _name
     * @param _name
     * @return
     */
    public Connection getConnection(String _name) {
        Connection conn;
        try {
            /* Load the JDBC driver and establish a connection. */
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432" + _name;
            conn = DriverManager.getConnection(url, "geo", "geo");

            /* Add the geometry types to the connection. */
            PGConnection c = (PGConnection) conn;
            c.addDataType("geometry", (Class<? extends PGobject>) Class.forName("org.postgis.PGgeometry"));
            c.addDataType("box2d", (Class<? extends PGobject>) Class.forName("org.postgis.PGbox2d"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    /**
     * creates Query
     * @param _connection
     * @param _query
     * @return
     */
    public Vector<GeoObject> createQuery(Connection _connection, String _query) {
        /* Create a statement and execute a select query. */
        try {
            Statement s = _connection.createStatement();
            ResultSet r = s.executeQuery(_query);

            Vector<GeoObject> objects = turnIntoGeoObjects(r);

            s.close();
            return  objects;
        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * turns ResultSet into Vector<GeoObjects>s
     * @param _resultSet
     * @return
     */
    public Vector<GeoObject> turnIntoGeoObjects(ResultSet _resultSet) {
        Vector<GeoObject> objects = new Vector<>();

        try {
            while (_resultSet.next()) {
                String id = _resultSet.getString("id");
                int type = _resultSet.getInt("type");
                PGgeometry geom = (PGgeometry) _resultSet.getObject("geom");


                switch(geom.getGeoType()) {
                    case Geometry.POLYGON : {
                        String wkt = geom.toString();
                        org.postgis.Polygon p = new org.postgis.Polygon(wkt);
                        if (p.numRings() >= 1) {
                            Polygon poly = new Polygon();
                            // Ring 0 --> main polygon ... rest should be holes
                            LinearRing ring = p.getRing(0);
                            for (int i = 0; i < ring.numPoints(); i++) {
                                org.postgis.Point pPG = ring.getPoint(i);
                                poly.addPoint((int) pPG.x, (int) pPG.y);
                            }
                            objects.add(new GeoObject(id, type, new GeoObjectPart[] {new Area(poly)}));
                        }
                    }
                    break;
                    case Geometry.MULTIPOLYGON : {
                        String wkt = geom.toString();
                        org.postgis.MultiPolygon p = new org.postgis.MultiPolygon(wkt);
                        if (p.getPolygons().length >= 1) {
                            for (int j = 0; j < p.getPolygons().length-1; j++) {
                                Polygon poly = new Polygon();
                                // Ring 0 --> main polygon ... rest should be holes
                                LinearRing ring = p.getPolygon(j).getRing(0);
                                for (int i = 0; i < ring.numPoints(); i++) {
                                    org.postgis.Point pPG = ring.getPoint(i);
                                    poly.addPoint((int) pPG.x, (int) pPG.y);
                                }
                                objects.add(new GeoObject(id, type, new GeoObjectPart[] {new Area(poly)}));
                            }
                        }
                    }
                    break;
                    case Geometry.POINT : {
                        String wkt = geom.toString();
                        org.postgis.Point p = new org.postgis.Point(wkt);
                        Point point = new Point((int) p.getX(), (int) p.getY());
                        objects.add(new GeoObject(id, type, new GeoObjectPart[] {new at.fhooe.mc.ois.Geo.Parts.Point(point)}));
                    }
                    break;
                    case Geometry.MULTIPOINT : {
                        String wkt = geom.toString();
                        org.postgis.MultiPoint points = new org.postgis.MultiPoint(wkt);
                        for(org.postgis.Point p : points.getPoints()){
                            Point point = new Point((int) p.getX(), (int) p.getY());
                            objects.add(new GeoObject(id, type, new GeoObjectPart[] {new at.fhooe.mc.ois.Geo.Parts.Point(point)}));
                        }
                    }
                    break;
                    case Geometry.LINESTRING : {
                        String wkt = geom.toString();
                        org.postgis.LineString line = new org.postgis.LineString(wkt);
                        Point[] points = new Point[line.getPoints().length];
                        int i = 0;
                        for(org.postgis.Point p : line.getPoints()){
                            Point point = new Point((int) p.getX(), (int) p.getY());
                            points[i] = point;
                            i++;
                        }
                        objects.add(new GeoObject(id, type, new GeoObjectPart[] {new at.fhooe.mc.ois.Geo.Parts.Line(points)}));
                    }
                    break;
                    case Geometry.MULTILINESTRING : {
                        String wkt = geom.toString();
                        org.postgis.MultiLineString lines = new org.postgis.MultiLineString(wkt);
                        for(org.postgis.LineString line : lines.getLines()) {
                            Point[] points = new Point[line.getPoints().length];
                            int i = 0;
                            for (org.postgis.Point p : line.getPoints()) {
                                Point point = new Point((int) p.getX(), (int) p.getY());
                                points[i] = point;
                                i++;
                            }
                            objects.add(new GeoObject(id, type, new GeoObjectPart[]{new at.fhooe.mc.ois.Geo.Parts.Line(points)}));
                        }
                    }
                    break;
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return objects;
    }


    /**
     * saves current image
     */
    public void save() {
        File file = new File("bild.png");
        try {
            ImageIO.write(mImage, "png", file);
        } catch (IOException _e) {
            _e.printStackTrace();
        }
    }

    /**
     * zooms window to given scale value
     */
    public void scale(String _scale){
        if (_scale.toString() == "")
            return;
        try {
            int scale = Integer.parseInt(_scale);
            int currentScale = calculateScale();
            double newScale  = (double) currentScale / (double) scale;
            Rectangle window = new Rectangle(0, 0, mWidth, mHeight);
            zoom(new Point((int) window.getCenterX(), (int) window.getCenterY()), newScale);
        } catch(ClassCastException _exc) {
            _exc.printStackTrace();
        }
    }

    /**
     * changes mPolygons to ones that are currently shown
     */
    public void sticky() {
        if (mSticky == "") {
            Rectangle bounds = new Rectangle(getMapPoint(new Point(0,0)));
            bounds.add(getMapPoint(new Point(mWidth, mHeight)));
            mSticky = "AS a WHERE a.geom && ST_MakeEnvelope(" + bounds.x + ", " + bounds.y + ", " + (bounds.x + bounds.width) + ", " + (bounds.y + bounds.height) + ");";
            mMatrix = null;
            mPolygons = null;
            connectTo(mMap);
            loadData();
        }
        else {
            mSticky = "";
        }
    }

}

