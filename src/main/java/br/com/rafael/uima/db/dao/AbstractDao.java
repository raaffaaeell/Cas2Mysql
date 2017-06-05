package br.com.rafael.uima.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public abstract class AbstractDao {

	protected Logger logger;

	private Connection connection;
	private String url;
	private String database;
	private String driver;

	public AbstractDao(String driver, String database, String url) throws ClassNotFoundException, SQLException {
		super();

		this.database = database;
		this.url = url;
		this.driver = driver;

	}

	public void initConnection() throws ClassNotFoundException, SQLException {

		try {
			Class.forName(driver);

			String connectionUrl = "jdbc:" + database + ":" + url;

			connection = DriverManager.getConnection(connectionUrl);
			logger.log(Level.INFO, "Got connection");
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Driver not found. Error: " + e.getMessage());
			System.err.println();
			throw e;
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error stabilishing connection. Error: " + e.getMessage());
			throw e;
		}


	}

	public Connection getConnection() {
		return connection;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}