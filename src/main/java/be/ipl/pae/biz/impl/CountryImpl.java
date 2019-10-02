package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfaces.Country;

import utils.Logs;

public class CountryImpl implements Country {

  private int id;
  private String countryName;
  private int programId;
  private String code;

  /**
   * Crée un objet CountryImpl.
   * 
   * @param id l'id du pays.
   * @param countryName le nom du pays.
   * @param code le code du programme.
   * @param programId le programme lié au pays.
   */
  public CountryImpl(int id, String countryName, String code, int programId) {
    super();
    this.id = id;
    this.countryName = countryName;
    this.programId = programId;
    this.code = code;
  }

  public CountryImpl() {
    super();
  }

  /**
   * vérifie si le programme est valide.
   * 
   * @return true si le programme est validé, false sinon.
   */
  public boolean checkProgram() {
    Logs.fine("Vérification de la validité du programme du pays");
    if (this.programId == 1 || this.programId == 2 || this.programId == 3) {
      return true;
    }
    Logs.fine("Le programIdme " + programId + " n'est pas valide pour le pays " + countryName + " ("
        + code + ")");
    return false;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public int getProgramId() {
    return this.programId;
  }

  public void setProgramId(int programId) {
    this.programId = programId;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }



}
