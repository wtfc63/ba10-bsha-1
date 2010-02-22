package touchpad;

/**
 * <p>Title: MicroGeste</p>
 *
 * <p>Description: Diese MicroGeste wird aubeim ermitteln der MicroGesten erstellt, wird von microGeste abgeleitet.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */

public class microGesteMeasure
    extends microGeste {

  private double length; // Absolute Länge der MicroGeste
  private int startPointID; // StartPunkt der MicroGeste

  /**
   * Konstruktor um die Erkannten Microgesten aus Vektoren zu erstellen
   *
   * @param type - Typ der MicroGeste
   * @param startPointID - Startpunkt der MicroGeste
   */
  public microGesteMeasure(int type, int startPointID) {
    this.type = type;
    this.startPointID = startPointID;
  }

  /**
   * Konstruktor um zwei microGesten zu verschmelzen (3. Stufe nicht sinnvolle Kombinationen zusammenschliessen)
   *
   * @param type - Typ der MicroGeste
   * @param startPointID - Startpunkt der MicroGeste
   * @param length - Länge der neuen MicroGeste
   */
  public microGesteMeasure(int type, double length, int startPointID) {
    this.type = type;
    this.length = length;
    this.relLength = calculateRelLength(this.length);
    this.startPointID = startPointID;
  }

  public void setLength(double l) {
    length = l;
    relLength = calculateRelLength(length);
  }

  public void setStartPointID(int spid) {
    startPointID = spid;
  }

  public int getStartPointID() {
    return startPointID;
  }

  public double getLength() {
    return length;
  }

  /**
   * Relative Länge auslesen. Dieser Wert wird aus der Absoluten Länge ermittelt.
   *
   * @return int
   */
  public int getRelLength() {
    return relLength;
  }

  /**
   * MicroGeste als String ausgeben
   *
   * @return String
   */
  public String toString() {
    String s = "Microgeste ";
    switch (type) {
      case 0:
        s = "starke Krümmung r";
        break;
      case 1:
        s = "schwache Krümmung r";
        break;
      case 2:
        s = "gerade";
        break;
      case 3:
        s = "schwache Krümmung l";
        break;
      case 4:
        s = "starke Krümmung l";
        break;
      case 5:
        s = "spitzkehre";
        break;
    }
    return " Art:" + s + "; Relative Länge:" + relLength + "; Länge:" + length +
        "; startPointID:" + startPointID;
  }

  private int calculateRelLength(double l) {
    if (l <= Const.RELLENGTH) {
      return 0;
    }
    if (l > Const.RELLENGTH) {
      return 1;
    }
    return -1;
  }

}
