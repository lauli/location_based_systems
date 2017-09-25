package at.fhooe.mc.ois.Dialog;

import at.fhooe.mc.ois.Dialog.Interfaces.DialogDataObserver;
import at.fhooe.mc.ois.Dialog.Interfaces.DialogViewObserver;
import at.fhooe.mc.ois.DrawingPanel;
import at.fhooe.mc.ois.Geo.GeoObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;


/**
 * Created by laureenschausberger on 27.04.17.
 */
public class DialogView implements DialogDataObserver, DialogViewObserver {

    private DialogController mController = null;
    private DrawingPanel mPanel = null;
    private Frame mFrame = null;
    private TextField mId = null;
    private TextField mType = null;
    private TextField mGeoType = null;
    private List mList = null;
    private List mListDetails = null;
    private Point mBeginPoint = null;


    public DialogView(DialogController _controller, Point _point) {
        mController = _controller;
        mBeginPoint = _point;
    }

    public void setView() {
        // -------------------------- FRAME --------------------------
        mFrame = new Frame();

        mFrame.setMinimumSize(new Dimension(300, 500));
        mFrame.setMaximumSize(new Dimension(300, 500));
        mFrame.setLocation((int) mBeginPoint.getX(), (int) mBeginPoint.getY());

        mFrame.addWindowListener(mController);

        // -------------------------- PANEL & LAYOUT PANEL --------------------------
        mPanel = new DrawingPanel();
        Panel panel = new Panel(new GridLayout(2, 1));
        Panel layoutPanel = new Panel(new GridLayout(1, 2));

        // -------------------------- LEFT PANEL - OBJECTS --------------------------
        mList = new List();
        mList.setMultipleMode(false);
        mList.setBackground(Color.white);
        mList.addItemListener(mController);
        Panel objectsPanel = new Panel(new GridLayout(1,1));
        objectsPanel.add(mList);

        // -------------------------- DATA PANEL --------------------------
        Panel dataPanel = new Panel(new GridLayout(5, 2));
        dataPanel.setBackground(Color.white);

        mId = new TextField("ID");
        mType = new TextField("Type");
        mGeoType = new TextField("GeoType");
        mId.setEnabled(false);
        mType.setEnabled(false);
        mGeoType.setEnabled(false);

        dataPanel.add( new Label("ID:"));
        dataPanel.add(mId);

        dataPanel.add( new Label("Type:"));
        dataPanel.add(mType);

        dataPanel.add( new Label("Geo Type:"));
        dataPanel.add(mGeoType);

        // -------------------------- LIST OF OBJECTS --------------------------
        mListDetails = new List();
        mListDetails.setBackground(Color.lightGray);

        // -------------------------- RIGHT PANEL - DETAILS --------------------------
        Panel detailsPanel = new Panel(new GridLayout(2, 1));
        detailsPanel.add(dataPanel);
        detailsPanel.add(mListDetails);

        layoutPanel.add(objectsPanel);
        layoutPanel.add(detailsPanel);


        panel.add(layoutPanel);
        panel.add(mPanel);

        mFrame.add(panel);
        mFrame.setVisible(true);
    }

    @Override
    public void update(BufferedImage _image) {
        mPanel.setImage(_image);
        mPanel.repaint();
    }

    @Override
    public void close() {
        mFrame.setVisible(false);
        mFrame.dispose();
    }

    @Override
    public Rectangle getDrawBounds() {
        return mPanel.getBounds();
    }

    @Override
    public void fillGeoList(Vector<GeoObject> _geoObjects) {
        for (GeoObject object : _geoObjects) {
            mList.add(object.getId());
        }
    }

    @Override
    public void fillDetails(GeoObject _geoObject) {
        mType.setText(String.valueOf(_geoObject.getType()));
        mId.setText(_geoObject.getId());
        mGeoType.setText(_geoObject.getGeoObjectString());
    }

    @Override
    public int getSelectedIndex() {
        return mList.getSelectedIndex();
    }



}
