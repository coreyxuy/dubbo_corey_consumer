package com.debug.mooc.dubbo.two.server.service;

import com.debug.mooc.dubbo.two.server.data.DubboRecordResponse;
import com.debug.mooc.dubbo.two.server.request.RecordPushRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import okhttp3.*;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: corey
 * @Date: 2019/4/1 14:21
 **/
@Service
public class OrderRecordService {

    private static final Logger log = LoggerFactory.getLogger(OrderRecordService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpService httpService;


    private static final String url = "http://127.0.0.1:9013/record/push";

    private OkHttpClient httpClient = new OkHttpClient();

    /**
     * 处理controller层过来的用户下单数据
     *
     * @param pushRequest
     */
    public void pushOrder(RecordPushRequest pushRequest) throws Exception {
        try {
            //构造 builder
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .header("Content-Type", "application/json");
            //构造请求体
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                    objectMapper.writeValueAsString(pushRequest));
            //构造请求
            Request request = builder.post(requestBody).build();
            //发起请求
            Response response = httpClient.newCall(request).execute();
            log.info(response.body().toString());
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 处理controller层过来的用户下单数据-采用通用化http服务类实战
     *
     * @param pushRequest
     */
    public void pushOrderV2(RecordPushRequest pushRequest) throws Exception {
        try {
            Map<String, String> headMap = new HashMap<>();
            headMap.put("Content-Type", "application/json");
            String req = httpService.post(url, headMap, "application/json",
                    objectMapper.writeValueAsString(pushRequest));
            log.info("响应结果:{}", req);


            //解析返回数据 1 map解析(反序列化) 针对响应数据较少的场景
            Map<String,Object> resMap=objectMapper.readValue(req,Map.class);
            log.info("得到响应解析结果:{}",resMap);
            Integer code= (Integer) resMap.get("code");
            String msg= (String) resMap.get("msg");
            Integer data= (Integer) resMap.get("data");
            log.info("获取到数据:code={} msg={} data={} ",code,msg,data);


            //对象解析 响应参数 通用化的 数据量比较复杂
            DubboRecordResponse dubboRecordResponse = objectMapper.readValue(req, DubboRecordResponse.class);
            log.info("得到响应解析结果:{}",dubboRecordResponse);


        } catch (Exception e) {
            throw e;
        }
    }
}



















