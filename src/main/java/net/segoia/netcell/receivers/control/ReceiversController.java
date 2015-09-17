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
package net.segoia.netcell.receivers.control;

import java.io.File;
import java.util.concurrent.Executors;

import net.segoia.cfgengine.core.configuration.ConfigurationManager;
import net.segoia.cfgengine.core.exceptions.ConfigurationException;
import net.segoia.cfgengine.util.PackageCfgLoader;
import net.segoia.netcell.control.Command;
import net.segoia.netcell.control.CommandResponse;
import net.segoia.netcell.control.NetCell;
import net.segoia.netcell.control.NetcellNode;
import net.segoia.netcell.control.commands.interpreters.cas.CasCommandInterpreter;
import net.segoia.netcell.control.commands.interpreters.jsonrpc.JsonrpcCommandInterpreter;
import net.segoia.netcell.control.exceptions.InitializationException;
import net.segoia.netcell.control.receivers.RmiCommandReceiver;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.execution.ExecutionEntity;
import net.segoia.util.io.Receiver;
import net.segoia.util.io.SocketProcessor;
import net.segoia.util.io.SocketReceiver;
import net.segoia.util.io.ThreadedReceiver;
import net.segoia.util.logging.Logger;
import net.segoia.util.logging.MasterLogManager;

public class ReceiversController implements NetcellNode {
    public static String CFG_FILE_NAME = "receivers-controller-config.xml";
    // public static String LOGGING_CFG_FILE = "logging.xml";
    private static Logger logger = MasterLogManager.getLogger(ReceiversController.class.getName());

    private String rootDir;
    private ExecutionEntity<Command, CommandResponse> commandExecutor;
    private ConfigurationManager cfgManager;

    private CasCommandInterpreter casInterpreter;
    private JsonrpcCommandInterpreter jsonInterpreter;

    public void start(String rootDir, ExecutionEntity<Command, CommandResponse> commandExecutor)
	    throws InitializationException {
	this.rootDir = rootDir;
	this.commandExecutor = commandExecutor;
	init();
    }

    private void init() throws InitializationException {
	loadConfig();
	startSubprocesses();
    }

    /**
     * Loads the information from the configuration file
     * 
     * @throws Exception
     */
    private void loadConfig() throws InitializationException {
	try {
	    cfgManager = PackageCfgLoader.getInstance().load(getConfigFilePath(), this.getClass().getClassLoader());
	} catch (ConfigurationException e) {
	    throw new InitializationException("Error loading receivers from " + getConfigFilePath() + e);
	}

    }

    private String getConfigFilePath() {
	return new File(rootDir, CFG_FILE_NAME).getPath();
    }

    public void startSubprocesses() throws InitializationException {
	startCasReceiver();
	startJsonReceiver();
	startRmiReceiver();
    }

    private void startReceiver(Receiver rec) {
	new ThreadedReceiver(rec).start();
    }

    private void startCasReceiver() throws InitializationException {
	// TODO: This should be configured dynamically based on the tag names or other way
	CasCommandInterpreter ci = (CasCommandInterpreter) cfgManager.getObjectById("CasCommandInterpreter");
	ci.setCommandExecutor(commandExecutor);
	// TODO: The executor should be obtained from the Runtime Environment or based on a configuration
	SocketProcessor pr = (SocketProcessor) cfgManager.getObjectById("CasCommandsProcessor");
	pr.setExecutor(Executors.newCachedThreadPool());
	// TODO: This should also be done dynamically
	SocketReceiver rec = (SocketReceiver) cfgManager.getObjectById("CasCommandsSocketReceiver");
	casInterpreter = ci;
	startReceiver(rec);
    }

    private void startJsonReceiver() throws InitializationException {
	// TODO: This should be configured dynamically based on the tag names or other way
	JsonrpcCommandInterpreter ci = (JsonrpcCommandInterpreter) cfgManager.getObjectById("JsonCommandInterpreter");

	if (ci == null) {
	    return;
	}
	ci.setCommandExecutor(commandExecutor);
	// TODO: The executor should be obtained from the Runtime Environment or based on a configuration
	SocketProcessor pr = (SocketProcessor) cfgManager.getObjectById("JsonCommandsProcessor");
	pr.setExecutor(Executors.newCachedThreadPool());
	// TODO: This should also be done dynamically
	SocketReceiver rec = (SocketReceiver) cfgManager.getObjectById("JsonCommandsSocketReceiver");
	jsonInterpreter = ci;
	startReceiver(rec);
    }

    private void startRmiReceiver() throws InitializationException {
	RmiCommandReceiver rec = (RmiCommandReceiver) cfgManager.getObjectById("RmiCommandReceiver");
	rec.setEngine((NetCell) commandExecutor);
	startReceiver(rec);
    }
    /**
     * Convenience method to execute a cas command from within the same node
     */
    @Override
    public String executeCasCommand(String command) throws Exception {
	return casInterpreter.execute(command);
    }
    /**
     * Convenience method to execute a jsonrpc command from within the same node
     */
    @Override
    public String executeJsonrpcCommand(String command) throws Exception {
	return jsonInterpreter.execute(command);
    }

    @Override
    public GenericNameValueContext executeCasWithoutFormattingResult(String command) throws Exception {
	return casInterpreter.executeWithoutFormattingResult(command);
    }

    @Override
    public GenericNameValueContext executeJsonrpcWithoutFormattingResult(String command) throws Exception {
	return jsonInterpreter.executeWithoutFormattingResult(command);
    }
}
