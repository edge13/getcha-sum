package com.singly.util;

import java.util.Map;

import org.apache.http.HttpException;

public interface HttpClientService {

  public byte[] get(String url, Map<String, String> requestParams)
    throws HttpException;

  public byte[] post(String url, Map<String, String> requestParams)
    throws HttpException;

  public byte[] postAsBody(String url, byte[] body, String mime, String charset)
    throws HttpException;

}
