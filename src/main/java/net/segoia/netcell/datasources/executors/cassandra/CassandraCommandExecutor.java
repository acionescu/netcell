/**
 * netcell - Java ESB with an embedded business process modeling engine
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.netcell.datasources.executors.cassandra;

import net.segoia.scriptdao.core.CommandContext;
import net.segoia.scriptdao.core.CommandExecutor;
import net.segoia.scriptdao.core.ScriptDaoCommand;
import net.segoia.util.data.GenericNameValueList;

import org.apache.log4j.Logger;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class CassandraCommandExecutor implements CommandExecutor<CassandraCommandResponse> {
    private static Logger logger = Logger.getLogger(CassandraCommandExecutor.class.getName());

    @Override
    public CassandraCommandResponse executeCommand(ScriptDaoCommand command) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public CassandraCommandResponse[] executeAsTransaction(ScriptDaoCommand[] commands) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public CassandraCommandResponse executeCommand(CommandContext commandContext) throws Exception {
	Session session = (Session) commandContext.getConnectionManager().getConnection();

	if (session == null) {
	    logger.error("Could not get a connection to cassandra.");
	    return null;
	}

	ScriptDaoCommand command = commandContext.getCommand();
	if (logger.isDebugEnabled()) {
	    logger.debug("Executing " + command);
	}

	ResultSet result = session.execute(command.getContent());

	try {
	    GenericNameValueList resultsAsList = CassandraUtil.getResultsAsList(result, session.getCluster()
		    .getConfiguration().getProtocolOptions().getProtocolVersion());
	    if (logger.isDebugEnabled()) {
		logger.debug("Returning response to cassandra query: " + resultsAsList);
	    }
	    CassandraCommandResponse response = new CassandraCommandResponse(resultsAsList);
	    return response;
	} catch (Exception e) {
	    logger.error("Error when processing command " + command.getContent(), e);
	    throw e;
	}

    }

    @Override
    public CassandraCommandResponse[] executeAsTransaction(CommandContext commandContext) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

}
