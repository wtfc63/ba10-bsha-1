package touchpad;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * <p>Title: Adjazenz List Graph</p>
 *
 * <p>Description: Enthält den Graphen, der zur Ermittlung von einem Zeichen Gebraucht wird. Die Knoten Stellen die Zeichen dar, microGesten dienen als Kanten. Eine Folge von MicroGesten führen so zu einem Zeichen</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */

public class AdjListGraph {

  private LinkedList<Sign> signs = new LinkedList(); // Liste über alle Zeichen

  public AdjListGraph() {
  }

  /**
   * Ein Zeichen zum Graphen hinzuführen
   *
   * @param id - ID vom Zeichen
   * @param meaning - Bedeutung vom Zeichen
   * @param ascender - ob das Zeichen eine Oberlänge hat
   * @param descender - ob das Zeichen eine Unterlänge hat
   */
  public void addSign(int id, String meaning, boolean ascender,
                         boolean descender) {
    Sign vrtx = findSignByID(id);
    if (vrtx == null) {
      vrtx = new Sign(id, meaning, ascender, descender);
      signs.add(vrtx);
    }
  }

  /**
   * Wurzel vom Graphen ermitteln
   *
   * @return Sign - Wurzel vom Graph zurückgeben, als Einstiegspunkt für eine Suche
   */
  public Sign getRootSign() {
    return findSign("root");
  }

  /**
   * Geste zum Graphen hinzufügen, und 2 Knoten miteinander verbinden
   *
   * @param sourceID - ID der Quelle
   * @param destinationID - ID vom Ziel
   * @param type - Art der MicroGeste
   * @param relLength - Relative Länge der MicroGeste
   * @param probability - Wahrscheinlichkeit für diesen Pfad
   */
  public void addGeste(int sourceID, int destinationID, int type, int relLength,
                       double probability) {
    Sign src = findSignByID(sourceID);
    Sign dst = findSignByID(destinationID);
    microGesteGraph edge = new microGesteGraph(dst, type, relLength,
                                               probability);
    src.addGeste(edge);
  }

  /**
   * Ausgabe des Graphen als String
   *
   * @return String - Graph in Textform
   */
  public String toString() {
    String s = "";
    Iterator<Sign> its = signs.iterator();
    Iterator<microGesteGraph> itg;
    Sign sign;
    microGesteGraph mg;
    while (its.hasNext()) {
      sign = its.next();
      s += "From:" + sign.getID() + "(" + sign.getMeaning() + ")  To: ";
      itg = sign.getGesten();
      while (itg.hasNext()) {
        mg = itg.next();
        if(mg.getDestination()!=null){
          s += mg.printType() + "->" + mg.getDestination().getID() + "(" +
              mg.getDestination().getMeaning() + ")  | ";
        }
        else {
          System.out.println("ERROR im GRAPH microGeste hat kein Ziel: "+sign+" "+mg.getType());
        }
      }
      s += "\n";
    }
    return s;
  }

  /**
   * Ein Zeichen anhand der Bedeutung suchen
   *
   * @param id - Bedeutung vom gesuchten Zeichen
   * @return Sign - Gefundenes Zeichen zurückgeben
   */
  private Sign findSign(String id) {
    Iterator<Sign> itr = signs.iterator();
    Sign z;
    while (itr.hasNext()) {
      z = itr.next();
      if (id.equals(z.getMeaning())) {
        return z;
      }
    }
    return null;
  }

  /**
   * Ein Zeichen anhand der ID suchen
   *
   * @param id - ID vom gesuchten Zeichen
   * @return Sign - Gefundenes Zeichen zurückgeben
   */
  private Sign findSignByID(int id) {
    Iterator<Sign> itr = signs.iterator();
    Sign z;
    while (itr.hasNext()) {
      z = itr.next();
      if (id == z.getID()) {
        return z;
      }
    }
    return null;
  }


}
