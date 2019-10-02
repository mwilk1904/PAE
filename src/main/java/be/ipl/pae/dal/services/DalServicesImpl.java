package be.ipl.pae.dal.services;

import be.ipl.pae.exceptions.DalException;
import be.ipl.pae.exceptions.FatalException;

import org.apache.commons.dbcp2.BasicDataSource;

import utils.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DalServicesImpl implements DalBackendServices, DalServices {


  // {
  // url = Config.getValueOfKey("urlJDBC");
  // }

  // private String url;
  // private Connection conn = null;
  private BasicDataSource datasource; // réprésente la connection pool
  private ThreadLocal<Connection> connections;

  /**
   * Cree un objet DalServices.
   * 
   */
  public DalServicesImpl() {
    connections = new ThreadLocal<Connection>();
    try {
      datasource = new BasicDataSource();
      datasource.setDriverClassName(Config.getValueOfKey("driver"));
      datasource.setUrl(Config.getValueOfKey("urlJDBC"));
      datasource.setUsername(Config.getValueOfKey("pseudo"));
      datasource.setPassword(Config.getValueOfKey("pswd"));
    } catch (Exception ex) {
      throw new FatalException("Impossible de joindre le serveur.", ex);
    }
  }

  @Override
  public void startTransaction() {
    if (connections.get() != null) { // si le thread courant a déjà une valeur
      throw new FatalException("Transaction déjà ouverte");
    }
    try {
      Connection connection = this.datasource.getConnection(); // etablit une connection
      connections.set(connection);
      connection.setAutoCommit(false); // toute les instructions SQL sont considérés comme une
      // transaction
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * prépare une query
   * 
   * @param query la query correspondante.
   * @return la query demandée.
   */
  public PreparedStatement getPreparedStatement(String query) {
    try {
      return connections.get().prepareStatement(query);
    } catch (SQLException sqle) {
      throw new FatalException("Erreur de prepared statement", sqle);
    }
  }


  @Override
  public void commit() {
    try (Connection connection = connections.get()) {
      if (connection == null) {
        throw new FatalException("Ne peut pas commit si pas de transaction");
      }
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      connections.remove();
    }
  }



  @Override
  public void rollback() { // Annuler toutes les modifications faites par la transactions
    try (Connection connection = connections.get()) {
      if (connection == null) {
        throw new FatalException("Ne peut pas rollback si pas de transaction");
      }
      connection.rollback();
      connection.setAutoCommit(true);
    } catch (SQLException sqle) {
      throw new DalException("Erreur d'accès à la db", sqle);
    } finally {
      connections.remove();
    }

  }

}
