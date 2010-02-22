package touchpad;

/**
 * <p>Title: Vektor</p>
 *
 * <p>Description: Beschreibt ein Vektor zwischen zwei Punkten.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David K�lbener
 * @version 1.0
 */
public class Vector {

  private int xstart, ystart, xziel, yziel;
  private double distance, angleToNextVector;

  public Vector(int xstart, int ystart, int xziel, int yziel) {
    this.xstart = xstart;
    this.ystart = ystart;
    this.xziel = xziel;
    this.yziel = yziel;
    angleToNextVector = -1;

    distance = Math.sqrt( (xziel - xstart) * (xziel - xstart) +
                         (yziel - ystart) * (yziel - ystart));
    if (distance == 0) {
      distance = 1;
    }

  }

  /**
   * L�nge vom Vektor ermitteln
   *
   * @return double
   */
  public double getDistance() {
    return distance;
  }

  public void setAngle(double angle) {
    this.angleToNextVector = angle;
  }

  /**
   * Winkel zum n�chsten Vektor ermitteln
   *
   * @param angle double
   */
  public double getAngle() {
    return angleToNextVector;
  }

  /**
   * Richtung zu einem Vektor ermitteln, dadurch wird bestimmt, in welche Richtung die Kr�mung geht.
   *
   * @param v Vector
   * @return int - -1=Kr�mmung rechts, 1=Kr�mmung nach links
   */
  public int direction(Vector v) {
    double sign = getY() * v.getX() -getX() * v.getY();
    if (sign >= 0) {
      return 1;
    }
    return -1;
  }

  public int getXsrc() {
    return xstart;
  }

  public int getYsrc() {
    return ystart;
  }

  public int getXdst() {
    return xziel;
  }

  public int getYdst() {
    return yziel;
  }

  /**
   * Distanz, die in X-Richtung zur�ckgelegt wird ermitteln
   *
   * @return double
   */
  public double getX() {
    return xziel - xstart;
  }

  /**
   * Distanz, die in Y-Richtung zur�ckgelegt wird ermitteln
   *
   * @return double
   */
  public double getY() {
    return yziel - ystart;
  }

  /**
   * Vektor als String ausgeben
   *
   * @return String
   */
  public String toString() {
    return " L�nge: " + distance + " Winkel: " + angleToNextVector;
  }
}
