package ro.zg.netcell.datasources.executors.cassandra;

import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class CassandraUtil {
    
    public static GenericNameValueList getResultsAsList(ResultSet rs){
	ColumnDefinitions columnDefinitions = rs.getColumnDefinitions();
	
	int colDefSize = columnDefinitions.size();
	
	GenericNameValueList resultsList = new GenericNameValueList();
	
	while(!rs.isExhausted()) {
	    Row row = rs.one();
	    GenericNameValueContext rowData = new GenericNameValueContext();
	    
	    for(int i=0;i < colDefSize;i++) {
		String colName = columnDefinitions.getName(i);
		DataType colType = columnDefinitions.getType(i);
		
		rowData.put(colName, colType.deserialize(row.getBytesUnsafe(i)));
	    }
	    
	    resultsList.addValue(rowData);
	};
	
	return resultsList;
    }

}
