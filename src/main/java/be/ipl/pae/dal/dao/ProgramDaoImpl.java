package be.ipl.pae.dal.dao;

import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.interfaces.BizFactory;
import be.ipl.pae.dal.interfaces.ProgramDao;
import be.ipl.pae.dal.services.DalBackendServices;
import be.ipl.pae.exceptions.DalException;

import utils.Logs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProgramDaoImpl implements ProgramDao {
  private DalBackendServices dalBackendServices;
  private BizFactory bizFactory;

  /**
   * Cree un objet ProgramDaoImpl.
   * 
   * @param dalBackendServices un objet DalBackendServices.
   * @param bizFactory un objet BizFactory.
   */
  public ProgramDaoImpl(BizFactory bizFactory, DalBackendServices dalBackendServices) {
    super();
    this.dalBackendServices = dalBackendServices;
    this.bizFactory = bizFactory;
  }

  @Override
  public List<ProgramDto> findAllPrograms() {
    try {
      PreparedStatement findAllPrograms =
          dalBackendServices.getPreparedStatement("SELECT * FROM projet_ae.programs");
      try (ResultSet rs = findAllPrograms.executeQuery()) {
        List<ProgramDto> programs = new ArrayList<ProgramDto>();
        while (rs.next()) {
          Logs.fine("Récupération de l'instance ProgramDto avec un id " + rs.getInt(1)
              + " dans la base de données");
          ProgramDto program = bizFactory.getProgramDto();
          program.setId(rs.getInt(1));
          program.setProgramName(rs.getString(2));
          programs.add(program);
        }

        return Collections.unmodifiableList(programs);
      }
    } catch (SQLException ex) {
      throw new DalException("Erreur lors de selectPrograms ", ex);
    }
  }

  @Override
  public ProgramDto findProgramById(int id) {
    try {
      PreparedStatement findProgramById = dalBackendServices
          .getPreparedStatement("SELECT * FROM projet_ae.programs WHERE program_id=?");
      findProgramById.setInt(1, id);
      return getPreparedStatementResult(findProgramById);
    } catch (SQLException sqle) {
      throw new DalException("Erreur lors de findProgramById", sqle);
    }

  }

  private ProgramDto getPreparedStatementResult(PreparedStatement statement) throws SQLException {
    ProgramDto programToComplete = bizFactory.getProgramDto();
    try (ResultSet rs = statement.executeQuery()) {
      if (rs.next()) {
        Logs.fine("Récupération de l'instance ProgramDto associée à la mobilité " + rs.getInt(1)
            + " dans la base de données");
        programToComplete.setId(rs.getInt(1));
        programToComplete.setProgramName(rs.getString(2));

        return programToComplete;
      }
    }
    return null;
  }


}
