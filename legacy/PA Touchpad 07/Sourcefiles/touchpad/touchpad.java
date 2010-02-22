package touchpad;

import java.awt.*;
import javax.swing.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.io.*;
import java.util.Date;

/**
 * <p>Title: Touchpad</p>
 *
 * <p>Description: Mainklasse vom Programm. Das Touchpad ist die Schnittstelle zwischen dem GUI und dem Programm.
 * Sowie wird hier auch die Ermittlung von einem Zeichen durch den Graph gemacht.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */

public class touchpad {

  boolean packFrame = false;

  private conditioningInterface vGesture = new conditioningBending();

  private Persistence per = new Persistence();
  private AdjListGraph graph; // Graph auf dem die ermittlung vom Zeichen durchgeführt wird.
  private AdjListGraph graphs[] = new AdjListGraph[4]; // graphe die zur verfügung stehen für die Ermittlung vom Zeichen. Je nach schreibstiel und einstellungen wird ein anderer Graph genommen.
  private touchpadFrame frame;
  private String log = "";
  private Date d = new Date();
  private String filename = "logTouchpad_" + d.getDate() + "-" + (d.getMonth()+1) + "-" +
      (d.getYear()+1900) + ".txt";

  /**
   * Konstruktor, erstellt das GUI, und lät die Graphen aus der DB
   */
  public touchpad() {

    frame = new touchpadFrame(this);
    // Validate frames that have preset sizes
    // Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }

    // Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation( (screenSize.width - frameSize.width) / 2,
                      (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);

    // Graphen aus der DB laden
    graphs[Const.SlowBig] = per.load(Const.SlowBig+1);
    graphs[Const.FastBig] = per.load(Const.FastBig+1);
    graphs[Const.SlowSmall] = per.load(Const.SlowSmall+1);
    graphs[Const.FastSmall] = per.load(Const.FastSmall+1);
    per.closeConnection();
    if(!per.hasDB()){
      frame.setResult("Graph wurde ohne DB aufgebaut");
    }
    else{
      frame.setResult("Graph wurde mit DB aufgebaut");
    }

    if(!per.hasConfFile()){
      frame.setResult("Konfigruations-File conf.txt wurde nicht gefunden!");
    }

    //graphs[Const.SlowBig] = per.loadohneDB();

    // Logfile schreiben
    File f = new File(filename);
    if (!f.exists()) {
      writeLog("Touchpad Log\n============\n\n");
      writeLog("Gross,Langsam\n");
      writeLog(graphs[0].toString());
      writeLog("\nGross,Schnell\n");
      writeLog(graphs[1].toString());
      writeLog("\nKlein,Langsam\n");
      writeLog(graphs[2].toString());
      writeLog("\nKlein,Schnell\n");
      writeLog(graphs[3].toString());
    }
  }

  /**
   * Application entry point.
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception exception) {
          exception.printStackTrace();
        }

        new touchpad();
      }
    });
  }

  /**
   * Die Methode Calculate wird vom touchpadListener aufgerufen, sobald er einen Timeout hat.
   * Diese Methode verbindet die Analyse der Microgesten mit dem Ermitteln vom Zeichen.
   */
  public void calculate() {
    log="";
    //Analyse durchführen
    Measure m = vGesture.analyse();
    log += "\n\n\nMessung:\n========";
    if (m == null) {
      log += "\nZu wenig Zeichen um eine Analyse durchzuführen\n";
      frame.setResult("Zu wenig Zeichen um eine Analyse durchzuführen");
    }
    else {
      log += m.toString();
      // Resultat ermitteln und ausgeben
      frame.setResult(findSign(m));
    }
    // Log schreiben
    writeLog(log);
  }

  /**
   * Ermittelt aus der Messung ein Zeichen.
   *
   * @param m - Messung
   * @return String - Das ermittelte Zeichen oder eine Fehlermeldung
   */
  private String findSign(Measure m) {

    String result = "";
    LinkedList<LinkedList> lists = new LinkedList<LinkedList> ();
    LinkedList<microGesteMeasure> mGesten = m.mg;
    int level = 0; // Schritt bei in welcher Tiefe man sich im Graphen befindet
    double probability = 0; // Wahrscheinlichkeit vom aktuellen SignContainer
    Iterator<microGesteGraph> itz;
    microGesteGraph mgg;
    SignContainer destination, source;

    //Graph auswählen
    if (m.getDuration() < Const.BORDERTOFASTWRITING) {
      if (Const.LINEHIGHT > Const.BORDERTOSMALLWRITING) {
        graph = graphs[Const.FastBig];
      }
      else {
        graph = graphs[Const.FastSmall];
      }
    }
    else {
      if (Const.LINEHIGHT > Const.BORDERTOSMALLWRITING) {
        graph = graphs[Const.SlowBig];
      }
      else {
        graph = graphs[Const.SlowSmall];
      }
    }

    LinkedList<SignContainer> qn = new LinkedList<SignContainer> ();
    LinkedList<SignContainer> q = new LinkedList<SignContainer> ();

    q.add(new SignContainer(graph.getRootSign(), 1));
    lists.add(q);

    log += "\nGraph:\n";
    while (level < mGesten.size()) {
      qn = new LinkedList<SignContainer> ();
      lists.add(qn);
      q = lists.get(level);
      log += "Level " + level + ":";

      // Alle microGesten aus der Messung abarbeiten
      for (int i = 0; i < q.size(); i++) {
        source = q.get(i);
        log += " " + source.getSign().getID() + "(" + source.getProbability() +
            ")";
        itz = source.getSign().getGesten();
        // Alle Microgesten vom aktuellen Knoten überprüfen, ob es weiterführende Pfade gibt (übereinstimmungen von MicroGesten der Messung und dem Knoten)
        while (itz.hasNext()) {
          mgg = itz.next();
          if (mgg.getType() == mGesten.get(level).getType() &&
              (mgg.getRelLength() == mGesten.get(level).getRelLength() ||
               mgg.getRelLength() == -1)) {
            destination = new SignContainer(mgg.getDestination(), 0);
            probability = mgg.getProbability() * source.getProbability();
            if (probability >= Const.MINPROBABILITY) { // Alle unter Const.MINPROBABILTY ausscheiden
              destination.setProbability(probability);
              qn.add(destination);
            }
          }
        }
      }
      log += "\n";
      level++;
    }

    // Ober -und Unterlängen überprüfen
    if (level == mGesten.size()) {
      qn = new LinkedList();
      q = lists.get(level); // oberste queue nehmen
      log += "Level hight:";
      for (int i = 0; i < q.size(); i++) {
        source = q.get(i);
        log += " " + source.getSign().getID() + "(" + source.getProbability() +
            ")";
        if (source.getSign().hasDescender() == m.hasDescender() &&
            source.getSign().hasAscender() == m.hasAscender() &&
            !source.getSign().getMeaning().equals("")) {
          qn.add(source);
        }
      }
      log += "\n";
    }

    // Resultate Sortieren
    LinkedList<SignContainer> resultList = new LinkedList<SignContainer> ();
    for (int i = 0; i < qn.size(); i++) {
      source = qn.get(i);
      resultList.add(source);
    }
    Collections.sort(resultList);

    // Alle Möglichen Resultate ins Log schreiben
    Iterator<SignContainer> itr = resultList.iterator();
    log += "\nVarianten: ";
    while (itr.hasNext()) {
      source = itr.next();
      log += " " + source.getSign().getMeaning();
      log += "(" + source.getProbability() + ")";
    }
    log += "\n";

    // Endgültiges Resultat ausgeben
    itr = resultList.iterator();
    if (itr.hasNext()) {
      source = itr.next();
      result += "Resultat: " + source.getSign().getMeaning();
      log += "Resultat: " + source.getSign().getMeaning() + "(" +
          source.getProbability() + ")\n";
    }
    else {
      result += " Zeichen nicht erkannt!";
      log += "Resultat: Zeichen nicht erkannt!";
    }

    return result;
  }

  /**
   * Facade um alle Positionen von conditioningBending weiter an das touchpadFrame zu leiten.
   *
   * @return Dot - Array von Dot allen Positionen
   */
  public Dot[] getDots() {
    return vGesture.getDots();
  }

  /**
   * Facade um die Startzeit vom touchpadFrame an conditioningBending weiterzuleiten.
   *
   * @param start - Startzeit
   */
  public void setStartTime(long start) {
    vGesture.setStartTime(start);
  }

  /**
   * Facade um die Endzeit vom touchpadFrame an conditioningBending weiterzuleiten.
   *
   * @param start - Startzeit
   */
  public void setEndTime(long end) {
    vGesture.setEndTime(end);
  }

  /**
   * Facade um ein Punkt vom touchpadFrame an conditioningBending weiterzuleiten.
   *
   * @param start - Startzeit
   */
  public void addDot(Dot d) {
    vGesture.addDot(d);
  }

  /**
   * Facade um die anzahl Punkte von conditioningBending an touchpadFrame  weiterzuleiten.
   *
   * @return int
   */
  public int getDotCounter() {
    return vGesture.getDotCounter();
  }

  /**
   * Facade um den Buffer von conditioningBending zu Lehren, wenn eine neue Messung beginnt
   */
  public void clearBuffer() {
    vGesture.clearBuffer();
  }

  /**
   * Funtkion um ein String ins Logfile zu schreiben.
   *
   * @param log String
   */
  private void writeLog(String log) {
    PrintWriter writeFile;

    try {
      writeFile = new PrintWriter(new FileWriter(filename, true), true);
      writeFile.print(log);
      writeFile.close();
    }
    catch (IOException event) {
      System.exit(1);
    }
  }
}
