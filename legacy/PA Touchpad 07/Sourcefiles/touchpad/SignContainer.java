package touchpad;

/**
 * <p>Title: Kontainer für die Zeichen</p>
 *
 * <p>Description: Der Kontainer wird benötigt, da beim ermitteln des Buchstabens, das gleiche Zeichen
 * auf mehreren Pfaden erreicht wird, und dadurch unterschiedliche Wahrscheinlichkeiten hat.</p>
 *
 * <p>Copyright: Copyright (c) 2007 ZHW</p>
 *
 * @author Daniel Fernandez
 * @author David Kölbener
 * @version 1.0
 */
public class SignContainer
    implements Comparable<SignContainer> {

  private Sign s;
  private double probability;

  /**
   * SignContainer
   *
   * @param s - Zeichen
   * @param probability - Wahrscheinlichkeit das es dieses Zeichen ist.
   */
  public SignContainer(Sign s, double probability) {
    this.s = s;
    this.probability = probability;
  }

  public void setSign(Sign s) {
    this.s = s;
  }

  public void setProbability(double probability) {
    this.probability = probability;
  }

  public Sign getSign() {
    return s;
  }

  public double getProbability() {
    return probability;
  }

  /**
   * compareTo - Container lassen sich nach ihrer Wahrscheinlichkeit sortieren. Dadurch wird das Zeichen mit der grössten Wahrscheinlichkeit ermittelt.
   *
   * @param s SignContainer
   * @return int
   */
  public int compareTo(SignContainer s) {
    return (int) (probability - s.getProbability());
  }

}
