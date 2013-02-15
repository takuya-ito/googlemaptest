package com.example.googlemaptest;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class LocationListLoader extends android.content.AsyncTaskLoader<String[]> {
	String lat;
	String lon;
	String id;
	MainActivity ma;
	public LocationListLoader(MainActivity context,String id, String lat, String lon) {
		super(context);
		ma = context;
		this.id=id;
		this.lat=lat;
		this.lon=lon;
	}
	@Override
	public String[] loadInBackground() {
		getLocation(id,lat,lon);
		return null;
	}
	//androidとサーバとの通信
	public void getLocation(String id,String lat, String lon) {
		String APIKey = "2wEp0Gqxg65ij00Wb_spy3WBkhzxAJbvuS9dt.esakGKCWUQriw2v10RgDlcGxqCIw--";
		Element element = null;
		ArrayList<String> NounArray = new ArrayList<String>();
		if (lat != null && lon != null && id != null) {
			Element root = null;
			try {
//				String query = URLEncoder.encode("http://kawagon.p.united.jp/OwnJointLocation.php?id="+id+"&lat="+lat+"&lon="+lon, "UTF-8");
//				query = new String(query.getBytes("MS932"), "UTF-8");
				Log.d("getLocation","id="+id+"&lat="+lat+"&lon="+lon);
				URL url = new URL("http://kawagon.p.united.jp/OwnJointLocation.php?id="+id+"&lat="+lat+"&lon="+lon);
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(url.openConnection().getInputStream());
				root = doc.getDocumentElement();
				if (root != null) {
					//件数取得
					NodeList list = root.getElementsByTagName("hitcount");
					for (int i = 0; i < list.getLength(); i++) {
						element = (Element) list.item(i);
						Log.d("getLocation","hitcount="+element.getFirstChild()
									.getNodeValue());
						// Log.v("logs","mopheme:"+element.getElementsByTagName("Keyphrase").item(0).getFirstChild().getNodeValue());
						
					}
					//件数分の位置取得
					ArrayList<PointData> pl = new ArrayList<PointData>();
					list = root.getElementsByTagName("User");
					for (int i = 0; i < list.getLength(); i++) {
						PointData pd = new PointData();
						element = (Element) list.item(i);
						if (element != null) {
							//id格納
							NodeList nounList = element
									.getElementsByTagName("id");
							Element nounElement = (Element) nounList.item(0);
							Log.d("getLocation","id="+nounElement.getFirstChild()
									.getNodeValue());
							pd.setId(Integer.valueOf(nounElement.getFirstChild().getNodeValue()));
							//lat格納
							nounList = element
									.getElementsByTagName("latitude");
							nounElement = (Element) nounList.item(0);
							Log.d("getLocation","latitude="+nounElement.getFirstChild()
									.getNodeValue());
							pd.setLat(Double.valueOf(nounElement.getFirstChild().getNodeValue()));
							//lon格納
							nounList = element
									.getElementsByTagName("longitude");
							nounElement = (Element) nounList.item(0);
							Log.d("getLocation","longitude="+nounElement.getFirstChild()
									.getNodeValue());
							pd.setLon(Double.valueOf(nounElement.getFirstChild().getNodeValue()));
							
							pl.add(pd);
						}
					}
					AddPins(pl);
					pl.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("logs", "root:" +root.toString());
				Log.d("logs", "getNoun err");
			}
		}
	}
	
	public void AddPins(ArrayList<PointData> pl){
		Log.d("log","pl.size:"+pl.size());
		for(int i=0;i<pl.size();i++){
			GeoPoint geopoint = new GeoPoint( (int)(pl.get(i).getLat()*1000000), (int)(pl.get(i).getLon()*1000000));
	        ma.addPin(geopoint);
		}
	}
	
	public boolean checkNoun(Element element) { // 形態素が名詞かどうかの判定．（名詞ならtrue,それ以外はfalse）
		NodeList posList = element.getElementsByTagName("pos");
		Element posElement = (Element) posList.item(0);
		if (posElement.getFirstChild().getNodeValue().equals("名詞")) {
			return true;
		} else {
			return false;
		}
	}

	public String halfCharaToSpace(String src) {
		char c;
		boolean isDelete = false;
		for (int i = 0; i < src.length(); i++) {
			c = src.charAt(i);
			if (isHankaku(c)) {

			} else {
				isDelete = false;
			}
		}

		return src;
	}

	public static boolean isHankaku(char c) {
		boolean res = false;
		if (('\u0020' <= c && c <= '\u007e') || // 半角英数記号
				('\uff61' <= c && c <= '\uff9f')) { // 半角カタカナ
			res = true;
		}
		return res;
	}
	
}
