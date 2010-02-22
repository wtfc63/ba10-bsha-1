package touchpad;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p>Title: Datenbankanbindung</p>
 *
 * <p>Description: Persistence erstellt liest die Daten aus der Datenbank und erstellt daraus die benötigten Graphen.
 * Die Zurgriffsinformationen für die Datenbank werden aus dem File conf.txt gelesen.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */

public class Persistence {

  private Connection conn = null;
  private String ip = "";
  private String db = "";
  private String username = "";
  private String pw = "";
  private boolean hasDB = false;
  private boolean hasConfFile = false;

  public Persistence() {
    readConfFile();
    if (hasDB) {

      // Datenbanktreiber Suchen
      try {
        Class.forName("com.mysql.jdbc.Driver");
      }
      catch (ClassNotFoundException e) {
        hasDB = false;
        System.out.println("Could not load the driver:\n " + e.toString());
      }

      // Connection zur Datenbank aufbauen
      try {
        conn = DriverManager.getConnection("jdbc:mysql://" + ip + "/" + db,
                                           username, pw);
      }
      catch (SQLException ex) {
        hasDB = false;
        System.err.println("SQLException: " + ex.getMessage());
      }
    }
  }

  /**
   * Graphen aus der Datenbank lesen
   *
   * @param signType - Typ vom Graph der erstellt werden soll.
   * @return AdjListGraph
   */
  public AdjListGraph load(int signType) {

    if (hasDB) {
      AdjListGraph graph = new AdjListGraph();

      try {
        Statement stmt = conn.createStatement();

        ResultSet rset = stmt.executeQuery("select * from sign where quadrant=" +
                                           signType);
        while (rset.next()) {
          graph.addSign(rset.getInt("id"), rset.getString("meaning"),
                           rset.getBoolean("ascender"),
                           rset.getBoolean("descender"));
        }

        rset = stmt.executeQuery(
            "SELECT * FROM microgeste,sign WHERE sourceid = sign.id and sign.quadrant=" +
            signType);
        while (rset.next()) {
          graph.addGeste(rset.getInt("sourceid"), rset.getInt("destinationid"),
                         rset.getInt("type"), rset.getInt("length"),
                         rset.getDouble("probability"));
        }
      }
      catch (SQLException e) {
        System.err.println("SQLException: " + e.getMessage());
      }
      return graph;
    }
    else {
      return loadohneDB();
    }
  }

  /**
   * closeConnection schliesst die Datenbankverbindung
   */
  public void closeConnection() {
    if (hasDB) {

      try {
        conn.close();
      }
      catch (SQLException e) {
        System.err.println("SQLException: " + e.getMessage());
      }
    }
  }

  /**
   * hasDB - überprüft ob der Graph mit einer DB oder lokal aufgebaut wurde
   *
   * @return boolean
   */
  public boolean hasDB(){
    return hasDB;
  }

  /**
   * hasConfFile - gibt an, ob das Konfigurationsfile conf.txt gefunden wurde.
   *
   * @return boolean
   */
  public boolean hasConfFile(){
    return hasConfFile;
  }

  /**
   * loadohneDB - Baut einen minimalen Graph für die Zeichen e,l,o,v auf,
   * wenn keine DB konfiguriert wurde.
   *
   * @return AdjListGraph
   */
  private AdjListGraph loadohneDB() {
    AdjListGraph graph = new AdjListGraph();
    graph.addSign(1, "start", false, false);
    graph.addSign(2, "", false, false);
    graph.addSign(3, "", false, false);
    graph.addSign(4, "l", true, false);
    graph.addSign(5, "e", false, false);
    graph.addSign(8, "", false, false);
    graph.addSign(9, "o", false, false);
    graph.addSign(10, "a", false, false);
    graph.addSign(11, "o", false, false);
    graph.addSign(12, "o", false, false);
    graph.addSign(13, "", false, false);
    graph.addSign(14, "z", true, false);

    graph.addGeste(1, 2, Const.WeakBendingLeft, -1, 0.5);
    graph.addGeste(1, 2, Const.straight, -1, 0.5);
    graph.addGeste(1, 8, Const.WeakBendingLeft, -1, 0.5);
    graph.addGeste(1, 12, Const.WeakBendingLeft, -1, 0.5);
    graph.addGeste(1, 13, Const.StrongBendingLeft, -1, 1);

    graph.addGeste(2, 3, Const.StrongBendingLeft, -1, 1);

    graph.addGeste(3, 4, Const.WeakBendingLeft, -1, 0.9);
    graph.addGeste(3, 5, Const.WeakBendingLeft, -1, 0.9);
    graph.addGeste(3, 8, Const.WeakBendingLeft, -1, 0.9);

    graph.addGeste(4, 4, Const.StrongBendingLeft, -1, 1);
    graph.addGeste(4, 14, Const.StrongBendingLeft, -1, 0.5);

    graph.addGeste(5, 5, Const.StrongBendingLeft, -1, 1);

    graph.addGeste(8, 9, Const.StrongBendingRight, -1, 0.9);
    graph.addGeste(8, 9, Const.peak, -1, 0.9);

    graph.addGeste(9, 10, Const.WeakBendingLeft, 1, 1);
    graph.addGeste(9, 11, Const.WeakBendingLeft, 0, 1);

    graph.addGeste(12, 8, Const.StrongBendingLeft, -1, 1);
    graph.addGeste(13, 5, Const.WeakBendingLeft, -1, 1);

    return graph;
  }

  private void readConfFile() {
    String fileName = "conf.txt";
    BufferedReader fileInput;

    try {
      fileInput = new BufferedReader(new FileReader(fileName));

      hasConfFile=true;
      if (fileInput.readLine().equals("enable")) {
        hasDB = true;
      }
      else {
        hasDB = false;
      }

      ip = fileInput.readLine();
      db = fileInput.readLine();
      username = fileInput.readLine();
      pw = fileInput.readLine();

      fileInput.close();

    }
    catch (IOException event) {
      hasConfFile=false;
      hasDB = false;
    }
  }

}
