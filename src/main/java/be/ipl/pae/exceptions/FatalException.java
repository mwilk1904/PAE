package be.ipl.pae.exceptions;

public class FatalException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public FatalException() {
    super("Erreur Fatal.");
  }

  public FatalException(String message) {
    super(message);
  }

  public FatalException(String message, Throwable cause) {
    super(message, cause.getCause() == null ? cause : cause.getCause());
  }

}
