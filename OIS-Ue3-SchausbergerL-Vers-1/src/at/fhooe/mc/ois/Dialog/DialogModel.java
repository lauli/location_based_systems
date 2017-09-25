package at.fhooe.mc.ois.Dialog;

import at.fhooe.mc.ois.Dialog.Interfaces.DialogDataObserver;
import at.fhooe.mc.ois.Dialog.Interfaces.DialogModelNotifier;
import at.fhooe.mc.ois.DrawingContext;
import at.fhooe.mc.ois.Geo.GeoObject;
import at.fhooe.mc.ois.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Created by laureenschausberger on 27.04.17.
 */
public class DialogModel implements DialogModelNotifier {

    private Vector<GeoObject> mObjects = null;
    private DialogDataObserver mDialogDataObserver = null;

    public DialogModel(Vector<GeoObject> _objects) {
        mObjects = _objects;
    }

    public void addObserver(DialogDataObserver _observer) {
        mDialogDataObserver = _observer;
    }

    @Override
    public void closeDialog() {
        mDialogDataObserver.close();
    }


    @Override
    public void drawSelectedItemAt(int _index) {
        mDialogDataObserver.fillDetails(mObjects.get(_index));
        draw(_index);
    }

    private void draw(int _index) {
        Rectangle bounds = mDialogDataObserver.getDrawBounds();

        BufferedImage image = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        GeoObject object = mObjects.get(_index);
        Matrix matrix = Matrix.zoomToFit(object.getBounds(), new Rectangle(0, 0, bounds.width-1, bounds.height-1));
        DrawingContext drawingContext = new DrawingContext();

        graphics.setColor(Color.white);
        object.paint(graphics, matrix, drawingContext.getSchema(object.getType()));
        graphics.dispose();
        mDialogDataObserver.update(image);
    }

    public void fillView() {
        mDialogDataObserver.fillGeoList(mObjects);
        draw(0);
    }
}
