package ro.zg.netcell.datasources.executors.ldap;

import com.unboundid.ldap.sdk.LDAPConnection;

import ro.zg.scriptdao.core.CommandContext;
import ro.zg.scriptdao.core.CommandExecutor;
import ro.zg.scriptdao.core.ScriptDaoCommand;

public class LdapCommandExecutor implements CommandExecutor<LdapCommandResponse>{

   
    public LdapCommandResponse executeCommand(CommandContext commandContext) throws Exception {
	LDAPConnection ldapConnection = (LDAPConnection)commandContext.getConnectionManager().getConnection();
	ScriptDaoCommand command = commandContext.getCommand();
	
	String baseDn = (String)command.getArgument("baseDn");
	String searchScope = (String)command.getArgument("searchScope");
	
	
	return null;
    }
    
    public LdapCommandResponse executeCommand(ScriptDaoCommand command) throws Exception {
	throw new UnsupportedOperationException();
    }

    public LdapCommandResponse[] executeAsTransaction(ScriptDaoCommand[] commands) throws Exception {
	throw new UnsupportedOperationException();
    }

    public LdapCommandResponse[] executeAsTransaction(CommandContext commandContext) throws Exception {
	throw new UnsupportedOperationException();
    }

}
