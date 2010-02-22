package touchpad;

/**
 * <p>Title: Touchpad Listener</p>
 *
 * <p>Description: Nimmt die Koordinaten vom touchpadFrame entgegen und dient als zwischenspeicher.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */

public class TouchpadListener
    extends Thread {

  private Queue dotQueue = new Queue();
  private touchpadFrame frame;
  private boolean runner = true;

  public TouchpadListener(touchpadFrame frame) {
    this.frame = frame;
  }

  /**
   * Position hinzufügen
   *
   * @param x - X-Koordinate
   * @param y - Y-Koordinate
   */
  public synchronized void addPos(int x, int y) {
    dotQueue.enqueue(new Dot(x, y));
  }

  /**
   * Thread vom touchpadListener
   */
  public void run() {
    Dot p;

    while (runner) {
      if (dotQueue.peek() == null) {
        runner = false;
      }
      while (dotQueue.peek() != null) {
        p = getPosition();
      }

      try {
        sleep(Const.TIMESLOT);
      }
      catch (InterruptedException e) {}

    }
    frame.setEndTime(System.currentTimeMillis() - Const.TIMESLOT);
    frame.touchpadReleased();
  }

  /**
   * Position von der Queue nehmen
   *
   * @return Dot
   */
  private synchronized Dot getPosition() {
    return (Dot) dotQueue.dequeue();
  }

}
