package org.jeecg.modules.prom.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.annotations.JsonAdapter;
import org.jeecg.modules.prom.entity.ClientLog;
import org.jeecg.modules.prom.entity.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface PromService {
    public JSONObject getCPU(String instance);
    public JSONObject getMemory(String instance);
    public JSONObject getMemoryAvailable(String instance);
    public JSONObject getFile(String instance);
    public JSONObject getDiskRead(String instance);
    public JSONObject getDiskWrite(String instance);

    public JSONObject getProcess(String container);
    public JSONObject getThread(String container);
    public JSONObject getFd(String container);
    public JSONObject getctnCpu(String container);
    public JSONObject getctnMem(String container);

    public JSONObject getPod(String str);
    public JSONObject getPodLabel(String str);
    public JSONObject Start(String str);
    public JSONObject Pause(String str);
    public JSONObject Delete(String str);

    public JSONObject getServiceList();

    public List<Log> getLogList();
    public List<ClientLog> getClientLogList();

    public Log createServiceLogInfo(String appId, String serviceId, String serviceName, String content);
    public Log createServiceLogWarning(String appId, String serviceId, String serviceName, String content);
    public Log createServiceLogError(String appId, String serviceId, String serviceName, String content);
    public Log createServiceLogCritical(String appId, String serviceId, String serviceName, String content);

    public ClientLog createClientLogInfo(String clientId, String content);
    public ClientLog createClientLogWarning(String clientId, String content);
    public ClientLog createClientLogError(String clientId, String content);
    public ClientLog createClientLogCritical(String clientId, String content);

    public Log deleteServiceLog(Integer id);
    public ClientLog deleteClientLog(Integer id);
}
