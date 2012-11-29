package ro.zg.netcell.control.commands.interpreters.jsonrpc;

import ro.zg.netcell.control.Command;

public class JsonRpcCommand {
    private String clientCommandId;
    private Command command;
    
    public JsonRpcCommand(String clientCommandId, Command command) {
	super();
	this.clientCommandId = clientCommandId;
	this.command = command;
    }
    /**
     * @return the clientCommandId
     */
    public String getClientCommandId() {
        return clientCommandId;
    }
    /**
     * @return the command
     */
    public Command getCommand() {
        return command;
    }
    
    

}
