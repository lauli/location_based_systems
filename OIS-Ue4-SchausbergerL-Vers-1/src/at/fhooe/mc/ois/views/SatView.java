package at.fhooe.mc.ois.views;

import at.fhooe.mc.ois.NMEAInfo;
import at.fhooe.mc.ois.SatelliteInfo;

import java.awt.*;
import java.util.Vector;

/**
 * Created by laureenschausberger on 04.05.17.
 */
public class SatView extends PositionUpdateListener {

    /**
     * satellite infos
     */
    private Vector<SatelliteInfo> mSatelliteInfos = new Vector<>();

    /**
     * x coords
     */
    private Vector<Integer> mX = new Vector<>();

    /**
     * y coords
     */
    private Vector<Integer> mY = new Vector<>();

    /**
     * paints satellites in rings
     * @param _g
     */
    @Override
    public void paint(Graphics _g) {
        int rad = (int) (Math.cos(Math.toRadians(45)) * 250);

        // ----------------------------------------------------------- OVERVIEW

        _g.setColor(Color.lightGray);
        Graphics2D graphics2D = (Graphics2D) _g;

        graphics2D.drawLine(25, 275, 525, 275);
        graphics2D.drawLine(275, 25, 275, 525);

        graphics2D.drawOval(25, 25, 500, 500);
        graphics2D.drawOval(275 - rad, 275 - rad, rad * 2, rad * 2);


        if (mSatelliteInfos == null)
            return;

        // ----------------------------------------------------------- SATELLITE

        for (int i = 0; i < mSatelliteInfos.size(); i++) {
            // ----------------------------------------------------------- SATELLITE - FILL COLOR
            if (mSatelliteInfos.get(i).mUsedForCalculation)
                _g.setColor(Color.green);
            else if (!mSatelliteInfos.get(i).mUsedForCalculation && mSatelliteInfos.get(i).mSNR != -1)
                _g.setColor(Color.blue);
            else
                _g.setColor(Color.red);

            _g.fillOval(mX.get(i), mY.get(i), 30, 30);

            // ----------------------------------------------------------- SATELLITE - NUMBER
            _g.setColor(Color.white);
            _g.drawString(String.valueOf(mSatelliteInfos.get(i).mId), mX.get(i)+10, mY.get(i)+20);
        }

    }

    /**
     * updates infos and repaints
     * @param _info
     */
    @Override
    public void update(NMEAInfo _info) {
        mX = new Vector<>();
        mY = new Vector<>();

        mSatelliteInfos = _info.mSatelliteInfos;
        if (mSatelliteInfos != null) {
            for (int i = 0; i < mSatelliteInfos.size(); i++) {
                int radius = (int) (Math.cos(Math.toRadians(mSatelliteInfos.get(i).mAngleVertical)) * 250);
                int deltaX = (int) (radius * Math.sin(Math.toRadians(mSatelliteInfos.get(i).mAngleHorizontal)));
                int deltaY = (int) (radius * Math.cos(Math.toRadians(mSatelliteInfos.get(i).mAngleHorizontal)));
                mX.add(deltaX + 250 + 25 - 15);
                mY.add(-deltaY + 250 + 25 - 15);
            }
            repaint();
        }
    }
}
