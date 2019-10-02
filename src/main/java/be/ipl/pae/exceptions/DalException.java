package be.ipl.pae.exceptions;

public class DalException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DalException() {
    super("Erreur DAL inconnue.");
  }

  public DalException(String message, Throwable cause) {
    super(message, cause.getCause() == null ? cause : cause.getCause());
  }

}
