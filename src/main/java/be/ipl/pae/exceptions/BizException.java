package be.ipl.pae.exceptions;

public class BizException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final String target;

  public BizException() {
    this("Erreur Biz inconnue.", null);
  }

  public BizException(String message) {
    this(message, null);
  }

  public BizException(String message, String target) {
    super(message);
    this.target = target;
  }

  /**
   * Renvoie le nom de ce qui a causé l'erreur.
   * 
   * @return String Ce qui a causé l'erreur
   */
  public String getTarget() {
    return this.target;
  }

}
