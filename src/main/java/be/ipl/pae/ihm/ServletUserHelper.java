package be.ipl.pae.ihm;

import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.impl.UserImpl;
import be.ipl.pae.biz.interfaces.UserUcc;
import be.ipl.pae.exceptions.IhmException;

import utils.Logs;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUserHelper {

  private static UserUcc userUcc;

  /**
   * Initialise le ServletUserHelper.
   * 
   * @param userUcc L'UserUcc avec lequel interagir
   */
  public static void init(UserUcc userUcc) {
    ServletUserHelper.userUcc = userUcc;
  }

  /**
   * Permet l'authentification d'un utilisateur.
   * 
   * @param req request.
   * @param resp response.
   * @param token la token, à null si inconnue
   * @return un objet UserDto.
   */
  public static UserDto authenticate(HttpServletRequest req, HttpServletResponse resp,
      String token) {
    Logs.info("Traitement de la requête de connection par JWT");
    String jwt = token == null ? ServletHelper.getCookieValue("token", req) : token;
    int userId = ServletHelper.getIdFromJwt(jwt, req, resp);
    Logs.fine("Authentification: vérification de l'id trouvé dans la JWT (" + userId + ")");
    if (userId < 1) {
      Logs.fine("Authentification: mauvais id trouvé, suppression du cookie JWT");
      ServletHelper.removeCookie("token", resp);
      ServletHelper.removeCookie("teacherToken", resp);
      throw new IhmException("Authentification impossible: mauvais id dans la JWT.");
    }
    UserDto userData = userUcc.authentification(userId);
    Logs.info("Authentification réussie: id=" + userId + " role=" + userData.getRole());
    return userData;
  }

  /**
   * Permet la connexion d'un utilisateur.
   * 
   * @param data les données.
   * @return un objet UserDto
   */
  public static UserDto login(String data) {
    @SuppressWarnings("unchecked")
    Map<String, Object> dataMap = (Map<String, Object>) ServletHelper.deserialize(data, Map.class);
    String pseudo = (String) dataMap.get("pseudo");
    if (pseudo == null) {
      throw new IhmException("Ce champ ne peut pas être vide.", "pseudo");
    }
    String password = (String) dataMap.get("password");
    if (password == null) {
      throw new IhmException("Ce champ ne peut pas être vide.", "password");
    }
    Logs.info("Traitement de la requête de connection: pseudo=" + pseudo + " password=" + password);
    return userUcc.login(pseudo, password);
  }

  /**
   * Permet à un professeur de se connecter en tant qu'étudiant.
   * 
   * @param data Les données
   * @param req La requête HTTP
   * @return l'userDto associé à l'étudiant
   */
  public static UserDto loginAsStudent(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête de connection en tant qu'étudiant");
    if (!ServletHelper.isTeacher(req)) {
      throw new IhmException("L'utilisateur n'est pas un professeur", 403);
    }
    @SuppressWarnings("unchecked")
    Map<String, Long> dataMap = (Map<String, Long>) ServletHelper.deserialize(data, Map.class);
    int userId = dataMap.get("userId").intValue();
    if (userId < 1) {
      throw new IhmException("L'id d'utilisateur fourni est incorrect");
    }
    UserDto studentData = userUcc.authentification(userId);
    if (!"student".equals(studentData.getRole())) {
      throw new IhmException("L'utilisateur associé à l'id " + userId + " n'est pas un étudiant");
    }
    return studentData;
  }

  /**
   * Permet l'inscription d'un utilisateur.
   * 
   * @param data les données.
   * @return une méthode de UserUcc.
   */
  public static UserDto signup(String data) {
    Logs.info("Traitement du formulaire d'inscription: " + data);
    UserDto userData = (UserImpl) ServletHelper.deserialize(data, UserImpl.class);
    Logs.fine("Inscription: vérification de l'adresse email " + userData.getEmail());
    if (!ServletHelper.verifyEmail(userData.getEmail())) {
      throw new IhmException("L'adresse e-mail est incorrecte.", "email");
    }
    Logs.fine("Inscription: vérification du pseudo " + userData.getPseudo());
    if (userData.getPseudo() == null || userData.getPseudo().length() < 2) {
      throw new IhmException("Ce champ doit contenir au moins 2 caractères.", "pseudo");
    }
    Logs.fine("Inscription: vérification du mot de passe " + userData.getPassword());
    if (userData.getPassword() == null || userData.getPassword().length() < 5) {
      throw new IhmException("Ce champ doit contenir au moins 5 caractères.", "password");
    }
    Logs.fine("Inscription: vérification du prénom " + userData.getFirstName());
    if (userData.getFirstName() == null) {
      throw new IhmException("Ce champ ne peut pas être vide.", "firstName");
    }
    Logs.fine("Inscription: vérification du nom de famille " + userData.getLastName());
    if (userData.getLastName() == null || userData.getLastName().length() < 2) {
      throw new IhmException("Ce champ doit contenir au moins 2 caractères.", "lastName");
    }
    return userUcc.signUp(userData);
  }

  /**
   * Se déconnecte.
   * 
   * @param resp la requête.
   */
  public static void logout(HttpServletResponse resp) {
    Logs.info("Traitement de la requête de déconnection");
    ServletHelper.removeCookie("token", resp);
    ServletHelper.removeCookie("teacherToken", resp);
  }

  /**
   * Modifie un professeur.
   * 
   * @param data les données.
   * @param req la requete.
   */
  public static void setTeacher(String data, HttpServletRequest req) {
    if (!(boolean) req.getAttribute("isRespTeacher")) {
      throw new IhmException("L'utilisateur n'est pas un professeur responsable", 403);
    }
    @SuppressWarnings("unchecked")
    Map<String, Long> dataMap = (Map<String, Long>) ServletHelper.deserialize(data, Map.class);
    int userId = dataMap.get("userId").intValue();
    userUcc.setTeacher(userId);
  }

  public static List<UserDto> getUsers() {
    Logs.info("Traitement de la requête d'envoi des utilisateurs");
    return userUcc.getUsers();
  }

  public static List<UserDto> getStudents() {
    Logs.info("Traitement de la requête d'envoi des utilisateurs étant des étudiants");
    return userUcc.getUsersWithStudentRole();
  }

}
