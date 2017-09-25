package at.fhooe.mc.ois.views;

import at.fhooe.mc.ois.NMEAInfo;

import java.awt.*;
import java.net.URL;

/**
 * Created by laureenschausberger on 05.05.17.
 */
public class DeviationView extends PositionUpdateListener{

    /**
     * constructor
     */
    public DeviationView() {
        this.setLayout(new GridLayout(1, 1));
        this.add(new AWTImageComponent());
    }

    /**
     * should update everything..
     * @param _info
     */
    @Override
    public void update(NMEAInfo _info) {
    }
}

class AWTImageComponent extends Panel
{
    /**
     * mImage to be shown
     */
    Image mImage;

    /**
     * constructor
     */
    public AWTImageComponent() {
        loadImage();
    }

    /**
     * draws mImage
     * @param _graphics
     */
    public void paint(Graphics _graphics) {
        super.paint(_graphics);
        int w = getWidth();
        int h = getHeight();
        int imageWidth = mImage.getWidth(this);
        int imageHeight = mImage.getHeight(this);
        int x = (w - imageWidth)/2;
        int y = (h - imageHeight)/2;
        _graphics.drawImage(mImage, x, y, this);
    }

    /**
     * For the ScrollPane or other Container.
     */
    public Dimension getPreferredSize() {
        return new Dimension(mImage.getWidth(this), mImage.getHeight(this));
    }

    /**
     * loads image into mImage
     */
    private void loadImage() {
        String fileName = "/at/fhooe/mc/ois/werbung.png";
        URL url = getClass().getResource(fileName);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        mImage = toolkit.getImage(url);
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(mImage, 0);
        try {
            tracker.waitForID(0);
        } catch(InterruptedException ie) {
            System.out.println("interrupt: " + ie.getMessage());
        }
    }
}
