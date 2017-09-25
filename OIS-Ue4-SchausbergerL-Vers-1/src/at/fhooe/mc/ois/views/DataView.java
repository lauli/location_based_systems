package at.fhooe.mc.ois.views;

import at.fhooe.mc.ois.NMEAInfo;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by laureenschausberger on 04.05.17.
 */
public class DataView extends PositionUpdateListener {

    /**
     * is shown instead of infos when no infos are available
     */
    private String mErrorMessage    = "Not available..";

    /**
     * time
     */
    private Label mTimeText         = new Label(mErrorMessage);

    /**
     * infos about latitude
     */
    private Label mLatText          = new Label(mErrorMessage);

    /**
     * infos about longitude
     */
    private Label mLongText         = new Label(mErrorMessage);

    /**
     * infos about height
     */
    private Label mHeightText       = new Label(mErrorMessage);

    /**
     * infos about used/available satellites
     */
    private Label mSatellitesText   = new Label(mErrorMessage);

    /**
     * infos about PDOP
     */
    private Label mPDOPText         = new Label(mErrorMessage);

    /**
     * infos about HDOP
     */
    private Label mHDOPText         = new Label(mErrorMessage);

    /**
     * infos about HDOP
     */
    private Label mVDOPText         = new Label(mErrorMessage);


    /**
     * constructor
     */
    public DataView() {
        this.setLayout(new GridLayout(8, 1));


        Panel timePanel = new Panel();
        timePanel.setLayout(new FlowLayout());
        timePanel.add(mTimeText);
        this.add(timePanel);

        Panel latPanel = new Panel();
        latPanel.setLayout(new FlowLayout());
        Label labLabel = new Label("Latitude: ");
        latPanel.add(labLabel);
        latPanel.add(mLatText);
        this.add(latPanel);

        Panel longPanel = new Panel();
        longPanel.setLayout(new FlowLayout());
        Label longLabel = new Label("Longitude: ");
        longPanel.add(longLabel);
        longPanel.add(mLongText);
        this.add(longPanel);

        Panel heightpanel = new Panel();
        heightpanel.setLayout(new FlowLayout());
        Label heightLabel = new Label("Height: ");
        heightpanel.add(heightLabel);
        heightpanel.add(mHeightText);
        this.add(heightpanel);


        Panel PDOPPanel = new Panel();
        PDOPPanel.setLayout(new FlowLayout());
        Label PDOPLabel = new Label("PDOP: ");
        PDOPPanel.add(PDOPLabel);
        PDOPPanel.add(mPDOPText);
        this.add(PDOPPanel);

        Panel HDOPPanel = new Panel();
        HDOPPanel.setLayout(new FlowLayout());
        Label HDOPLabel = new Label("HDOP: ");
        HDOPPanel.add(HDOPLabel);
        HDOPPanel.add(mHDOPText);
        this.add(HDOPPanel);

        Panel VDOPPanel = new Panel();
        VDOPPanel.setLayout(new FlowLayout());
        Label VDOPLabel = new Label("VDOP: ");
        VDOPPanel.add(VDOPLabel);
        VDOPPanel.add(mVDOPText);
        this.add(VDOPPanel);

        Panel satellitesPanel = new Panel();
        satellitesPanel.setLayout(new FlowLayout());
        Label satellitesLabel = new Label("Used Satellites:");
        satellitesPanel.add(satellitesLabel);
        satellitesPanel.add(mSatellitesText);
        this.add(satellitesPanel);
    }

    /**
     * updates labels
     * @param _info
     */
    @Override
    public void update(NMEAInfo _info) {
        mPDOPText.setText(String.valueOf(_info.mPDOP));
        mHDOPText.setText(String.valueOf(_info.mHDOP));
        mVDOPText.setText(String.valueOf(_info.mVDOP));

        // ----------------------------------------------------------- LATITUDE
        DecimalFormat df = new DecimalFormat("#.0000");
        if (_info.mLatitude != 0d)
            mLatText.setText(df.format(_info.mLatitude));
        else
            mLatText.setText(mErrorMessage);


        // ----------------------------------------------------------- LONGITUDE
        if (_info.mLongitude != 0d)
            mLongText.setText(df.format(_info.mLongitude));
        else
            mLongText.setText(mErrorMessage);


        // ----------------------------------------------------------- TIME
        if (_info.mTime.length() >= 6)
            mTimeText.setText(_info.mTime.substring(0, 2) + ":" + _info.mTime.substring(2, 4) + ":" + _info.mTime.substring(4, 6));
        else
            mTimeText.setText(mErrorMessage);


        // ----------------------------------------------------------- HEIGHT
        if (_info.mHeight > 0)
            mHeightText.setText(_info.mHeight + " meters");
        else
            mHeightText.setText(mErrorMessage);
    }

    /**
     * sets mSateliitesTest to given value
     * @param _s
     */
    public void setSatellitesText(String _s) {
        mSatellitesText.setText(_s);
    }
}
