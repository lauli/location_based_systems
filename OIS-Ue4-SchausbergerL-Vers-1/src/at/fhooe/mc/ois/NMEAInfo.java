package at.fhooe.mc.ois;

import java.util.Vector;

/**
 * Created by laureenschausberger on 04.05.17.
 */
public class NMEAInfo {

    /**
     * infos about latitude, longitude, Height, P-, H- and VDOP
     */
    public double mLatitude, mLongitude, mPDOP, mHDOP, mVDOP, mHeight;

    /**
     * infos about North-South, Ost-West, and Time
     */
    public String mNorthSouth, mOstWest, mTime;

    /**
     * infos about satellite count and quality
     */
    public int mCount, mQuality;

    /**
     * satellite informations
     */
    public Vector<SatelliteInfo> mSatelliteInfos;

    /**
     * constructor
     * @param _lat
     * @param _long
     * @param _ns
     * @param _ow
     * @param _time
     * @param _count
     * @param _pdop
     * @param _hdop
     * @param _vdop
     * @param _fixQuality
     * @param _height
     * @param _infos
     */
    public NMEAInfo(double _lat, double _long, String _ns, String _ow, String _time, int _count,
                    double _pdop, double _hdop, double _vdop, int _fixQuality, double _height, Vector<SatelliteInfo> _infos) {
        mLatitude = _lat;
        mLongitude = _long;

        mNorthSouth = _ns;
        mOstWest = _ow;

        mTime = _time;
        mCount = _count;

        mPDOP = _pdop;
        mHDOP = _hdop;
        mVDOP = _vdop;

        mQuality = _fixQuality;
        mHeight = _height;

        mSatelliteInfos = _infos;
    }


}
