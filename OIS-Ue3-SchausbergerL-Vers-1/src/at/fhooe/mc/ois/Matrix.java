package at.fhooe.mc.ois;

import at.fhooe.mc.ois.Geo.GeoDoublePoint;

import java.awt.*;

/**
 * Created by laureenschausberger on 07.04.17.
 */
public class Matrix {

    /// (1,1-3)
    private double[] mMatrix_1 = new double[3];

    /// (2,1-3)
    private double[] mMatrix_2 = new double[3];

    /// (3,1-3)
    private double[] mMatrix_3 = new double[3];


    public static void main(String[] _args){
        Matrix m = new Matrix(3,   5,    1,
                              2,  4 ,   5,
                              1, 2 ,   2);
        Matrix invers = m.invers();
        System.out.println(invers.toString());
        testZTF();
    }

    /**
     * test method
     */
    static void testZTF(){
        System.out.println("----- TEST ZTF ----");
        Rectangle world = new Rectangle(47944531, 608091485, 234500, 213463);
        Rectangle win   = new Rectangle(0, 0, 640, 480);

        Matrix matrix   = Matrix.zoomToFit(world,win);
        Matrix matrixI  = matrix.invers();
        Rectangle transform        = matrix.multiply(world);
        Rectangle transformReverse = matrixI.multiply(transform);

        System.out.println("world bounds..." + world);
        System.out.println("win   bounds..." + win);
        System.out.println("transformed...." + transform);
        System.out.println("retransformed.." + transformReverse);
    }

    /**
     * Standardkonstruktor
     */
    public Matrix(){
        mMatrix_1 = new double[]{0, 0, 0};
        mMatrix_2 = new double[]{0, 0, 0};
        mMatrix_3 = new double[]{0, 0, 0};
    }

    /**
     * Standardkonstruktor
     * @param _m11 Der Wert des at.fhooe.mc.ois.Matrix Feldes 1x1 (Zeile1/Spalte1) *
     * @param _m12 Der Wert des at.fhooe.mc.ois.Matrix Feldes 1x2 (Zeile1/Spalte2) *...
     */
    public Matrix(double _m11, double _m12, double _m13,
                  double _m21, double _m22, double _m23,
                  double _m31, double _m32, double _m33){

        mMatrix_1 = new double[]{_m11, _m12, _m13};
        mMatrix_2 = new double[]{_m21, _m22, _m23};
        mMatrix_3 = new double[]{_m31, _m32, _m33};
    }

    /**
     * Liefert eine String-Repräsentation der at.fhooe.mc.ois.Matrix *
     * @return Ein String mit dem Inhalt der at.fhooe.mc.ois.Matrix * @see java.lang.String
     */
    public String toString(){
        return (mMatrix_1[0] + "\t" + mMatrix_1[1] + "\t" + mMatrix_1[2] + "\n" +
                mMatrix_2[0] + "\t" + mMatrix_2[1] + "\t" + mMatrix_2[2] + "\n" +
                mMatrix_3[0] + "\t" + mMatrix_3[1] + "\t" + mMatrix_3[2] + "\n");
    }

    /**
     * Liefert die Invers-at.fhooe.mc.ois.Matrix der Transformationsmatrix
     * @return Die Invers-at.fhooe.mc.ois.Matrix
     */
    public Matrix invers(){
        double determinant = (mMatrix_1[0] * mMatrix_2[1] * mMatrix_3[2])
                + (mMatrix_1[1] * mMatrix_2[2] * mMatrix_3[0])
                + (mMatrix_1[2] * mMatrix_2[0] * mMatrix_3[1])
                - (mMatrix_1[0] * mMatrix_2[2] * mMatrix_3[1])
                - (mMatrix_1[1] * mMatrix_2[0] * mMatrix_3[2])
                - (mMatrix_1[2] * mMatrix_2[1] * mMatrix_3[0]);

        if (determinant == 0) {
            return null;
        }

        double u = 1/determinant;

        double m11 = getDeterminantOf(mMatrix_2[1], mMatrix_2[2], mMatrix_3[1], mMatrix_3[2]);
        double m12 = getDeterminantOf(mMatrix_1[2], mMatrix_1[1], mMatrix_3[2], mMatrix_3[1]);
        double m13 = getDeterminantOf(mMatrix_1[1], mMatrix_1[2], mMatrix_2[1], mMatrix_2[2]);

        double m21 = getDeterminantOf(mMatrix_2[2], mMatrix_2[0], mMatrix_3[2], mMatrix_3[0]);
        double m22 = getDeterminantOf(mMatrix_1[0], mMatrix_1[2], mMatrix_3[0], mMatrix_3[2]);
        double m23 = getDeterminantOf(mMatrix_1[2], mMatrix_1[0], mMatrix_2[2], mMatrix_2[0]);

        double m31 = getDeterminantOf(mMatrix_2[0], mMatrix_2[1], mMatrix_3[0], mMatrix_3[1]);
        double m32 = getDeterminantOf(mMatrix_1[1], mMatrix_1[0], mMatrix_3[1], mMatrix_3[0]);
        double m33 = getDeterminantOf(mMatrix_1[0], mMatrix_1[1], mMatrix_2[0], mMatrix_2[1]);

        Matrix matrix = new Matrix(m11, m12, m13, m21, m22, m23, m31, m32, m33);

        return new Matrix(matrix.mMatrix_1[0] * u, matrix.mMatrix_1[1] * u, matrix.mMatrix_1[2] * u,
                          matrix.mMatrix_2[0] * u, matrix.mMatrix_2[1] * u, matrix.mMatrix_2[2] * u,
                          matrix.mMatrix_3[0] * u, matrix.mMatrix_3[1] * u, matrix.mMatrix_3[2] * u);
    }

    /**
     * returns determinant of 2x2 matrix
     * @param _m11 (1,1)
     * @param _m12 (1,2)
     * @param _m21 (2,1)
     * @param _m22 (2,2)
     * @return determinant
     */
    public double getDeterminantOf(double _m11, double _m12, double _m21, double _m22) {
        return (_m11*_m22 - _m12*_m21);
    }

    /**
     * Liefert eine at.fhooe.mc.ois.Matrix, die das Ergebnis einer Matrizen-
     * multiplikation zwischen dieser und der übergebenen at.fhooe.mc.ois.Matrix * ist
     *
     * @param _other Die at.fhooe.mc.ois.Matrix mit der Multipliziert werden soll
     * @return Die Ergebnismatrix der Multiplikation */
    public Matrix multiply(Matrix _other) {
        Matrix matrix = new Matrix();

        matrix.mMatrix_1[0] = mMatrix_1[0] * _other.mMatrix_1[0] + mMatrix_1[1] * _other.mMatrix_2[0] + mMatrix_1[2] * _other.mMatrix_3[0];
        matrix.mMatrix_1[1] = mMatrix_1[0] * _other.mMatrix_1[1] + mMatrix_1[1] * _other.mMatrix_2[1] + mMatrix_1[2] * _other.mMatrix_3[1];
        matrix.mMatrix_1[2] = mMatrix_1[0] * _other.mMatrix_1[2] + mMatrix_1[1] * _other.mMatrix_2[2] + mMatrix_1[2] * _other.mMatrix_3[2];

        matrix.mMatrix_2[0] = mMatrix_2[0] * _other.mMatrix_1[0] + mMatrix_2[1] * _other.mMatrix_2[0] + mMatrix_2[2] * _other.mMatrix_3[0];
        matrix.mMatrix_2[1] = mMatrix_2[0] * _other.mMatrix_1[1] + mMatrix_2[1] * _other.mMatrix_2[1] + mMatrix_2[2] * _other.mMatrix_3[1];
        matrix.mMatrix_2[2] = mMatrix_2[0] * _other.mMatrix_1[2] + mMatrix_2[1] * _other.mMatrix_2[2] + mMatrix_2[2] * _other.mMatrix_3[2];

        matrix.mMatrix_3[0] = mMatrix_3[0] * _other.mMatrix_1[0] + mMatrix_3[1] * _other.mMatrix_2[0] + mMatrix_3[2] * _other.mMatrix_3[0];
        matrix.mMatrix_3[1] = mMatrix_3[0] * _other.mMatrix_1[1] + mMatrix_3[1] * _other.mMatrix_2[1] + mMatrix_3[2] * _other.mMatrix_3[1];
        matrix.mMatrix_3[2] = mMatrix_3[0] * _other.mMatrix_1[2] + mMatrix_3[1] * _other.mMatrix_2[2] + mMatrix_3[2] * _other.mMatrix_3[2];
        return matrix;
    }
    /**
     * Multipliziert einen Punkt mit der at.fhooe.mc.ois.Matrix und liefert das Ergebnis der Multiplikation zurück
     *
     * @param _pt Der Punkt, der mit der at.fhooe.mc.ois.Matrix multipliziert
     * werden soll
     * @return Ein neuer Punkt, der das Ergebnis der
     * Multiplikation repräsentiert
     * @see java.awt.Point
     */
    public java.awt.Point multiply(java.awt.Point _pt){
        double x = mMatrix_1[0] * _pt.getX() + mMatrix_1[1] * _pt.getY() + mMatrix_1[2];
        double y = mMatrix_2[0] * _pt.getX() + mMatrix_2[1] * _pt.getY() + mMatrix_2[2];

        return new Point((int) Math.round(x), (int) Math.round(y));
    }
    /**
     * Multipliziert ein Rechteck mit der at.fhooe.mc.ois.Matrix und liefert das
     * Ergebnis der Multiplikation zurück
     *
     * @param _rect Das Rechteck, das mit der at.fhooe.mc.ois.Matrix multipliziert
     *
     * @return Ein neues Rechteck, das das Ergebnis der
     *         Multiplikation repräsentiert
     * @see java.awt.Rectangle
     */
    public java.awt.Rectangle multiply(java.awt.Rectangle _rect) {
        Point point_lb = new Point(_rect.x, _rect.y);
        Point point_rt = new Point(_rect.x + _rect.width, _rect.y + _rect.height);

        Point lb = multiply(point_lb);
        Point rt = multiply(point_rt);

        Rectangle rect = new Rectangle(lb);
        rect.add(rt);

        return rect;
    }
    /**
     * Multipliziert ein Polygon mit der at.fhooe.mc.ois.Matrix und liefert das
     * Ergebnis der Multiplikation zurück
     *
     * @param _poly Das Polygon, das mit der at.fhooe.mc.ois.Matrix multipliziert
    werden soll
     *
     * @return Ein neues Polygon, das das Ergebnis der
     *         Multiplikation repräsentiert
     * @see java.awt.Polygon :P
     */
    public java.awt.Polygon multiply(java.awt.Polygon _poly){
        int[] pointsX = new int[_poly.npoints];
        int[] pointsY = new int[_poly.npoints];
        int   pointsN = _poly.npoints;

        for (int i = 0 ; i < pointsN ; i++) {
            pointsX[i] = (int)(mMatrix_1[0]*(double)_poly.xpoints[i] + mMatrix_1[1]*(double)_poly.ypoints[i] + mMatrix_1[2]);
            pointsY[i] = (int)(mMatrix_2[0]*(double)_poly.xpoints[i] + mMatrix_2[1]*(double)_poly.ypoints[i] + mMatrix_2[2]);
        }

        Polygon result = new Polygon(pointsX, pointsY, pointsN);
        return result;
    }

    /**
     * Multipliziert einen GeoDOublePoint mit der at.fhooe.mc.ois.Matrix und liefert das
     * Ergebnis der Multiplikation zurück
     */
    public GeoDoublePoint multiply(GeoDoublePoint _pt) {
        double srcx = _pt.m_x;
        double srcy = _pt.m_y;
        double destx = mMatrix_1[0] * srcx + mMatrix_1[1] * srcy;
        double desty = mMatrix_2[0] * srcx + mMatrix_2[1] * srcy;
        return new GeoDoublePoint(destx,desty);
    }

    /**
     * Liefert eine Translationsmatrix
     *
     * @param _x Der Translationswert
     * @param _y Der Translationswert
     * @return Die Translationsmatrix
     */
    public static Matrix translate(double _x, double _y) {
        return new Matrix(1, 0, _x,
                          0, 1, _y,
                          0, 0,  1);
    }

    /**
     * Liefert eine Translationsmatrix
     *
     * @param _pt Ein Punkt, der die Translationswerte enthält * @return Die Translationsmatrix
     * @see java.awt.Point
     */
    public static Matrix translate(java.awt.Point _pt) {
        return translate(_pt.x, _pt.y);
    }

    /**
     * Liefert eine Skalierungsmatrix
     *
     * @param _scaleVal Der Skalierungswert der at.fhooe.mc.ois.Matrix *
     * @return Die Skalierungsmatrix
     */
    public static Matrix scale(double _scaleVal) {
        return new Matrix(_scaleVal, 0,   0,
                        0,      _scaleVal,0,
                        0,      0, 1);
    }

    /**
     * Liefert eine Spiegelungsmatrix (X-Achse) *
     * @return Die Spiegelungsmatrix
     */
    public static Matrix mirrorX() {
        return new Matrix(1, 0, 0,
                          0,-1, 0,
                          0, 0, 1);
    }
    /**
     * Liefert eine Spiegelungsmatrix (Y-Achse) *
     * @return Die Spiegelungsmatrix
     */
    public static Matrix mirrorY() {
        return new Matrix(-1, 0, 0,
                          0, 1, 0,
                          0, 0, 1);
    }
    /**
     * Liefert eine Rotationsmatrix
     *
     * @param _alpha Der Winkel (in rad), um den rotiert werden * soll
     * @return Die Rotationsmatrix
     */
    public static Matrix rotate(double _alpha) {
        return new Matrix(Math.cos(_alpha), -Math.sin(_alpha), 0,
                          Math.sin(_alpha),  Math.cos(_alpha), 0,
                         0,         0,            1);
    }

    /**
     * Liefert den Faktor, der benötigt wird, um das _world-
     * Rechteck in das _win-Rechteck zu skalieren (einzupassen)
     * bezogen auf die X-Achse  Breite *
     * @param _world Das Rechteck in
     * @param _win   Das Rechteck in
     * @return Der Skalierungsfaktor
     * @see java.awt.Rectangle
     */
    public static double getZoomFactorX(java.awt.Rectangle _world, java.awt.Rectangle _win){
        if (_world != null && _win != null) {
            if (_world.getWidth() != 0) {
                return _win.getWidth() / _world.getWidth();
            }
        }
        return 1.0;
    }

    /**
     * Liefert den Faktor, der benötigt wird, um das _world-
     * Rechteck in das _win-Rechteck zu skalieren (einzupassen)
     * bezogen auf die Y-Achse  Höhe *
     * @param _world Das Rechteck in
     * @param _win   Das Rechteck in
     * @return Der Skalierungsfaktor
     * @see java.awt.Rectangle
     */
    public static double getZoomFactorY(java.awt.Rectangle _world, java.awt.Rectangle _win) {
        if (_world != null && _win != null) {
            if (_world.getHeight() != 0) {
                return _win.getHeight() / _world.getHeight();
            }
        }
        return 1.0;
    }

    /**
     * Liefert eine at.fhooe.mc.ois.Matrix, die alle notwendigen Transformationen
     * beinhaltet (Translation, Skalierung, Spiegelung und
     * Translation), um ein _world-Rechteck in ein _win-Rechteck * abzubilden
     *
     * @param _world Das Rechteck in Weltkoordinaten
     * @param _win Das Rechteck in Bildschirmkoordinaten * @return Die Transformationsmatrix
     * @see java.awt.Rectangle
     */
    public static Matrix zoomToFit(java.awt.Rectangle _world, java.awt.Rectangle _win) {
        Matrix translate    = Matrix.translate(-_world.getCenterX(), -_world.getCenterY());

        double scaleFactor  = getZoomFactorX(_world, _win) < getZoomFactorY(_world, _win) ? getZoomFactorX(_world, _win) : getZoomFactorY(_world, _win);
        Matrix scale        = Matrix.scale(scaleFactor);

        Matrix mirrorX      = Matrix.mirrorX();
        Matrix trans        = Matrix.translate(_win.getCenterX(), _win.getCenterY());

        return trans.multiply(mirrorX.multiply(scale.multiply(translate)));
    }

    /**
     * Liefert eine at.fhooe.mc.ois.Matrix, die eine vorhandene Transformations-
     * matrix erweitert,     um an einem bestimmten Punkt um einen
     * bestimmten Faktor     in die Karte hinein- bzw. heraus zu zoomen
     * Die zu erweiternde Transformationsmatrix Der Punkt an dem gezoomt werden soll
     * Der Zoom-Faktor um den gezoomt werden soll
     * @param _old matrix
     * @param _zoomPt point
     * @param _zoomScale zoomscale
     * @return Die neue Transformationsmatrix
     * @see java.awt.Point
     */
    public static Matrix zoomPoint(Matrix _old, java.awt.Point _zoomPt, double _zoomScale) {
        Matrix translate = Matrix.translate(-_zoomPt.getX(), -_zoomPt.getY());
        Matrix scale     = Matrix.scale(_zoomScale);
        Matrix trans     = Matrix.translate(_zoomPt.getX(), _zoomPt.getY());

        return trans.multiply(scale.multiply(translate.multiply(_old)));
    }
}
