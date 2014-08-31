package ro.zg.netcell.datasources.executors.cassandra;

import org.apache.log4j.Logger;

import ro.zg.scriptdao.core.CommandContext;
import ro.zg.scriptdao.core.CommandExecutor;
import ro.zg.scriptdao.core.ScriptDaoCommand;
import ro.zg.util.data.GenericNameValueList;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class CassandraCommandExecutor implements CommandExecutor<CassandraCommandResponse>{
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
	Session session = (Session)commandContext.getConnectionManager().getConnection();
	
	if(session == null) {
	    logger.error("Could not get a connection to cassandra.");
	    return null;
	}
	
	ScriptDaoCommand command = commandContext.getCommand();
	if (logger.isDebugEnabled()) {
	    logger.debug("Executing " + command);
	}
	
	
	ResultSet result = session.execute(command.getContent());
	
	GenericNameValueList resultsAsList = CassandraUtil.getResultsAsList(result);
	
	CassandraCommandResponse response = new CassandraCommandResponse(resultsAsList);
	
	return response;
	
    }

    @Override
    public CassandraCommandResponse[] executeAsTransaction(CommandContext commandContext) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

}
