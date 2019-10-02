package be.ipl.pae.ihm;

import be.ipl.pae.biz.dto.MobilityCsvDto;
import be.ipl.pae.biz.impl.MobilityCsvImpl;
import be.ipl.pae.exceptions.FatalException;
import be.ipl.pae.exceptions.IhmException;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.owlike.genson.Genson;

import utils.Config;
import utils.Logs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletHelper {

  private static String jwtSecret;
  private static Genson genson;

  /**
   * Initialise le ServletHelper.
   */
  public static void init() {
    genson = new Genson();
    jwtSecret = Config.getValueOfKey("jwt_secret");
  }

  /**
   * Crée une token JWT à partir d'un id d'utilisateur.
   * 
   * @param userId L'id de l'utilisateur
   * @return La token JWT créée
   */
  public static String createToken(int userId) {
    Logs.fine("Création de la token JWT pour l'utilisateur " + userId);
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("id", userId);
    return new JWTSigner(jwtSecret).sign(claims);
  }

  /**
   * Supprime un cookie.
   * 
   * @param cookieName Le nom du cookie
   * @param resp La réponse HTTP à modifier pour effacer le cookie contenant la JWT
   */
  public static void removeCookie(String cookieName, HttpServletResponse resp) {
    Logs.fine("Suppression de la token JWT des cookies");
    addCookie(cookieName, "", 0, resp);
  }

  /**
   * Ajoute un cookie à une réponse HTTP.
   * 
   * @param resp La reponse HTTP
   * @param name Le nom du cookie
   * @param value La valeur du cookie
   * @param maxAge Le temps avant que le cookie expire
   */
  public static void addCookie(String name, String value, int maxAge, HttpServletResponse resp) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setMaxAge(maxAge);
    resp.addCookie(cookie);
  }

  /**
   * Recherche un cookie dans la requête et renvoie sa valeur.
   * 
   * @param cookieName Le nom du cookie
   * @param req La requête HTTP
   * @return la valeur du cookie
   */
  public static String getCookieValue(String cookieName, HttpServletRequest req) {
    Logs.fine("Recherche du cookie " + cookieName);
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie c : cookies) {
        Logs.fine("Cookie trouvé: " + c.getName() + "=" + c.getValue());
        if (cookieName.equals(c.getName())) {
          return c.getValue();
        }
      }
      throw new IhmException("Aucun cookie " + cookieName + " trouvé dans la requête");
    }
    throw new IhmException("Aucun cookie trouvé dans la requête");
  }

  /**
   * Cherche et renvoie l'id de l'utilisateur présent dans la jwt.
   * 
   * @param jwt La JWT
   * @param req La requête HTTP
   * @param resp La réponse HTTP
   * @return int L'id d'utilisateur
   * @throws IhmException si il y a une erreur non fatale lors de la vérification de la JWT
   * @throws FatalException si il y a une erreur fatale lors de la vérification de la JWT
   */
  public static int getIdFromJwt(String jwt, HttpServletRequest req, HttpServletResponse resp) {
    Logs.fine("Recherche de la JWT dans les cookies");
    int id = 0;
    if (jwt == null) {
      Logs.fine("JWT illégale: null");
    }
    Logs.fine("Vérification de la JWT obtenue");
    try {
      Map<String, Object> claims = new JWTVerifier(jwtSecret).verify(jwt);
      id = (int) claims.get("id");
      Logs.fine("JWT vérifiée: id = " + id);
      return id;
    } catch (SignatureException | IllegalStateException | JWTVerifyException jve) {
      removeCookie("token", resp);
      removeCookie("teacherToken", resp);
      throw new IhmException("Erreur lors de la vérification de la JWT");
    } catch (Exception fe) {
      throw new FatalException("Erreur fatale lors de la vérification de la JWT.", fe);
    }
  }

  /**
   * Désérialise un JSON.
   * 
   * @param data Le JSON à désérialiser
   * @param clazz Le type vers lequel désérialiser
   * @return Object L'objet issu de la désérialisation
   * @throws IhmException si aucune donnée fournie, ou si les données fournies ne sont pas
   *         désérialisables vers le type voulu
   */
  public static Object deserialize(String data, Class<?> clazz) {
    if (data == null) {
      throw new IhmException("Aucune donnée fournie", 400);
    }
    try {
      return genson.deserialize(data, clazz);
    } catch (Exception ex) {
      throw new IhmException("Les données fournies n'ont pas pu être désérialisées vers le type "
          + clazz.getSimpleName(), 400);
    }
  }

  /**
   * Vérifie si l'utilisateur connecté est un professeur.
   * 
   * @param req La requête HTTP
   * @return boolean Si l'utilisateur connecté est un professeur
   */
  public static boolean isTeacher(HttpServletRequest req) {
    return (boolean) req.getAttribute("isTeacher");
  }

  /**
   * Vérifie si une adresse email est au bon format.
   * 
   * @param email L'adresse email
   * @return Si l'adresse email est au bon format
   */
  public static boolean verifyEmail(String email) {
    // Même expression régulière que celle utilisée par le bootstrapValidator de jQuery
    Pattern pattern = Pattern.compile(
        "^(([^<>()\\[\\]\\\\.,;:\\s@\\\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\\\"]+)*)|(\\\".+\\\"))"
            + "@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?"
            + "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
    return pattern.matcher(email).matches();
  }

  /**
   * Vérifie si un numéro de carte bancaire est au bon format.
   * 
   * @param bankCardNumber Le numéro de carte bancaire
   * @return Si le numéro de carte bancaire est au bon format
   */
  public static boolean verifyBankCardNumber(String bankCardNumber) {
    Pattern pattern = Pattern.compile("^BE ?\\d{2}( ?\\d{4}){3}$", Pattern.CASE_INSENSITIVE);
    return pattern.matcher(bankCardNumber).matches();
  }

  /**
   * Renvoie un id d'utilisateur ou d'étudiant accompagné d'un booléen indiquant ce que cet id est.
   * 
   * @param data Un JSON contenant un id d'étudiant, ou étant à null
   * @param req La requête HTTP
   * @param resp La réponse HTTP
   * @return [id, isStudentId] l'id et un booléen indiquant s'il s'agit d'un id d'étudiant, sinon
   *         c'est un id d'utilisateur
   */
  public static Object[] getRequiredIdInformations(String data, HttpServletRequest req,
      HttpServletResponse resp) {
    String jwt = ServletHelper.getCookieValue("token", req);
    int id = ServletHelper.getIdFromJwt(jwt, req, resp);
    boolean isStudentId = false;
    if (data != null) {
      if (!ServletHelper.isTeacher(req)) {
        throw new IhmException("L'utilisateur n'est pas un professeur", 403);
      }
      @SuppressWarnings("unchecked")
      Map<String, Object> dataMap =
          (Map<String, Object>) ServletHelper.deserialize(data, Map.class);
      id = ((Long) dataMap.get("studentId")).intValue();
      isStudentId = true;
    }
    return new Object[] {id, isStudentId};
  }

  /**
   * Construit une page HTML sur base des fichiers HTML, JS et CSS présents dans www.
   * 
   * @return La page construite
   */
  public static byte[] buildHtmlPage() {
    Logs.info("Construction de la page HTML");
    Path path = Paths.get("www/index.html");
    try {
      Logs.fine("Lecture du fichier www/index.html");
      List<String> lines = Files.readAllLines(path);
      int insertIndex = 0;
      Pattern stylePattern = Pattern.compile("\"/?(style/.+.css)\"");
      Pattern scriptPattern = Pattern.compile("\"/?(scripts/.+.js)\"");
      for (String line : lines) {
        Matcher scriptMatcher = scriptPattern.matcher(line);
        if (scriptMatcher.find()) {
          Logs.fine("Balise script trouvée: " + line.trim());
          Logs.fine("Lecture et insertion du fichier www/" + scriptMatcher.group(1));
          Path scriptPath = Paths.get("www/" + scriptMatcher.group(1));
          lines.set(lines.indexOf(line),
              "<script>" + new String(Files.readAllBytes(scriptPath)) + "</script>");
        } else {
          Matcher styleMatcher = stylePattern.matcher(line);
          if (styleMatcher.find()) {
            Logs.fine("Balise style trouvée: " + line.trim());
            Logs.fine("Lecture et insertion du fichier www/" + styleMatcher.group(1));
            Path stylePath = Paths.get("www/" + styleMatcher.group(1));
            lines.set(lines.indexOf(line),
                "<style>" + new String(Files.readAllBytes(stylePath)) + "</style>");
          } else if (line.matches(".*INSERT_HERE.*")) {
            insertIndex = lines.indexOf(line);
            Logs.fine("Index d'insertion des pages HTML trouvé: " + insertIndex);
          }
        }
      }
      File[] pagesNames = new File("www/pages").listFiles();
      for (File page : pagesNames) {
        Path pagePath = Paths.get(page.getPath());
        Logs.fine("Lecture et insertion du fichier " + pagePath.toString().replace("\\", "/"));
        lines.addAll(insertIndex, Files.readAllLines(pagePath));
      }
      return String.join("\n", lines).getBytes();
    } catch (IOException ioe) {
      throw new FatalException("Erreur lors de la lecture d'un fichier du répertoir www", ioe);
    }
  }

  /**
   * Génère un CSV à partir d'une liste de demandes de mobilité.
   * 
   * @param data Un JSON contenant la liste à transformer
   * @param req La requête HTTP
   * @return Le CSV
   */
  public static String generateCsv(String data, HttpServletRequest req) {
    MobilityCsvDto[] mobs =
        (MobilityCsvDto[]) ServletHelper.deserialize(data, MobilityCsvImpl[].class);
    List<String> lines = new ArrayList<String>();
    for (MobilityCsvDto mob : mobs) {
      String line =
          mob.getApplicationNumber() + "," + mob.getLastName() + " " + mob.getFirstName() + ","
              + mob.getDepartment() + "," + mob.getPreferenceOrderNumber() + "," + mob.getProgram()
              + "," + mob.getMobilityCode() + "," + mob.getSemester() + "," + mob.getPartner();
      lines.add(line);
    }
    return String.join("\n", lines);
  }

}
