package at.fhooe.mc.vis;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by laureenschausberger on 05.04.17.
 */
public class GISView implements DataOberserver {

    /**
     * drawing panel in which the image will be shown
     */
    DrawingPanel mDrawingPanel = new DrawingPanel();

    /**
     * window frame in which de drawing panel and a buttonpanel lay
     */
    Frame mFrame = new Frame();

    /**
     * constructor
     * adds all panels to our frame
     * panels are created by default values
     * listener are added to frame/panels
     * @param _c
     */
    public GISView(GISController _c) {
        mFrame.setSize(600, 400);

        mFrame.addWindowListener(_c);
        mFrame.addComponentListener(_c);

        Panel panel = new Panel();
        panel.setBackground(Color.orange);

        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(Color.orange);

        Button button = new Button();
        button.setLabel("Draw");
        button.addActionListener(_c);

        buttonPanel.add(button, BorderLayout.CENTER);

        BorderLayout borderLayout = new BorderLayout();

        mDrawingPanel.setBackground(Color.cyan);
        mDrawingPanel.setSize(600, 350);
        mDrawingPanel.addMouseListener(_c);

        panel.setLayout(borderLayout);
        panel.add(mDrawingPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);


        mFrame.add(panel);
        mFrame.setVisible(true);
    }


    /**
     * update method which will be called in GISModel whenever the observers have registered some changes
     * calls setImage and repaint for/in our DrawingPanel
     * @param _data
     */
    @Override
    public void update(BufferedImage _data) {
        mDrawingPanel.setImage(_data);
        mDrawingPanel.repaint();
    }
}