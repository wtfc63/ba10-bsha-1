package touchpad;

/**
 * <p>Title: MicroGeste</p>
 *
 * <p>Description: Beschreibt eine MicroGeste mit Typ und relativer L�nge. Von dieser MicroGeste werden die Microgeste f�r die Messung und die vom Graphen abgeleitet.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 *
 * @author Daniel Fernandez
 * @author David K�lbener
 * @version 1.0
 */

abstract public class microGeste {

  protected int relLength; //Relative L�nge der Microgeste
  protected int type; // Typ der Microgeste

  /**
   * Relative L�nge der MicroGeste ermitteln. Ist abstract, da je nach Typ, die L�nge verschieden ermittelt werden muss.
   *
   * @return int - Relative L�nge 0=halbe Zeilenh�he, 1=gr�sser als eine halbe Zeilenh�he
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
        s = "starke Kr�mmung r";
        break;
      case 1:
        s = "schwache Kr�mmung r";
        break;
      case 2:
        s = "gerade";
        break;
      case 3:
        s = "schwache Kr�mmung l";
        break;
      case 4:
        s = "starke Kr�mmung l";
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
        s = "starke Kr�mmung r";
        break;
      case 1:
        s = "schwache Kr�mmung r";
        break;
      case 2:
        s = "gerade";
        break;
      case 3:
        s = "schwache Kr�mmung l";
        break;
      case 4:
        s = "starke Kr�mmung l";
        break;
      case 5:
        s = "spitzkehre";
        break;
    }
    return "Art: " + s + " Relative L�nge: " + relLength;
  }

}
