package at.fhooe.mc.ois;

import at.fhooe.mc.ois.views.DataView;
import at.fhooe.mc.ois.views.DeviationView;
import at.fhooe.mc.ois.views.SatView;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by laureenschausberger on 04.05.17.
 */
public class MainPanel extends WindowAdapter {

    /**
     * frame that shows data-, sat- and deviationview
     */
    private Frame mFrame;

    /**
     * sat view that displays satellites
     */
    private SatView mSatView;

    /**
     * data view that displays informations
     */
    private DataView mDataView;

    /**
     * deviation view that's not working ;)
     */
    private DeviationView mDeviationView;

    /**
     * sets up view
     */
    public void setup() {
        mFrame = new Frame();
        mFrame.setSize(1400, 600);
        mFrame.addWindowListener(this);
        mFrame.setLayout(new GridLayout(1,2));

        Panel dataPanel = new Panel();
        dataPanel.setLayout(new GridLayout(2, 1));

        mDataView = new DataView();
        dataPanel.add(mDataView);
        mDeviationView = new DeviationView();
        dataPanel.add(mDeviationView);

        mSatView = new SatView();

        mFrame.add(dataPanel);
        mFrame.add(mSatView);

        mFrame.setVisible(true);
    }

    /**
     * updates all views
     * @param _info
     */
    public void update(NMEAInfo _info) {
        int used = 0;
        int available = 0;

        if (_info.mSatelliteInfos != null) {
            available = _info.mSatelliteInfos.size();
            for (int i = 0; i < _info.mSatelliteInfos.size(); i++) {
                if (_info.mSatelliteInfos.get(i).mUsedForCalculation)
                    used++;

            }
        }

        mDataView.setSatellitesText(used + " / " + available);
        mDataView.update(_info);
        mSatView.update(_info);
        mDeviationView.update(_info);

    }

    /**
     * closes frame when clicked on close button
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }


}
