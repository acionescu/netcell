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
package net.segoia.netcell.control.receivers;

import java.net.Socket;

import net.segoia.util.execution.ExecutionEntity;
import net.segoia.util.io.SocketWorker;
import net.segoia.util.io.SocketWorkerFactory;

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
