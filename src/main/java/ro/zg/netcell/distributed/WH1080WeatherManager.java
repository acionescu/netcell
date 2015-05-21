package ro.zg.netcell.distributed;

import ro.zg.netcell.module.wh1080.Wh1080DataEntryTranslator;
import ro.zg.scriptdao.core.ResponseTranslator;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;
import ro.zg.weather.util.WeatherObservation;
import ro.zg.weather.wh1080.WH1080Driver;

public class WH1080WeatherManager {
    private static Logger logger = MasterLogManager.getLogger(WH1080WeatherManager.class.getName());
    private WH1080Driver wh1080Driver;

    private ResponseTranslator<WeatherObservation, GenericNameValueContext> translator;

    public void init() {
	try {
	    wh1080Driver.start();

	    translator = new Wh1080DataEntryTranslator();
	} catch (Exception e) {
	    logger.error("WH1080 MANAGER INIT FAILED", e);
	}
    }

    public GenericNameValueContext readLastData() throws Exception {

	WeatherObservation obs = wh1080Driver.readLastDataEntry();
	if (obs != null) {
	    GenericNameValueContext result = translator.translate(obs);
	    
	    return result;
	}
	return null;

    }

    /**
     * @return the wh1080Driver
     */
    public WH1080Driver getWh1080Driver() {
	return wh1080Driver;
    }

    /**
     * @param wh1080Driver
     *            the wh1080Driver to set
     */
    public void setWh1080Driver(WH1080Driver wh1080Driver) {
	this.wh1080Driver = wh1080Driver;
    }

}
