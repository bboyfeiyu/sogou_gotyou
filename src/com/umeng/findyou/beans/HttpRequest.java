
package com.umeng.findyou.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: HttpRequest.java
 * @Package com.umeng.gotme.net
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class HttpRequest {

    /**
     * HTTP请求参数
     */
    private Map<String, String> mReqParams = new HashMap<String, String>();
    /**
     * 
     */
    private RequestType mReqType = RequestType.GET;

    /**
     * 数据返回的格式
     */
    private ResponseFormat mRespFormat = ResponseFormat.JSON;

    /**
     * @ClassName: RequestType
     * @Description:
     * @author Honghui He
     */
    public enum RequestType {
        POST,
        GET
    }

    /**
     * @ClassName: ResponseFormat
     * @Description: 数据返回格式，分为json, xml
     * @author Honghui He
     */
    public enum ResponseFormat {
        JSON,
        XML
    }

    /**
     * @Title: HttpRequest
     * @Description:  
     * 		HttpRequest Constructor
     *
     */
    public HttpRequest() {
        this(null);
    }

    /**
     * @Title: HttpRequest
     * @Description:  
     * 		HttpRequest Constructor
     * 
     * @param params
     */
    public HttpRequest(Map<String, String> params) {
        this(params, RequestType.GET);
    }
    
    /**
     * @Title: HttpRequest
     * @Description:  
     *      HttpRequest Constructor
     * 
     * @param params
     */
    public HttpRequest(Map<String, String> params, RequestType type) {
        this(params, type, ResponseFormat.JSON);
    }
    
    
    /**
     * @Title: HttpRequest
     * @Description:  
     *      HttpRequest Constructor
     * 
     * @param params
     */
    public HttpRequest(Map<String, String> params, RequestType type, ResponseFormat format) {
        mReqParams = params ;
        mReqType = type;
        mRespFormat = format ;
    }
    
    
    /**
     * @Title: doRequest
     * @Description: 发送请求
     *
     *       
     * @throws
     */
    public void doRequest() {
        
    }

    /**获取 mReqParams
     * @return 返回 mReqParams
     */
    public Map<String, String> getReqParams() {
        return mReqParams;
    }

    /**设置 mReqParams
     * @param 对mReqParams进行赋值
     */
    public void setReqParams(Map<String, String> params) {
        this.mReqParams = params;
    }

    /**获取 mReqType
     * @return 返回 mReqType
     */
    public RequestType getReqType() {
        return mReqType;
    }

    /**设置 mReqType
     * @param 对mReqType进行赋值
     */
    public void setReqType(RequestType reqType) {
        this.mReqType = reqType;
    }

    /**获取 mDataFormat
     * @return 返回 mDataFormat
     */
    public ResponseFormat getRespFormat() {
        return mRespFormat;
    }

    /**设置 mDataFormat
     * @param 对mDataFormat进行赋值
     */
    public void setRespFormat(ResponseFormat format) {
        this.mRespFormat = format;
    }
    
}
