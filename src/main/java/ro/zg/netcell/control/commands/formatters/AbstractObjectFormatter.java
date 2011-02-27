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
package ro.zg.netcell.control.commands.formatters;

import java.util.Collection;
import java.util.Map;

public class AbstractObjectFormatter implements ObjectFormatter<Object> {
    private Map<String, BaseObjectFormatter<Object>> formatters;
    private ObjectFormatter<Object> defaultFormatter;

    public void init() {
	for (BaseObjectFormatter f : formatters.values()) {
	    f.setParentFormatter(this);
	}
    }

    public String format(Object obj) throws Exception {
	ObjectFormatter<Object> formatter = formatters.get(obj.getClass().getName());
	if (formatter == null) {
	    formatter = formatters.get(obj.getClass().getSimpleName());
	    if (formatter == null) {
		if (obj instanceof Collection) {
		    formatter = formatters.get("Collection");
		}
		if (formatter == null) {
		    if (defaultFormatter != null) {
			formatter = defaultFormatter;
		    }
		    else{
			return obj.toString();
		    }
		}
	    }
	}
	return formatter.format(obj);
    }

    public Map<String, BaseObjectFormatter<Object>> getFormatters() {
	return formatters;
    }

    public void setFormatters(Map<String, BaseObjectFormatter<Object>> formatters) {
	this.formatters = formatters;
    }

    public ObjectFormatter<Object> getDefaultFormatter() {
	return defaultFormatter;
    }

    public void setDefaultFormatter(ObjectFormatter<Object> defaultFormatter) {
	this.defaultFormatter = defaultFormatter;
    }
}
