package touchpad;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * <p>Title: Zeichen</p>
 *
 * <p>Description: Stellt ein Knoten im Graph dar. Das Zeichen kann eine Bedeutung haben(z.B. ein Buchstaben).
 * Ein Pfad durch den Graph f�hrt �ber MicroGesten zu Zeichen. Alle Attribute werden aus der Datenbank gelesen.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David K�lbener
 * @version 1.0
 */

public class Sign {

  private int id; // ID vom Zeichen
  private String meaning = ""; // Bedeutung
  private LinkedList<microGesteGraph> gesten; // weiterf�hrende Gesten
  private boolean ascender, descender; // Ob dieses Zeichen Ober -oder Unterl�ngen hat

  /**
   * Sign
   *
   * @param id - ID des Zeichens
   * @param meaning - Bedeutung vom Zichen
   * @param ascender - true, wenn das Zeichen Oberl�ngen hat
   * @param descender - true, wenn das Zeichen Unterl�ngen hat
   */
  public Sign(int id, String meaning, boolean ascender, boolean descender) {
    this.id = id;
    this.meaning = meaning;
    gesten = new LinkedList<microGesteGraph> ();
    this.ascender = ascender;
    this.descender = descender;
  }

  /**
   * gibt true zur�ck, wenn das Zeichen Oberl�ngen hat
   *
   * @return boolean
   */
  public boolean hasAscender() {
    return ascender;
  }

  /**
   * gibt true zur�ck, wenn das Zeichen Unterl�ngen hat
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
   * MicroGeste hinzuf�gen, die zu einem weiteren Zeichen f�hrt.
   *
   * @param g microGesteGraph
   */
  public void addGeste(microGesteGraph g) {
    gesten.add(g);
  }

  /**
   * Iterator �ber die MicroGesten
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
