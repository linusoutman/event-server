package com.linusoutman.eventserver.web.common.utils;


import com.linusoutman.eventserver.web.common.ResultStatus;

import java.util.HashMap;
import java.util.Map;

public class ResponseInfo extends HashMap<String, Object> {
  private static final long serialVersionUID = 1L;

  public ResponseInfo() {
    put("code", 0);
    put("msg", "success");
  }

  public static ResponseInfo error() {
    return error(500, "unknown err");
  }

  public static ResponseInfo error(String msg) {
    return error(500, msg);
  }

  public static ResponseInfo error(int code, String msg) {
    ResponseInfo responseInfo = new ResponseInfo();
    responseInfo.put("code", code);
    responseInfo.put("msg", msg);
    return responseInfo;
  }

  public static ResponseInfo error(ResultStatus httpCode) {
    ResponseInfo responseInfo = new ResponseInfo();
    responseInfo.put("code", httpCode.getCode());
    responseInfo.put("msg", httpCode.getMsg());
    return responseInfo;
  }

  public static ResponseInfo ok(String msg) {
    ResponseInfo responseInfo = new ResponseInfo();
    responseInfo.put("msg", msg);
    return responseInfo;
  }

  public static ResponseInfo ok(Map<String, Object> map) {
    ResponseInfo responseInfo = new ResponseInfo();
    responseInfo.putAll(map);
    return responseInfo;
  }

  public static ResponseInfo ok() {
    return new ResponseInfo();
  }

  public ResponseInfo put(String key, Object value) {
    super.put(key, value);
    return this;
  }
}
