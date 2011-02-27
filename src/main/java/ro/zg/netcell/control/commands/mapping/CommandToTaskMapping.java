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
package ro.zg.netcell.control.commands.mapping;

import java.util.List;

import ro.zg.netcell.control.Command;

public class CommandToTaskMapping {
    private String commandName;
    /**
     * The list of tasks to be sent when the specified command is received
     */
    private List<TaskMappingInfo> tasksList;
    
    /**
     * A {@link CommandResponseBuilder} may be specified for a {@link Command}
     */
    private CommandResponseBuilder commandResponseBuilder;
    /**
     * @return the commandName
     */
    public String getCommandName() {
        return commandName;
    }
    /**
     * @return the tasksList
     */
    public List<TaskMappingInfo> getTasksList() {
        return tasksList;
    }
    /**
     * @param commandName the commandName to set
     */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
    /**
     * @param tasksList the tasksList to set
     */
    public void setTasksList(List<TaskMappingInfo> tasksList) {
        this.tasksList = tasksList;
    }
   
    /**
     * @return the commandResponseBuilder
     */
    public CommandResponseBuilder getCommandResponseBuilder() {
        return commandResponseBuilder;
    }
    
    /**
     * @param commandResponseBuilder the commandResponseBuilder to set
     */
    public void setCommandResponseBuilder(CommandResponseBuilder commandResponseBuilder) {
        this.commandResponseBuilder = commandResponseBuilder;
    }
   
    
}
