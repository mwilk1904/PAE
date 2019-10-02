package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.PartnerDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.PartnerDao;
import be.ipl.pae.dal.services.DalBackendServices;
import be.ipl.pae.exceptions.DalException;

import utils.Logs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartnerDaoImpl implements PartnerDao {

  private DalBackendServices dalBackendServices;
  private BizFactory bizFactory;

  public PartnerDaoImpl(BizFactory bizFactory, DalBackendServices dalBackendServices) {
    this.dalBackendServices = dalBackendServices;
    this.bizFactory = bizFactory;
  }

  @Override
  public List<PartnerDto> findPartners() {
    try {
      PreparedStatement findPartners =
          dalBackendServices.getPreparedStatement("SELECT * FROM projet_ae.partners");
      try (ResultSet rs = findPartners.executeQuery()) {
        List<PartnerDto> partners = new ArrayList<PartnerDto>();
        while (rs.next()) {
          Logs.fine("Récupération de l'instance PartnerDto associée au partenaire " + rs.getInt(1)
              + " dans la base de données");

          PartnerDto partner = getPartnerFromRs(rs);
          partners.add(partner);
        }
        return Collections.unmodifiableList(partners);

      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findPartners", sqle);
    }
  }


  private PartnerDto getPartnerFromRs(ResultSet rs) {
    PartnerDto partner = bizFactory.getPartnerDto();
    try {
      partner.setId(rs.getInt(1));
      partner.setLegalName(rs.getString(2));
      partner.setBusinessName(rs.getString(3));
      partner.setFullName(rs.getString(4));
      partner.setDepartment(rs.getString(5));
      partner.setOrganisationType(rs.getString(6));
      partner.setEmployeesCount(rs.getInt(7));
      partner.setAddress(rs.getString(8));
      partner.setCountryId(rs.getInt(9));
      partner.setArea(rs.getString(10));
      partner.setZipCode(rs.getString(11));
      partner.setCity(rs.getString(12));
      partner.setEmail(rs.getString(13));
      partner.setWebsite(rs.getString(14));
      partner.setPhoneNumber(rs.getString(15));
      partner.setMobilityCode(rs.getString(16));
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de getPartnerFromRs", sqle);
    }
    // TODO Auto-generated method stub
    return partner;
  }

  @Override
  public List<PartnerDto> getKeywordPartners(String keyword) {
    try {
      PreparedStatement getKeywordPartners = dalBackendServices
          .getPreparedStatement("SELECT * FROM projet_ae.partners WHERE upper(name) LIKE ?");
      getKeywordPartners.setString(1, "%" + keyword.toUpperCase() + "%");
      try (ResultSet rs = getKeywordPartners.executeQuery()) {
        List<PartnerDto> partners = new ArrayList<PartnerDto>();
        while (rs.next()) {
          PartnerDto partner = getPartnerFromRs(rs);
          partners.add(partner);
        }
        return Collections.unmodifiableList(partners);
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur getKeywordPartners", sqle);
    }
  }

  @Override
  public void insertNewPartner(PartnerDto partnerDto) {
    Logs.fine("Insertion de lu partenaire dans la base de données");
    try {
      PreparedStatement insertPartner = dalBackendServices.getPreparedStatement(
          "INSERT INTO projet_ae.partners VALUES(DEFAULT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      insertPartner.setString(1, partnerDto.getLegalName());
      insertPartner.setString(2, partnerDto.getBusinessName());
      insertPartner.setString(3, partnerDto.getFullName());
      insertPartner.setString(4, partnerDto.getDepartment());
      insertPartner.setString(5, partnerDto.getOrganisationType());
      insertPartner.setInt(6, partnerDto.getEmployeesCount());
      insertPartner.setString(7, partnerDto.getAddress());
      insertPartner.setInt(8, partnerDto.getCountryId());
      insertPartner.setString(9, partnerDto.getArea());
      insertPartner.setString(10, partnerDto.getZipCode());
      insertPartner.setString(11, partnerDto.getCity());
      insertPartner.setString(12, partnerDto.getEmail());
      insertPartner.setString(13, partnerDto.getWebsite());
      insertPartner.setString(14, partnerDto.getPhoneNumber());
      insertPartner.setString(15, partnerDto.getMobilityCode());
      try {
        insertPartner.executeUpdate();
      } catch (SQLException sqle) {
        throw new DalException("Erreur lors de l'insertion du partenaire.", sqle);
      }
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de la requête SQL d'insertion du partenaire", sqle);
    }
  }

  @Override
  public PartnerDto findPartnerById(int partnerId) {
    try {
      PreparedStatement findPartnerById = dalBackendServices
          .getPreparedStatement("SELECT * FROM projet_ae.partners WHERE partner_id=?");
      findPartnerById.setInt(1, partnerId);
      return getPreparedStatementResult(findPartnerById);
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findPartnerById", sqle);
    }
  }

  private PartnerDto getPreparedStatementResult(PreparedStatement statement) throws SQLException {
    PartnerDto partnerToComplete = bizFactory.getPartnerDto();
    try (ResultSet rs = statement.executeQuery()) {
      if (rs.next()) {
        Logs.fine("Récupération de l'instance PartnerDto associée au partenaire" + rs.getInt(1)
            + " dans la base de données");
        partnerToComplete.setId(rs.getInt(1));
        partnerToComplete.setLegalName(rs.getString(2));
        partnerToComplete.setBusinessName(rs.getString(3));
        partnerToComplete.setFullName(rs.getString(4));
        partnerToComplete.setDepartment(rs.getString(5));
        partnerToComplete.setOrganisationType(rs.getString(6));
        partnerToComplete.setEmployeesCount(rs.getInt(7));
        partnerToComplete.setAddress(rs.getString(8));
        partnerToComplete.setCountryId(rs.getInt(9));
        partnerToComplete.setArea(rs.getString(10));
        partnerToComplete.setZipCode(rs.getString(11));
        partnerToComplete.setCity(rs.getString(12));
        partnerToComplete.setEmail(rs.getString(13));
        partnerToComplete.setWebsite(rs.getString(14));
        partnerToComplete.setPhoneNumber(rs.getString(15));
        partnerToComplete.setMobilityCode(rs.getString(16));
        return partnerToComplete;
      }
    }
    return null;
  }


}
