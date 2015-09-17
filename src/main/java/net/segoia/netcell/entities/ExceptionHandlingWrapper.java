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
package net.segoia.netcell.entities;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.commons.exceptions.ExceptionHandler;
import net.segoia.util.execution.ExecutionEntity;

public class ExceptionHandlingWrapper<I,O> implements ExecutionEntity<I,O>{
    private ExecutionEntity<I, O> wrappedEntity;
    private ExceptionHandler<O> exceptionHandler;
    
    public O execute(I input) throws Exception {
	try{
	    return wrappedEntity.execute(input);
	}
	catch(ContextAwareException e){
	    return exceptionHandler.handle(e);
	}
	catch(Exception e){
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("executionEntity",wrappedEntity);
	    ec.put("inputContext",input);
	    return exceptionHandler.handle(new ContextAwareException("EXECUTION_ERROR",e,ec));
	}
    }

    public ExecutionEntity<I, O> getWrappedEntity() {
        return wrappedEntity;
    }

    public ExceptionHandler<O> getExceptionHandler() {
        return exceptionHandler;
    }

    public void setWrappedEntity(ExecutionEntity<I, O> wrappedEntity) {
        this.wrappedEntity = wrappedEntity;
    }

    public void setExceptionHandler(ExceptionHandler<O> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
    
}
