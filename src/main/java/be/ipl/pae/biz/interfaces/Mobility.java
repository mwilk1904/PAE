package be.ipl.pae.biz.interfaces;

import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.dto.ProgramDto;

public interface Mobility extends MobilityDto {
  boolean checkMobility();

  boolean isCancelled();

  boolean isThisAcademicYear();

  boolean isBeforeDocsFilled(ProgramDto program);

  boolean isAllDocsFilled(ProgramDto program);

  boolean isOneDocFilled(ProgramDto program);
}
