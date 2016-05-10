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
package net.segoia.netcell.datasources.executors.cassandra;

import java.nio.ByteBuffer;

import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.GenericNameValueList;

import org.apache.log4j.Logger;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class CassandraUtil {
    private static Logger logger = Logger.getLogger(CassandraUtil.class.getName());

    public static GenericNameValueList getResultsAsList(ResultSet rs, ProtocolVersion pv) {
	if (logger.isDebugEnabled()) {
	    logger.debug("Extracting results for protocol " + pv);
	}
	ColumnDefinitions columnDefinitions = rs.getColumnDefinitions();

	int colDefSize = columnDefinitions.size();

	GenericNameValueList resultsList = new GenericNameValueList();

	int size = 0;
	while (!rs.isExhausted()) {
	    Row row = rs.one();
	    size++;
	    GenericNameValueContext rowData = new GenericNameValueContext();

	    for (int i = 0; i < colDefSize; i++) {
		String colName = columnDefinitions.getName(i);
		DataType colType = columnDefinitions.getType(i);
		
		ByteBuffer bytesUnsafe = row.getBytesUnsafe(i);
		if (bytesUnsafe != null) {
		    rowData.put(colName, CodecRegistry.DEFAULT_INSTANCE.codecFor(colType).deserialize(bytesUnsafe, pv));
		}
	    }

	    resultsList.addValue(rowData);
	}
	;
	if (logger.isDebugEnabled()) {
	    logger.debug("Extracted " + resultsList.size() + " results from " + size);
	}

	return resultsList;
    }

}
