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
package net.segoia.netcell.control.exceptions;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionHandler;
import net.segoia.netcell.control.CommandResponse;
import net.segoia.util.logging.Logger;
import net.segoia.util.logging.MasterLogManager;

/**
 * Maps all exceptions on {@link CommandResponse} instances in order to provide
 * the client with a useful error response
 * 
 * @author adi
 * 
 */
public class BorderExceptionHandler implements
	ExceptionHandler<CommandResponse> {
    private CommandResponsesManager responsesManager;
    private Logger globalErrorLogger;

    public CommandResponse handle(ContextAwareException cause)
	    throws ContextAwareException {

	CommandResponse response = responsesManager
		.getResponseForException(cause);
	/* if internal error code, then some unexpected error happened, log it */
	if (response.getResponseCode().equals(
		responsesManager.getInternalErrorResponseCode())) {
	    getGlobalErrorsLogger().error("Error: ", cause);
	}
	return response;
    }

    public CommandResponsesManager getResponsesManager() {
	return responsesManager;
    }

    public void setResponsesManager(CommandResponsesManager responsesManager) {
	this.responsesManager = responsesManager;
    }

    private Logger getGlobalErrorsLogger() {
	if (globalErrorLogger == null) {
	    globalErrorLogger = MasterLogManager.getLogger("ALL_ERRORS");
	}
	return globalErrorLogger;
    }
}
