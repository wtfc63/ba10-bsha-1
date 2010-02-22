package touchpad;

/**
 * <p>Title: Ermittlung der MicroGesten via den Winkel</p>
 *
 * <p>Description: Diese Klasse ist eine Alternative zu conditioningBending. Sie wurde nicht vollständig
 * implementiert, sondern dient als Beispiel, wie eine alternative Klasse zur Ermittlung der
 * MicroGesten aussehen kann.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */
public class conditioningAngle
    implements conditioningInterface {

  final int MAX = 500;
  final int X = 0;
  final int Y = 1;
  private int[][] pos = new int[MAX][2];
  private int[][] winkel = new int[MAX][2];
  private Dot[] positions = new Dot[MAX]; // Punkte für die Visualisierung der gezeichneten Zeichen
  private int posCounter = 0;
  private int angleCounter = 0;
  private int dotCounter = 0;
  private Measure m = new Measure();

  double aktuellerWinkel = -1;

  public conditioningAngle() {
    clearBuffer();
  }

  /**
   * Punkt für die Ausgabe zum Benutzer Speichern
   *
   * @param d - Punkt der gespeichert werden soll
   */
  public void addDot(Dot d) {
    if (dotCounter >= (MAX-1)) {
      posCounter = 0;
    }

    positions[dotCounter] = d;
    dotCounter++;

    addPos(d.getX(), d.getY());
  }

  /**
   * Alle gespeicherten Punkte zurückgeben
   *
   * @return Dot[] - Gespeicherte Punkte
   */
  public Dot[] getDots() {
    return positions;
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
   * X- und Y-Koordinaten, die vom TouchpadListener eingelesen werden übernehmen und Vektoren daraus erstellen
   *
   * @param x - X-Koordinate
   * @param y - Y-Koordinate
   */
  private void addPos(int x, int y) {
    if (posCounter >= MAX) {
      posCounter = 0;
    }
    pos[posCounter][X] = x;
    pos[posCounter][Y] = y;
    //System.out.println(pos[counter][X]+"/"+pos[counter][Y]);
    posCounter++;
  }

  /**
   * Alle Buffer löschen, damit eine neue Messung erfolgen kann
   */
  public void clearBuffer() {
    for (int i = 0; i < MAX; i++) {
      pos[i][X] = -1;
      pos[i][Y] = -1;
      winkel[i][0] = 0;
      winkel[i][1] = 0;
    }
    posCounter = 0;
    angleCounter = 0;
    aktuellerWinkel = -1;
  }


  /**
     * Abgeschlossene Messung analysieren. - (Diese Methode ist nicht voll implementiert worden,
     * da bei dieser Variante von der MicroGesten-Bestimmung der Schreibwinkel berücksichtig werden
     * muss.)
     *
     * @return Measure - Analysierte Messung
   */
  public Measure analyse() {
    Point[] dv = getVector();

    if (dv != null) {
      int neuerWinkel = 0;
      if (dv.length >= 1) {
        //System.out.println("einzelwinkel: "+dv[0]);
      }

      for (int i = 0; i < dv.length; i++) {
        //System.out.println("part-winkel " + i + " :" + dv[i].getAngle());

        if ( (i == 0) && (dv.length >= 2)) {
          if ( (dv[i].getAngle() == 0) && (dv[i + 1].getAngle() == 270)) {
            neuerWinkel = neuerWinkel + 360;
          }
          else {
            neuerWinkel += dv[i].getAngle();
          }
        }
        else if ( (i == 1) && (dv.length >= 2)) {
          if ( (dv[i].getAngle() == 0) && (dv[i - 1].getAngle() == 270)) {
            neuerWinkel = neuerWinkel + 360;
          }
          else {
            neuerWinkel += dv[i].getAngle();
          }
        }
        else if ( (i >= 1) && (i <= dv.length - 2)) {
          if ( (dv[i].getAngle() == 0) &&
              ( (dv[i - 1].getAngle() == 270) || (dv[i + 1].getAngle() == 270))) {
            neuerWinkel = neuerWinkel + 360;
          }
          else {
            neuerWinkel += dv[i].getAngle();
          }
        }
        else if ( (i >= 2) && (i == dv.length - 1)) {
          if ( (dv[i].getAngle() == 0) && (dv[i - 1].getAngle() == 270)) {
            neuerWinkel = neuerWinkel + 360;
          }
          else {
            neuerWinkel += dv[i].getAngle();
          }
        }
        else {
          neuerWinkel += dv[i].getAngle();
        }
      }

      //System.out.print("   Berechnung:"+neuerWinkel+"/"+(dv.length-1));
      neuerWinkel = neuerWinkel / (dv.length);
      //System.out.println(" = "+neuerWinkel);


      if (neuerWinkel % 45 >= 22.5) {
        neuerWinkel = neuerWinkel + 45 - neuerWinkel % 45;
      }
      else {
        neuerWinkel = neuerWinkel - neuerWinkel % 45;
      }

      //System.out.println("   Resultat nach Rundung:"+neuerWinkel);

      if (aktuellerWinkel != neuerWinkel) {
        aktuellerWinkel = neuerWinkel;
        winkel[angleCounter][0] = neuerWinkel;
        winkel[angleCounter][1] = 1;
        //System.out.println("Winkel: "+winkel[winkelCounter][0]);
        angleCounter++;
      }
      else {
        winkel[angleCounter - 1][1] += 1;
      }

      System.out.println("RESULT:");
      for (int i = 0; i < angleCounter; i++) {
        System.out.println("winkel: " + winkel[i][0] + " dauer:" + winkel[i][1]);
      }

    }
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

  private Point[] getVector() {
    Point[] dv = null;
    int size = 5;
    if (posCounter > 0) {
      if (posCounter <= 4) {
        dv = new Point[posCounter];
        size = posCounter;
      }
      else {
        dv = new Point[5];
      }

      //System.out.println("    Berechnung:");
      for (int i = size - 1; i >= 0; i--) {
        dv[i] = new Point(pos[posCounter - i][X] - pos[posCounter - (i + 1)][X],
                          pos[posCounter - i][Y] - pos[posCounter - (i + 1)][Y]);
        //System.out.println("    x:"+pos[posCounter-i][X]+"-"+pos[posCounter-(i+1)][X]+" y:"+pos[posCounter-i][Y]+"-"+pos[posCounter-(i+1)][Y]);
      }

    }
    return dv;
  }

}
