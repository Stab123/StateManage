package org.jeecg.modules.prom.controller;

        import com.alibaba.fastjson.JSONArray;
        import com.alibaba.fastjson.JSONObject;
        import org.jeecg.modules.prom.entity.ClientLog;
        import org.jeecg.modules.prom.entity.Log;
        import org.jeecg.modules.prom.service.PromService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.*;

        import java.sql.ResultSet;
        import java.sql.Statement;
        import java.util.List;
        import java.util.Map;

@RestController
@RequestMapping("/prom")
public class PromController {
    @Autowired
    private PromService promService;

    @RequestMapping(value = "/cpu", method = RequestMethod.GET)
    public JSONObject getCpu(@RequestParam(name="instance") String instance) {
        return promService.getCPU(instance);
    }

    @RequestMapping(value = "/memory", method = RequestMethod.GET)
    public JSONObject getMemory(@RequestParam(name="instance") String instance) {
        return promService.getMemory(instance);
    }

    @RequestMapping(value = "/memAvailable", method = RequestMethod.GET)
    public JSONObject getMemoryAvailable(@RequestParam(name="instance") String instance) {
        return promService.getMemoryAvailable(instance);
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public JSONObject getFile(@RequestParam(name="instance") String instance) {
        return promService.getFile(instance);
    }

    @RequestMapping(value = "/diskRead", method = RequestMethod.GET)
    public JSONObject getDiskRead(@RequestParam(name="instance") String instance) {
        return promService.getDiskRead(instance);
    }

    @RequestMapping(value = "/diskWrite", method = RequestMethod.GET)
    public JSONObject getDiskWrite(@RequestParam(name="instance") String instance) {
        return promService.getDiskWrite(instance);
    }

    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public JSONObject getProcess(@RequestParam(name="container") String container) {
        return promService.getProcess(container);
    }

    @RequestMapping(value = "/thread", method = RequestMethod.GET)
    public JSONObject getThread(@RequestParam(name="container") String container) {
        return promService.getThread(container);
    }

    @RequestMapping(value = "/fd", method = RequestMethod.GET)
    public JSONObject getFd(@RequestParam(name="container") String container) {
        return promService.getFd(container);
    }

    @RequestMapping(value = "/ctnCpu", method = RequestMethod.GET)
    public JSONObject getctnCpu(@RequestParam(name="container") String container) {
        return promService.getctnCpu(container);
    }

    @RequestMapping(value = "/ctnMem", method = RequestMethod.GET)
    public JSONObject getctnMem(@RequestParam(name="container") String container) {
        return promService.getctnMem(container);
    }

    @RequestMapping(value = "/pod", method = RequestMethod.GET)
    public JSONObject getPod(@RequestParam(name="url") String url) {
        return promService.getPod(url);
    }

    @RequestMapping(value = "/podLabel", method = RequestMethod.GET)
    public JSONObject getPodLabel(@RequestParam(name="url") String url) {
        return promService.getPodLabel(url);
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public JSONObject Start(@RequestParam(name="url") String url) {
        return promService.Start(url);
    }

    @RequestMapping(value = "/pause", method = RequestMethod.GET)
    public JSONObject Pause(@RequestParam(name="url") String url) {
        return promService.Pause(url);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public JSONObject Delete(@RequestParam(name="url") String url) {
        return promService.Delete(url);
    }

    @RequestMapping(value = "/serviceList", method = RequestMethod.GET)
    public JSONObject getServiceList() {
        return promService.getServiceList();
    }

    @RequestMapping(value = "/logList", method = RequestMethod.GET)
    public List<Log> getLogList() {
        return promService.getLogList();
    }

    @RequestMapping(value = "/clientLogList", method = RequestMethod.GET)
    public List<ClientLog> getClientLogList() {
        return promService.getClientLogList();
    }

    @PostMapping("/addServiceLogInfo")
    public Log createServiceLogInfo(@RequestParam("app_id") String appId, @RequestParam("service_id") String serviceId, @RequestParam("service_name") String serviceName, @RequestParam("content") String content){
        return promService.createServiceLogInfo(appId, serviceId, serviceName, content);
    }

    @PostMapping("/addServiceLogWarning")
    public Log createServiceLogWarning(@RequestParam("app_id") String appId, @RequestParam("service_id") String serviceId, @RequestParam("service_name") String serviceName, @RequestParam("content") String content){
        return promService.createServiceLogWarning(appId, serviceId, serviceName, content);
    }

    @PostMapping("/addServiceLogError")
    public Log createServiceLogError(@RequestParam("app_id") String appId, @RequestParam("service_id") String serviceId, @RequestParam("service_name") String serviceName, @RequestParam("content") String content){
        return promService.createServiceLogError(appId, serviceId, serviceName, content);
    }

    @PostMapping("/addServiceLogCritical")
    public Log createServiceLogCritical(@RequestParam("app_id") String appId, @RequestParam("service_id") String serviceId, @RequestParam("service_name") String serviceName, @RequestParam("content") String content){
        return promService.createServiceLogCritical(appId, serviceId, serviceName, content);
    }

    @PostMapping("/addClientLogInfo")
    public ClientLog createClientLogInfo(@RequestParam("client_id") String clientId,  @RequestParam("content") String content){
        return promService.createClientLogInfo(clientId, content);
    }

    @PostMapping("/addClientLogWarning")
    public ClientLog createClientLogWarning(@RequestParam("client_id") String clientId, @RequestParam("content") String content){
        return promService.createClientLogWarning(clientId, content);
    }

    @PostMapping("/addClientLogError")
    public ClientLog createClientLogError(@RequestParam("client_id") String clientId, @RequestParam("content") String content){
        return promService.createClientLogError(clientId, content);
    }

    @PostMapping("/addClientLogCritical")
    public ClientLog createClientLogCritical(@RequestParam("client_id") String clientId, @RequestParam("content") String content){
        return promService.createClientLogCritical(clientId, content);
    }

    @DeleteMapping("/deleteServiceLog")
    public Log deleteServiceLog(@RequestParam("id") Integer id){
        return promService.deleteServiceLog(id);
    }

    @DeleteMapping("/deleteClientLog")
    public ClientLog deleteClientLog(@RequestParam("id") Integer id){
        return promService.deleteClientLog(id);
    }
}
