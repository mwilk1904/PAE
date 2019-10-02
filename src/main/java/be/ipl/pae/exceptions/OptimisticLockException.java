package be.ipl.pae.exceptions;

public class OptimisticLockException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public OptimisticLockException() {
    super("Erreur Optimistic lock");
  }

  public OptimisticLockException(String message) {
    super(message);
  }

  public OptimisticLockException(String message, Throwable cause) {
    super(message, cause.getCause() == null ? cause : cause.getCause());
  }
}
