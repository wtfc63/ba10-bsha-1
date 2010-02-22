package touchpad;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * <p>Title: Zeichen</p>
 *
 * <p>Description: Stellt ein Knoten im Graph dar. Das Zeichen kann eine Bedeutung haben(z.B. ein Buchstaben).
 * Ein Pfad durch den Graph führt über MicroGesten zu Zeichen. Alle Attribute werden aus der Datenbank gelesen.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */

public class Sign {

  private int id; // ID vom Zeichen
  private String meaning = ""; // Bedeutung
  private LinkedList<microGesteGraph> gesten; // weiterführende Gesten
  private boolean ascender, descender; // Ob dieses Zeichen Ober -oder Unterlängen hat

  /**
   * Sign
   *
   * @param id - ID des Zeichens
   * @param meaning - Bedeutung vom Zichen
   * @param ascender - true, wenn das Zeichen Oberlängen hat
   * @param descender - true, wenn das Zeichen Unterlängen hat
   */
  public Sign(int id, String meaning, boolean ascender, boolean descender) {
    this.id = id;
    this.meaning = meaning;
    gesten = new LinkedList<microGesteGraph> ();
    this.ascender = ascender;
    this.descender = descender;
  }

  /**
   * gibt true zurück, wenn das Zeichen Oberlängen hat
   *
   * @return boolean
   */
  public boolean hasAscender() {
    return ascender;
  }

  /**
   * gibt true zurück, wenn das Zeichen Unterlängen hat
   *
   * @return boolean
   */
  public boolean hasDescender() {
    return descender;
  }

  public int getID() {
    return id;
  }

  public String getMeaning() {
    return meaning;
  }

  /**
   * MicroGeste hinzufügen, die zu einem weiteren Zeichen führt.
   *
   * @param g microGesteGraph
   */
  public void addGeste(microGesteGraph g) {
    gesten.add(g);
  }

  /**
   * Iterator über die MicroGesten
   *
   * @return Iterator
   */
  public Iterator<microGesteGraph> getGesten() {
    return gesten.iterator();
  }

  /**
   * Git das Zeichen als MicroGeste aus.
   *
   * @return String
   */
  public String toString() {
    return "Sign ID:" + id + " Meaning:" + meaning + " asc:" + ascender +
        " dsc:" + descender;
  }

}
