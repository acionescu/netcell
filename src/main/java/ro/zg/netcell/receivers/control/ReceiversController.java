/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.netcell.receivers.control;

import java.io.File;
import java.util.concurrent.Executors;

import ro.zg.cfgengine.core.configuration.ConfigurationManager;
import ro.zg.cfgengine.core.exceptions.ConfigurationException;
import ro.zg.cfgengine.util.PackageCfgLoader;
import ro.zg.netcell.control.Command;
import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.control.NetCell;
import ro.zg.netcell.control.commands.interpreters.CommandInterpreter;
import ro.zg.netcell.control.commands.interpreters.cas.CasCommandInterpreter;
import ro.zg.netcell.control.exceptions.InitializationException;
import ro.zg.netcell.control.receivers.RmiCommandReceiver;
import ro.zg.util.execution.ExecutionEntity;
import ro.zg.util.io.Receiver;
import ro.zg.util.io.SocketProcessor;
import ro.zg.util.io.SocketReceiver;
import ro.zg.util.io.ThreadedReceiver;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class ReceiversController {
    public static String CFG_FILE_NAME = "receivers-controller-config.xml";
//    public static String LOGGING_CFG_FILE = "logging.xml";
    private static Logger logger = MasterLogManager.getLogger("ReceiversControllerLogger");
    
    private String rootDir;
    private ExecutionEntity<Command, CommandResponse> commandExecutor;
    private ConfigurationManager cfgManager;
    
    public void start(String rootDir, ExecutionEntity<Command, CommandResponse> commandExecutor) throws InitializationException{
	this.rootDir = rootDir;
	this.commandExecutor = commandExecutor;
	init();
    }
    
    private void init() throws InitializationException{
	loadConfig();
	startSubprocesses();
	logger.info("Receivers controller successfully initialized.");
    }
    
    /**
     * Loads the information from the configuration file
     * @throws Exception 
     */
    private void loadConfig() throws InitializationException{
	try {
	    cfgManager = PackageCfgLoader.getInstance().load(getConfigFilePath(),this.getClass().getClassLoader());
	} catch (ConfigurationException e) {
	    throw new InitializationException("Error loading receivers from "+getConfigFilePath()+e);
	}
	
    }
    
    private String getConfigFilePath(){
	return new File(rootDir,CFG_FILE_NAME).getPath();
    }
    
    
    public void startSubprocesses() throws InitializationException{
	startCasReceiver();
	startJsonReceiver();
	startRmiReceiver();
    }
    
    private void startReceiver(Receiver rec){
	new ThreadedReceiver(rec).start();
    }
    
    private void startCasReceiver() throws InitializationException{
	//TODO: This should be configured dynamically based on the tag names or other way
	CasCommandInterpreter ci = (CasCommandInterpreter)cfgManager.getObjectById("CasCommandInterpreter");
	ci.setCommandExecutor(commandExecutor);
	//TODO: The executor should be obtained from the Runtime Environment or based on a configuration
	SocketProcessor pr = (SocketProcessor)cfgManager.getObjectById("CasCommandsProcessor");
	pr.setExecutor(Executors.newCachedThreadPool());
	//TODO: This should also be done dynamically
	SocketReceiver rec = (SocketReceiver)cfgManager.getObjectById("CasCommandsSocketReceiver");
	
	startReceiver(rec);
    }
    
    private void startJsonReceiver() throws InitializationException{
   	//TODO: This should be configured dynamically based on the tag names or other way
	CommandInterpreter ci = (CommandInterpreter)cfgManager.getObjectById("JsonCommandInterpreter");
   	ci.setCommandExecutor(commandExecutor);
   	//TODO: The executor should be obtained from the Runtime Environment or based on a configuration
   	SocketProcessor pr = (SocketProcessor)cfgManager.getObjectById("JsonCommandsProcessor");
   	pr.setExecutor(Executors.newCachedThreadPool());
   	//TODO: This should also be done dynamically
   	SocketReceiver rec = (SocketReceiver)cfgManager.getObjectById("JsonCommandsSocketReceiver");
   	
   	startReceiver(rec);
       }
    
    private void startRmiReceiver() throws InitializationException{
	RmiCommandReceiver rec = (RmiCommandReceiver)cfgManager.getObjectById("RmiCommandReceiver");
	rec.setEngine((NetCell)commandExecutor);
	startReceiver(rec);
    }
}
