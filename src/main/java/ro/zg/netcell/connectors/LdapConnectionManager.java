package ro.zg.netcell.connectors;

import ro.zg.scriptdao.constants.DataSourceConfigParameters;
import ro.zg.util.data.ConfigurationData;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;

public class LdapConnectionManager extends BaseConnectionManager<LDAPConnection>{
    private String serverName;
    private int port;
    private LDAPConnectionOptions options=new LDAPConnectionOptions();
    
    
    public void init() {
	ConfigurationData cfg = dataSourceDefinition.getConfigData();
	serverName = (String)cfg.getParameterValue(DataSourceConfigParameters.HOST);
	port = (Integer)cfg.getParameterValue(DataSourceConfigParameters.PORT);
    }
    
    
    public LDAPConnection getConnection() throws Exception {
	return new LDAPConnection(options, serverName, port);
    }

}
