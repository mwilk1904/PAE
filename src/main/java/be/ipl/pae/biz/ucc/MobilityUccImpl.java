package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.interfaces.Mobility;
import be.ipl.pae.biz.interfaces.MobilityUcc;
import be.ipl.pae.dal.interfaces.MobilityDao;
import be.ipl.pae.dal.interfaces.ProgramDao;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.BizException;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;

import utils.Logs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class MobilityUccImpl implements MobilityUcc {
  private MobilityDao mobilityDao;
  private StudentDao studentDao;
  private ProgramDao programDao;
  private DalServices dalServices;

  /**
   * Crée un objet MobilityUccImpl.
   * 
   * @param mobilityDao un objet MobilityDao.
   * @param studentDao un objet StudentDao.
   * @param programDao un objet ProgramDao.
   * @param dalServices un objet DalServices.
   */
  public MobilityUccImpl(MobilityDao mobilityDao, StudentDao studentDao, ProgramDao programDao,
      DalServices dalServices) {
    super();
    this.mobilityDao = mobilityDao;
    this.studentDao = studentDao;
    this.programDao = programDao;
    this.dalServices = dalServices;

  }

  @Override
  public List<MobilityDto> getMobilities(int id, boolean isStudentId) {
    Logs.fine("Récupération des mobilités associées à l'id d'"
        + (isStudentId ? "étudiant" : "utilisateur") + id);
    List<MobilityDto> allMobFromOneStud = new ArrayList<MobilityDto>();
    dalServices.startTransaction();
    if (!isStudentId) {
      int oldId = id;
      id = studentDao.findStudentIdByUserId(id);
      if (id == 0) {
        dalServices.rollback();
        throw new BizException(
            "L'id d'utilisateur " + oldId + " ne correspond à aucun id d'étudiant en db");
      }
    }
    if (id >= 1) {
      try {
        List<MobilityDto> allMobFromOneStudPrep = mobilityDao.getMobilities(id);
        for (MobilityDto mob : allMobFromOneStudPrep) {
          if (((Mobility) mob).isThisAcademicYear()) {
            allMobFromOneStud.add(mob);
          }
        }
      } catch (DalException de) {
        dalServices.rollback();
        throw new FatalException(de.getMessage(), de);
      }
      dalServices.commit();
    } else {
      dalServices.rollback();
      throw new BizException("L'id " + id + " est incorrect", isStudentId ? "id" : null);
    }
    return Collections.unmodifiableList(allMobFromOneStud);
  }

  /**
   * crée une liste de toutes les mobilités.
   * 
   * @return une liste de mobilité.
   */
  public List<MobilityDto> getAllMobilities() {
    List<MobilityDto> allMobilities;
    dalServices.startTransaction();
    try {
      allMobilities = mobilityDao.getAllMobilities();

    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
    return Collections.unmodifiableList(allMobilities);

  }

  /**
   * annule toutes les mobilitees d un etudiant.
   * 
   * @param user l'utilisateur.
   * @param reason la raison de son annulation.
   */
  @Override
  public void cancelMobilities(UserDto user, String reason) {
    Logs.fine(
        "Annulation des mobilités pour l'utilisateur " + user.getPseudo() + ", raison: " + reason);
    try {
      dalServices.startTransaction();
      int studentId = studentDao.findStudentIdByUserId(user.getId());
      List<MobilityDto> mobDtos = mobilityDao.getMobilities(studentId);
      for (MobilityDto mobDto : mobDtos) {
        if (mobDto.getStudentId() == studentId && ((Mobility) mobDto).isThisAcademicYear()) {
          Logs.fine("Annulation de la mobilité " + mobDto.getId() + " pour l'utilisateur "
              + user.getPseudo());
          mobDto.setCancellationReason(reason);
          mobDto.setCancelledByTeacher(false);
          mobDto.setState("cancelled");
          mobilityDao.updateMobility(mobDto);
        } else {
          throw new BizException(
              "Une autre personne que l etudiant essaye d annuler toutes ses mobilites");
        }
      }
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();

  }

  /**
   * annule une mobilité.
   * 
   * @param user l'utilisateur.
   * @param reason la raison de son annulation.
   * @param mobId l'id de la mobilité.
   */
  @Override
  public void cancelMobility(UserDto user, String reason, int mobId) {
    Logs.fine("Annulation d une mobilitee (id:" + mobId + ") pour l'utilisateur " + user.getPseudo()
        + ", raison: " + reason);
    if (user.getRole().equals("teacher") || user.getRole().equals("resp_teacher")) {
      try {
        dalServices.startTransaction();
        MobilityDto mobDto = mobilityDao.findMobById(mobId);
        mobDto.setCancellationReason(reason);
        mobDto.setCancelledByTeacher(true);
        mobDto.setState("cancelled");
        mobilityDao.updateMobility(mobDto);

      } catch (DalException de) {
        dalServices.rollback();
        throw new FatalException(de.getMessage(), de);
      }
      dalServices.commit();
    } else {
      throw new BizException("prof ou prof resp peuvent annuler une mobilite");
    }
  }


  /**
   * confirme une mobilité.
   * 
   * @param mobId le numéro de la mobilité confirmée
   */
  public void confirmMobility(int mobId) {
    Logs.fine("Confirmation de la mobilité " + mobId);
    if (mobId >= 1) {
      try {
        dalServices.startTransaction();
        MobilityDto mob = mobilityDao.findMobById(mobId);
        mob.setConfirmed(true);
        mob.setState("created");
        mobilityDao.updateMobility(mob);
        dalServices.commit();
      } catch (DalException de) {
        dalServices.rollback();
        throw new FatalException(de.getMessage(), de);
      }
    } else {
      throw new BizException("L'id de mobilité " + mobId + "est incorrect");
    }
  }

  /**
   * Déclare des mobilités.
   * 
   * @param userId le numéro de l'utilisateur qui la déclare
   * @param mobDtos les mobilités déclarées
   */
  @Override
  public MobilityDto[] declareMobilities(int userId, MobilityDto[] mobDtos) {
    LocalDate now = LocalDate.now();
    int semester = (int) Math.ceil((double) now.getMonthValue() / 6);
    dalServices.startTransaction();
    try {
      int studentId = studentDao.findStudentIdByUserId(userId);
      if (studentId < 1) {
        dalServices.rollback();
        throw new BizException("Aucun étudiant n'a " + userId + " en tant qu'userId");
      }
      int highestPref = mobilityDao.findHighestPreferenceOrderNumberForStudent(studentId);
      for (int i = 0; i < mobDtos.length; i++) {
        Logs.fine("Déclaration d'une mobilité pour l'utilisateur " + userId + " (countryId: "
            + mobDtos[i].getCountryId() + ", partnerId: " + mobDtos[i].getPartnerId());
        Mobility mob = (Mobility) mobDtos[i];
        mob.setStudentId(studentId);
        mob.setPreferenceOrderNumber(i + highestPref + 1);
        mob.setState("none");
        mob.setIntroductionDate(new Date());
        mob.setSemester(semester);
        if (mob.checkMobility()) {
          mobilityDao.insertMobility(mob);
        } else {
          dalServices.rollback();
          throw new BizException("La mobilite n est pas correcte");
        }
      }
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
    return mobDtos;
  }

  @Override
  public void setMobilityChoice(MobilityDto mob) {
    Logs.fine("Mise à jour du choix de mobilité " + mob.getId());
    try {
      dalServices.startTransaction();
      ProgramDto program = programDao.findProgramById(mob.getProgramId());
      mobilityDao.updateMobility(setMobilityState(mob, program));
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
  }

  private static MobilityDto setMobilityState(MobilityDto mob, ProgramDto program) {
    if (((Mobility) mob).isAllDocsFilled(program)) {
      if ("Suisse".equals(program.getProgramName())) {
        mob.setState("switzerland_post");
      } else if (mob.getSecondPayReq()) {
        mob.setState("second_demand");
      } else {
        mob.setState("to_pay_balance");
      }
    } else if (((Mobility) mob).isBeforeDocsFilled(program)) {
      if ("Suisse".equals(program.getProgramName())) {
        mob.setState("switzerland");
      } else if (mob.getFirstPayReq()) {
        mob.setState("first_demand");
      } else {
        mob.setState("to_pay");
      }
    } else if (mob.getFirstPayReq() && !"Suisse".equals(program.getProgramName())) {
      mob.setState("first_demand");
    } else if (((Mobility) mob).isOneDocFilled(program)) {
      mob.setState("in_preparation");
    } else {
      mob.setState("created");
    }
    return mob;
  }

  @Override
  public MobilityDto getMobility(int mobId) {
    Logs.fine("Réception de la mobilité " + mobId);
    MobilityDto mob;
    try {
      dalServices.startTransaction();
      mob = mobilityDao.findMobById(mobId);
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
    return mob;
  }

  @Override
  public void updateMobility(MobilityDto mob) {
    Logs.fine("Mise à jour de la mobilité " + mob.getId());
    try {
      dalServices.startTransaction();
      ProgramDto program = programDao.findProgramById(mob.getProgramId());
      mobilityDao.updateMobility(setMobilityState(mob, program));
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
  }

  @Override
  public List<String> getCancellationReasons() {
    List<String> reasons = new ArrayList<String>();
    Logs.fine("Récupération des raisons d'annulation des pro");
    try {
      dalServices.startTransaction();
      List<MobilityDto> mobilities = mobilityDao.getAllMobilities();
      for (MobilityDto mob : mobilities) {
        if (mob.isCancelledByTeacher()) {
          reasons.add(mob.getCancellationReason());
        }
      }
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
    dalServices.commit();
    return Collections.unmodifiableList(new ArrayList<String>(new HashSet<>(reasons)));
  }



}
