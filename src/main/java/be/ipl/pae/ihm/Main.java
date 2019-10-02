package be.ipl.pae.ihm;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import utils.Config;
import utils.Logs;

import java.io.IOException;

public class Main {

  private static WebAppContext context;
  private static Creator creator;

  /**
   * Exécute le serveur.
   * 
   * @param args Les arguments de la ligne de commande
   * @throws IOException l'exception de type IO.
   * @throws SecurityException l'exception de type Security
   * @throws Exception si une erreur a été rencontrée.
   */
  public static void main(String[] args) throws SecurityException, IOException {
    Config.init("prod.properties");
    Logs.init();
    Logs.info("Lancement de l'injection dépendance");
    try {
      creator = new Creator(Servlet.class);
      context = new WebAppContext();
      context.addServlet(new ServletHolder(creator.getServlet()), "/");
      context.setResourceBase("www");
      Server server = new Server(Integer.parseInt(Config.getValueOfKey("port")));
      server.setHandler(context);
      Logs.info("Démarrage du serveur");
      try {
        server.start();
      } catch (Exception fe) {
        Logs.severe(fe.getMessage(), fe);
        System.exit(1);
      }

    } catch (Exception ex) {
      Logs.severe("Erreur lors de l'injection dépendance", ex);
      System.exit(1);
    }

  }

}
