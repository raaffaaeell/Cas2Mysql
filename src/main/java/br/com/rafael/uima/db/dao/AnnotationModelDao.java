package br.com.rafael.uima.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.uima.util.Level;

import br.com.rafael.uima.db.util.DBUtils;
import br.com.rafael.uima.model.AnnotationModel;

public class AnnotationModelDao extends AbstractDao {

	public AnnotationModelDao(String driver, String database, String url, String table)
	throws ClassNotFoundException, SQLException {
		super(driver, database, url);
		this.table = table;
	}

	private String table;

	public void createTable() throws SQLException {

		logger.log(Level.INFO, "Create table method");
		Statement stmt = null;
		try {
			stmt = getConnection().createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS " + table + " (doc varchar(255) DEFAULT NULL, " +
				" beginpos int(11) DEFAULT NULL, " + " endpos int(11) DEFAULT NULL, " + " coveredText text, " +
				" createdAt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
				" annotationName varchar(255) DEFAULT NULL )";

			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error creating table");
			throw e;
		} finally {
			if (stmt != null) {
				DBUtils.closeStmt(stmt);
			}
		}

		logger.log(Level.INFO, "Create table successfull");
	}

	public int[] insertAnnotations(List < AnnotationModel > annotations, String Doc) throws SQLException {

		int[] ret;
		PreparedStatement ps = null;
		Connection connection = null;

		String query = "INSERT INTO " + this.table + " (doc, annotationName, coveredText, beginpos, endpos, createdAt) " +
			"VALUES(?, ?, ?, ?, ?, ?)";

		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(query);

			ps.setString(1, Doc);
			ps.setTimestamp(6, new Timestamp((new Date()).getTime()));

			for (AnnotationModel annotation: annotations) {
				ps.setString(2, annotation.getAnnotName());
				ps.setString(3, annotation.getAnnotCoveredText());
				ps.setInt(4, annotation.getStartPos());
				ps.setInt(5, annotation.getEndPos());

				ps.addBatch();
			}

			logger.log(Level.INFO, "Executing batch");
			ret = ps.executeBatch();

		} catch (SQLException e) {
			DBUtils.Rollback(connection);
			logger.log(Level.WARNING, "Error inserting annotations");
			throw e;
		} finally {
			if (ps != null) {
				DBUtils.closePs(ps);
			}
			connection.setAutoCommit(true);
		}

		return ret;

	}

	public void dropTable() throws SQLException {

		Statement stmt = null;
		try {
			stmt = getConnection().createStatement();

			String sql = "DROP TABLE " + this.table;
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error in drop table");
			throw e;
		} finally {

			DBUtils.closeStmt(stmt);
		}



	}
}