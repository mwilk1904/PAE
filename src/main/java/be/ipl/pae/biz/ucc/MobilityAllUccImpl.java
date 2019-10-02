package be.ipl.pae.biz.ucc;

import be.ipl.pae.biz.dto.CountryDto;
import be.ipl.pae.biz.dto.MobilityAllDto;
import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.dto.PartnerDto;
import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.dto.UserDto;
import be.ipl.pae.biz.impl.MobilityAllImpl;
import be.ipl.pae.biz.interfaces.MobilityAllUcc;
import be.ipl.pae.dal.interfaces.CountryDao;
import be.ipl.pae.dal.interfaces.MobilityDao;
import be.ipl.pae.dal.interfaces.PartnerDao;
import be.ipl.pae.dal.interfaces.ProgramDao;
import be.ipl.pae.dal.interfaces.StudentDao;
import be.ipl.pae.dal.interfaces.UserDao;
import be.ipl.pae.dal.services.DalServices;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;

import utils.Logs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MobilityAllUccImpl implements MobilityAllUcc {
  private DalServices dalServices;
  private StudentDao studentDao;
  private MobilityDao mobilityDao;
  private PartnerDao partnerDao;
  private CountryDao countryDao;
  private UserDao userDao;
  private ProgramDao programDao;

  /**
   * Crée un objet MobilityAllUccImpl.
   * 
   * @param dalServices un objet DalServices.
   * @param studentDao un objet StudentDao.
   * @param mobilityDao un objet MobilityDao.
   * @param partnerDao un objet PartnerDao.
   * @param countryDao un objet CountryDao.
   * @param userDao un objet UserDao.
   * @param programDao un objet ProgramDao.
   */
  public MobilityAllUccImpl(DalServices dalServices, StudentDao studentDao, MobilityDao mobilityDao,
      PartnerDao partnerDao, CountryDao countryDao, UserDao userDao, ProgramDao programDao) {
    super();
    this.dalServices = dalServices;
    this.studentDao = studentDao;
    this.mobilityDao = mobilityDao;
    this.partnerDao = partnerDao;
    this.countryDao = countryDao;
    this.userDao = userDao;
    this.programDao = programDao;
  }

  @Override
  public List<MobilityAllDto> overview() {
    Logs.fine("Récupération des données du tableau");
    try {
      dalServices.startTransaction();
      List<MobilityAllDto> mobilitiesAll = new ArrayList<MobilityAllDto>();
      List<MobilityDto> mobilities = mobilityDao.getAllMobilities();
      for (MobilityDto mobilityDto : mobilities) {
        Logs.fine("Récupération des données à renvoyer pour la mobilité");


        PartnerDto partnerDto = partnerDao.findPartnerById(mobilityDto.getPartnerId());
        int partnerId;
        if (partnerDto == null) {
          partnerId = 0;
        } else {
          partnerId = partnerDto.getId();
        }
        CountryDto countryDto = countryDao.findCountryById(mobilityDto.getCountryId());
        int countryId;
        if (countryDto == null) {
          countryId = 0;
        } else {
          countryId = countryDto.getId();
        }
        ProgramDto programDto = programDao.findProgramById(mobilityDto.getProgramId());
        int programId;
        if (programDto == null) {
          programId = 0;
        } else {
          programId = programDto.getId();
        }
        StudentDto studentDto = studentDao.findStudentById(mobilityDto.getStudentId());
        UserDto userDto = userDao.findUserById(studentDto.getUserId());
        MobilityAllDto all = new MobilityAllImpl(mobilityDto.getMobilityCode(), countryId,
            partnerId, userDto.getFirstName(), userDto.getLastName(), mobilityDto.getState(),
            mobilityDto.getPreferenceOrderNumber(), mobilityDto.getId(),
            mobilityDto.getCancellationReason(), programId, mobilityDto.getApplicationNumber(),
            mobilityDto.isConfirmed(), mobilityDto.getFirstPayReq(), mobilityDto.getSecondPayReq(),
            mobilityDto.getIntroductionDate());
        mobilitiesAll.add(all);
      }
      dalServices.commit();
      return Collections.unmodifiableList(mobilitiesAll);
    } catch (DalException de) {
      dalServices.rollback();
      throw new FatalException(de.getMessage(), de);
    }
  }

}
