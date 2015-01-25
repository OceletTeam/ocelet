package fr.ocelet.datafacer.ocltypes;

import org.opengis.feature.simple.SimpleFeature;

import fr.ocelet.datafacer.GtDataRecord;

public class PostgisDataRec extends GtDataRecord {

	protected final String ERR_HEADER = "Datafacer Postgis: ";
	
	public PostgisDataRec(SimpleFeature sf) {
		super(sf);
	}

	@Override
	public String getErrHeader() {
		return this.ERR_HEADER;
	}
	

}
