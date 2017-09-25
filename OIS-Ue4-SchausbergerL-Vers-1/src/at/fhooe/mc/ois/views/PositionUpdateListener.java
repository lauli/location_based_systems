package at.fhooe.mc.ois.views;

import at.fhooe.mc.ois.NMEAInfo;

import java.awt.*;

/**
 * Created by laureenschausberger on 04.05.17.
 */
public abstract class PositionUpdateListener extends Panel {
    public abstract void update(NMEAInfo _info);
}
