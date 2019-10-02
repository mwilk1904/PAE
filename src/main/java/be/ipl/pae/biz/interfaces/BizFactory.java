package be.ipl.pae.biz.interfaces;

import be.ipl.pae.biz.dto.CountryDto;
import be.ipl.pae.biz.dto.MobilityAllDto;
import be.ipl.pae.biz.dto.MobilityDto;
import be.ipl.pae.biz.dto.MobilityPostulantDto;
import be.ipl.pae.biz.dto.PartnerDto;
import be.ipl.pae.biz.dto.ProgramDto;
import be.ipl.pae.biz.dto.StudentDto;
import be.ipl.pae.biz.dto.UserDto;

public interface BizFactory {

  UserDto getUserDto();

  PartnerDto getPartnerDto();

  CountryDto getCountryDto();

  MobilityDto getMobilityDto();

  StudentDto getStudentDto();

  ProgramDto getProgramDto();

  MobilityAllDto getMobilityAllDto();

  MobilityPostulantDto getMobilityPostulantDto();


}
