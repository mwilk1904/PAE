package be.ipl.pae.ihm;

import be.ipl.pae.biz.dto.MobilityPostulantDto;
import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.impl.StudentImpl;
import be.ipl.pae.biz.interfaces.MobilityPostulantUcc;
import be.ipl.pae.biz.interfaces.StudentUcc;
import be.ipl.pae.exceptions.IhmException;

import utils.Logs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletStudentHelper {

  private static StudentUcc studentUcc;
  private static MobilityPostulantUcc mobilityPostulantUcc;

  /**
   * Initialise le ServletStudentHelper.
   * 
   * @param studentUcc Le StudentUcc avec lequel interagir
   * @param mobilityPostulantUcc Le MobilityPostulantUcc avec lequel interagir
   */
  public static void init(StudentUcc studentUcc, MobilityPostulantUcc mobilityPostulantUcc) {
    ServletStudentHelper.studentUcc = studentUcc;
    ServletStudentHelper.mobilityPostulantUcc = mobilityPostulantUcc;
  }

  /**
   * Reçoit les données d'un étudiant.
   * 
   * @param data les données.
   * @param req request.
   * @param resp response.
   * @return un objet StudentDto.
   */
  public static StudentDto getStudentData(String data, HttpServletRequest req,
      HttpServletResponse resp) {
    Logs.info("Traitement de la requête d'envoi des données de l'étudiant");
    Logs.fine("Requête d'envoi des données d'étudiant faite par un "
        + (data == null ? "étudiant" : "professeur"));
    Object[] requiredInformations = ServletHelper.getRequiredIdInformations(data, req, resp);
    return studentUcc.getPersonalData((int) requiredInformations[0],
        (boolean) requiredInformations[1]);
  }

  /**
   * Met à jour les données d'un étudiant.
   * 
   * @param data les données.
   * @param req request.
   * @param resp response.
   */
  public static void updateStudentData(String data, HttpServletRequest req,
      HttpServletResponse resp) {
    Logs.info("Traitement de la requête de mise à jour des données de l'étudiant");
    StudentDto student = (StudentImpl) ServletHelper.deserialize(data, StudentImpl.class);
    int id = ((UserDto) req.getAttribute("user")).getId();
    student.setUser(id);
    Logs.fine("Mise à jour des données: vérification du numéro de carte bancaire "
        + student.getBankCardNumber());
    if (student.getBankCardNumber() != null
        && !ServletHelper.verifyBankCardNumber(student.getBankCardNumber())) {
      throw new IhmException("Le numéro de carte bancaire est invalide", "bankCardNumber");
    }
    studentUcc.updatePersonalData(student);
  }

  /**
   * Reçoit les mobilités d'un postulant.
   * 
   * @param data les données.
   * @param req request.
   * @param resp response.
   * @return une liste de mobilité.
   */
  public static List<MobilityPostulantDto> getMobilityPostulants(String data,
      HttpServletRequest req, HttpServletResponse resp) {
    Logs.info("Traitement de la requête d'étudiants demandeurs");
    if (!ServletHelper.isTeacher(req)) {
      throw new IhmException("L'utilisateur n'est pas un professeur", 403);
    }
    return mobilityPostulantUcc.studentOverview();
  }

}
