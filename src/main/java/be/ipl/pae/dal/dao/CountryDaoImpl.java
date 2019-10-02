package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.CountryDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.CountryDao;
import be.ipl.pae.dal.services.DalBackendServices;
import be.ipl.pae.exceptions.DalException;

import utils.Logs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryDaoImpl implements CountryDao {

  private DalBackendServices dalBackendServices;
  private BizFactory bizFactory;

  /**
   * Cree un objet CountryDaoImpl.
   * 
   * @param dalBackendServices un objet DalBackendServices.
   * @param bizFactory un objet BizFactory.
   */
  public CountryDaoImpl(BizFactory bizFactory, DalBackendServices dalBackendServices) {
    super();
    this.dalBackendServices = dalBackendServices;
    this.bizFactory = bizFactory;
  }

  @Override
  public List<CountryDto> findCountriesByProgram() {
    try {
      PreparedStatement findCountriesOrderByProgram = dalBackendServices
          .getPreparedStatement("SELECT * FROM projet_ae.countries c ,projet_ae.programs p"
              + " WHERE c.program_id= p.program_id ORDER BY c.program_id");
      try (ResultSet rs = findCountriesOrderByProgram.executeQuery()) {
        List<CountryDto> countries = new ArrayList<CountryDto>();
        while (rs.next()) {
          Logs.fine("Récupération de l'instance CountryDto associée au pays " + rs.getInt(1)
              + " dans la base de données");
          CountryDto country = bizFactory.getCountryDto();
          country.setId(rs.getInt(1));
          country.setCode(rs.getString(2));
          country.setCountryName(rs.getString(3));
          country.setProgramId(rs.getInt(4));
          countries.add(country);
        }

        return Collections.unmodifiableList(countries);
      }
    } catch (SQLException ex) {
      throw new DalException("Erreur lors de selectCountriesOrderByProgram ", ex);
    }
  }

  @Override
  public CountryDto findCountryById(int countryId) {
    try {
      PreparedStatement findCountryById = dalBackendServices
          .getPreparedStatement("SELECT * FROM projet_ae.countries WHERE country_id=?");
      findCountryById.setInt(1, countryId);
      return getPreparedStatementResult(findCountryById);

    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findCountryById", sqle);
    }
  }

  private CountryDto getPreparedStatementResult(PreparedStatement statement) throws SQLException {
    CountryDto countryToComplete = bizFactory.getCountryDto();
    try (ResultSet rs = statement.executeQuery()) {
      if (rs.next()) {
        Logs.fine("Récupération de l'instance CountryDto associée au pays" + rs.getInt(1)
            + " dans la base de données");
        countryToComplete.setId(rs.getInt(1));
        countryToComplete.setCode(rs.getString(2));
        countryToComplete.setCountryName(rs.getString(3));
        countryToComplete.setProgramId(rs.getInt(4));
        return countryToComplete;
      }
    }
    return null;
  }



}
