package at.fhooe.mc.ois.Geo;

/**
 * Created by laureenschausberger on 01.05.17.
 */
public class GeoDoublePoint {

    /**
     * x coordinates
      */
    public double m_x = 0.0;

    /**
     * y coordinates
     */
    public double m_y = 0.0;


    public GeoDoublePoint(double _x, double _y) {
        m_x = _x;
        m_y = _y;
    }

    /**
     * calculates length
     * @return
     */
    public double length() {
        return Math.sqrt((m_x * m_x) + (m_y * m_y));
    }

    /**
     * returns string of point coords
     * @return
     */
    public String toString() {
        return "POINT (" + m_x + "," + m_y + ")";
    }

}
