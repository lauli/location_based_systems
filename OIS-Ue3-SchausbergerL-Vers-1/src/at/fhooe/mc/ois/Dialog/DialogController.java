package at.fhooe.mc.ois.Dialog;

import at.fhooe.mc.ois.Dialog.Interfaces.DialogModelNotifier;
import at.fhooe.mc.ois.Dialog.Interfaces.DialogViewObserver;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 * Created by laureenschausberger on 27.04.17.
 */
public class DialogController implements WindowListener, ItemListener {

    private DialogModelNotifier mModelNotifier = null;
    private DialogViewObserver mViewObserver = null;

    public DialogController(DialogModelNotifier _modelNotifier) {
        this.mModelNotifier = _modelNotifier;
    }

    public void addViewObserver(DialogViewObserver _observer) {
        this.mViewObserver = _observer;
    }

    @Override
    public void windowOpened(WindowEvent _event) {

    }

    @Override
    public void windowClosing(WindowEvent _event) {
        this.mModelNotifier.closeDialog();
    }

    @Override
    public void windowClosed(WindowEvent _event) {

    }

    @Override
    public void windowIconified(WindowEvent _event) {

    }

    @Override
    public void windowDeiconified(WindowEvent _event) {

    }

    @Override
    public void windowActivated(WindowEvent _event) {

    }

    @Override
    public void windowDeactivated(WindowEvent _event) {

    }

    @Override
    public void itemStateChanged(ItemEvent _event) {
        int selected = this.mViewObserver.getSelectedIndex();

        if (selected == (int)_event.getItem()) {
            this.mModelNotifier.drawSelectedItemAt(selected);
        }
    }
}
