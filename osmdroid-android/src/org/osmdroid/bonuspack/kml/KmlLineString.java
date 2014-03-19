package org.osmdroid.bonuspack.kml;

import java.io.IOException;
import java.io.Writer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * KML and/or GeoJSON LineString
 * @author M.Kergall
 */
public class KmlLineString extends KmlGeometry {
	
	public KmlLineString(){
		super();
	}
	
	public KmlLineString(JSONObject json){
		this();
		JSONArray coordinates = json.optJSONArray("coordinates");
		mCoordinates = KmlGeometry.parseGeoJSONPositions(coordinates);
	}
	
	/** Build the corresponding Polyline overlay */	
	@Override public Overlay buildOverlay(MapView map, Style defaultStyle, KmlPlacemark kmlPlacemark, 
			KmlDocument kmlDocument, boolean supportVisibility){
		Context context = map.getContext();
		Polyline lineStringOverlay = new Polyline(context);
		Style style = kmlDocument.getStyle(kmlPlacemark.mStyle);
		if (style != null){
			lineStringOverlay.setPaint(style.getOutlinePaint());
		} else if (defaultStyle!=null && defaultStyle.mLineStyle!=null){ 
			lineStringOverlay.setPaint(defaultStyle.getOutlinePaint());
		}
		lineStringOverlay.setPoints(mCoordinates);
		if (supportVisibility && !kmlPlacemark.mVisibility)
			lineStringOverlay.setEnabled(kmlPlacemark.mVisibility);
		return lineStringOverlay;
	}
	
	@Override public void saveAsKML(Writer writer){
		try {
			writer.write("<LineString>\n");
			writeKMLCoordinates(writer, mCoordinates);
			writer.write("</LineString>\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override public JSONObject asGeoJSON(){
		try {
			JSONObject json = new JSONObject();
			json.put("type", "LineString");
			json.put("coordinates", KmlGeometry.geoJSONCoordinates(mCoordinates));
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Cloneable implementation ------------------------------------
	
	@Override public KmlLineString clone(){
		KmlLineString kmlLineString = (KmlLineString)super.clone();
		return kmlLineString;
	}
	
	//Parcelable implementation ------------
	
	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}
	
	public static final Parcelable.Creator<KmlLineString> CREATOR = new Parcelable.Creator<KmlLineString>() {
		@Override public KmlLineString createFromParcel(Parcel source) {
			return new KmlLineString(source);
		}
		@Override public KmlLineString[] newArray(int size) {
			return new KmlLineString[size];
		}
	};
	
	public KmlLineString(Parcel in){
		super(in);
	}
}
