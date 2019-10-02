package be.ipl.pae.dal.services;

public interface DalServices {

  void startTransaction();

  void rollback();

  void commit();

}
