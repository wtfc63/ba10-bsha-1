package touchpad;

import java.awt.Color;
import java.util.LinkedList;

/**
 * <p>Title: Ermittlung der MicroGesten via die Krümmung</p>
 *
 * <p>Description: Kreirt aus den Punkten, die vom TouchpadListener kommen MicroGesten. Unterhält eine Liste aller Gezeichneten Punken, damit sie dem Benutzer zur Visualisierung ausgegeben werden können.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 *
 * @version 1.0
 */

public class conditioningBending implements conditioningInterface{

  private final int MAX = 500; // Puffergrösse für die Punkte
  private int ascenderLine = Const.ZEROLINE + Const.LINEHIGHT;
  private int descenderLine = Const.ZEROLINE - 2 * Const.LINEHIGHT;
  private Dot[] pos = new Dot[MAX]; // Punkte für die Visualisierung der gezeichneten Zeichen
  private Vector[] vectors = new Vector[MAX]; // Vektoren, die aus den Punkten erstellt werden
  private int posCounter = 0, dotCounter = 0; // Counter für den Punkte -und Vektorenarray
  private Measure m = new Measure(); // Messungseinheit, die zur Ermittlung des Zeichens zurückgegeben wird
  private int[] bendingCounter = new int[6]; // Wage die angibt, wieviel Gewicht eine Microgeste haben muss, bis sie angenommen wird (6 Microgesten)

  public conditioningBending() {
    resetBendingCounter( -1); // Initialisieren der MicroGesten-Wage
  }

  /**
   * Punkt der aktuellen Messung hinzufügen
   *
   * @param d - Punkt der gespeichert werden soll
   */
  public void addDot(Dot d) {
    ascenderLine = Math.min(ascenderLine, -d.getY()); // Höchster Punkt vom Zeichen ermiteln
    descenderLine = Math.max(descenderLine, -d.getY()); // Tiefster Punkt vom Zeichen Ermitteln

    if (dotCounter >= (MAX-1)) {
      dotCounter = 0;
    }

    pos[dotCounter] = d;
    dotCounter++;

    addPos(d.getX(), d.getY());
  }

  /**
   *  aus X/Y-Koordinaten Vektoren erstellen
   *
   * @param x - X-Koordinate
   * @param y - Y-Koordinate
   */
  private void addPos(int x, int y) {

    if (posCounter >= MAX) {
      posCounter = 0;
    }

    if (posCounter == 0) {
      vectors[posCounter] = new Vector(x, y, x, y);
    }

    if (posCounter > 0) {
      vectors[posCounter] = new Vector(vectors[posCounter - 1].getXdst(),
                                       vectors[posCounter - 1].getYdst(), x, y);

      double cos = (vectors[posCounter].getX() * vectors[posCounter - 1].getX() +
                    vectors[posCounter].getY() * vectors[posCounter - 1].getY()) /
          (vectors[posCounter].getDistance() * vectors[posCounter -
           1].getDistance());

      // Durch Rundungen kann ein Cosinus von 1.0000000001 entstehen,
      // wodurch bei der Cosinus-Funktion eine NaN entsteht
      if (cos > 1) {
        cos = 1;
      }

      double angle = vectors[posCounter].direction(vectors[posCounter - 1]) *
          Math.toDegrees(Math.acos(cos));

      vectors[posCounter].setAngle(angle);
    }
    posCounter++;
  }

  /**
   * Alle gespeicherten Punkte zurückgeben
   *
   * @return Dot[] - Gespeicherte Punkte
   */
  public Dot[] getDots() {
    return pos;
  }

  /**
   * Anzahl gespeicherter Punkte zurückgeben
   *
   * @return int - Anzahl Punkte
   */
  public int getDotCounter() {
    return dotCounter;
  }

  /**
   * Alle Buffer löschen, damit eine neue Messung erfolgen kann
   */
  public void clearBuffer() {
    for (int i = 0; i < MAX; i++) {
      pos[i] = null;
      vectors[i] = null;
    }
    posCounter = 0;
    dotCounter = 0;
    ascenderLine = Const.ZEROLINE + Const.LINEHIGHT;
    descenderLine = Const.ZEROLINE - 2 * Const.LINEHIGHT;

    m = new Measure();
  }

  /**
   * Abgeschlossene Messung analysieren.
   * Erfolgt in 3 Stufen:
   * 1. Glättung der Vektoren
   * 2. microGesten erstellen
   * 3. Microgesten untersuchen, nicht sinnvolle Kombinationen zusammenschliessen, kleine Microgesten löschen
   *
   * @return Measure - Analysierte Messung
   */
  public Measure analyse() {

    // überprüfen, ob genug Vektoren vorhanden sind
    if (posCounter < Const.INERTANCE) {
      return null;
    }

    int start = 1, i = start, radiusCounter = 0; // Startwerte für die Messung
    int distance = -1, kruemmung = -1, kruemmungalt = -1; // Startwerte für die Messung
    double angle = 0; // Winkel einer Messeinheit
    double radius[] = new double[posCounter]; // Alle Ermittelten Radien
    int gestureStart = 0; // Position vom Startvektor der aktuellen Geste im Vektoren-Array
    double gestureLength = 0; // Länge der gemessenen Geste
    resetBendingCounter( -1); // MicroGesten-Wage auf 0 setzen
    LinkedList<microGesteMeasure>
        microGesten = new LinkedList<microGesteMeasure> (); // Liste der Ermittelten Microgesten

    // 1. Stufe Glättung
    while (i < posCounter) {
      i = start;
      distance = 0;
      angle = 0;
      boolean spiz = false;
      while (distance <= Const.CALCULATEWINDOW && i < posCounter) {
        distance += vectors[i].getDistance();
        angle += vectors[i].getAngle();
        if (vectors[i].getAngle() >= 95 || vectors[i].getAngle() <= -95) {
          spiz = true;
        }
        i++;
      }

      if (spiz) {
        radius[radiusCounter] = 2;
        radiusCounter++;
      }

      else if (distance >= Const.CALCULATEWINDOW) {
        if (angle == 0) {
          angle = 1;
        }
        radius[radiusCounter] = 1 / ( (distance * 360 / angle) / (2 * Math.PI));
        radiusCounter++;
      }
      start++;
    }

    // 2. Stufe Krümung zuordnen
    for (int a = 0; a <= radiusCounter; a++) {
      if (radius[a] < -1 / (Const.LINEHIGHT / 3.8)) { //-0.09 bei Zeilenhöhe 40
        kruemmung = Const.StrongBendingRight; // starke rechts
      }

      if (radius[a] >= -1 / (Const.LINEHIGHT / 3.8) &&
          radius[a] < -1 / (Const.LINEHIGHT / 0.28)) { //-0.09 && -0.007 bei Zeilenhöhe 40
        kruemmung = Const.WeakBendingRight; // schwache rechts
      }

      if (radius[a] >= -1 / (Const.LINEHIGHT / 0.28) &&
          radius[a] < 1 / (Const.LINEHIGHT / 0.28)) { //-0.007 && 0.007 bei Zeilenhöhe 40
        kruemmung = Const.straight; // gerade
      }

      if (radius[a] >= 1 / (Const.LINEHIGHT / 0.28) &&
          radius[a] < 1 / (Const.LINEHIGHT / 3.8)) { // 0.007 && 0.09 bei Zeilenhöhe 40
        kruemmung = Const.WeakBendingLeft; // schwache links
      }

      if (radius[a] >= 1 / (Const.LINEHIGHT / 3.8)) { // 0.09 bei Zeilenhöhe 40
        kruemmung = Const.StrongBendingLeft; // starke links
      }

      if (radius[a] == 2) {
        kruemmung = Const.peak; // spitzkehre
      }

      // Wenn sich die Krümmung verändert hat, die neue Krümung auf die Wage legen
      if (kruemmung >= 0) {
        kruemmung = calculateBending(kruemmung);
      }

      if (kruemmung != kruemmungalt) {
        pos[a - (Const.INERTANCE)].setColor(Color.BLUE);
        gestureLength = calculateLength(gestureStart, a - Const.INERTANCE);
        if (microGesten.size() > 0) {
          // Die Länge der vorhergehenden Geste berechnen
          microGesten.get(microGesten.size() - 1).setLength(gestureLength);
        }

        // Startpunkt der MicroGeste ermitteln
        gestureStart = a - Const.INERTANCE;
        // Neue Microgeste hinzufügen
        microGesten.add(new microGesteMeasure(kruemmung, a - Const.INERTANCE));
        kruemmungalt = kruemmung;
      }
    }
    // Länge der letzten Geste ermitteln
    if(microGesten.size()>1){
      if (microGesten.get(microGesten.size() - 1) != null) {
        gestureLength = calculateLength(gestureStart, posCounter);
        microGesten.get(microGesten.size() - 1).setLength(gestureLength);
      }
    }

    // 3. Stufe nicht sinnvolle Kombinationen zusammenschliessen, kleine Microgesten löschen
    LinkedList<microGesteMeasure> mg = adjustGestures(microGesten);


    // Einfärbung der Punkte(blau), der geglätteten Gesten
    for (int a = 0; a < mg.size(); a++) {
      pos[mg.get(a).getStartPointID()].setColor(Color.YELLOW);
    }

    m.mg = mg;
    m.setAscender(ascenderLine);
    m.setDescender(descenderLine);

    return m;
  }

  /**
   * Startzeit der Messung wird in der Klasse Measure gespeichert.
   * (conditioningBending dient als Facade)
   *
   * @param start - Startzeit
   */
  public void setStartTime(long start) {
    m.setStartTime(start);
  }

  /**
   * Endzeit der Messung wird in der Klasse Measure gespeichert.
   * (conditioningBending dient als Facade)
   *
   * @param start - EndZeit
   */
  public void setEndTime(long end) {
    m.setEndTime(end);
  }

  /**
   * Wage der Kümmungen, berechnet welche Krümmung die Oberhand hat
   *
   * @param bending - übergabe einer Ermittelten Krümmung
   * @return int - Krümmung, die momentan die Oberhand hat
   */
  private int calculateBending(int bending) {
    int tempBending = 0;
    bendingCounter[bending]++;

    if (countAllBendings() <= Const.INERTANCE) {
      return -1;
    }

    if (bendingCounter[bending] == Const.INERTANCE) {
      resetBendingCounter(bending);
      return bending;
    }

    else {
      bending = -1;

      for (int i = 0; i < bendingCounter.length; i++) {
        if (bendingCounter[i] > tempBending &&
            bendingCounter[i] >= Const.INERTANCE) {
          bending = i;
          tempBending = bendingCounter[i];
        }
      }
      return bending;
    }
  }

  /**
   * Länge einer MicroGeste berechnen
   *
   * @param startDot - Startpunkt der MicroGeste
   * @param endDot - Endpunkt der MicroGeste
   * @return double - Länge der MicroGeste
   */
  private double calculateLength(int startDot, int endDot) {
    double length = 0;
    for (int i = startDot; i < endDot; i++) {
      length += vectors[i].getDistance();
    }
    return length;
  }

  /**
   * Krümmungswage auf 0 setzen
   *
   * @param b - Krümmung, die ihr Gewicht behalten soll
   */
  private void resetBendingCounter(int except) {
    for (int i = 0; i < bendingCounter.length; i++) {
      if (i != except) {
        bendingCounter[i] = 0;
      }
    }
  }

  /**
   * Anzahl Krümmungen die auf der Wage sind Zählen
   *
   * @return int - Anzahl Krümmungen
   */
  private int countAllBendings() {
    int tot = 0;
    for (int i = 0; i < bendingCounter.length; i++) {
      tot += bendingCounter[i];
    }
    return tot;
  }

  /**
   * 3. Microgesten untersuchen, nicht sinnvolle Kombinationen zusammenschliessen, kleine Microgesten löschen
   *
   * @param source - Zu untersuchende folge MicroGesten
   * @return LinkedList - Untersuchte und nachbearbeitete Microgesten
   */
  private LinkedList<microGesteMeasure> adjustGestures(LinkedList<
      microGesteMeasure> source) {
    LinkedList<microGesteMeasure> fine = new LinkedList<microGesteMeasure> ();
    LinkedList<microGesteMeasure> result = new LinkedList<microGesteMeasure> ();


    microGesteMeasure[] m = new microGesteMeasure[3];

    if(source.size()==1){
      fine.add(source.get(0));
    }

    else{
      for (int i = 0; i < source.size(); i++) {

        m[0] = source.get(i);
        if (m[0].getType() < 4 && m[0].getType() > 0 && i < source.size() - 1) {

          m[1] = source.get(i + 1);
          if (m[1].getType() < 4 && m[1].getType() > 0) {

            if ( ( (m[0].getType() + 1) == m[1].getType()) ||
                ( (m[0].getType() - 1) == m[1].getType())) { // Abweichung =1?

              if (i < source.size() - 2) {
                m[2] = source.get(i + 2);
                if (m[2].getType() == m[0].getType() ||
                    m[2].getType() == m[1].getType()) {
                  // 0 ist der Längere
                  if (m[0].getLength() > m[1].getLength()) {
                    fine.add(new microGesteMeasure(m[0].getType(),
                                                   m[0].getLength() +
                                                   m[1].getLength() +
                                                   m[2].getLength(),
                                                   m[0].getStartPointID()));
                  }
                  else {
                    fine.add(new microGesteMeasure(m[1].getType(),
                                                   m[0].getLength() +
                                                   m[1].getLength() +
                                                   m[2].getLength(),
                                                   m[0].getStartPointID()));
                  }
                  i += 2;
                }
                else {
                  // 0 ist Länger als 1
                  if (m[0].getLength() >= m[1].getLength()) {
                    m[0].setLength(m[0].getLength() + m[1].getLength());
                    fine.add(m[0]);
                    i++;
                  }
                  // 1 ist Länger als 2
                  else {
                    m[1].setLength(m[0].getLength() + m[1].getLength());
                    m[1].setStartPointID(m[0].getStartPointID());
                    fine.add(m[1]);
                    i++;
                  }
                }
              }
              else {
                // 0 ist Länger als 1
                if (m[0].getLength() >= m[1].getLength()) {
                  m[0].setLength(m[0].getLength() + m[1].getLength());
                  fine.add(m[0]);
                  i++;
                }
                // 1 ist Länger als 2
                else {
                  m[1].setLength(m[0].getLength() + m[1].getLength());
                  m[1].setStartPointID(m[0].getStartPointID());
                  fine.add(m[1]);
                  i++;
                }
              }
            }
            else {
              if (m[0].getLength() > Const.MINGESTURELENGTH) { // Kleine Gesten ausfiltern
                fine.add(m[0]);
              }
            }
          }
          else {
            if (m[0].getLength() > Const.MINGESTURELENGTH) { // Kleine Gesten ausfiltern
              fine.add(m[0]);
            }
          }
        }
        else {
          if (m[0].getLength() > Const.MINGESTURELENGTH) { // Kleine Gesten ausfiltern
            fine.add(m[0]);
          }
        }
      }
    }

    for(int i=0; i < fine.size()-1; i++){
      if(fine.get(i).getType()==fine.get(i+1).getType()){
        fine.get(i).setLength(fine.get(i).getLength()+fine.get(i+1).getLength());
        result.add(fine.get(i));
        i++;
      }
      else{
        result.add(fine.get(i));
      }
    }

    return result;
  }
}
