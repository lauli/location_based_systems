package at.fhooe.mc.vis;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

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
    public int mWidth = 600;

    /**
     * height of drawingpanel
     */
    public int mHeight = 350;

    /**
     * last polygon that we created
     */
    public Polygon mPolygon;

    /**
     * last image that we created
     */
    public BufferedImage mImage;

    /**
     * size of house-point-distances that we want to use
     * will change depending on which mouse button was clicked
     */
    public int mHouseSize = 50;

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

    /**
     * creates new polygon, returns it and saves it into global variable mPolygon
     * @param _x    x point where house should start (if Integer.MIN_VALUE, a random point will be created)
     * @param _y    y point where house should start (if Integer.MIN_VALUE, a random point will be created)
     * @return  polygon
     */
    public Polygon createNewPolygon(int _x, int _y) {
        int xRandomNum, yRandomNum;

        if(_x == Integer.MIN_VALUE && _y == Integer.MIN_VALUE) {
            Random r = new Random();
            xRandomNum = 1 + r.nextInt((this.mWidth - 51) + 1);
            yRandomNum = 1 + r.nextInt((this.mHeight - 126) + 1);
        }
        else {
            xRandomNum = _x;
            yRandomNum = _y;
        }

        int size = mHouseSize;
        int[] x = {xRandomNum,     xRandomNum+size,    xRandomNum+size,    xRandomNum,     xRandomNum,         xRandomNum+size, xRandomNum+(size/2),                   xRandomNum,       xRandomNum+size};
        int[] y = {yRandomNum,     yRandomNum,         yRandomNum+size,    yRandomNum,     yRandomNum+size,    yRandomNum+size, yRandomNum+intValue(size*1.5),     yRandomNum+size,  yRandomNum};

        mPolygon = new Polygon(x, y, x.length);
        return mPolygon;
    }

    /**
     * created image that will be shown in drawingpanel
     * calls createNewPolygon to get polygon that should be in image
     * @param _withNewPolygon   if true, than a new polygon position will be created - if false, the old one will be used
     * @param _x    x point where house should start
     * @param _y    y point where house should start
     * @return image
     */
    public BufferedImage createNewImage(boolean _withNewPolygon, int _x, int _y) {
        BufferedImage image = new BufferedImage(this.mWidth, this.mHeight, BufferedImage.TYPE_INT_RGB);

        Graphics graphics   = image.getGraphics();
        graphics.setColor(Color.gray);
        graphics.fillRect(0,0, this.mWidth, this.mHeight);
        graphics.setColor(Color.black);

        if(_withNewPolygon) {
            graphics.drawPolygon(createNewPolygon(_x, _y));
        }
        else {
            graphics.drawPolygon(mPolygon);
        }

        graphics.dispose();
        mImage = image;
        return image;
    }

    /**
     * calls update in GSIView
     * calls createNewImage() with Integer.MIN_Value as its int-params, so that the polygon can be newly positioned depending on the _withNewPolygon param
     * @param _withNewPolygon   true if a randomly positioned polygon should be created or the same as last time
     */
    public void generateRndHome(boolean _withNewPolygon) {
        for(DataOberserver observer : mDataOberservers){
            if (_withNewPolygon) {
                observer.update(createNewImage(true, Integer.MIN_VALUE, Integer.MIN_VALUE));
            }
            else {
                observer.update(createNewImage(false, Integer.MIN_VALUE, Integer.MIN_VALUE));
            }
        }
    }

    /**
     * sets width and height for image
     * calls generateRndHome(false) afterwards to repaint image with new size settings
     * falls will be the param because we want the polygon to stay at its position. just the size of the image shall be changed
     * @param _width    width
     * @param _height   height
     */
    public void setSize(int _width, int _height) {
        this.mHeight = _height;
        this.mWidth = _width;
        this.generateRndHome(false);
    }

    /**
     * calls update in GSIView
     * calls createNewImage() with defined int-params so that the polygon will be positioned exactly where the params are
     * @param _x    x point
     * @param _y    y point
     */
    public void mouseClicked(int _x, int _y) {
        for(DataOberserver observer : mDataOberservers){
            observer.update(createNewImage(true, _x, _y));
        }
    }


}

