package at.fhooe.mc.vis;

import java.awt.event.*;

/**
 * Created by laureenschausberger on 05.04.17.
 * Controls all Listener that will be needed from our Frame or Drawing Panel
 */
public class GISController implements ActionListener, WindowListener, ComponentListener, MouseListener {

    /**
     * Referenz to our GISModel, so that we can interact
     */
    GISModel mGISModel;

    /**
     * contructor which sets global variable mGISModel to our param
     * @param _m
     */
    public GISController(GISModel _m){
        mGISModel = _m;
    }

    /**
     * calls generateRndHome in GISModel
     * @param _e
     */
    @Override
    public void actionPerformed(ActionEvent _e) {
        mGISModel.generateRndHome(true);
    }

    @Override
    public void windowOpened(WindowEvent _e) {

    }

    @Override
    public void windowClosing(WindowEvent _e) {
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent _e) {

    }

    @Override
    public void windowIconified(WindowEvent _e) {

    }

    @Override
    public void windowDeiconified(WindowEvent _e) {

    }

    @Override
    public void windowActivated(WindowEvent _e) {

    }

    @Override
    public void windowDeactivated(WindowEvent _e) {

    }

    /**
     * calls setSize in our GISModel
     * @param _e
     */
    @Override
    public void componentResized(ComponentEvent _e) {
        mGISModel.setSize(_e.getComponent().getWidth(), _e.getComponent().getHeight()-60);
    }

    @Override
    public void componentMoved(ComponentEvent _e) {

    }

    @Override
    public void componentShown(ComponentEvent _e) {

    }

    @Override
    public void componentHidden(ComponentEvent _e) {

    }

    @Override
    public void mouseClicked(MouseEvent _e) {
    }

    @Override
    public void mousePressed(MouseEvent _e) {
    }

    /**
     * change/sets mHouseSize in mGISModel depending on which mousebutton was clicked
     * calls mouseClicked method in GISModel
     * @param _e
     */
    @Override
    public void mouseReleased(MouseEvent _e) {
        if(_e.getButton() == 1) {
            mGISModel.mHouseSize = 50;
        }
        else {
            mGISModel.mHouseSize = 75;
        }
        mGISModel.mouseClicked(_e.getX(), _e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent _e) {

    }

    @Override
    public void mouseExited(MouseEvent _e) {

    }
}
