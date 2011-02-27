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
package ro.zg.netcell.distributed;

import java.io.Serializable;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.distributed.framework.AbstractDistributedService;
import ro.zg.distributed.framework.Task;
import ro.zg.distributed.framework.TaskProcessingResponse;
import ro.zg.netcell.control.ExecutionEngineController;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.reflection.ReflectionUtility;

public class ExecutionEngineDS extends AbstractDistributedService{
    private ExecutionEngineController engine;
    
    public void start() throws ContextAwareException {
	super.start();
	engine.setResourcesLoader(getResourcesLoader());
	try {
	    engine.init();
	} catch (Exception e) {
	    throw new ContextAwareException("INIT_ERROR",e);
	}
    }
    
    @Override
    protected void configureProperty(String propName, Object value) throws ContextAwareException {
	try {
	    ReflectionUtility.setValueToField(engine, propName, value);
	} catch (Exception e) {
	    throw new ContextAwareException(e);
	}
    }

    public TaskProcessingResponse processTask(Task task) throws Exception {
	GenericNameValueContext input = (GenericNameValueContext)(((Serializable[])task.getContent())[0]);
	String log = "engineds:"+task.getTaskId()+":"+input+"=>";
	GenericNameValueContext output = engine.execute(input);
	log+= output;
	System.out.println(log);
	return new TaskProcessingResponse(task.getTaskId(),output);
    }

    /**
     * @return the engine
     */
    public ExecutionEngineController getEngine() {
        return engine;
    }

    /**
     * @param engine the engine to set
     */
    public void setEngine(ExecutionEngineController engine) {
        this.engine = engine;
    }

    
}
