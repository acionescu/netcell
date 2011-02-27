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
import ro.zg.netcell.control.commands.formatters.ObjectFormatter;

public class OuterBorderExceptionHandler implements ExceptionHandler<String>{
    private BorderExceptionHandler borderExceptionHandler;
    private ObjectFormatter<CommandResponse> formatter;
    
    public String handle(ContextAwareException cause) throws ContextAwareException {
	try {
	    return formatter.format(borderExceptionHandler.handle(cause));
	} catch (Exception e) {
	    throw new ContextAwareException("FORMAT_EXCEPTION",e);
	}
    }
    public BorderExceptionHandler getBorderExceptionHandler() {
        return borderExceptionHandler;
    }
    public void setBorderExceptionHandler(BorderExceptionHandler borderExceptionHandler) {
        this.borderExceptionHandler = borderExceptionHandler;
    }
    public ObjectFormatter<CommandResponse> getFormatter() {
        return formatter;
    }
    public void setFormatter(ObjectFormatter<CommandResponse> formatter) {
        this.formatter = formatter;
    }

}
