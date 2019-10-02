package be.ipl.pae.ihm;

import be.ipl.pae.biz.dto.CountryDto;
import be.ipl.pae.biz.dto.MobilityAllDto;
import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.dto.MobilityPostulantDto;
import be.ipl.pae.biz.dto.PartnerDto;
import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.interfaces.CountryUcc;
import be.ipl.pae.biz.interfaces.MobilityAllUcc;
import be.ipl.pae.biz.interfaces.MobilityPostulantUcc;
import be.ipl.pae.biz.interfaces.MobilityUcc;
import be.ipl.pae.biz.interfaces.PartnerUcc;
import be.ipl.pae.biz.interfaces.ProgramUcc;
import be.ipl.pae.biz.interfaces.StudentUcc;
import be.ipl.pae.biz.interfaces.UserUcc;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.FatalException;
import be.ipl.pae.exceptions.IhmException;
import be.ipl.pae.exceptions.OptimisticLockException;

import com.owlike.genson.Genson;

import org.eclipse.jetty.servlet.DefaultServlet;

import utils.Config;
import utils.Logs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends DefaultServlet {

  private static final long serialVersionUID = 1L;
  private static int COOKIEMAXAGE;

  private byte[] htmlPage;
  private boolean htmlCaching;
  private Genson genson = new Genson();
  private UserUcc userUcc;
  private StudentUcc studentUcc;
  private MobilityUcc mobilityUcc;
  private PartnerUcc partnerUcc;
  private CountryUcc countryUcc;
  private ProgramUcc programUcc;
  private MobilityPostulantUcc mobilityPostulantUcc;
  private MobilityAllUcc mobilityAllUcc;

  /**
   * Crée un objet Servlet.
   * 
   * @param userUcc le user.
   * @param studentUcc les ucs de l'étudiant.
   * @param mobilityUcc les ucs de la mobilité.
   * @param partnerUcc les ucs du partenaire.
   * @param countryUcc les ucs du pays.
   * @param mobilityPostulantUcc les ucs d'une mobilité spéciale.
   * @param mobilityAllUcc les ucs d'une mobilité spéciale.
   * @param programUcc les ucs du programme.
   */
  public Servlet(UserUcc userUcc, StudentUcc studentUcc, MobilityUcc mobilityUcc,
      PartnerUcc partnerUcc, CountryUcc countryUcc, MobilityPostulantUcc mobilityPostulantUcc,
      MobilityAllUcc mobilityAllUcc, ProgramUcc programUcc) {
    super();
    this.userUcc = userUcc;
    this.studentUcc = studentUcc;
    this.mobilityUcc = mobilityUcc;
    this.partnerUcc = partnerUcc;
    this.countryUcc = countryUcc;
    this.mobilityPostulantUcc = mobilityPostulantUcc;
    this.mobilityAllUcc = mobilityAllUcc;
    this.programUcc = programUcc;
  }

  @Override
  public void init() {
    Logs.info("Initialisation de la servlet");
    try {
      super.init();
    } catch (UnavailableException ua) {
      Logs.severe(ua.getMessage(), ua);
      System.exit(1);
    }
    COOKIEMAXAGE = Integer.parseInt(Config.getValueOfKey("cookie_max_age"));

    Logs.info("Initialisation des helpers de la servlet");
    ServletHelper.init();
    ServletMobilityHelper.init(mobilityUcc, partnerUcc, countryUcc, mobilityAllUcc, programUcc);
    ServletStudentHelper.init(studentUcc, mobilityPostulantUcc);
    ServletUserHelper.init(userUcc);

    // On met la page HTML en cache si la mise en cache est autorisée
    this.htmlCaching = Boolean.parseBoolean(Config.getValueOfKey("html_cache"));
    Logs.info("Mise en cache des fichiers front-end " + (htmlCaching ? "activée" : "désactivée"));
    if (htmlCaching) {
      this.htmlPage = ServletHelper.buildHtmlPage();
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    String uri = req.getRequestURI();
    Logs.info("Requête HTTP GET " + uri + ": IP " + req.getRemoteAddr());
    try {
      switch (uri) {
        case "/":
          sendResponse(resp, 200, "text/html",
              htmlCaching ? htmlPage : ServletHelper.buildHtmlPage());
          break;
        default:
          super.doGet(req, resp);
      }
    } catch (Exception ex) {
      Logs.severe(ex.getMessage(), ex);
      System.exit(1);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String data = req.getParameter("data");
    String action = req.getParameter("action");
    Logs.info("Requête HTTP POST \"" + action + "\" reçue: " + data);
    try {
      if (action == null) {
        throw new IhmException("Vous ne pouvez pas laisser l'action vide", 400);
      }
      // Actions ne nécessitant pas qu'un utilisateur soit connecté
      switch (action) {
        case "isconnected":
          UserDto authUserData = ServletUserHelper.authenticate(req, resp, null);
          saveAndSendUserData(authUserData, resp);
          return;
        case "login":
          UserDto loginUserData = ServletUserHelper.login(data);
          saveAndSendUserData(loginUserData, resp);
          return;
        case "signUp":
          UserDto signupUserData = ServletUserHelper.signup(data);
          saveAndSendUserData(signupUserData, resp);
          return;
        case "logout":
          ServletUserHelper.logout(resp);
          return;
        default:
          break;
      }
      // Actions nécessitant qu'un utilisateur soit connecté
      UserDto user = ServletUserHelper.authenticate(req, resp, null);
      req.setAttribute("user", user);
      req.setAttribute("isTeacher", !"student".equals(user.getRole()));
      req.setAttribute("isRespTeacher", "resp_teacher".equals(user.getRole()));
      switch (action) {
        case "getStudentMobilities":
          List<MobilityDto> mobilities =
              ServletMobilityHelper.getStudentMobilities(data, req, resp);
          sendResponse(resp, 200, "application/json", genson.serializeBytes(mobilities));
          break;
        case "declareStudentMobilities":
          MobilityDto[] declaredMobilities = ServletMobilityHelper.declareMobilities(data, req);
          sendResponse(resp, 200, "application/json", genson.serializeBytes(declaredMobilities));
          break;
        case "getStudentData":
          StudentDto studentData = ServletStudentHelper.getStudentData(data, req, resp);
          sendResponse(resp, 200, "application/json", genson.serializeBytes(studentData));
          break;
        case "updateStudentData":
          ServletStudentHelper.updateStudentData(data, req, resp);
          sendResponse(resp, 200, "text/plain", "ok".getBytes());
          break;
        case "getPartners":
          List<PartnerDto> partners = ServletMobilityHelper.getPartners();
          sendResponse(resp, 200, "application/json", genson.serializeBytes(partners));
          break;
        case "getPartnerData":
          PartnerDto partnerData = ServletMobilityHelper.getPartnerData(data, req);
          sendResponse(resp, 200, "application/json", genson.serializeBytes(partnerData));
          break;
        case "getCancellationReasons":
          List<String> reasons = ServletMobilityHelper.getCancellationReasons();
          sendResponse(resp, 200, "application/json", genson.serializeBytes(reasons));
          break;
        case "getCountries":
          List<CountryDto> countries = ServletMobilityHelper.getCountries();
          sendResponse(resp, 200, "application/json", genson.serializeBytes(countries));
          break;
        case "getMobility":
          MobilityDto mobility = ServletMobilityHelper.getMobility(data, req);
          sendResponse(resp, 200, "application/json", genson.serializeBytes(mobility));
          break;
        case "cancelMobility":
          ServletMobilityHelper.cancelMobility(data, req);
          sendResponse(resp, 200, "text/plain", "ok".getBytes());
          break;
        case "cancelMobilities":
          ServletMobilityHelper.cancelMobilities(data, req);
          sendResponse(resp, 200, "text/plain", "ok".getBytes());
          break;
        case "getMobilityPostulants":
          List<MobilityPostulantDto> postulants =
              ServletStudentHelper.getMobilityPostulants(data, req, resp);
          sendResponse(resp, 200, "application/json", genson.serializeBytes(postulants));
          break;
        case "getUsers":
          List<UserDto> users = ServletUserHelper.getUsers();
          sendResponse(resp, 200, "application/json", genson.serializeBytes(users));
          break;
        case "getAllStudents":
          List<UserDto> students = ServletUserHelper.getStudents();
          sendResponse(resp, 200, "application/json", genson.serializeBytes(students));
          break;
        case "getPrograms":
          List<ProgramDto> programs = ServletMobilityHelper.getPrograms();
          sendResponse(resp, 200, "application/json", genson.serializeBytes(programs));
          break;
        case "confirmMobility":
          ServletMobilityHelper.confirmMobility(data, req);
          sendResponse(resp, 200, "text/plain", "ok".getBytes());
          break;
        case "updateMobility":
          ServletMobilityHelper.updateMobility(data, req);
          sendResponse(resp, 200, "text/plain", "ok".getBytes());
          break;
        case "getAllMobilities":
          List<MobilityDto> allMobilities = ServletMobilityHelper.getAllMobilities();
          sendResponse(resp, 200, "application/json", genson.serializeBytes(allMobilities));
          break;
        case "getAllMobilitiesOverview":
          List<MobilityAllDto> mobilitiesOverview =
              ServletMobilityHelper.getAllMobilitiesOverview(data, req);
          sendResponse(resp, 200, "application/json", genson.serializeBytes(mobilitiesOverview));
          break;
        case "setTeacher":
          ServletUserHelper.setTeacher(data, req);
          sendResponse(resp, 200, "text/plain", "ok".getBytes());
          break;
        case "generateCsv":
          String csv = ServletHelper.generateCsv(data, req);
          sendResponse(resp, 200, "text/csv", csv.getBytes());
          break;
        case "addPartner":
          ServletMobilityHelper.addPartner(data, req);
          sendResponse(resp, 200, "text/plain", "ok".getBytes());
          break;
        case "loginAsStudent":
          UserDto loginStudentData = ServletUserHelper.loginAsStudent(data, req);
          String teacherToken = ServletHelper.getCookieValue("token", req);
          ServletHelper.addCookie("teacherToken", teacherToken, COOKIEMAXAGE, resp);
          saveAndSendUserData(loginStudentData, resp);
          break;
        case "backToTeacher":
          String token = ServletHelper.getCookieValue("teacherToken", req);
          if (token == null) {
            throw new IhmException(
                "Vous n'êtes pas connecté en tant que professeur à un compte étudiant");
          }
          ServletUserHelper.logout(resp);
          UserDto backToTeacherData = ServletUserHelper.authenticate(req, resp, token);
          saveAndSendUserData(backToTeacherData, resp);
          break;
        default:
          Logs.info("L'action " + action + " n'est pas supportée");
          sendError(resp, 400, "L'action " + action + " n'est pas supportée", null);
          break;
      }
    } catch (OptimisticLockException ole) {
      Logs.warning(ole.getMessage() + " (409)", ole);
      sendError(resp, 409, ole.getMessage(), null);
    } catch (IhmException ie) {
      Logs.warning(ie.getMessage() + " (" + ie.getStatusCode() + ")", ie);
      sendError(resp, ie.getStatusCode(), ie.getMessage(), ie.getTarget());
    } catch (BizException be) {
      if ("id".equals(be.getTarget())) {
        ServletHelper.removeCookie("token", resp);
        ServletHelper.removeCookie("teacherToken", resp);
      }
      Logs.warning(be.getMessage() + " (401)", be);
      sendError(resp, 401, be.getMessage(), be.getTarget());
    } catch (Exception ex) {
      Logs.severe(ex.getMessage() + " (500)", ex);
      sendError(resp, 500, "Une erreur hors de votre portée a été rencontrée", null);
      System.exit(1);
    }
  }

  /**
   * Sauvegarde les informations d'un utilisateur dans les cookies du client et envoie ses
   * informations en tant que reponse, ou une erreur JWT.
   * 
   * @param userData Les informations de l'utilisateur
   * @param resp La reponse HTTP
   * @throws IOException si il y a une erreur lors de l'envoi d'une erreur JWT
   */
  private void saveAndSendUserData(UserDto userData, HttpServletResponse resp) throws IOException {
    try {
      int userId = userData.getId();
      String token = ServletHelper.createToken(userId);
      Logs.fine("Ajout de la token JWT aux cookies de l'utilisateur " + userId);
      ServletHelper.addCookie("token", token, COOKIEMAXAGE, resp);
      sendResponse(resp, 200, "application/json", genson.serializeBytes(userData));
    } catch (Exception fe) {
      throw new FatalException("Erreur de création de JWT.", fe);
    }
  }

  /**
   * Envoie une erreur en tant que réponse HTTP.
   * 
   * @param resp La reponse HTTP
   * @param status Le code de statut de la reponse
   * @param message Le message de l'erreur
   * @param target Le nom de ce qui a causé l'erreur
   * @throws IOException si une erreur I/O a lieu
   */
  private void sendError(HttpServletResponse resp, int status, String message, String target)
      throws IOException {
    Map<String, String> errorData = new HashMap<String, String>();
    errorData.put("message", message);
    errorData.put("target", target);
    sendResponse(resp, status, "application/json", genson.serializeBytes(errorData));
  }

  /**
   * Envoie une reponse HTTP sur base d'un tableau de bytes.
   * 
   * @param resp La reponse HTTP
   * @param status Le code de statut de la reponse
   * @param contentType Le type de contenu a envoyer
   * @param body Le contenu a envoyer
   * @throws IOException si une erreur I/O a lieu
   */
  private void sendResponse(HttpServletResponse resp, int status, String contentType, byte[] body)
      throws IOException {
    if (contentType.equals("application/json") || contentType.contentEquals("text/plain")
        || contentType.equals("text/csv")) {
      Logs.info("Réponse HTTP " + status + " renvoyée: " + new String(body, "UTF-8"));
    } else {
      Logs.info("Réponse HTTP " + status + " renvoyée: " + "(fichier front-end)");
    }
    resp.setStatus(status);
    resp.setContentType(contentType);
    resp.setCharacterEncoding("UTF-8");
    resp.getOutputStream().write(body);
  }

}

