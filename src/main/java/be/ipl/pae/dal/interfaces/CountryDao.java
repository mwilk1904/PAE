package be.ipl.pae.dal.interfaces;

import be.ipl.pae.biz.dto.CountryDto;

import java.util.List;

public interface CountryDao {

  /**
   * Renvoie une liste de pays présents dans la base de données.
   * 
   * @return la liste de pays
   */

  List<CountryDto> findCountriesByProgram();

  CountryDto findCountryById(int countryId);

}
