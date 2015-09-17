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

import net.segoia.netcell.vo.WorkflowContext;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.processing.Destination;

public class AsyncResponseHandler implements Destination<WorkflowContext, GenericNameValueContext>{
    private GenericNameValueContext response;
    private Exception error;
    private boolean finished = false;
    private WorkflowContext input;
    
    public void addError(WorkflowContext input, Exception e) {
	error = e;
	this.input = input;
	setFinished();
    }

    public void addOutput(WorkflowContext input, GenericNameValueContext output) {
	response = output;
	setFinished();
    }
    
    private synchronized void setFinished() {
	finished = true;
	notifyAll();
    }

    public synchronized void waitToFinish() throws Exception {
	if(!finished) {
	    wait();
	}
    }

    /**
     * @return the response
     */
    public GenericNameValueContext getResponse() {
        return response;
    }

    /**
     * @return the error
     */
    public Exception getError() {
        return error;
    }

    /**
     * @return the finished
     */
    public boolean isFinished() {
        return finished;
    }

    public boolean isSucessful() {
	return (response != null);
    }

    /**
     * @return the input
     */
    public WorkflowContext getInput() {
        return input;
    }
    
    
}
