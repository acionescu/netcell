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
package ro.zg.netcell.control.exceptions;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionHandler;
import ro.zg.netcell.control.CommandResponse;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

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
