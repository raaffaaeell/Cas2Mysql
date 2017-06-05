package br.com.rafael.uima.db.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class DBUtils {
	public static PreparedStatement closePs(PreparedStatement ps){
		try {
			if(ps != null){
				ps.close();
				ps = null;
			}
		}
		catch(Exception e) {
			return null;
		}		
		return ps;
	}

	public static Statement closeStmt(Statement stmt){
		try {
			if(stmt != null){
				stmt.close();
				stmt = null;
			}
		}
		catch(Exception e) {
			return null;
		}		
		return stmt;
	}

	
	public static ResultSet closeResultSet(ResultSet resultSet){
		try {
			if(resultSet != null){
				resultSet.close();
				resultSet = null;
			}
		}
		catch(Exception e) {
			return null;
		}		
		
		return resultSet;
	}

	public static void Rollback(Connection con) throws SQLException {
		con.rollback();
	}

}
