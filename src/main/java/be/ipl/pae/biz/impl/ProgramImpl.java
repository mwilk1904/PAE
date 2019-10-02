package be.ipl.pae.biz.impl;

import be.ipl.pae.biz.interfaces.Program;

public class ProgramImpl implements Program {


  private int id;
  private String programName;


  /**
   * Crée un objet ProgramImpl.
   * 
   * @param id l'id du programme.
   * @param programName le nom du programme.
   */
  public ProgramImpl(int id, String programName) {
    super();
    this.id = id;
    this.programName = programName;
  }

  public ProgramImpl() {
    super();
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getProgramName() {
    return programName;
  }

  @Override
  public void setProgramName(String programName) {
    this.programName = programName;
  }

}
