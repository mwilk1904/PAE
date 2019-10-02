package be.ipl.pae.ihm;

import be.ipl.pae.biz.dto.CountryDto;
import be.ipl.pae.biz.dto.MobilityAllDto;
import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.dto.PartnerDto;
import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.impl.MobilityImpl;
import be.ipl.pae.biz.impl.PartnerImpl;
import be.ipl.pae.biz.interfaces.CountryUcc;
import be.ipl.pae.biz.interfaces.MobilityAllUcc;
import be.ipl.pae.biz.interfaces.MobilityUcc;
import be.ipl.pae.biz.interfaces.PartnerUcc;
import be.ipl.pae.biz.interfaces.ProgramUcc;
import be.ipl.pae.exceptions.IhmException;

import utils.Logs;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletMobilityHelper {

  private static MobilityUcc mobilityUcc;
  private static PartnerUcc partnerUcc;
  private static CountryUcc countryUcc;
  private static MobilityAllUcc mobilityAllUcc;
  private static ProgramUcc programUcc;

  /**
   * Initialise le ServletMobilityHelper.
   * 
   * @param mobilityUcc Le MobilityUcc avec lequel interagir
   * @param partnerUcc Le PartnerUcc avec lequel interagir
   * @param countryUcc Le CountryUcc avec lequel interagir
   */
  public static void init(MobilityUcc mobilityUcc, PartnerUcc partnerUcc, CountryUcc countryUcc,
      MobilityAllUcc mobilityAllUcc, ProgramUcc programUcc) {
    ServletMobilityHelper.mobilityUcc = mobilityUcc;
    ServletMobilityHelper.partnerUcc = partnerUcc;
    ServletMobilityHelper.countryUcc = countryUcc;
    ServletMobilityHelper.mobilityAllUcc = mobilityAllUcc;
    ServletMobilityHelper.programUcc = programUcc;
  }

  /**
   * Reçoit les mobilités d'un étudiant.
   * 
   * @param data les données.
   * @param req request.
   * @param resp response.
   * @return une liste des mobilités de l'étudiant.
   */
  public static List<MobilityDto> getStudentMobilities(String data, HttpServletRequest req,
      HttpServletResponse resp) {
    Logs.info("Traitement de la requête d'envoi des mobilités de l'étudiant");
    Logs.fine(
        "Requête d'envoi des mobilités faite par un " + (data == null ? "étudiant" : "professeur"));
    Object[] requiredInformations = ServletHelper.getRequiredIdInformations(data, req, resp);
    return mobilityUcc.getMobilities((int) requiredInformations[0],
        (boolean) requiredInformations[1]);
  }

  /**
   * Reçoit les déclarations de mobilités de l'étudiant.
   * 
   * @param data les données.
   * @param req request.
   * @return un tableau de mobilités.
   */
  public static MobilityDto[] declareMobilities(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête de déclaration des mobilités de l'étudiant");
    MobilityDto[] mobilities =
        (MobilityImpl[]) ServletHelper.deserialize(data, MobilityImpl[].class);
    int userId = ((UserDto) req.getAttribute("user")).getId();
    Logs.fine("Traitement des mobilités");
    return mobilityUcc.declareMobilities(userId, mobilities);
  }

  /**
   * Supprime un choix de mobilité.
   * 
   * @param data Les données
   * @param req La requête HTTP
   */
  public static void cancelMobility(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête de suppression d'une mobilité de l'étudiant");
    @SuppressWarnings("unchecked")
    Map<String, Object> dataMap = (Map<String, Object>) ServletHelper.deserialize(data, Map.class);
    String reason = (String) dataMap.get("reason");
    int mobilityId = ((Long) dataMap.get("mobilityId")).intValue();
    mobilityUcc.cancelMobility((UserDto) req.getAttribute("user"), reason, mobilityId);
  }

  /**
   * Supprime des choix de mobilité.
   * 
   * @param data Les données
   * @param req La requête HTTP
   */
  public static void cancelMobilities(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête de suppression de mobilités de l'étudiant");
    @SuppressWarnings("unchecked")
    Map<String, Object> dataMap = (Map<String, Object>) ServletHelper.deserialize(data, Map.class);
    String reason = (String) dataMap.get("reason");
    mobilityUcc.cancelMobilities((UserDto) req.getAttribute("user"), reason);
  }

  /**
   * Confirme une mobilité.
   * 
   * @param data les données.
   * @param req request.
   */
  public static void confirmMobility(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête de confirmation des mobilités d'un étudiant");
    if (!ServletHelper.isTeacher(req)) {
      throw new IhmException("L'utilisateur n'est pas un professeur", 403);
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> dataMap = (Map<String, Object>) ServletHelper.deserialize(data, Map.class);
    int mobilityId = ((Long) dataMap.get("mobilityId")).intValue();
    mobilityUcc.confirmMobility(mobilityId);
  }

  /**
   * Ajoute un paramètre.
   * 
   * @param data les données.
   * @param req une requete.
   */
  public static void addPartner(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête d'ajout d'un partenaire");
    PartnerDto partner = (PartnerDto) ServletHelper.deserialize(data, PartnerImpl.class);
    partnerUcc.insertPartner(partner);
  }

  /**
   * Reçoit une liste de partenaire.
   * 
   * @return une liste de partenaire.
   */
  public static List<PartnerDto> getPartners() {
    Logs.info("Traitement de la requête d'envoi des partenaires");
    return partnerUcc.getPartners();
  }

  /**
   * Reçoit les données d'un partenaire.
   * 
   * @param data Les données de la requête
   * @param req La requête HTTP
   * @return les données du partenaire
   */
  public static PartnerDto getPartnerData(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête d'envoi des données d'un partenaire");
    @SuppressWarnings("unchecked")
    Map<String, Object> dataMap = (Map<String, Object>) ServletHelper.deserialize(data, Map.class);
    int partnerId = ((Long) dataMap.get("partnerId")).intValue();
    return partnerUcc.getPartnerData(partnerId);
  }

  /**
   * Reçoit une liste de pays.
   * 
   * @return une liste de pays.
   */
  public static List<CountryDto> getCountries() {
    Logs.info("Traitement de la requête d'envoi des pays");
    return countryUcc.getCountries();
  }

  /**
   * Reçoit une liste de programmes.
   * 
   * @return une liste de programmes.
   */
  public static List<ProgramDto> getPrograms() {
    Logs.info("Traitement de la requête d'envoi des programmes");
    return programUcc.getPrograms();
  }

  /**
   * Reçoit une liste des mobilites.
   * 
   * @return une liste de mobilites.
   */
  public static List<MobilityDto> getAllMobilities() {
    Logs.info("Traitement de la requête d'envoi des mobilities");
    return mobilityUcc.getAllMobilities();
  }

  /**
   * Reçoit l'overview de toutes les mobilites
   * 
   * @param data les données.
   * @param req request.
   * @return une liste de mobilité.
   */
  public static List<MobilityAllDto> getAllMobilitiesOverview(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête de la vue d'ensemble des mobilites");
    return mobilityAllUcc.overview();
  }

  /**
   * Reçoit une liste de raisons d'annulation de mobilités des professeurs.
   * 
   * @return la liste des raisons d'annulation
   */
  public static List<String> getCancellationReasons() {
    Logs.info("Traitement de la requête d'envoi des raisons d'annulation des professeurs");
    return mobilityUcc.getCancellationReasons();
  }

  /**
   * Reçoit une mobilité.
   * 
   * @param data Les données
   * @param req La requête HTTP
   * @return la mobilité
   */
  public static MobilityDto getMobility(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête d'envoi d'une mobilité");
    @SuppressWarnings("unchecked")
    Map<String, Object> dataMap = (Map<String, Object>) ServletHelper.deserialize(data, Map.class);
    int mobId = ((Long) dataMap.get("mobilityId")).intValue();
    return mobilityUcc.getMobility(mobId);
  }

  /**
   * Met à jour une mobilité.
   * 
   * @param data La mobilité à mettre à jour
   * @param req La requête HTTP
   */
  public static void updateMobility(String data, HttpServletRequest req) {
    Logs.info("Traitement de la requête de confirmation de réception de documents");
    if (!ServletHelper.isTeacher(req)) {
      throw new IhmException("L'utilisateur n'est pas un professeur", 403);
    }
    MobilityDto mobility = (MobilityDto) ServletHelper.deserialize(data, MobilityImpl.class);
    mobilityUcc.updateMobility(mobility);
  }

}
