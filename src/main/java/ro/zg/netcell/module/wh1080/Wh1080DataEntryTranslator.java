package ro.zg.netcell.module.wh1080;

import java.util.Map;

import javax.measure.Measure;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import ro.zg.scriptdao.core.ResponseTranslator;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.weather.util.ObservedWeatherMeasure;
import ro.zg.weather.util.WeatherObservation;

public class Wh1080DataEntryTranslator implements ResponseTranslator<WeatherObservation, GenericNameValueContext> {

    @Override
    public GenericNameValueContext translate(WeatherObservation input) {
	GenericNameValueContext out = new GenericNameValueContext();

	GenericNameValueContext values = new GenericNameValueContext();
	GenericNameValueContext units = new GenericNameValueContext();

	out.put("values", values);
	out.put("units", units);

	for (Map.Entry<String, ObservedWeatherMeasure<?, ? extends Quantity>> e : input.getMeasuresAsTable().entrySet()) {
	    Measure<?, ? extends Quantity> measure = e.getValue().getMeasure();

	    // GenericNameValueContext record = new GenericNameValueContext();
	    //
	    // record.put("value", measure.getValue());
	    // record.put("unit", measure.getUnit());
	    //
	    // out.put(e.getKey(), record);

	    values.put(e.getKey(), measure.getValue());
	    units.put(e.getKey(), measure.getUnit());

	}

	// GenericNameValueContext timestampRec = new GenericNameValueContext();
	// timestampRec.put("value", input.getTimestamp().getTime());
	// timestampRec.put("unit", SI.MILLI(SI.SECOND));
	//
	// out.put("TIMESTAMP", timestampRec);
	//
	// GenericNameValueContext durationRec = new GenericNameValueContext();
	//
	Measure<Long, Duration> duration = input.getDuration();
	// durationRec.put("value", duration.getValue());
	// durationRec.put("unit", duration.getUnit());
	// out.put("DURATION", durationRec);

	values.put("TIMESTAMP", input.getTimestamp().getTime());
	units.put("TIMESTAMP", SI.MILLI(SI.SECOND));

	values.put("DURATION", duration.getValue());
	units.put("DURATION", duration.getUnit());

	return out;
    }

    @Override
    public GenericNameValueContext translate(WeatherObservation[] inputs) {
	// TODO Auto-generated method stub
	return null;
    }

}
