package utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Logs {

  private static final int FILE_SIZE = 1024 * 1024;
  private static final Logger LOGGER = Logger.getGlobal();
  private static final boolean ANSI_SUPPORTED =
      Boolean.parseBoolean(Config.getValueOfKey("ansi_support"));

  /**
   * Initialise le logger.
   * 
   * @throws SecurityException si un security manager existe et qu'on appelle le constructeur de
   *         FileHandler sans avoir la permission LoggingPermission("control")
   * @throws IOException si il y a eu un problème IO lors de l'ouverture du fichier logs/logs.log
   */
  public static void init() throws SecurityException, IOException {
    LogManager.getLogManager().reset();
    LOGGER.setLevel(Level.ALL);

    File logsDirectory = new File("logs");
    if (!logsDirectory.exists()) {
      logsDirectory.mkdir();
    }

    LogsFormatter formatter = new LogsFormatter();

    FileHandler fh = new FileHandler("logs/logs.log", FILE_SIZE, 5, true);
    fh.setFormatter(formatter);
    fh.setLevel(Level.FINE);
    LOGGER.addHandler(fh);

    ConsoleHandler ch = new ConsoleHandler();
    ch.setFormatter(formatter);
    ch.setLevel(Level.INFO);
    LOGGER.addHandler(ch);

    info("Logger initialisé");
  }

  /**
   * Log un message de type FINE. À utiliser pour communiquer une information peu importante.
   * 
   * @param message Le message
   */
  public static void fine(String message) {
    LOGGER.log(Level.FINE, message);
  }

  /**
   * Log un message de type INFO. À utiliser pour communiquer une information importante.
   * 
   * @param message le message
   */
  public static void info(String message) {
    LOGGER.log(Level.INFO, message);
  }

  /**
   * Log un message de type WARNING. À utiliser pour communiquer une erreur non fatale.
   * 
   * @param message le message
   */
  public static void warning(String message, Exception exception) {
    if (exception.getCause() == null) {
      LOGGER.log(Level.WARNING, message, exception);
    } else {
      LOGGER.log(Level.WARNING, message, exception.getCause());
    }
  }

  /**
   * Log un message de type SEVERE. À utiliser pour communiquer une erreur fatale.
   * 
   * @param message le message
   */
  public static void severe(String message, Exception exception) {
    if (exception.getCause() == null) {
      LOGGER.log(Level.SEVERE, message, exception);
    } else {
      LOGGER.log(Level.SEVERE, message, exception.getCause());
    }
  }

  private static class LogsFormatter extends Formatter {

    private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {
      StackTraceElement[] steArr = Thread.currentThread().getStackTrace();
      StringBuilder logBuilder = new StringBuilder(1000);
      logBuilder.append(dateFormat.format(new Date(record.getMillis())));
      logBuilder.append(" - [" + record.getLevel() + "]");
      logBuilder.append(" [" + steArr[8].getClassName() + "." + steArr[8].getMethodName() + "] ");
      logBuilder.append(record.getMessage());
      // Si on formatte le message pour la console, on change les couleurs en fonction de
      // l'importance des messages
      boolean isHandledInConsole =
          steArr[3].getClassName().equals(ConsoleHandler.class.getCanonicalName());
      if (isHandledInConsole) {
        if (ANSI_SUPPORTED) {
          addAnsiColorCode(logBuilder, record.getLevel());
        }
      } else if (steArr[8].getClassName()
          .equals(this.getClass().getDeclaringClass().getCanonicalName())) {
        // Si il s'agit du premier message à afficher par Logs, on le précède d'un marqueur dans le
        // fichier .logs afin de plus facilement s'y retrouver en lisant le fichier
        logBuilder.insert(0, "\n" + String.join("", Collections.nCopies(100, "=")) + "\n\n");
      }
      logBuilder.append("\n");
      if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
        StackTraceElement[] exceptionSteArr = record.getThrown().getStackTrace();
        StringBuilder errorLogBuilder = new StringBuilder(10000);
        errorLogBuilder.append("\t" + record.getThrown().getClass().getCanonicalName() + " : "
            + record.getThrown().getMessage() + "\n");
        boolean isSevere = record.getLevel().intValue() == Level.SEVERE.intValue();
        for (StackTraceElement ste : exceptionSteArr) {
          // Si l'exception est fatale, on log toute la stack trace
          // Sinon on log juste la partie correspondant aux classes de l'application
          if (ste.getLineNumber() >= 1
              && (isSevere || ste.getClassName().matches("(be.ipl.pae|utils).*"))) {
            errorLogBuilder.append("\t\tà " + ste.toString() + "\n");
          }
        }
        if (ANSI_SUPPORTED && isHandledInConsole) {
          addAnsiColorCode(errorLogBuilder, record.getLevel());
        }
        return logBuilder.toString() + errorLogBuilder.toString();
      }
      return logBuilder.toString();
    }

    /**
     * Ajoute les codes ANSI nécessaires pour afficher un message en couleur dans la console.
     * 
     * @param builder Le constructeur du message
     * @param level Le niveau d'importance du message duquel dépend la couleur
     */
    public void addAnsiColorCode(StringBuilder builder, Level level) {
      switch (level.toString()) {
        case "SEVERE":
          builder.insert(0, "\u001B[31;1m"); // Texte en rouge
          break;
        case "WARNING":
          builder.insert(0, "\u001B[35;1m"); // Texte en mauve
          break;
        default:
          builder.insert(0, "\u001B[30m"); // Texte en noir
      }
      builder.append("\u001B[0m"); // Reset les couleurs
    }

  }

}
