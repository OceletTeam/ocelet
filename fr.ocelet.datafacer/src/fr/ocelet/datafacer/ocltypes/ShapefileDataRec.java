package fr.ocelet.datafacer.ocltypes;

import org.opengis.feature.simple.SimpleFeature;

import fr.ocelet.datafacer.GtDataRecord;

public class ShapefileDataRec extends GtDataRecord {

	protected final String ERR_HEADER = "Datafacer ShapeFile: ";
	
	public ShapefileDataRec(SimpleFeature sf) {
		super(sf);
	}
	
	@Override
	public String getErrHeader() {
		return this.ERR_HEADER;
	}
	
}
