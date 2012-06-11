/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 * $Id: GenericSqlCommandDao.java,v 1.4 2008/07/22 09:51:44 aionescu Exp $
 */
package ro.zg.netcell.datasources.executors.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ro.zg.scriptdao.core.ScriptDaoCommand;
import ro.zg.scriptdao.core.CommandContext;
import ro.zg.scriptdao.core.CommandExecutor;

public class SqlCommandExecutor implements CommandExecutor<SqlCommandResponse>{
	private static Logger logger = Logger.getLogger(SqlCommandExecutor.class
			.getName());

	private DataSource dataSource;
	
	/**
	 * JNDI name of datasource defined in server
	 */
	private String dataSourceName;
	private int maxRowsToFetch = 500;
	public SqlCommandExecutor(){
		
	}
	
	public SqlCommandExecutor(DataSource datasource){
		this.dataSource = datasource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected Connection getConnection() throws SQLException {
		if(dataSource == null) {
			if(dataSourceName != null) {
				Context ctx;
				try {
					ctx = new InitialContext();
					dataSource = (DataSource) (ctx.lookup(dataSourceName));
				} catch (NamingException e) {
					logger.error("Error obtaining a datasource for dataSourceName="+dataSourceName,e);
				}
				
			}
		}
		return dataSource.getConnection();
	}

	public SqlCommandResponse executeCommand(ScriptDaoCommand command) throws SQLException {
		if (logger.isDebugEnabled()) {
			logger.debug("Executing " + command);
		}

		SqlCommandResponse resp = null;
		Connection conn = getConnection();
		if (conn == null) {
			logger.error("Could not get a connection to db.");
			return null;
		}
		try {
			PreparedStatement stmt = conn.prepareStatement(command.getContent());
			boolean isResultSet = stmt.execute();
			stmt.setMaxRows(maxRowsToFetch);
			if (isResultSet) {
				resp = new SqlCommandResponse(DbUtil.getResultsAsList(stmt.getResultSet()));
			} else {
				resp = new SqlCommandResponse(stmt.getUpdateCount());
			}
		}
		finally {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		if (logger.isDebugEnabled()) {
			if (resp != null) {
				logger.debug("Command executed on " + resp.getRowsCount()
						+ " records.");
			} else {
				logger.debug("Command response is null.");
			}
		}

		return resp;
	}

	public SqlCommandResponse[] executeAsTransaction(ScriptDaoCommand[] commands) throws SQLException {
		if (logger.isDebugEnabled()) {
			logger.debug("Starting transaction with " + commands.length
					+ " commands");
		}
		Connection conn = getConnection();
		if (conn == null) {
			logger.error("Could not get a connection to db.");
			return null;
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error("Error setting autocommit to false", e);
			if( conn != null) {
				conn.close();
			}
			return null;
		}
		
		SqlCommandResponse[] responses = new SqlCommandResponse[commands.length];
		String command = null;
		try {
			for (int i = 0; i < commands.length; i++) {
				command = commands[i].getContent();
				if(logger.isDebugEnabled()) {
					logger.debug("Executing command["+i+"]: "+command );
				}
				SqlCommandResponse resp = null;

				PreparedStatement stmt = conn.prepareStatement(command);
				boolean isResultSet = stmt.execute();

				if (isResultSet) {
					resp = new SqlCommandResponse(DbUtil.getResultsAsList(stmt.getResultSet()));
				} else {
					resp = new SqlCommandResponse(stmt.getUpdateCount());
				}
				
				responses[i] = resp;
			}
			//all commands successfully executed. commiting transaction
			conn.commit();
			
		} catch (SQLException e) {
			logger.error("Error in transaction when executing '"+command+"'. Starting rollback.",e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					logger.debug("Error on rollback",e);
					throw e1;
				}
			}
			throw e;
		}
		finally {
			if(conn != null) {
				conn.close();
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Transaction ended.");
		}
		
		return responses;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public int getMaxRowsToFetch() {
		return maxRowsToFetch;
	}

	public void setMaxRowsToFetch(int maxRowsToFetch) {
		this.maxRowsToFetch = maxRowsToFetch;
	}

	public SqlCommandResponse[] executeAsTransaction(CommandContext commandContext) throws Exception {
	    // TODO Auto-generated method stub
	    return null;
	}

	public SqlCommandResponse executeCommand(CommandContext commandContext) throws Exception {
	    // TODO Auto-generated method stub
	    return null;
	}
	
	
}
