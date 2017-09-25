package at.fhooe.mc.ois.Dialog;

import at.fhooe.mc.ois.Geo.GeoObject;

import java.awt.*;
import java.util.Vector;


/**
 * Created by laureenschausberger on 27.04.17.
 */
public class Dialog {

    /**
     * View that displays information about objects
     */
    DialogView mView = null;

    public Dialog(Vector<GeoObject> _objects, Point _point) {
        DialogModel model = new DialogModel(_objects);
        DialogController controller = new DialogController(model);
        mView = new DialogView(controller, _point);

        model.addObserver(mView);
        controller.addViewObserver(mView);
        mView.setView();
        mView.fillDetails(_objects.elementAt(0));
        model.fillView();
    }

   public void close() {
        mView.close();
    }
}
