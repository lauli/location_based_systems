package at.fhooe.mc.ois.Dialog.Interfaces;

import at.fhooe.mc.ois.Geo.GeoObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Created by laureenschausberger on 27.04.17.
 */
public interface DialogDataObserver {
    void update(BufferedImage _image);
    void close();
    Rectangle getDrawBounds();
    void fillGeoList(Vector<GeoObject> _geoObjects);
    void fillDetails(GeoObject _geoObject);
}
