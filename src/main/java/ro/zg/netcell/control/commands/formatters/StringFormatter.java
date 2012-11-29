package ro.zg.netcell.control.commands.formatters;

public class StringFormatter extends BaseObjectFormatter<String>{

    public String format(String obj) throws Exception {
	return getStringDelimiter()+obj+getStringDelimiter();
    }

}
