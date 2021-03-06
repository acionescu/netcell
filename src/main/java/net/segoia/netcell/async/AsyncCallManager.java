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
package net.segoia.netcell.async;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.netcell.vo.WorkflowContext;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.processing.SimpleRunnableProcessor;

public class AsyncCallManager {
    private AsyncCallManagerConfig config = new AsyncCallManagerConfig();

    private static AsyncCallManager instance;

    public static AsyncCallManager getInstance() {
	if (instance == null) {
	    instance = new AsyncCallManager();
	}
	return instance;
    }

    private AsyncCallManager() {

    }

    public AsyncResponseHandler execute(WorkflowContext context) {
	AsyncResponseHandler responseHandler = new AsyncResponseHandler();
	if (config.getExecutor().getActiveCount() < 500) {
	    SimpleRunnableProcessor<WorkflowContext, GenericNameValueContext> rp = new SimpleRunnableProcessor<WorkflowContext, GenericNameValueContext>(
		    config.getProcessor(), context, responseHandler);
	    config.getExecutor().execute(rp);
	} else {
	    responseHandler.addError(context, new ContextAwareException("NO_RESOURCES_AVAILABLE"));
	}
	return responseHandler;
    }

    /**
     * @return the config
     */
    private AsyncCallManagerConfig getConfig() {
	return config;
    }

    /**
     * @param config
     *            the config to set
     */
    private void setConfig(AsyncCallManagerConfig config) {
	this.config = config;
    }

}
