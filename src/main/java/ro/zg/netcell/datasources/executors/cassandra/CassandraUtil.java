package ro.zg.netcell.datasources.executors.cassandra;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

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
		    rowData.put(colName, colType.deserialize(bytesUnsafe, pv));
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
