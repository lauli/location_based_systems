package at.fhooe.mc.ois;

/**
 * Created by laureenschausberger on 04.05.17.
 */
public class SatelliteInfo {

    /**
     * infos about id, horizontal and vertical angel, SNR
     */
    public int mId, mAngleHorizontal, mAngleVertical, mSNR;
    public boolean mUsedForCalculation = false;

    /**
     * constructor
     * @param _id
     * @param _angleH
     * @param _angleV
     * @param _SNR
     */
    public SatelliteInfo(int _id, int _angleH, int _angleV, int _SNR) {
        mId = _id;
        mAngleHorizontal = _angleH;
        mAngleVertical = _angleV;
        mSNR = _SNR;
    }

}
