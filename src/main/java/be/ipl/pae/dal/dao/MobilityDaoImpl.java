package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.MobilityDao;
import be.ipl.pae.dal.services.DalBackendServices;
import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.OptimisticLockException;

import utils.Logs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MobilityDaoImpl implements MobilityDao {
  private DalBackendServices dalBackendServices;
  private BizFactory bizFactory;


  /**
   * Cree un objet MobilityDaoImpl.
   * 
   * @param dalBackendServices un objet DalBackendServices
   * @param bizFactory un objet BizFactory
   */
  public MobilityDaoImpl(BizFactory bizFactory, DalBackendServices dalBackendServices) {
    this.dalBackendServices = dalBackendServices;
    this.bizFactory = bizFactory;
  }

  // calme
  @Override
  public MobilityDto findMobById(int mobId) {
    try {
      PreparedStatement findMobById = dalBackendServices
          .getPreparedStatement("SELECT * FROM projet_ae.mobilities WHERE mobility_id=?");
      findMobById.setInt(1, mobId);
      try (ResultSet rs = findMobById.executeQuery()) {
        if (rs.next()) {
          MobilityDto mobility = populateMobilityDto(rs);
          return mobility;
        }
        return null;
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findMobById", sqle);
    }
  }

  @Override
  public void updateMobility(MobilityDto mob) {
    Logs.fine("Mise à jour des données de la mobilité " + mob.getId() + " dans la base de données");
    try {
      PreparedStatement updateMobility = dalBackendServices.getPreparedStatement(
          "UPDATE projet_ae.mobilities SET application_number=?,preference_order_number=?,"
              + "mobility_code=?,semester=?,partner=?,"
              + "stay_certificat= stay_certificat OR ?, transcripts= transcripts OR ?,"
              + "stage_certificat= stage_certificat OR ?,final_report=?,"
              + "proof_passing_lang_tests_go=proof_passing_lang_tests_go OR ?,"
              + "school_scholarship_contract=school_scholarship_contract OR ?,"
              + "internship_study_agreement=internship_study_agreement OR ?,"
              + "student_chart=student_chart OR ?,"
              + "proof_passing_lang_tests_back=proof_passing_lang_tests_back OR ?,"
              + "commitment_doc= commitment_doc OR ?,cancellation_reason=?,"
              + "first_payment_request=first_payment_request OR ?,"
              + "second_payment_request=second_payment_request OR ?,"
              + "state=?,pro_eco=pro_eco OR ?,mobility_tool=mobility_tool OR ?,"
              + "mobi=mobi OR ?,student_id=?,confirmed_mobility=confirmed_mobility OR ?,"
              + "country_id=?, program_id=?,version_number=?,cancelled_by_teacher=? "
              + "WHERE version_number=? AND mobility_id=?");
      updateMobility.setInt(1, mob.getApplicationNumber());
      updateMobility.setInt(2, mob.getPreferenceOrderNumber());
      updateMobility.setString(3, mob.getMobilityCode());
      updateMobility.setInt(4, mob.getSemester());

      if (mob.getPartnerId() > 0) {
        updateMobility.setInt(5, mob.getPartnerId());
      } else {
        updateMobility.setNull(5, java.sql.Types.INTEGER);
      }
      updateMobility.setBoolean(6, mob.isStayCertificate());
      updateMobility.setBoolean(7, mob.isTranscripts());
      updateMobility.setBoolean(8, mob.isStageCertificate());

      updateMobility.setString(9, mob.getFinalReport());
      updateMobility.setBoolean(10, mob.isPassingLangTestProofGo());
      updateMobility.setBoolean(11, mob.isScholarshipContract());
      updateMobility.setBoolean(12, mob.isInternshipStudyAgreement());
      updateMobility.setBoolean(13, mob.isStudentChart());

      updateMobility.setBoolean(14, mob.isPassingLangTestProofBack());
      updateMobility.setBoolean(15, mob.isCommitmentDoc());
      updateMobility.setString(16, mob.getCancellationReason());
      updateMobility.setBoolean(17, mob.getFirstPayReq());
      updateMobility.setBoolean(18, mob.getSecondPayReq());

      updateMobility.setString(19, mob.getState());
      updateMobility.setBoolean(20, mob.isProEco());
      updateMobility.setBoolean(21, mob.isMobilityTool());
      updateMobility.setBoolean(22, mob.isMobi());
      updateMobility.setInt(23, mob.getStudentId());

      updateMobility.setBoolean(24, mob.isConfirmed());

      if (mob.getCountryId() > 0) {
        updateMobility.setInt(25, mob.getCountryId());
      } else {
        updateMobility.setNull(25, java.sql.Types.INTEGER);
      }
      updateMobility.setInt(26, mob.getProgramId());
      updateMobility.setInt(27, mob.getVersionNumber() + 1);
      updateMobility.setBoolean(28, mob.isCancelledByTeacher());
      updateMobility.setInt(29, mob.getVersionNumber());
      updateMobility.setInt(30, mob.getId());

      try {
        updateMobility.executeUpdate();
      } catch (SQLException sqle) {
        throw new OptimisticLockException("Erreur lors de l'update de la mobilité", sqle);
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de updateMobility", sqle);
    }
  }

  @Override
  public void confirmMobility(int mobId) {
    Logs.fine("Mise à jour des champs confirmed_mobility à " + "TRUE et state à 'created'"
        + " pour la mobilité " + mobId + " dans la base de données");
    try {
      PreparedStatement updateMobility = dalBackendServices
          .getPreparedStatement("UPDATE projet_ae.mobilities SET confirmed_mobility = TRUE, "
              + "state = 'created' WHERE mobility_id = ?");
      updateMobility.setInt(1, mobId);
      try {
        updateMobility.executeUpdate();
      } catch (SQLException sqle) {
        throw new DalException("Erreur lors de la confirmation de la mobilite", sqle);
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de confirmMobility", sqle);
    }
  }

  @Override
  public void confirmDocumentReception(MobilityDto mobDto) {
    Logs.fine("Mise à jour des booléens correspondant aux réceptions de document pour la mobilité "
        + mobDto.getId() + " dans la base de données");
    try {
      PreparedStatement confirmDocument =
          dalBackendServices.getPreparedStatement("UPDATE projet_ae.mobilities"
              + "SET school_scholarship_contract = school_scholarship_contract OR ?,"
              + "internship_study_agreement = internship_study_agreement OR ?,"
              + "proof_passing_lang_tests_go = proof_passing_lang_tests_go OR ?,"
              + "student_chart = student_chart OR ?, commitment_doc = commitment_doc OR ?,"
              + "stay_certificat = stay_certificat OR ?, stage_certificat = stage_certificat OR ?,"
              + "transcripts = transcripts OR ?, final_report = final_report OR ?,"
              + "proof_passing_lang_tests_back = proof_passing_lang_tests_back OR ?,"
              + "version_number = ? WHERE version_number = ? AND mobility_id = ?");
      confirmDocument.setBoolean(1, mobDto.isScholarshipContract());
      confirmDocument.setBoolean(2, mobDto.isInternshipStudyAgreement());
      confirmDocument.setBoolean(3, mobDto.isPassingLangTestProofGo());
      confirmDocument.setBoolean(4, mobDto.isStudentChart());
      confirmDocument.setBoolean(5, mobDto.isCommitmentDoc());
      confirmDocument.setBoolean(6, mobDto.isStayCertificate());
      confirmDocument.setBoolean(7, mobDto.isStageCertificate());
      confirmDocument.setBoolean(8, mobDto.isTranscripts());
      confirmDocument.setString(9, mobDto.getFinalReport());
      confirmDocument.setBoolean(10, mobDto.isPassingLangTestProofBack());
      confirmDocument.setInt(11, mobDto.getVersionNumber() + 1);
      confirmDocument.setInt(12, mobDto.getVersionNumber());
      confirmDocument.setInt(13, mobDto.getId());
      try {
        confirmDocument.executeUpdate();
      } catch (SQLException sqle) {
        throw new DalException("Erreur lors de la confirmation de la mobilité", sqle);
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de confirmDocumentReception", sqle);
    }
  }

  @Override
  public void insertMobility(MobilityDto mobDto) {
    Logs.fine("Insertion de la mobilité dans la base de données");
    try {
      PreparedStatement insertNewMobility = dalBackendServices.getPreparedStatement(
          "INSERT INTO projet_ae.mobilities VALUES (DEFAULT,?,?,?,?  ,?,?,FALSE,FALSE,FALSE  ,"
              + "FALSE,FALSE,FALSE,FALSE,FALSE  ," + "FALSE,FALSE,NULL,FALSE,"
              + "FALSE,  ?,FALSE,FALSE,FALSE,FALSE,FALSE," + "?,?,?,1)");
      insertNewMobility.setInt(1, mobDto.getApplicationNumber());
      insertNewMobility.setInt(2, mobDto.getPreferenceOrderNumber());
      insertNewMobility.setString(3, mobDto.getMobilityCode());
      insertNewMobility.setInt(4, mobDto.getSemester());
      if (mobDto.getPartnerId() >= 1) {
        insertNewMobility.setInt(5, mobDto.getPartnerId());
      } else {
        insertNewMobility.setNull(5, java.sql.Types.INTEGER);
      }
      insertNewMobility.setObject(6, mobDto.getIntroductionDate(), java.sql.Types.TIMESTAMP);
      insertNewMobility.setString(7, mobDto.getState());
      insertNewMobility.setInt(8, mobDto.getStudentId());
      if (mobDto.getCountryId() >= 1) {
        insertNewMobility.setInt(9, mobDto.getCountryId());
      } else {
        insertNewMobility.setNull(9, java.sql.Types.INTEGER);
      }
      if (mobDto.getCountryId() >= 1) {
        insertNewMobility.setInt(10, mobDto.getProgramId());
      } else {
        insertNewMobility.setInt(10, java.sql.Types.INTEGER);
      }
      try {
        insertNewMobility.executeUpdate();
      } catch (SQLException sqle) {
        throw new DalException("Erreur lors de l'insertion de la mobilite.", sqle);
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de insertMobility", sqle);
    }

  }

  @Override
  public int findHighestPreferenceOrderNumberForStudent(int studentId) {
    Logs.fine("Recherche de l'ordre de préférence le plus élevé pour l'étudiant " + studentId);
    try {
      PreparedStatement highestPreferenceNumber = dalBackendServices.getPreparedStatement(
          "SELECT max(preference_order_number) FROM projet_ae.mobilities " + "WHERE student_id=?");
      highestPreferenceNumber.setInt(1, studentId);
      try (ResultSet rs = highestPreferenceNumber.executeQuery()) {
        if (rs.next()) {
          Logs.fine("Plus grand ordre de préférence trouvé pour l'étudiant " + studentId + ": "
              + rs.getInt(1));
          return rs.getInt(1);
        }
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findHighestPreferenceOrderNumberForStudent", sqle);
    }
    return 0;
  }

  @Override
  public List<MobilityDto> getMobilities(int studentId) {
    try {
      PreparedStatement mobilitiesRs = dalBackendServices
          .getPreparedStatement("SELECT * FROM projet_ae.mobilities WHERE student_id = ?");
      mobilitiesRs.setInt(1, studentId);
      return getListMobilities(mobilitiesRs);
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de getMobilities ", sqle);
    }
  }

  @Override
  public List<MobilityDto> getAllMobilities() {
    PreparedStatement allMobilites =
        dalBackendServices.getPreparedStatement("SELECT * FROM projet_ae.mobilities");
    return getListMobilities(allMobilites);
  }


  /**
   * Crée une liste de mobilités.
   * 
   * @param ps la requête sql.
   * @return une liste de mobilité.
   */
  public List<MobilityDto> getListMobilities(PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      List<MobilityDto> mobilities = new ArrayList<MobilityDto>();
      while (rs.next()) {
        MobilityDto mobility = populateMobilityDto(rs);
        mobilities.add(mobility);
      }
      return Collections.unmodifiableList(mobilities);
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
    return null;
  }



  @Override
  public int countMobilitiesByStudentId(int studentId) {
    try {
      PreparedStatement countRs = dalBackendServices
          .getPreparedStatement("SELECT COUNT(*) FROM projet_ae.mobilities WHERE student_id = ?");
      countRs.setInt(1, studentId);
      try (ResultSet rs = countRs.executeQuery()) {
        int count = -1;
        if (rs.next()) {
          Logs.fine("Nombre de mobilités de l'étudiant " + studentId + ": " + rs.getInt(1));
          count = rs.getInt(1);
        }
        return count;
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de countMobilitiesByStudentId ", sqle);
    }
  }

  /**
   * Trouve le numéro de version de la mobilité.
   * 
   * @param mobDto la mobilité correspondante.
   */
  public int findVersionNumberFrom(MobilityDto mobDto) {
    try {
      PreparedStatement findVersionNumberFromMob = dalBackendServices.getPreparedStatement(
          "SELECT version_number FROM projet_ae.mobilities WHERE mobility_id=?");
      findVersionNumberFromMob.setInt(1, mobDto.getId());
      try (ResultSet rs = findVersionNumberFromMob.executeQuery()) {
        if (rs.next()) {
          Logs.fine("Numéro de version de la mobilité " + mobDto.getId() + ": " + rs.getInt(1));
          return rs.getInt(1);
        }
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findRespTeacher", sqle);
    }
    return 0;
  }

  @Override
  public int countUnconfirmedByStudentId(int studentId) {
    try {
      PreparedStatement countRs = dalBackendServices.getPreparedStatement(
          "SELECT COUNT(*) FROM projet_ae.mobilities WHERE student_id = ? AND "
              + "confirmed_mobility = FALSE");
      countRs.setInt(1, studentId);
      try (ResultSet rs = countRs.executeQuery()) {
        int count = -1;
        if (rs.next()) {
          Logs.fine("Nombre de mobilités non confirmées de l'étudiant " + studentId + ": "
              + rs.getInt(1));
          count = rs.getInt(1);
        }
        return count;
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de countUnconfirmedByStudentId ", sqle);
    }
  }

  /**
   * Trouves les encodages d'une mobilité.
   * 
   * @param mobId l'id de la mobilité.
   * @return un objet MobilityDto.
   */
  public MobilityDto findEncodingsbyMobilityId(int mobId) {
    try {
      PreparedStatement findEncodingsByMobId =
          dalBackendServices.getPreparedStatement("SELECT * FROM projet_ae.encodings WHERE id=?");
      findEncodingsByMobId.setInt(1, mobId);
      try (ResultSet rs = findEncodingsByMobId.executeQuery()) {
        if (rs.next()) {
          MobilityDto mobility = populateMobilityDto(rs);
          return mobility;
        }
        return null;
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findMobById", sqle);
    }
  }

  private MobilityDto populateMobilityDto(ResultSet rs) throws SQLException {
    Logs.fine("Récupération de l'instance MobilityDto associée à la mobilité " + rs.getInt(1)
        + " dans la base de données");
    MobilityDto mobilityToComplete = bizFactory.getMobilityDto();
    mobilityToComplete.setId(rs.getInt(1));
    mobilityToComplete.setApplicationNumber(rs.getInt(2));
    mobilityToComplete.setPreferenceOrderNumber(rs.getInt(3));
    mobilityToComplete.setMobilityCode(rs.getString(4));
    mobilityToComplete.setSemester(rs.getInt(5));
    mobilityToComplete.setPartnerId(rs.getInt(6));
    mobilityToComplete.setIntroductionDate(rs.getDate(7) == null ? null : rs.getDate(7));
    mobilityToComplete.setStayCertificate(rs.getBoolean(8));
    mobilityToComplete.setTranscripts(rs.getBoolean(9));
    mobilityToComplete.setStageCertificate(rs.getBoolean(10));
    mobilityToComplete.setFinalReport(rs.getString(11));
    mobilityToComplete.setPassingLangTestProofGo(rs.getBoolean(12));
    mobilityToComplete.setScholarshipContract(rs.getBoolean(13));
    mobilityToComplete.setInternshipStudyAgreement(rs.getBoolean(14));
    mobilityToComplete.setStudentChart(rs.getBoolean(15));
    mobilityToComplete.setPassingLangTestProofBack(rs.getBoolean(16));
    mobilityToComplete.setCommitmentDoc(rs.getBoolean(17));
    mobilityToComplete.setCancellationReason(rs.getString(18));
    mobilityToComplete.setFirstPayReq(rs.getBoolean(19));
    mobilityToComplete.setSecondPayReq(rs.getBoolean(20));
    mobilityToComplete.setState(rs.getString(21));
    mobilityToComplete.setConfirmed(rs.getBoolean(22));
    mobilityToComplete.setProEco(rs.getBoolean(23));
    mobilityToComplete.setMobilityTool(rs.getBoolean(24));
    mobilityToComplete.setMobi(rs.getBoolean(25));
    mobilityToComplete.setCancelledByTeacher(rs.getBoolean(26));
    mobilityToComplete.setStudentId(rs.getInt(27));
    mobilityToComplete.setCountryId(rs.getInt(28));
    mobilityToComplete.setProgramId(rs.getInt(29));
    mobilityToComplete.setVersionNumber(rs.getInt(30));
    return mobilityToComplete;
  }

}
