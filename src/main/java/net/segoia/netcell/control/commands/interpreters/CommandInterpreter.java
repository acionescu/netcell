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
package net.segoia.netcell.control.commands.interpreters;

import net.segoia.netcell.control.Command;
import net.segoia.netcell.control.CommandResponse;
import net.segoia.netcell.control.commands.formatters.ObjectFormatter;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.execution.ExecutionEntity;

public abstract class CommandInterpreter<F> implements ExecutionEntity<String, String>{
    private ExecutionEntity<Command,CommandResponse> commandExecutor;
    private ObjectFormatter<F> formatter;

    public abstract GenericNameValueContext executeWithoutFormattingResult(String input)throws Exception;

    public ExecutionEntity<Command, CommandResponse> getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(ExecutionEntity<Command, CommandResponse> commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public ObjectFormatter<F> getFormatter() {
        return formatter;
    }

    public void setFormatter(ObjectFormatter<F> formatter) {
        this.formatter = formatter;
    }
    
}
