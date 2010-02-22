package touchpad;

/**
 * <p>Title: Konstanten</p>
 *
 * <p>Description: Werte, die überall Gebraucht werden</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */
public class Const {

  /**
   * Ab welcher Anzahl von Milisekunden, der Graph für schnelle Schrift genommen werden soll.
   */
  public static final int BORDERTOFASTWRITING = 1000;

  /**
   * Ab welchem Zeilenabstand(in Pixel), der Graph für kleine Schrift genommen werden soll.
   */
  public static final int BORDERTOSMALLWRITING = 30;

  /**
   * Timeout(in Milisekunden) vom touchpadListener
   */
  public static final int TIMESLOT = 150;

  /**
   * Länge(in Pixel) vom Messungsfenster bei der Radiusmessung
   */
  public static final double CALCULATEWINDOW = 8;

  /**
   * Anz. Messungen die es Braucht, bis eine Geste als Gültige empfunden wird
   */
  public static final int INERTANCE = 5;

  /**
   * Minimale microGesten-Länge(in Pixel), damit sie nicht ausgefiltert wird
   */
  public static final int MINGESTURELENGTH = 10;

  /**
   * Abstand zwischen zwei Zeilen(in Pixel)
   */
  public static final int LINEHIGHT = 40;

  /**
   * Position der Null-Linie, von der oberen Kante aus(in Pixel)
   */
  public static final int ZEROLINE = 160;

  /**
   * Grenze, zwischen kleiner und grosser MicroGesten(Proportional zur Zeilenhöhe)
   */
  public static final double RELLENGTH = LINEHIGHT / 2;

  /**
   * Mindestwahrscheinlichkeit wo ein Knoten noch behalten wird
   */
  public static final double MINPROBABILITY = 0.03;

  // MicroGestures
  public static final int StrongBendingRight = 0;
  public static final int WeakBendingRight = 1;
  public static final int straight = 2;
  public static final int WeakBendingLeft = 3;
  public static final int StrongBendingLeft = 4;
  public static final int peak = 5;

  // Graphen
  public static final int SlowBig = 0;
  public static final int SlowSmall = 1;
  public static final int FastBig = 2;
  public static final int FastSmall = 3;

}
