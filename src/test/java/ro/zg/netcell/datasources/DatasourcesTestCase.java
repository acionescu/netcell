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
package ro.zg.netcell.datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

public class DatasourcesTestCase {
    
    @Test
    public void test() throws SQLException{
	String url = "jdbc:postgresql://localhost:5432/problems_db";
//	Properties props = new Properties();
//	props.setProperty("user","metaguvernare");
//	props.setProperty("password","meta-guvernare");
//	
//	Connection conn = DriverManager.getConnection(url, props);

	
	BasicDataSource ds = new BasicDataSource();
	
	
	
	ds.setUrl(url);
	ds.setUsername("metaguvernare");
	ds.setPassword("meta-guvernare");
//	ds.setDriverClassName("org.postgresql.Driver");
	
	Connection conn = ds.getConnection();
	
	Statement st = conn.createStatement();
	boolean rs = st.execute(" begin;\n" + 
		"delete from entities_links_users where entity_link_id=889;\n" + 
		"\n" + 
		"delete from entities_links where entity_link_id =889;\n" + 
		"\n" + 
		"commit;");
	System.out.println(rs);
	conn.close();
    }

}
