package com.yogapay.wx.service;

import com.yogapay.wx.web.URLResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

class Utils {

	private static final ParameterWriter defaultParameterWriter = new ParameterWriter() {

		@Override
		public void write(Writer w, Map<String, Object> params) throws IOException {
			boolean first = true;
			for (Map.Entry<String, Object> t : params.entrySet()) {
				if (!first) {
					w.append('&');
				}
				w.append(t.getKey()).append('=').append(URLEncoder.encode(t.getValue().toString(), "UTF-8"));
				first = false;
			}
			w.flush();
		}
	};

	public static void post(String url, Map<String, Object> params, URLResponse response) throws IOException {
		post(url, params, response, defaultParameterWriter);
	}

	public static void post(String url, Map<String, Object> params, URLResponse response, ParameterWriter writer) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.connect();
		PrintWriter w = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
		try {
			writer.write(w, params);
		} finally {
			IOUtils.closeQuietly(w);
		}
		int code = conn.getResponseCode();
		response.setResponse_code(code);
		Object[][] arrays = new Object[][]{
			{conn.getInputStream(), null},
			{conn.getErrorStream(), null}};
		for (Object[] t : arrays) {
			InputStream in = (InputStream) t[0];
			if (in != null) {
				try {
					t[1] = IOUtils.toString(in, "UTF-8");
				} catch (IOException ex) {
					t[1] = ExceptionUtils.getStackTrace(ex);
				} finally {
					IOUtils.closeQuietly(in);
				}
			}
		}
		response.setResponse((String) arrays[0][1]);
		response.setError((String) arrays[1][1]);
	}

	public static void parseXML(Object obj) {
	}
}
