package at.fhooe.mc.ois;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;
import at.fhooe.mc.ois.Dialog.Dialog;
import at.fhooe.mc.ois.Geo.GeoObject;

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
     * Dialog Window
     */
    Dialog mDialog = null;

    /**
     * Maßstab Label
     */
    Label mScale = new Label("Maßstab 1 : XX");

    /**
     * Maßstab TextField
     */
    TextField mScaleTextField = null;

    /**
     * constructor
     * adds all panels to our frame
     * panels are created by default values
     * listener are added to frame/panels
     * @param _c
     */
    public GISView(GISController _c) {
        mFrame.setSize(640, 600);

        mFrame.addWindowListener(_c);
        mFrame.addComponentListener(_c);

        Panel panel = new Panel();
        panel.setBackground(Color.orange);

        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(Color.orange);

        // ----------------------------------------------------------- BASICS PANEL

        Button buttonZTF = new Button();
        buttonZTF.setLabel("ZoomToFit");
        buttonZTF.addActionListener(_c);

        Button buttonLoad = new Button();
        buttonLoad.setLabel("Load");
        buttonLoad.addActionListener(_c);

        Button buttonPOIs = new Button();
        buttonPOIs.setLabel("POIs");
        buttonPOIs.addActionListener(_c);


        GridLayout basicLayout = new GridLayout(3,1);
        Panel basicPanel = new Panel();
        basicPanel.setLayout(basicLayout);
        basicPanel.add(buttonLoad);
        basicPanel.add(buttonZTF);
        basicPanel.add(buttonPOIs);

        // ----------------------------------------------------------- ZOOM PANEL

        Button buttonZoomIn = new Button();
        buttonZoomIn.setLabel("+");
        buttonZoomIn.addActionListener(_c);

        Button buttonZoomOut = new Button();
        buttonZoomOut.setLabel("-");
        buttonZoomOut.addActionListener(_c);

        Button buttonRotate = new Button();
        buttonRotate.setLabel("Rotate");
        buttonRotate.addActionListener(_c);

        Panel zoomPanel = new Panel();
        zoomPanel.setBackground(Color.orange);

        GridLayout zoomLayout = new GridLayout(3,1);
        zoomPanel.setLayout(zoomLayout);
        zoomPanel.add(buttonZoomIn);
        zoomPanel.add(buttonZoomOut);
        zoomPanel.add(buttonRotate);

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

        // ----------------------------------------------------------- FIRST PANEL with SCALE

        Panel firstPanel = new Panel();
        GridLayout firstLayout = new GridLayout(3,1);
        firstPanel.setLayout(firstLayout);

        Panel scalePanel = new Panel();
        GridLayout scaleLayout = new GridLayout(1,2);
        scalePanel.setLayout(scaleLayout);

        mScaleTextField = new TextField();
        mScaleTextField.addActionListener(_c);
        mScaleTextField.setText("XXXX");

        Button buttonScale = new Button();
        buttonScale.setLabel("Scale");
        buttonScale.addActionListener(_c);

        scalePanel.add(mScaleTextField);
        scalePanel.add(buttonScale);

        Panel storePanel = new Panel();
        GridLayout storeLayout = new GridLayout(1,2);
        storePanel.setLayout(storeLayout);

        Button buttonSave = new Button();
        buttonSave.setLabel("Store");
        buttonSave.addActionListener(_c);

        Button buttonSticky = new Button();
        buttonSticky.setLabel("Stick");
        buttonSticky.addActionListener(_c);

        storePanel.add(buttonSticky);
        storePanel.add(buttonSave);

        firstPanel.add(mScale);
        firstPanel.add(scalePanel);
        firstPanel.add(storePanel);


        // ----------------------------------------------------------- BUTTON PANEL

        buttonPanel.add(firstPanel, BorderLayout.WEST);
        buttonPanel.add(basicPanel, BorderLayout.CENTER);
        buttonPanel.add(geoPanel, BorderLayout.CENTER);
        buttonPanel.add(zoomPanel, BorderLayout.CENTER);


        // ----------------------------------------------------------- DRAWING PANEL

        BorderLayout borderLayout = new BorderLayout();

        mDrawingPanel.setBackground(Color.cyan);
        mDrawingPanel.addMouseListener(_c);
        mDrawingPanel.addMouseMotionListener(_c);
        mDrawingPanel.addMouseWheelListener(_c);

        // -----------------------------------------------------------  PANEL

        panel.setLayout(borderLayout);
        panel.add(mDrawingPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        mFrame.add(panel);


        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Server");
        menu.add(new MenuItem("Austria"));
        menu.add(new MenuItem("Azores-4236"));
        menu.add(new MenuItem("Azores-3857"));
        menu.add(new MenuItem("Cyprus"));
        menu.add(new MenuItem("DummyServer"));
        menu.addActionListener(_c);
        menuBar.add(menu);
        mFrame.setMenuBar(menuBar);

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


    /**
     * shows Dialog
     * @param _objects
     * @param _p
     */
    @Override
    public void showDialogwithInformation(Vector<GeoObject> _objects, Point _p) {
        if(mDialog == null)
            mDialog = new Dialog(_objects, _p);
        else {
            mDialog.close();
            mDialog = new Dialog(_objects, _p);
        }

    }

    /**
     * updates scale textlabel
     * @param _scale
     */
    @Override
    public void changeScaleTo(int _scale) {
        mScale.setSize(80,20);
        mScale.setText("1 : " + _scale);
        mScale.repaint();
    }

}