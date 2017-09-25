package at.fhooe.mc.ois;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import static java.lang.Math.PI;

/**
 * Created by laureenschausberger on 05.04.17.
 * Controls all Listener that will be needed from our Frame or Drawing Panel
 */
public class GISController implements ActionListener, WindowListener, ComponentListener, MouseListener {

    /**
     * Referenz to our GISModel, so that we can interact
     */
    GISModel mGISModel;

    int lastX, lastY;

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
        Button button = (Button) _e.getSource();

        switch(button.getLabel()) {
            case "Load":
                System.out.println(button.getLabel());
                mGISModel.loadData();
            case "ZoomToFit":
                System.out.println(button.getLabel());
                mGISModel.zoomToFit();
                break;
            case "+":
                System.out.println(button.getLabel());
                mGISModel.zoom(1.3);
                break;
            case "-":
                System.out.println(button.getLabel());
                mGISModel.zoom(1/1.3);
                break;
            case "N":
                System.out.println(button.getLabel());
                mGISModel.scrollVertical(20);
                break;
            case "E":
                System.out.println(button.getLabel());
                mGISModel.scrollHorizontal(-20);
                break;
            case "S":
                System.out.println(button.getLabel());
                mGISModel.scrollVertical(-20);
                break;
            case "W":
                System.out.println(button.getLabel());
                mGISModel.scrollHorizontal(20);
                break;
            case "Rotate":
                System.out.println(button.getLabel());
                mGISModel.rotate(PI/4);
                break;
            default:
                System.out.println(button.getLabel());
                // Error Handling
        }    }

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
        lastX = _e.getX();
        lastY = _e.getY();
    }

    /**
     * change/sets mHouseSize in mGISModel depending on which mousebutton was clicked
     * calls mouseClicked method in GISModel
     * @param _e
     */
    @Override
    public void mouseReleased(MouseEvent _e) {
        mGISModel.scrollHorizontal( _e.getX() - lastX );
        mGISModel.scrollVertical(_e.getY() - lastY);
    }

    @Override
    public void mouseEntered(MouseEvent _e) {

    }

    @Override
    public void mouseExited(MouseEvent _e) {

    }
}
