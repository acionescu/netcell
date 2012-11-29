package ro.zg.netcell.control.receivers;

import java.net.Socket;

import ro.zg.util.execution.ExecutionEntity;
import ro.zg.util.io.SocketWorker;
import ro.zg.util.io.SocketWorkerFactory;

public class PlainTextCommandReceiverFactory implements SocketWorkerFactory{
    private ExecutionEntity<String, String> interpreter;
    
    
    public SocketWorker createSocketWorker(Socket socket) {
	PlainTextCommandReceiver rec = new PlainTextCommandReceiver(socket);
	rec.setCommandInterpreter(interpreter);
	return rec;
    }


    public ExecutionEntity<String, String> getInterpreter() {
        return interpreter;
    }


    public void setInterpreter(ExecutionEntity<String, String> interpreter) {
        this.interpreter = interpreter;
    }
}
