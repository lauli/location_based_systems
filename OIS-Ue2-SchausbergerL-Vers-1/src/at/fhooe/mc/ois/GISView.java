package at.fhooe.mc.ois;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.GridLayout.*;

/**
 * Created by laureenschausberger on 05.04.17.
 */
public class GISView implements DataOberserver {

    /**
     * drawing panel in which the image will be shown
     */
    DrawingPanel mDrawingPanel = new DrawingPanel();

    /**
     * window frame in which de drawing panel and a buttonpanel lay
     */
    Frame mFrame = new Frame();

    /**
     * constructor
     * adds all panels to our frame
     * panels are created by default values
     * listener are added to frame/panels
     * @param _c
     */
    public GISView(GISController _c) {
        mFrame.setSize(800, 600);

        mFrame.addWindowListener(_c);
        mFrame.addComponentListener(_c);

        Panel panel = new Panel();
        panel.setBackground(Color.orange);

        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(Color.orange);

        Button buttonZTF = new Button();
        buttonZTF.setLabel("ZoomToFit");
        buttonZTF.addActionListener(_c);

        Button buttonLoad = new Button();
        buttonLoad.setLabel("Load");
        buttonLoad.addActionListener(_c);

        Button buttonRotate = new Button();
        buttonRotate.setLabel("Rotate");
        buttonRotate.addActionListener(_c);

        // ----------------------------------------------------------- ZOOM PANEL

        Button buttonZoomIn = new Button();
        buttonZoomIn.setLabel("+");
        buttonZoomIn.addActionListener(_c);

        Button buttonZoomOut = new Button();
        buttonZoomOut.setLabel("-");
        buttonZoomOut.addActionListener(_c);

        Panel zoomPanel = new Panel();
        zoomPanel.setBackground(Color.orange);

        GridLayout zoomLayout = new GridLayout(2,1);
        zoomPanel.setLayout(zoomLayout);
        zoomPanel.add(buttonZoomIn);
        zoomPanel.add(buttonZoomOut);

        // ----------------------------------------------------------- GEO PANEL

        Button buttonNorth = new Button();
        buttonNorth.setLabel("N");
        buttonNorth.addActionListener(_c);

        Button buttonEast = new Button();
        buttonEast.setLabel("E");
        buttonEast.addActionListener(_c);

        Button buttonSouth = new Button();
        buttonSouth.setLabel("S");
        buttonSouth.addActionListener(_c);

        Button buttonWest = new Button();
        buttonWest.setLabel("W");
        buttonWest.addActionListener(_c);

        Panel geoPanel = new Panel();
        geoPanel.setBackground(Color.orange);

        GridLayout geoLayoutEastWest = new GridLayout(1,2);
        Panel geoPanelEastWest = new Panel();
        geoPanelEastWest.setLayout(geoLayoutEastWest);
        geoPanelEastWest.add(buttonWest);
        geoPanelEastWest.add(buttonEast);

        GridLayout geoLayout = new GridLayout(3,1);
        geoPanel.setLayout(geoLayout);
        geoPanel.add(buttonNorth);
        geoPanel.add(geoPanelEastWest);
        geoPanel.add(buttonSouth);

        // ----------------------------------------------------------- BUTTON PANEL

        buttonPanel.add(buttonLoad, BorderLayout.CENTER);
        buttonPanel.add(buttonZTF, BorderLayout.CENTER);
        buttonPanel.add(geoPanel, BorderLayout.CENTER);
        buttonPanel.add(zoomPanel, BorderLayout.CENTER);
        buttonPanel.add(buttonRotate, BorderLayout.CENTER);

        BorderLayout borderLayout = new BorderLayout();

        mDrawingPanel.setBackground(Color.cyan);
        //mDrawingPanel.setSize(800, 400);
        mDrawingPanel.addMouseListener(_c);

        panel.setLayout(borderLayout);
        panel.add(mDrawingPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);


        mFrame.add(panel);
        mFrame.setVisible(true);
    }


    /**
     * update method which will be called in GISModel whenever the observers have registered some changes
     * calls setImage and repaint for/in our DrawingPanel
     * @param _data
     */
    @Override
    public void update(BufferedImage _data) {
        mDrawingPanel.setImage(_data);
        mDrawingPanel.repaint();
    }
}