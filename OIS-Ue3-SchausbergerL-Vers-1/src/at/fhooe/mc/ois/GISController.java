package at.fhooe.mc.ois;

import at.fhooe.mc.ois.Geo.GeoObject;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import static java.lang.Math.PI;

/**
 * Created by laureenschausberger on 05.04.17.
 * Controls all Listener that will be needed from our Frame or Drawing Panel
 */
public class GISController implements ActionListener, WindowListener, ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener {

    /**
     * Referenz to our GISModel, so that we can interact
     */
    GISModel mGISModel;

    /**
     * Referenz to our GISView, so that we can interact
     */
    GISView mGISView;

    /**
     * last Coordinates where it was before
     */
    int mLastX, mLastY;

    /**
     * last Coordinates where Mouse was before
     */
    int mLastMousePosX, mLastMousePosY;


    /**
     * contructor which sets global variable mGISModel to our param
     * @param _m
     */
    public GISController(GISModel _m){
        mGISModel = _m;
    }

    public void addView(GISView _v) {
        mGISView = _v;
    }

    /**
     * calls generateRndHome in GISModel
     * @param _e
     */
    @Override
    public void actionPerformed(ActionEvent _e) {
        switch(_e.getActionCommand()) {
            case "Austria":
                mGISModel.connectTo("");
                mGISModel.mMap = "";
                break;
            case "Azores-4236":
                mGISModel.connectTo("-azores-4326");
                mGISModel.mMap = "-azores-4326";
                break;
            case "Azores-3857":
                mGISModel.connectTo("-azores-3857");
                mGISModel.mMap = "-azores-4326";
                break;
            case "Cyprus":
                mGISModel.connectTo("-cyprus");
                mGISModel.mMap = "-azores-4326";
                break;
            case "DummyServer":
                mGISModel.connectDummyServer();
                break;
            case  "Scale":
                mGISModel.scale(mGISView.mScaleTextField.getText().toString());
                break;
            case "Store":
                mGISModel.save();
                break;
            case "Stick":
                mGISModel.sticky();
                break;
            case "Load":
                mGISModel.loadData();
                break;
            case "POIs":
                mGISModel.setPoisTo(!mGISModel.getCurrentPOIBool());
                break;
            case "ZoomToFit":
                mGISModel.zoomToFit();
                break;
            case "+":
                mGISModel.zoom(1.3);
                break;
            case "-":
                mGISModel.zoom(1/1.3);
                break;
            case "N":
                mGISModel.scrollVertical(20);
                break;
            case "E":
                mGISModel.scrollHorizontal(-20);
                break;
            case "S":
                mGISModel.scrollVertical(-20);
                break;
            case "W":
                mGISModel.scrollHorizontal(20);
                break;
            case "Rotate":
                mGISModel.rotate(PI/4);
                break;
            default:
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
        if (_e.getClickCount() == 2 && !_e.isConsumed()) {
            System.out.println("Double click");
            if (_e.isControlDown()) {
                mGISModel.setClipboard(mGISModel.getClipboardString(mGISModel.getMapPoint(_e.getPoint())));
            } else {
                mGISModel.addToClipboard(mGISModel.getClipboardString(mGISModel.getMapPoint(_e.getPoint())));
                mGISModel.showDialogIn(new Point(_e.getX(), _e.getY()));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent _e) {
        mLastX = _e.getX();
        mLastY = _e.getY();
        mLastMousePosX = _e.getX();
        mLastMousePosY = _e.getY();

        _e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * change/sets mHouseSize in mGISModel depending on which mousebutton was clicked
     * calls mouseClicked method in GISModel
     * @param _e
     */
    @Override
    public void mouseReleased(MouseEvent _e) {
        _e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        if( _e.getButton() != 1) {
            //rechte Maustaste
            mGISModel.scrollHorizontal(_e.getX() - mLastX);
            mGISModel.scrollVertical(_e.getY() - mLastY);
        }
        else { //linke Maustaste
            mGISModel.setRectToNull();
            Rectangle rect = new Rectangle(mLastX, mLastY, _e.getX() - mLastX, _e.getY() - mLastY);
            if(rect.getWidth() < 20 || rect.getHeight() < 20) {
                System.out.println("Zoom Rect is too small..");
            }
            else {
                mGISModel.zoomRect(rect);
            }
        }

        if (_e.getClickCount() == 2) {
            System.out.println("Double Click Released");
            Vector<GeoObject> select = mGISModel.initSelection(_e.getPoint());
            if (select != null) {
                for (int i = 0 ; i < select.size() ; i++){
                    System.out.println(i + ". selected object --> " + select.elementAt(i));
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent _e) {

    }

    @Override
    public void mouseExited(MouseEvent _e) {

    }

    @Override
    public void mouseDragged(MouseEvent _e) {
        if(_e.getButton() == 1){
            //linke Maustaste
            mGISModel.drawRect(new Rectangle(mLastX, mLastY, _e.getX() - mLastX, _e.getY() - mLastY));
        }
        else {
            mGISModel.scroll(_e.getX() - mLastMousePosX, _e.getY() - mLastMousePosY);
            mLastMousePosX = _e.getX();
            mLastMousePosY = _e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent _e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent _e) {
        int notches = _e.getWheelRotation();
        if (notches >= 0) {
            //Mouse wheel moved UP
            mGISModel.zoom(_e.getPoint(), 1+_e.getScrollAmount()*0.1);
        } else {
            //Mouse wheel moved DOWN
            mGISModel.zoom(_e.getPoint(), 1/(1+_e.getScrollAmount()*0.1));
        }
    }
}
