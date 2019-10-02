package be.ipl.pae.dal.interfaces;

import be.ipl.pae.biz.dto.MobilityDto;

import java.util.List;

public interface MobilityDao {

  MobilityDto findMobById(int mobId);

  void updateMobility(MobilityDto mob);

  void confirmMobility(int mobId);

  void confirmDocumentReception(MobilityDto mobDto);

  void insertMobility(MobilityDto mobDto);

  List<MobilityDto> getMobilities(int studentId);

  int countMobilitiesByStudentId(int studentId);

  int countUnconfirmedByStudentId(int studentId);

  int findVersionNumberFrom(MobilityDto mobDto);

  int findHighestPreferenceOrderNumberForStudent(int studentId);

  List<MobilityDto> getAllMobilities();

}
