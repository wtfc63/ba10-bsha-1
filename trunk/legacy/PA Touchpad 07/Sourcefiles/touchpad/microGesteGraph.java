package touchpad;

/**
 * <p>Title: MicroGeste</p>
 *
 * <p>Description: Diese MicroGeste wird f�r den Graph verwendet, wird von microGeste abgeleitet.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David K�lbener
 * @version 1.0
 */
public class microGesteGraph
    extends microGeste {

  private double probability;
  private Sign destination;

  /**
   * microGesteGraph
   *
   * @param destination - Auf welches Zeichen die MicroGeste hinzeigt
   * @param type - Typ der MicroGeste
   * @param relLength - Relative L�nge der MicroGeste
   * @param probability double - Wahrscheinlichkeit vom Pfad im Graph durch diese MicroGeste
   */
  public microGesteGraph(Sign destination, int type, int relLength,
                         double probability) {
    this.probability = probability;
    this.destination = destination;
    this.type = type;
    this.relLength = relLength;
  }

  /**
   * Relative L�nge auslesen. Dieser Wert kommt aus der Datenbank
   *
   * @return int
   */
  public int getRelLength() {
    return relLength;
  }

  /**
   * Wahrscheinlichkeit auslesen
   *
   * @return double
   */
  public double getProbability() {
    return probability;
  }

  /**
   * Zeichen auslesen, zu dem diese MicroGeste hinf�hrt
   *
   * @return Sign
   */
  public Sign getDestination() {
    return destination;
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
    return "bis:" + destination.getID() + "; %:" + probability +
        "; Relative L�nge:" + relLength + "; Art: " + s + " Relative L�nge: " +
        relLength;
  }

}
