package at.fhooe.mc.ois;

import at.fhooe.mc.ois.Geo.GeoObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Created by laureenschausberger on 06.04.17.
 */

interface DataOberserver {
    void update(BufferedImage _data);
    void showDialogwithInformation(Vector<GeoObject> _objects, Point _p);
    void changeScaleTo(int _scale);
}
