package be.ipl.pae.dal.interfaces;

import be.ipl.pae.biz.dto.ProgramDto;

import java.util.List;

public interface ProgramDao {
  List<ProgramDto> findAllPrograms();

  ProgramDto findProgramById(int id);
}
