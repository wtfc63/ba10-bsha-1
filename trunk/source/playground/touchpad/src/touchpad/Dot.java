package touchpad;

import java.awt.Color;

/**
 * <p>Title: Dot</p>
 *
 * <p>Description: Beschreibt ein Punkt, wo und in welcher Farbe er auf dem Display wieder ausgegeben wird.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */

public class Dot {

  private int x, y;
  private Color c;

  /**
   * Dot
   *
   * @param x - X-Koordinate
   * @param y - Y-Koordinate
   */
  public Dot(int x, int y) {
    this.x = x;
    this.y = y;
    c = Color.red; // Standartfarbe ist Rot
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  /**
   * setzen der Farbe (Standartfarbe ist Rot)
   *
   * @param c - neue Farbe
   */
  public void setColor(Color c) {
    this.c = c;
  }

  public Color getColor() {
    return c;
  }
}
