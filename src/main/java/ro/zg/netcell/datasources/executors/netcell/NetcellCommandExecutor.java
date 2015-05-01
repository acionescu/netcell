package ro.zg.netcell.datasources.executors.netcell;

import ro.zg.netcell.control.Command;
import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.control.NetCell;
import ro.zg.netcell.control.NetcellNode;
import ro.zg.scriptdao.core.CommandContext;
import ro.zg.scriptdao.core.CommandExecutor;
import ro.zg.scriptdao.core.ScriptDaoCommand;
import ro.zg.util.data.GenericNameValueContext;

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
