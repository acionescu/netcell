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

import net.segoia.util.data.translation.DataTranslation;
import net.segoia.util.data.translation.DataTranslator;

public class JavaToHbaseTranslator implements DataTranslator<Object, Object> {
    private Map<String, DataTranslation> translations;
//    private HBaseConfiguration hbaseConfig = new HBaseConfiguration();

    public Object translate(Object source) throws Exception {
//	DataTranslation dt = translations.get(source.getClass().getName());
//	return translate(source, dt);
	return null;
    }

//    private void translate(Object source, Put put, String familyTree) throws Exception {
//	DataTranslation dt = translations.get(source.getClass().getName());
//	translate(source, dt, put, familyTree);
//    }
//
//    private Object translate(Object source, DataTranslation dt) throws Exception {
//	HTable table = new HTable(hbaseConfig, dt.getDestinationType());
//	Put put = new Put(Bytes.toBytes(dt.getIdGenerator().generate(source)));
//	translate(source, dt,put,"");
//	table.put(put);
//	return null;
//    }
//
//    private void translate(Object source, DataTranslation dt, Put put, String familyTree) throws Exception {
//	EntityMapping mapping = new EntityMapping();
//	mapping.setNestedMappings(dt.getDataMappings());
//	mapFields(source, mapping, put,familyTree);
//    }
//
//    private void mapFields(Object source, EntityMapping mapping, Put put, String familyTree) throws Exception {
//	if (source instanceof Map) {
//	    mapFieldsForMap((Map) source, mapping, put, familyTree);
//	} else if (source instanceof List) {
//	    mapFieldsForList((List) source, mapping, put,familyTree);
//	} else if (mapping.getNestedMappings() != null) {
//	    mapComplexObject(source, mapping, put, familyTree);
//	} else {
//	    mapSimpleObject(source, mapping, put, familyTree);
//	}
//    }
//
//    private void mapComplexObject(Object source, EntityMapping mapping, Put put, String familyTree) throws Exception {
//	for (Map.Entry<String, EntityMapping> entry : mapping.getNestedMappings().entrySet()) {
//	    Object fieldValue = ReflectionUtility.getValueForField(source, entry.getKey());
//	    EntityMapping fieldMapping = entry.getValue();
//	    mapFields(fieldValue, fieldMapping, put,familyTree);
//	}
//    }
//
//    private void mapSimpleObject(Object source, EntityMapping mapping, Put put, String familyTree) {
//	/* expects a string in the format <family>:<column> */
//	String path = familyTree;
//	String tgName = mapping.getTargetName();
//	if(tgName != null && !"".equals(tgName.trim())){
//	    if(path != null && !"".equals(path.trim())) {
//		path += ":"+tgName;
//	    }
//	    else {
//		path = tgName;
//	    }
//	}
//	int familyIndex = path.indexOf(":");
//	String family = path.substring(0, familyIndex);
//	String column = path.substring(familyIndex + 1);
//	put.add(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(source.toString()));
//    }
//
//   
//    private void mapFieldsForMap(Map<Object, Object> object, EntityMapping mapping, Put put, String familyTree) throws Exception {
//	for (Object keyObj : object.keySet()) {
//	    Object valueObj = object.get(keyObj);
//	    String newFamilyTree = familyTree;
//	    if(familyTree != null && !"".equals(familyTree)) {
//		newFamilyTree = familyTree+":"+mapping.getTargetName()+":"+keyObj.toString();
//	    }
//	    else {
//		newFamilyTree = mapping.getTargetName()+":"+keyObj.toString();
//	    }
//	    translate(valueObj, put, newFamilyTree);
//	}
//    }
//
//    private void mapFieldsForList(List<Object> object, EntityMapping mapping, Put put, String familyTree) {
//
//    }
//
//    /**
//     * @return the translations
//     */
//    public Map<String, DataTranslation> getTranslations() {
//	return translations;
//    }
//
//    /**
//     * @param translations
//     *            the translations to set
//     */
//    public void setTranslations(Map<String, DataTranslation> translations) {
//	this.translations = translations;
//    }
}
