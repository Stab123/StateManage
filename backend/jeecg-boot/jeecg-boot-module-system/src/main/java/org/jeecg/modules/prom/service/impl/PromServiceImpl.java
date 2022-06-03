package org.jeecg.modules.prom.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.encryption.EncryptedString;
import org.jeecg.modules.prom.entity.ClientLog;
import org.jeecg.modules.prom.entity.Log;
import org.jeecg.modules.prom.repository.ClientLogRepository;
import org.jeecg.modules.prom.repository.LogRepository;
import org.jeecg.modules.prom.service.PromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromServiceImpl implements PromService {
    @Autowired
    LogRepository logRepository;

    @Autowired
    ClientLogRepository clientLogRepository;

    RestTemplate restTemplate = new RestTemplate();

    public JSONObject getCPU(String instance) {
        String s = "{mode=\"idle\",instance=\"" + instance + "\"}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=1-avg(irate(node_cpu_seconds_total{s}[5m])) by (instance)", JSONObject.class, s);
        return response;
    }

    public JSONObject getMemory(String instance) {
        Map<String,String> map = new HashMap<String,String>();
        String s = "{instance=\"" + instance + "\"}";
        String ss = "{instance=\"" + instance + "\"}";
        map.put("s", s);
        map.put("ss", ss);
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=1 - (node_memory_MemAvailable_bytes{s} / (node_memory_MemTotal_bytes{ss}))", JSONObject.class, map);
        return response;
    }

    public JSONObject getMemoryAvailable(String instance) {
        String s = "{instance=\"" + instance + "\"}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=node_memory_MemAvailable_bytes{s}", JSONObject.class, s);
        return response;
    }

    public JSONObject getFile(String instance) {
        Map<String,String> map = new HashMap<String,String>();
        String s ="{fstype=~\"ext4|xfs\", instance =\"" + instance + "\"}";
        String ss = "{fstype=~\"ext4|xfs\", instance =\"" + instance + "\"}";
        map.put("s", s);
        map.put("ss", ss);
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=(1 - node_filesystem_avail_bytes{s} / node_filesystem_size_bytes{ss})", JSONObject.class, map);
        return response;
    }

    public JSONObject getDiskRead(String instance) {
        String s = "{instance=\"" + instance + "\"}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=sum by (instance) (rate(node_disk_reads_completed_total{s}[5m]))", JSONObject.class, s);
        return response;
    }

    public JSONObject getDiskWrite(String instance) {
        String s = "{instance=\"" + instance + "\"}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=sum by (instance) (rate(node_disk_writes_completed_total{s}[5m]))", JSONObject.class, s);
        return response;
    }

    public JSONObject getProcess(String container) {
        String s = "{container=\"" + container + "\"}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=container_processes{s}", JSONObject.class, s);
        return response;
    }

    public JSONObject getThread(String container) {
        String s = "{container=\"" + container + "\"}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=container_threads{s}", JSONObject.class, s);
        return response;
    }

    public JSONObject getFd(String container) {
        String s = "{container=\"" + container + "\"}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=container_file_descriptors{s}", JSONObject.class, s);
        return response;
    }

    public JSONObject getctnCpu(String container) {
        String s = "{container=\"" + container + "\"}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=container_cpu_usage_seconds_total{s}", JSONObject.class, s);
        return response;
    }

    public JSONObject getctnMem(String container) {
        String s = "{container=\"" + container + "\"}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=container_memory_usage_bytes{s}", JSONObject.class, s);
        return response;
    }

    public JSONObject getPod(String str) {
        String s = "{namespace=~'" + str + ".*'}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=kube_pod_status_phase{s} == 1", JSONObject.class, s);
        return response;
    }

    public JSONObject getPodLabel(String str) {
        String s = "{pod=" + str + "}";
        JSONObject response = restTemplate.getForObject("http://101.34.255.125:30000/api/v1/query?query=kube_pod_labels{s}[1y]", JSONObject.class, s);
        return response;
    }

    public JSONObject Start(String str) {
        String url = "http://101.34.255.125:15100/service_controller/start?deployment_id=deployment:" + str;
        System.out.println(url);
        JSONObject response = restTemplate.getForObject(url, JSONObject.class);
        return response;
    }

    public JSONObject Pause(String str) {
        String url = "http://101.34.255.125:15100/service_controller/pause?deployment_id=deployment:" + str;
        System.out.println(url);
        JSONObject response = restTemplate.getForObject(url, JSONObject.class);
        return response;
    }

    public JSONObject Delete(String str) {
        String url = "http://101.34.255.125:15100/service_controller/delete?deployment_id=deployment:" + str;
        System.out.println(url);
        JSONObject response = restTemplate.getForObject(url, JSONObject.class);
        return response;
    }

    public JSONObject getServiceList() {
        String url = "http://101.34.255.125:15100/service_controller/get_list";
        JSONObject response = restTemplate.getForObject(url, JSONObject.class);
        return response;
    }

    public List<Log> getLogList() {
        return logRepository.findAll();
    }

    public List<ClientLog> getClientLogList() {
        return clientLogRepository.findAll();
    }

    public Log createServiceLogInfo(String appId, String serviceId, String serviceName, String content){
        Log newLog = new Log();
        Date currentTime=new Date();

        newLog.setAppId(appId);
        newLog.setServiceId(serviceId);
        newLog.setServiceName(serviceName);
        newLog.setContent(content);
        newLog.setCreateTime(currentTime);
        newLog.setLevel("Info");

        logRepository.save(newLog);
        return newLog;
    }

    public Log createServiceLogWarning(String appId, String serviceId, String serviceName, String content){
        Log newLog = new Log();
        Date currentTime=new Date();

        newLog.setAppId(appId);
        newLog.setServiceId(serviceId);
        newLog.setServiceName(serviceName);
        newLog.setContent(content);
        newLog.setCreateTime(currentTime);
        newLog.setLevel("Warning");

        logRepository.save(newLog);
        return newLog;
    }

    public Log createServiceLogError(String appId, String serviceId, String serviceName, String content){
        Log newLog = new Log();
        Date currentTime=new Date();

        newLog.setAppId(appId);
        newLog.setServiceId(serviceId);
        newLog.setServiceName(serviceName);
        newLog.setContent(content);
        newLog.setCreateTime(currentTime);
        newLog.setLevel("Error");

        logRepository.save(newLog);
        return newLog;
    }

    public Log createServiceLogCritical(String appId, String serviceId, String serviceName, String content){
        Log newLog = new Log();
        Date currentTime=new Date();

        newLog.setAppId(appId);
        newLog.setServiceId(serviceId);
        newLog.setServiceName(serviceName);
        newLog.setContent(content);
        newLog.setCreateTime(currentTime);
        newLog.setLevel("Critical");

        logRepository.save(newLog);
        return newLog;
    }

    public ClientLog createClientLogInfo(String clientId, String content){
        ClientLog newLog = new ClientLog();
        Date currentTime=new Date();

        newLog.setClientId(clientId);
        newLog.setContent(content);
        newLog.setCreateTime(currentTime);
        newLog.setLevel("Info");

        clientLogRepository.save(newLog);
        return newLog;
    }

    public ClientLog createClientLogWarning(String clientId, String content){
        ClientLog newLog = new ClientLog();
        Date currentTime=new Date();

        newLog.setClientId(clientId);
        newLog.setContent(content);
        newLog.setCreateTime(currentTime);
        newLog.setLevel("Warning");

        clientLogRepository.save(newLog);
        return newLog;
    }

    public ClientLog createClientLogError(String clientId, String content){
        ClientLog newLog = new ClientLog();
        Date currentTime=new Date();

        newLog.setClientId(clientId);
        newLog.setContent(content);
        newLog.setCreateTime(currentTime);
        newLog.setLevel("Error");

        clientLogRepository.save(newLog);
        return newLog;
    }

    public ClientLog createClientLogCritical(String clientId, String content){
        ClientLog newLog = new ClientLog();
        Date currentTime=new Date();

        newLog.setClientId(clientId);
        newLog.setContent(content);
        newLog.setCreateTime(currentTime);
        newLog.setLevel("Critical");

        clientLogRepository.save(newLog);
        return newLog;
    }

    @Override
    public Log deleteServiceLog(Integer id){
        logRepository.deleteById(id);
        return null;
    }

    @Override
    public ClientLog deleteClientLog(Integer id){
        clientLogRepository.deleteById(id);
        return null;
    }
}
