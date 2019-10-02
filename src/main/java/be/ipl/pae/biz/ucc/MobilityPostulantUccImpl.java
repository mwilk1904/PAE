package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.MobilityPostulantDto;
import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.impl.MobilityPostulantImpl;
import be.ipl.pae.biz.interfaces.MobilityPostulantUcc;
import be.ipl.pae.dal.interfaces.MobilityDao;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.interfaces.UserDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;

import utils.Logs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MobilityPostulantUccImpl implements MobilityPostulantUcc {
  private DalServices dalServices;
  private StudentDao studentDao;
  private MobilityDao mobilityDao;
  private UserDao userDao;

  /**
   * Crée un objet MobilityPostulantUccImpl.
   * 
   * @param dalServices un objet DalServices.
   * @param studentDao un objet StudentDao.
   * @param mobilityDao un objet MobilityDao.
   * @param userDao un objet UserDao.
   */
  public MobilityPostulantUccImpl(DalServices dalServices, StudentDao studentDao,
      MobilityDao mobilityDao, UserDao userDao) {
    super();
    this.dalServices = dalServices;
    this.studentDao = studentDao;
    this.mobilityDao = mobilityDao;
    this.userDao = userDao;
  }

  @Override
  public List<MobilityPostulantDto> studentOverview() {
    Logs.fine("Récupération des données du tableau de la page d'accueil du professeur");
    try {
      dalServices.startTransaction();
      List<MobilityPostulantDto> mobilityPostulants = new ArrayList<MobilityPostulantDto>();
      List<StudentDto> students = studentDao.findStudents();
      for (StudentDto student : students) {
        Logs.fine("Récupération des données à renvoyer pour l'étudiant " + student.getId());
        UserDto user = userDao.findUserById(student.getUserId());
        int mobilityCount = mobilityDao.countMobilitiesByStudentId(student.getId());
        int unconfirmedMobilityCount = mobilityDao.countUnconfirmedByStudentId(student.getId());
        MobilityPostulantDto postulant = new MobilityPostulantImpl(student.getId(),
            user.getFirstName(), user.getLastName(), mobilityCount, unconfirmedMobilityCount);
        mobilityPostulants.add(postulant);
      }
      dalServices.commit();
      return Collections.unmodifiableList(mobilityPostulants);
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
  }

}
