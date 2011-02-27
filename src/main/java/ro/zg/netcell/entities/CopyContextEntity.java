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
package ro.zg.netcell.entities;

import java.util.List;
import java.util.Map;

import ro.zg.util.data.GenericNameValueContext;

public class CopyContextEntity extends GenericEntity<GenericNameValueContext> {

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	Object source = input.getValue("source");
	GenericNameValueContext destination = (GenericNameValueContext) input.getValue("destination");
	if (source instanceof List) {
	    destination.putAll((List) source);
	} else if (source instanceof GenericNameValueContext) {
	    ((GenericNameValueContext) source).copyTo(destination);
	} else if (source instanceof Map) {
	    destination.putAll((Map) source);
	}

	return new GenericNameValueContext();
    }

}
