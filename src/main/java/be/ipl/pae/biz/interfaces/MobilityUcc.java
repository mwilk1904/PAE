package be.ipl.pae.biz.interfaces;

import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.dto.UserDto;

import java.util.List;

public interface MobilityUcc {
  List<MobilityDto> getMobilities(int id, boolean isStudentId);

  List<MobilityDto> getAllMobilities();

  void setMobilityChoice(MobilityDto mob);

  void cancelMobilities(UserDto user, String reason);

  MobilityDto[] declareMobilities(int userId, MobilityDto[] mobDtos);

  void confirmMobility(int mobId);

  MobilityDto getMobility(int mobId);

  List<String> getCancellationReasons();

  void updateMobility(MobilityDto mob);

  void cancelMobility(UserDto user, String reason, int mobId);
}
