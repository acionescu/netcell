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
/**
 * $Id: DbUtil.java,v 1.4 2008/06/25 12:34:26 vdumitra Exp $
 */
package net.segoia.netcell.datasources.executors.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.segoia.util.data.reflection.ReflectionUtility;
import net.segoia.util.parser.utils.ParseUtils;

import org.postgresql.util.PGobject;

/**
 * @author Victor
 * @version $Revision: 1.4 $
 * 
 */
public class DbUtil {

    /**
	 * 
	 */
    private DbUtil() {

    }

    public static List<Map<String, ?>> getResultsAsList(ResultSet rs) throws SQLException {
	List<Map<String, ?>> results = new ArrayList<Map<String, ?>>();
	ResultSetMetaData meta = rs.getMetaData();
	int count = rs.getMetaData().getColumnCount();
	List<String> colNames = new ArrayList<String>();

	for (int i = 1; i <= count; i++) {
	    colNames.add(meta.getColumnName(i).toLowerCase());
	}
	while (rs.next()) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    for (int i = 1; i <= count; i++) {
		int colType = meta.getColumnType(i);
		String colTypeName = meta.getColumnTypeName(i);
		
		if (colType == Types.DATE || colType == Types.TIMESTAMP) {
		    result.put(colNames.get(i - 1), rs.getTimestamp(i));
		} 
		else if("json".equals(colTypeName) || "jsonb".equals(colTypeName)) {
		    result.put(colNames.get(i - 1), rs.getString(i));
		}
		else if (colType == Types.OTHER) {
		    result.put(colNames.get(i - 1), handleCustomDbObject(rs.getObject(i)));
		} else {
		    result.put(colNames.get(i - 1), rs.getObject(i));
		}
	    }
	    results.add(result);
	}

	return results;
    }

    private static Object handleCustomDbObject(Object o) {

	try {
	    Object c = ParseUtils.parseCollection(ReflectionUtility.objectToString(o));
	    return c;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}

    }

    /**
     * Get the first row of the ResultSet as Map, converting the column names to upper case.
     * 
     * @param rs
     * @return the {columnLabel, value} mappings
     * @throws SQLException
     */
    public static Map<String, Object> getResultsAsMap(ResultSet rs) throws SQLException {
	Map<String, Object> result = new TreeMap<String, Object>();
	ResultSetMetaData meta = rs.getMetaData();
	int count = meta.getColumnCount();
	List<String> colNames = new ArrayList<String>();
	for (int i = 1; i <= count; i++) {
	    // colNames.add(meta.getColumnName(i).toLowerCase());
	    colNames.add(meta.getColumnLabel(i).toUpperCase());
	}
	if (rs.next()) {
	    for (int i = 1; i <= count; i++) {
		int colType = meta.getColumnType(i);
		if (colType == Types.DATE || colType == Types.TIMESTAMP) {
		    result.put(colNames.get(i - 1), rs.getTimestamp(i));
		} else {
		    result.put(colNames.get(i - 1), rs.getObject(i));
		}
	    }
	}

	return result;
    }

    public static String composeSqlWithMultiParams(String sql1, String sql2, int count) {
	StringBuffer out = new StringBuffer(512);
	out.append(sql1);
	for (int i = 0; i < count; i++) {
	    if (i < (count - 1)) {
		out.append("?,");
	    } else {
		out.append("?");
	    }
	}
	out.append(sql2);
	return out.toString();
    }

    /**
     * Get the first row from the result set, with column labels in lower case.
     * 
     * @param rset
     * @return the {columnLabel, value} mappings
     * @throws SQLException
     */
    public static Map<String, Object> getFirstResult(ResultSet rset) throws SQLException {
	List results = getResultsAsList(rset);
	return (results.size() > 0) ? ((Map<String, Object>) results.get(0)) : null;
    }

}
