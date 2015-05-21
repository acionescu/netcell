package ro.zg.netcell.weather;

import org.junit.Test;

import ro.zg.netcell.module.wh1080.Wh1080DataEntryTranslator;
import ro.zg.weather.util.ObservedWeatherMeasure;
import ro.zg.weather.util.WeatherObservation;
import ro.zg.weather.wh1080.WH1080Driver;

public class WH1080Test{
    
    @Test
    public void getData() throws Exception {
	
	WH1080Driver driver = new WH1080Driver();
	
	driver.start();
	
	WeatherObservation data = driver.readNewDataIfAvailable();
	
//	FixedMemoryBlock data = driver.readFixedMemoryBlock();
	System.out.println(data);
	
//	for(ObservedWeatherMeasure<?, ?> m : data.getMeasures()) {
//	    System.out.println(m.getKey() +" : "+ m.getMeasure() );
//	}
	
	
//	System.out.println("model : "+Integer.toHexString(data.getModel()));
	
//	System.out.println(driver.readNewDataIfAvailable());
	
	Wh1080DataEntryTranslator tr = new Wh1080DataEntryTranslator();
	
	System.out.println(tr.translate(data));
	
	driver.stop();
    }

}
