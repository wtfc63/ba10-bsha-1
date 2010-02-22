package touchpad;

/**
 * <p>Title: MicroGeste</p>
 *
 * <p>Description: Beschreibt eine MicroGeste mit Typ und relativer Länge. Von dieser MicroGeste werden die Microgeste für die Messung und die vom Graphen abgeleitet.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */

abstract public class microGeste {

  protected int relLength; //Relative Länge der Microgeste
  protected int type; // Typ der Microgeste

  /**
   * Relative Länge der MicroGeste ermitteln. Ist abstract, da je nach Typ, die Länge verschieden ermittelt werden muss.
   *
   * @return int - Relative Länge 0=halbe Zeilenhöhe, 1=grösser als eine halbe Zeilenhöhe
   */
  abstract int getRelLength();

  /**
   * Typ der Microgeste ermitteln
   *
   * @return int - Typ der Microgeste
   */
  public int getType() {
    return type;
  }

  /**
   * Typ der MicroGeste als String ausgeben
   *
   * @return String
   */
  public String printType() {
    String s = "";
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
    return s;
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
    return "Art: " + s + " Relative Länge: " + relLength;
  }

}
