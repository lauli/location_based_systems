package at.fhooe.mc.ois;

import java.awt.*;
import java.util.Hashtable;

/**
 * Created by laureenschausberger on 21.04.17.
 */
public class DrawingContext {

    /**
     * Color Schema
     */
    private Hashtable<Integer,PresentationSchema> mSchemata = null;

    public DrawingContext() {
        mSchemata = new Hashtable<>();
        mSchemata.put(233, new PresentationSchema(Color.black, Color.white));
        mSchemata.put(931, new PresentationSchema(Color.black, Color.red));
        mSchemata.put(932, new PresentationSchema(Color.red, Color.orange));
        mSchemata.put(1101, new PresentationSchema(Color.green, Color.magenta));
    }
    /**
     * return PresentationSchema where _type matches
     * @param _type
     * @return
     */
    public PresentationSchema getSchema(int _type) {
        if (mSchemata.containsKey(_type)){
            return mSchemata.get(_type);
        }
        if (_type > 1000 && _type < 2000) {
            // highway
            return new PresentationSchema(Color.orange, Color.orange);

        } else if (_type > 2000 && _type < 3000) {
            // waterway
            return new PresentationSchema(Color.blue, Color.blue);

        } else if (_type > 3000 && _type < 4000) {
            // railway
            return new PresentationSchema(Color.red, Color.red);

        } else if (_type > 4000 && _type < 5000) {
            // leisure
            return new PresentationSchema(Color.white, Color.pink);

        } else if (_type > 5000 && _type < 6000) {
            // landuse
            return new PresentationSchema(Color.white, Color.green);

        } else if (_type > 6000 && _type < 7000) {
            // nature
            return new PresentationSchema(Color.white, Color.yellow);

        } else if (_type > 7000 && _type < 8000) {
            // place
            return new PresentationSchema(Color.orange, Color.white);

        } else if (_type > 8000 && _type < 9000) {
            // boundary
            return new PresentationSchema(Color.white, Color.lightGray);

        } else if (_type > 9000 && _type < 10000) {
            // building
            return new PresentationSchema(Color.white, Color.white);

        } else if (_type > 10000) {
            // amenity
            return new PresentationSchema(Color.white, Color.darkGray);
        }
        else {
            // fehler
            return new PresentationSchema(Color.white, Color.black);
        }
    }
}