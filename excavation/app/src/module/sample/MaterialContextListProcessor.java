package module.sample;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import module.common.bean.ResponseData;
import module.common.bean.SimpleData;
import module.common.http.HttpOperation;
import module.common.http.HttpProcessor;
import module.common.http.HttpRequester;
import module.common.http.Request;
import module.common.http.Response;
import module.common.http.Response.RESPONSE_RESULT;
import module.common.http.Response.STANDARD;
import module.common.http.bean.HttpObject;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MaterialContextListProcessor extends HttpOperation implements
		HttpProcessor {
	String ip_address;
	public MaterialContextListProcessor(String ip_address) {
		// TODO Auto-generated constructor stub
		this.ip_address=ip_address;
	}
	@Override
	public HttpObject getHttp(Map<Request, String> mapParams) {

		HttpObject object = new HttpObject();
		object.setInfo(HttpRequester.GET_LISTING);
		object.setUrl(generateUrlWithParams(HttpRequester.GET_LISTING,mapParams,ip_address));
		return object;
	}

	public enum LIST_SAMPLE_RESPONSE implements Response {
		material,context_number;
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseData parseObject(HttpObject object) {
		return null;
	}

	public enum LIST_SAMPLE_REQUESTER implements Request {
		mode,listing_type;

		@Override
		public String getParameter() {
			return this.name();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleData> parseList(HttpObject object) {

		SortedMap<Integer, SimpleData> map = new TreeMap<Integer, SimpleData>();

		SimpleData data = new SimpleData();
		data.id="Select Context number";
		map.put(0,data);
		object = request(object);
		checkHttpStatus(object, data);
		if (data.result == RESPONSE_RESULT.failed) {
			return new LinkedList<SimpleData>(map.values());
		}
		try {
			JSONObject resObj = new JSONObject(object.getResponseString());
			JSONObject resData = resObj.getJSONObject(STANDARD.responseData.name());
			Iterator<String> resIter = resData.keys();
			while (resIter.hasNext()) {
				String key = resIter.next();
				JSONObject resItem = resData.getJSONObject(key);
				SimpleData dataObject = parseObject(resItem);
				map.put(Integer.parseInt(key), dataObject);
			}
			resIter = null;
			resData = null;
			resObj = null;

		} catch (Exception e) {
			e.printStackTrace();
			return new LinkedList<SimpleData>(map.values());
		} finally {
			data = null;
			object.release();
			object = null;
		}

		return new LinkedList<SimpleData>(map.values());
	}

	@SuppressWarnings("unchecked")
	@Override
	public SimpleData parseObject(JSONObject object) throws JSONException {

		SimpleData data = new SimpleData();
		if(object.has("material")){
			data.material=get("material", object);
			Log.d("LOG", "==> "+data.material);
		}
		
		return data;
	}

	

}
