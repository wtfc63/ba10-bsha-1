package touchpad;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * <p>Title: Messung</p>
 *
 * <p>Description: Beinhaltet alle Messdaten einer Messung</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */

public class Measure {

  /**
   * Liste der MicroGesten
   */
  public LinkedList<microGesteMeasure> mg = new LinkedList<microGesteMeasure>();

  private long startTime = 0;
  private long endTime = 0;
  private int ascender = 0;
  private int descender = 0;

  public Measure() {
  }

  /**
   * Höchster gemessener Punkt vom Zeichen setzen. Dadurch wird ermittelt, ob das Zeichen eine Oberlänge hat
   *
   * @param asc - Höchster Punkt
   */
  public void setAscender(int asc) {
    ascender = asc;
  }

  /**
   * Tiefster gemessener Punkt vom Zeichen setzen. Dadurch wird ermittelt, ob das Zeichen eine Unterlänge hat
   *
   * @param dsc - Tiefster Punkt
   */
  public void setDescender(int dsc) {
    descender = dsc;
  }

  /**
   * Startzeit der Messung Speichern
   *
   * @param startTime long
   */
  public void setStartTime(long startTime){
    this.startTime=startTime;
  }

  /**
   * Endzeit der Messung Speichern
   *
   * @param endTime long
   */
  public void setEndTime(long endTime){
    this.endTime=endTime;
  }

  /**
   * true, wenn das Zeichen eine Oberlänge hat
   *
   * @return boolean
   */
  public boolean hasAscender() {
    if (ascender < Const.ZEROLINE - Const.LINEHIGHT * 1.4) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * true, wenn das Zeichen eine Unterlänge hat
   *
   * @return boolean
   */
  public boolean hasDescender() {
    if (descender > Const.ZEROLINE + Const.LINEHIGHT * 0.4) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Dauer der Messung ermitteln
   *
   * @return long - Dauer in Milisekunden
   */
  public long getDuration() {
    return endTime - startTime;
  }

  /**
   * Messung als String ausgeben
   *
   * @return String
   */
  public String toString() {
    String str = "\nMicrogesten:\n";

    Iterator<microGesteMeasure> it = mg.iterator();
    while (it.hasNext()) {
      str += it.next().toString() + "\n";
    }

    str += "hasAscender: " + hasAscender() + "  hasDescender: " + hasDescender() +
        "   Time: " + getDuration() + "ms\n";

    return str;
  }

}
