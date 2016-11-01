package com.yogapay.wx.service;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

interface ParameterWriter {

	public void write(Writer w, Map<String, Object> params) throws IOException;
}
