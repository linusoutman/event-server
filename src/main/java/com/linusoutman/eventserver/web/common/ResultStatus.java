package com.linusoutman.eventserver.web.common;


public enum ResultStatus {

  Failed(10001, "request failed"),
  CREATE_EVENT_FAILED(10002, "create job failed"),
  GET_EVENT_LIST_FAILED(10003, "get event list failed");

  private int code;

  private String msg;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  ResultStatus(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }
}
