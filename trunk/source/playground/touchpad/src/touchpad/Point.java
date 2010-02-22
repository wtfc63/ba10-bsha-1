package touchpad;

/**
 * <p>Title: Punkt</p>
 *
 * <p>Description: conditioningAngle ben�tigt diese Hilfsklasse zur bestimmung der MicroGesten.
 * Sie entspricht in ihrer Funktione in etwa der Vektor-Klasse, die von conditioningBending
 * ben�tigt wird.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David K�lbener
 * @version 1.0
 */
public class Point {
  private int x; // Zur�ckgelegte Distanz in X-Richtung
  private int y; // Zur�ckgelegte Distanz in Y-Richtung
  private double distance, angle;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
    int correction = 0;

    if (x > 0 && y > 0) {
      correction = 270;
    }
    else if (y > 0) {
      correction = 180;
    }

    distance = Math.sqrt(x * x + y * y);
    angle = Math.toDegrees(Math.acos(x / (distance))) + correction;

    if (angle % 45 >= 22.5) {
      angle = angle + 45 - angle % 45;
    }
    else {
      angle = angle - angle % 45;
    }
    //System.out.println(this);
  }

  /**
   * L�nge vom Vektor ermitteln
   *
   * @return double
   */
  public int getDistance() {
    return (int) distance;
  }

  /**
   * Neigungswinkel ermitteln
   *
   * @return int
   */
  public int getAngle() {
    return (int) angle;
  }

  /**
   * Distanz, die in X-Richtung zur�ckgelegt wird ausgeben
   *
   * @return double
   */
  public int getX() {
    return x;
  }

  /**
   * Distanz, die in Y-Richtung zur�ckgelegt wird ausgeben
   *
   * @return double
   */
  public int getY() {
    return y;
  }

  /**
   * den Punkt als String ausgeben
   *
   * @return String
   */
  public String toString() {
    return "Winkel: " + angle + "  Vektor: " + x + "/" + y + " L�nge: " +
        distance;
  }

}
