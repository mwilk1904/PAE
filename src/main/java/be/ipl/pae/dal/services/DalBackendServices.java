package be.ipl.pae.dal.services;

import java.sql.PreparedStatement;

public interface DalBackendServices {

  PreparedStatement getPreparedStatement(String query);

}
