package at.fhooe.mc.vis;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by laureenschausberger on 05.04.17.
 */
public class DrawingTest extends WindowAdapter implements ActionListener {
    Frame mFrame = new Frame();
    DrawingPanel mDrawingPanel = new DrawingPanel();

    public static void main(String[] _args) {
        DrawingTest test = new DrawingTest();
    }

    public DrawingTest() {
        mFrame.setSize(600, 400);
        mFrame.addWindowListener(this);

        Panel panel = new Panel();
        panel.setBackground(Color.darkGray);


        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(Color.darkGray);

        Button button = new Button();
        button.setLabel("Draw");
        button.addActionListener(this);

        buttonPanel.add(button, BorderLayout.CENTER);

        BorderLayout borderLayout = new BorderLayout();
        panel.setLayout(borderLayout);
        mDrawingPanel.setBackground(Color.darkGray);
        mDrawingPanel.setBounds(0,0,600,350);

        panel.add(mDrawingPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);


        mFrame.add(panel);
        mFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent _e) {
        //mDrawingPanel.getGraphics().drawRect(50,50,50,50);
        int xPoly[] = {50, 100, 100, 75, 50, 50};
        int yPoly[] = {50, 50, 100, 125, 100, 50};
        Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
        //mDrawingPanel.drawHouse(poly);
    }

    @Override
    public void windowClosing(WindowEvent _e) {
        super.windowClosing(_e);
        System.exit(0);
    }
}
