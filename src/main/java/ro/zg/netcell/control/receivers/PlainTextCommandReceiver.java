package ro.zg.netcell.control.receivers;

import java.net.Socket;

import ro.zg.util.execution.ExecutionEntity;
import ro.zg.util.io.SimpleSocketWorker;

public class PlainTextCommandReceiver  extends SimpleSocketWorker{
    
    public PlainTextCommandReceiver(Socket socket) {
	super(socket);
    }

    private ExecutionEntity<String, String> commandInterpreter;

    @Override
    public String process(String input) throws Exception {
	return commandInterpreter.execute(input);
    }

    /**
     * @return the commandInterpreter
     */
    public ExecutionEntity<String, String> getCommandInterpreter() {
        return commandInterpreter;
    }

    /**
     * @param commandInterpreter the commandInterpreter to set
     */
    public void setCommandInterpreter(ExecutionEntity<String, String> commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }
    
    
}
