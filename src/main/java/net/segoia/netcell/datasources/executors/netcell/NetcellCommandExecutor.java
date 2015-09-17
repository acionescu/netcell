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
package net.segoia.netcell.datasources.executors.netcell;

import net.segoia.netcell.control.Command;
import net.segoia.netcell.control.CommandResponse;
import net.segoia.netcell.control.NetCell;
import net.segoia.netcell.control.NetcellNode;
import net.segoia.scriptdao.core.CommandContext;
import net.segoia.scriptdao.core.CommandExecutor;
import net.segoia.scriptdao.core.ScriptDaoCommand;
import net.segoia.util.data.GenericNameValueContext;

public class NetcellCommandExecutor implements CommandExecutor<GenericNameValueContext>{

    @Override
    public GenericNameValueContext executeCommand(ScriptDaoCommand command) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public GenericNameValueContext[] executeAsTransaction(ScriptDaoCommand[] commands) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public GenericNameValueContext executeCommand(CommandContext commandContext) throws Exception {
	NetcellNode netcellNode = (NetcellNode)commandContext.getConnectionManager().getConnection();
	
	ScriptDaoCommand command = commandContext.getCommand();
	
	String content = command.getContent();
	
	return netcellNode.executeCasWithoutFormattingResult(content);
    }

    @Override
    public GenericNameValueContext[] executeAsTransaction(CommandContext commandContext) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

}
