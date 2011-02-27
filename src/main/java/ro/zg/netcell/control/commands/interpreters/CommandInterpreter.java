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
package ro.zg.netcell.control.commands.interpreters;

import ro.zg.netcell.control.Command;
import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.control.commands.formatters.ObjectFormatter;
import ro.zg.util.execution.ExecutionEntity;

public abstract class CommandInterpreter implements ExecutionEntity<String, String>{
    private ExecutionEntity<Command,CommandResponse> commandExecutor;
    private ObjectFormatter<CommandResponse> formatter;

   

    public ExecutionEntity<Command, CommandResponse> getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(ExecutionEntity<Command, CommandResponse> commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public ObjectFormatter<CommandResponse> getFormatter() {
        return formatter;
    }

    public void setFormatter(ObjectFormatter<CommandResponse> formatter) {
        this.formatter = formatter;
    }
    
}
