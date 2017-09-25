package at.fhooe.mc.ois;

import java.io.FileNotFoundException;
import java.util.Vector;

/**
 * Created by laureenschausberger on 04.05.17.
 */
public class NMEAParser extends Thread {

    /**
     * simulator
     */
    private GPSSimulator mGPSSimulator;

    /**
     * panel that shows data, sat and deviation
     */
    private MainPanel mPanel;

    /**
     * latitude and lonigtude information
     */
    double mLatitude, mLongitude;

    /**
     * infos about quality
     * count of satellites
     * count of already used satellites
     */
    int mQuality, mCount, mUsedCount;

    /**
     * height
     * infos about H-, V- and PDOP
     */
    double mHDOP, mHeight, mVDOP, mPDOP;

    /**
     * time
     * infos about north-south, ost-west
     */
    String mNorthSouth, mOstWest, mTime;

    /**
     * array with ids
     */
    int[] mIDs;

    /**
     * infos about satellites
     */
    Vector<SatelliteInfo> mSatelliteInfos = new Vector<>();

    /**
     * constructor
     * @param _pfad
     */
    public NMEAParser(String _pfad) {
        setDefault();
        String file = NMEAParser.class.getProtectionDomain().getCodeSource().getLocation().getPath() + _pfad;

        try {
            mGPSSimulator = new GPSSimulator(file, 1000, "$GPGGA");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mPanel = new MainPanel();
        mPanel.setup();
    }

    /**
     * runs application when thread is triggered
     */
    @Override
    public void run() {
        String line = mGPSSimulator.readLine();

        while (line != null) {

            String[] data = line.split(",");
            try {


                // ------------------------------------------------------------------------------- GPS DOP & Aktive Satelliten
                if (data[0].equals("$GPGSA") && checksum(data)) {
                    if (mUsedCount > 12)
                        mUsedCount = 12;

                    mIDs = new int[mUsedCount];

                    if (data[15].contains("$")) {
                        line = mGPSSimulator.readLine();
                        data = line.split(",");
                    }

                    setDOPs(data);
                }
                // ------------------------------------------------------------------------------- GPS Satelliten im View
                else if (data[0].equals("$GPGSV") && checksum(data)) {
                    if (!data[3].equals("") && !data[3].contains("*"))
                        mCount = Integer.valueOf(data[3]);
                    else
                        mCount = 0;


                    for (int i = 0; i < 19; i = i + 4) {
                        int snr;
                        int id = 0;
                        int angleH = 0;
                        int angleV = 0;

                        if (i+5 <= data.length) {
                            if (!data[i+4].equals("") && data[i+4] != null && !data[i+4].contains("*"))
                                id = Integer.valueOf(data[i+4]);


                            if (!data[i+5].equals("") && data[i+5] != null && !data[i+5].contains("*"))
                                angleV = Integer.valueOf(data[i+5]);


                            if (!data[i+6].equals("") && data[i+6] != null && !data[i+6].contains("*"))
                                angleH = Integer.valueOf(data[i+6]);


                            if (!data[i+7].equals("") && data[i+7] != null && !data[i+7].substring(0, 2).contains("*"))
                                snr = Integer.valueOf(data[i+7].substring(0, 2));
                            else
                                snr = -1;

                            mSatelliteInfos.add(new SatelliteInfo(id, angleH, angleV, snr));
                        }
                    }
                }
                 // ------------------------------------------------------------------------------- Global Positioning System Fix Data
                else if (data[0].equals("$GPGGA")) {
                        for (int i = 0; i < mSatelliteInfos.size(); i++) {
                            for (int j = 0; j < mIDs.length; j++) {

                                if (i < mSatelliteInfos.size() && mSatelliteInfos.get(i) != null && mSatelliteInfos.get(i).mId == mIDs[j]) {
                                    mSatelliteInfos.get(i).mUsedForCalculation = true;
                                    j = 0;
                                    i++;
                                }

                            }
                        }

                        mPanel.update(new NMEAInfo(mLatitude, mLongitude, mNorthSouth, mOstWest, mTime, mCount, mPDOP, mHDOP, mVDOP, mQuality, mHeight, mSatelliteInfos));
                        setDefault();

                        if (checksum(data)) {
                            if (!data[1].equals(""))
                                mTime = data[1];

                            if (!data[2].equals(""))
                                mLatitude = turnIntoDegrees(data[2]);

                            if (!data[3].equals(""))
                                mNorthSouth = data[3];

                            if (!data[4].equals(""))
                                mLongitude = turnIntoDegrees(data[4]);

                            if (!data[5].equals(""))
                                mOstWest = data[5];

                            if (!data[6].equals(""))
                                mQuality = Integer.valueOf(data[6]);

                            if (!data[7].equals(""))
                                mUsedCount = Integer.valueOf(data[7]);

                            if (!data[9].equals(""))
                                mHeight = Double.valueOf(data[9]);

                        }
                    }
                line = mGPSSimulator.readLine();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * checks the checksum
     * @param _d
     * @return
     */
    public boolean checksum(String[] _d) {
        String last = _d[_d.length - 1];
        if (last.length() >= 3 && !last.substring(last.length() - 2, last.length()).contains("*"))
            return true;
        return false;
    }


    /**
     * sets class variables to default values
     */
    public void setDefault() {
        mTime = "";

        mLatitude = 0;
        mLongitude = 0;

        mNorthSouth = "";
        mOstWest = "";

        mCount = 0;
        mUsedCount = 0;

        mHDOP = 0;
        mVDOP = 0;
        mPDOP = 0;

        mHeight = 0;
        mQuality = 0;

        mSatelliteInfos = new Vector<>();
    }

    /**
     * turns _d into right degrees
     * @param _d
     * @return
     */
    public double turnIntoDegrees(String _d) {
        if (_d.equals(""))
            return 0;

        int longDegree = (int) (Double.valueOf(_d) / 100);
        double longMinutes = Double.valueOf(_d) - longDegree * 100;

        return longDegree + longMinutes / 60;
    }

    /**
     * sets H-, D- and VDOP
     * @param _d
     */
    public void setDOPs(String[] _d) {
        if (!_d[15].equals(""))
            mPDOP = Double.valueOf(_d[15]);


        if (!_d[16].equals(""))
            mHDOP = Double.valueOf(_d[16]);


        if (!_d[17].equals("") && _d[17].length() >= 3)
            mVDOP = Double.valueOf(_d[17].substring(0, _d[17].length() - 3));


        for (int i = 0; i < mUsedCount; i++) {
            if (!_d[i + 3].equals("") && !_d[i + 3].contains(".")) {
                int satelliteID = Integer.valueOf(_d[i + 3]);
                mIDs[i] = satelliteID;
            }
        }
    }
}
