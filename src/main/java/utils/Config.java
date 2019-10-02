package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {

  private static Properties prop;

  /**
   * Crée un objet Config.
   * 
   * @param path L'adresse du fichier dans le système
   * @throws IOException exception de type IO
   */
  public static void init(String path) throws IOException {
    prop = new Properties();
    Path pathProp = FileSystems.getDefault().getPath(path);
    try (InputStream in = Files.newInputStream(pathProp)) {
      prop.load(in);
    }
  }

  /**
   * Retourne la valeur de la clé du fichier properties.
   * 
   * @param key La clé dont on cherche la valeur
   * @return la valeur de la clé, sinon null
   */
  public static String getValueOfKey(String key) {
    Logs.fine("Recherche de la valeur de la clé '" + key + "' dans les properties");
    if (!Config.prop.containsKey(key)) {
      return null;
    } else {
      return Config.prop.getProperty(key);
    }
  }

}
