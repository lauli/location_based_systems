package at.fhooe.mc.vis;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by laureenschausberger on 05.04.17.
 * Object in which the Image will be drawn.
 */
public class DrawingPanel extends Panel {
    /**
     * Image that will be shown in DrawingPanel, should include Polygon
     */
    BufferedImage mImage;

    /**
     * sets global variable mImage to param
     * @param _image
     */
    public void setImage(BufferedImage _image){
        mImage = _image;
    }

    /**
     * overriding paint method to draw image
     * @param _g
     */
    @Override
    public void paint(Graphics _g) {
        if(mImage != null){
            _g.drawImage(mImage,0,0,null);
        }
    }
}
