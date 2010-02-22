package touchpad;

/**
 * <p>Title: Queue</p>
 *
 * <p>Description: Wird zum zwischenspeichern von Dots gebraucht beim touchpadListener.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */
public class Queue {

  private Dot[] elements;
  private int l = 0, u = 0, noOfItems = 0;

  public Queue() {
    elements = new Dot[60];
  }

  public int size() {
    return noOfItems;
  }

  public Dot peek() {
    if (isEmpty()) {
      return null;
    }
    else {
      return elements[l];
    }

  }

  public void enqueue(Dot o) {
    // Buffer voll
    if (noOfItems >= elements.length) {
      System.out.println("Buffer Voll");
    }
    else {
      elements[u] = o;
      u = (u + 1) % elements.length;
      noOfItems++;
    }
  }

  public Dot dequeue() {
    if (isEmpty()) {
      return null;
    }
    else {
      Dot d = elements[l];
      elements[l] = null;
      // Unterster Zeiger aktualisieren
      l = (l + 1) % elements.length;
      noOfItems--;
      return d;
    }
  }

  public boolean isEmpty() {
    if (noOfItems == 0) {
      return true;
    }
    return false;
  }

  public String toString(){
    String ret="Queue: u:"+u+"\n";
    if(noOfItems>0){
      for(int i=0; i<elements.length; i++){
        if(elements[i]!=null){
          ret += "   "+i+" "+elements[i].toString()+"\n";
        }
      }
    }
    else{
      ret+="Queue ist leer\n";
    }

    return ret;
  }

}
