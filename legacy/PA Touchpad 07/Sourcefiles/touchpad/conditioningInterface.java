package touchpad;

/**
 * <p>Title: Interface zur MicroGesten erstellung</p>
 *
 * <p>Description: Wenn die Klasse, die die MicroGesten erstellt diesem Interface entspricht,
 * kann die Klasse einfach mit der "conditioningBending" ausgetauscht werden. Dazu muss nur
 * bei der Klasse touchpad die Zeile 27 angepasst werden.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David K�lbener
 * @version 1.0
 */
public interface conditioningInterface {

  /**
   * addDot ist zum hinzuf�gen von einem Punkt, der vom MouseListener eingelesen wird.
   *
   * @param d - Dot der vom GUI eingelesen und �bergeben wird.
   */
  public void addDot(Dot d);

  /**
   * getPos wird vom touchpadFrame aufgerufen und gibt alle Gespeicherten Punkte zur Anzeige zur�ck.
   *
   * @return Dot[]
   */
  public Dot[] getDots();

  /**
   * getDotCounter wird vom touchpadFrame aufgerufen und gibt die anzahl gespeicherten Punkte zur�ck.
   * Dies wird ben�tigt, da Dot[] eine fixe gr�sse hat.
   *
   * @return int
   */
  public int getDotCounter();

  /**
   * clearBuffer l�scht im minimum alle Anzeigetaten. Dies kann nicht automatisch nach einer
   * abgeschlossenen Messung und Analyse erfolgen, da evt. das Frame nochmals gezeichnet werden
   * muss und so die Daten von Dot[] nochmals ben�tigt werden.
   */
  public void clearBuffer();

  /**
   * analyse - wird von touchpad aufgerufen, nachdem der touchpadListener der Klasse touchpad
   * mitgeteilt hat, dass die Messung zu ende ist, und die Analyse beginnen kann.
   *
   * @return Measure - wird aus der Analyse der Daten erstellt, und wird dann weiter vom
   * touchpad ben�tigt, zur Ermittlung vom Zeichen.
   */
  public Measure analyse();

  /**
   * setStartTime - setzt die Startzeit der Messung in der Klasse Messung(Facade)
   *
   * @param start long
   */
  public void setStartTime(long start);

  /**
   * setEndTime - setzt die Endzeit der Messung in der Klasse Messung(Facade)
   *
   * @param end long
   */
  public void setEndTime(long end);

}
