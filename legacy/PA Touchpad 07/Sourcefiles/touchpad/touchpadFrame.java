package touchpad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Dimension;

/**
 * <p>Title: GUI für die Ausgabe zum Benutzer</p>
 *
 * <p>Description: Schnittstelle zum Benutzer</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */
public class touchpadFrame
    extends JFrame implements MouseMotionListener, MouseListener {
  JPanel contentPane;
  BorderLayout borderLayout1 = new BorderLayout();

  private int x = 0, y = 0;
  private touchpad touchpad;
  private TouchpadListener tpl = new TouchpadListener(this);
  private String result = "";

  /**
   * touchpadFrame
   *
   * @param touchpad - Schnittstelle zum Programm
   */
  public touchpadFrame(touchpad touchpad) {

    this.touchpad = touchpad;
    try {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      jbInit();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Component initialization.
   *
   * @throws java.lang.Exception
   */
  private void jbInit() throws Exception {
    contentPane = (JPanel) getContentPane();
    contentPane.setLayout(borderLayout1);
    setSize(new Dimension(400, 300));
    setTitle("Touchpad");
    addMouseMotionListener(this);
    addMouseListener(this);
  }

  /**
   * Zeichnet das ganze GUI
   */
  public void paint(Graphics g) {
    int dotCounter = touchpad.getDotCounter();
    Dot[] pos = touchpad.getDots();

    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(0, 0, 400, 300);

    g.setColor(Color.GRAY);
    g.drawLine(50, Const.ZEROLINE - 2 * Const.LINEHIGHT, 350,
               Const.ZEROLINE - 2 * Const.LINEHIGHT);
    g.drawLine(50, Const.ZEROLINE - Const.LINEHIGHT, 350,
               Const.ZEROLINE - Const.LINEHIGHT);
    g.setColor(Color.BLACK);
    g.drawLine(50, Const.ZEROLINE, 350, Const.ZEROLINE);
    g.setColor(Color.GRAY);
    g.drawLine(50, Const.ZEROLINE + Const.LINEHIGHT, 350,
               Const.ZEROLINE + Const.LINEHIGHT);

    for (int i = 0; i < dotCounter; i++) {
      g.setColor(pos[i].getColor());
      g.drawRect(pos[i].getX(), -pos[i].getY(), 1, 1);
    }

    g.setColor(Color.BLACK);
    g.drawString(result, 10, 40);

  }

  /**
   * Stellt Bewegungen der Maus fest, und Übergibt die aktuelle Mausposition an die Klasse "touchpad".
   * Startet eine Messung, wenn nicht bereits eine am laufen ist.
   */
  public void mouseDragged(MouseEvent me) {

    if ( ( (me.getX() != x) || (me.getY() != y)) && me.getY() < 260 &&
        me.getX() < 400) {
      x = me.getX();
      y = me.getY();
    }

    touchpad.addDot(new Dot(x, -y));

    // Wenn der Listener noch aktiv ist, den brauchen, ansosnten den Listener aktivieren.
    if (tpl.isAlive()) {
      tpl.addPos(x, -y);
    }
    else {
      touchpad.clearBuffer();
      touchpad.setStartTime(System.currentTimeMillis());
      tpl = new TouchpadListener(this);
      tpl.start();
      tpl.addPos(x, -y);
    }
    repaint();
  }

  /**
   * Stellt das Klicken der Maus fest, und Übergibt die aktuelle Mausposition an die Klasse "touchpad".
   * Startet eine Messung, wenn nicht bereits eine am laufen ist.
   */
  public void mousePressed(MouseEvent me) {
    if ( ( (me.getX() != x) || (me.getY() != y)) && me.getY() < 260 &&
        me.getX() < 400) {
      x = me.getX();
      y = me.getY();
    }

    touchpad.addDot(new Dot(x, -y));

    if (tpl.isAlive()) {
      tpl.addPos(x, -y);
    }
    else {
      touchpad.clearBuffer();
      touchpad.setStartTime(System.currentTimeMillis());
      tpl = new TouchpadListener(this);
      tpl.start();
      tpl.addPos(x, -y);
    }

    repaint();
  }

  public void mouseEntered(MouseEvent me) {}

  public void mouseExited(MouseEvent me) {}

  public void mouseReleased(MouseEvent me) {}

  public void mouseMoved(MouseEvent me) {}

  public void mouseClicked(MouseEvent me) {}

  /**
   * Stopt die Messung, und startet die Auswertung. Wird vom TouchpadListener aufgerufen, sobald der Timeout eintritt.
   */
  public void touchpadReleased() {
    touchpad.calculate();
    repaint();
  }

  /**
   * Resultat einer Messung dem Benutzer ausgeben
   *
   * @param s - Resultat als String
   */
  public void setResult(String s) {
    result = s;
  }

  /**
   * Endzeit der Messung setzen. (touchpadFrame dient als Facade zum touchpad)
   *
   * @param end - Endzeit
   */
  public void setEndTime(long end) {
    touchpad.setEndTime(end);
  }

}
