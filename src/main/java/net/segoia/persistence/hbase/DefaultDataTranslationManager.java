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
package net.segoia.persistence.hbase;

import java.util.Map;

import net.segoia.util.data.translation.DataTranslationManager;
import net.segoia.util.data.translation.DataTranslationRequest;
import net.segoia.util.data.translation.DataTranslator;

public class DefaultDataTranslationManager<S,D> implements DataTranslationManager<S, D>{
    private Map<String,DataTranslator<S, D>> translators;
    
    public D transalte(DataTranslationRequest<S> dtr) throws Exception {
	DataTranslator<S, D> translator = translators.get(dtr.getTranslationType());
	return translator.translate(dtr.getSourceObject());
    }

    /**
     * @return the translators
     */
    public Map<String, DataTranslator<S, D>> getTranslators() {
        return translators;
    }

    /**
     * @param translators the translators to set
     */
    public void setTranslators(Map<String, DataTranslator<S, D>> translators) {
        this.translators = translators;
    }

}
