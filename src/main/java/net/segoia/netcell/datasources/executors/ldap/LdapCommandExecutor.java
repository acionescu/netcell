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
package net.segoia.netcell.datasources.executors.ldap;

import net.segoia.scriptdao.core.CommandContext;
import net.segoia.scriptdao.core.CommandExecutor;
import net.segoia.scriptdao.core.ScriptDaoCommand;

import com.unboundid.ldap.sdk.LDAPConnection;

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
