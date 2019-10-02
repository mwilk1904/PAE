package be.ipl.pae.exceptions;

public class IhmException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final int statusCode;
  private final String target;

  public IhmException() {
    this("Mauvaises données", null);
  }

  public IhmException(String message) {
    this(message, 401);
  }

  public IhmException(String message, String target) {
    this(message, 401, target);
  }

  public IhmException(String message, int statusCode) {
    this(message, statusCode, null);
  }

  /**
   * crée un objet IhmException.
   * 
   * @param message le message de l'exception.
   * @param statusCode le statut de l'exception.
   * @param target sa cible.
   */
  public IhmException(String message, int statusCode, String target) {
    super(message);
    this.statusCode = statusCode;
    this.target = target;
  }

  /**
   * Renvoie le code HTTP correspondant à l'erreur.
   * 
   * @return int Le code d'erreur HTTP
   */
  public int getStatusCode() {
    return this.statusCode;
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
