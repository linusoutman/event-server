package com.linusoutman.eventserver.client;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Sets;
import com.linusoutman.eventserver.common.Constant;
import com.linusoutman.eventserver.message.EventData;
import com.linusoutman.eventserver.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EthereumEventClient {

    @Autowired
    private EventSubscriber eventSubscriber;

    private static KafkaProducerService kafkaProducerService;

    @Autowired
    public EthereumEventClient(KafkaProducerService kafkaProducerService) {
        EthereumEventClient.kafkaProducerService = kafkaProducerService;
    }

    private static ConcurrentHashMap<String, Set<String>> listeningAddrs = new ConcurrentHashMap<>();
    private static HashMap<String, Long> consumeIndexMap = new HashMap<String, Long>();

    public void init() {
        try {
            eventSubscriber.setup();
        } catch (Exception ex) {
            log.error("Exception in init: ", ex);
        }
    }


    public static void registerJob(String address, String[] topics) {
        Set<String> set = listeningAddrs.get(address);
        if (set == null) {
            set = Sets.newHashSet();
        }
        set.addAll(new HashSet<String>(Arrays.asList(topics)));
        if (listeningAddrs.get(address) == null) { // each address with only one listen task
            listeningAddrs.put(address, set);
            listenTask(address, topics); //通过jobId获取过滤事件
        } else {
            listeningAddrs.put(address, set); // only add recent
        }
    }

    private static void listenTask(String addr, String[] topics) {
        ScheduledExecutorService listenExecutor = Executors.newSingleThreadScheduledExecutor();
        listenExecutor.scheduleWithFixedDelay(
                () -> {
                    try {
                        listen(addr, topics);
                    } catch (Throwable t) {
                        log.error("Exception in listener ", t);
                    }
                },
                0,
                3000,
                TimeUnit.MILLISECONDS);
    }

    private static void listen(String addr, String[] topics) {
        List<EventData> events = new ArrayList<>();
        List<EventData> data = getEventData(addr, topics);
        if (data != null && data.size() > 0) {
            events.addAll(data);
        }
        if (events == null || events.size() == 0) {
            return;
        }
        // handle events
        for (EventData eventData : events) {
            updateConsumeMap(addr, eventData.getBlockNumber());
            // push kafka
            kafkaProducerService.sendMessageAsync(kafkaProducerService.getTopic(), JSONObject.toJSONString(eventData));
        }

    }

    private static List<EventData> getEventData(String addr, String[] topics) {
        List<EventData> data = new ArrayList<>();
        try{
            Admin web3jAdmin = Admin.build(new HttpService(Constant.JSON_RPC_URL));
            Long fromBlock = null == consumeIndexMap.get(addr) ? Constant.initBlock.longValue() : consumeIndexMap.get(addr);
            EthFilter ethFilter = new EthFilter(
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(fromBlock)),
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(fromBlock + 10)),//传值不能超出最新的已确认区块号
                    addr).addOptionalTopics(topics);
            EthLog ethLog = web3jAdmin.ethGetLogs(ethFilter).send();
            if (ethLog.hasError()) {
                log.error("Get logs of {} ERR: {}", addr, ethLog.getError().getMessage());
            }else{
                List<EthLog.LogResult> logs = ethLog.getLogs();
                for(EthLog.LogResult logResult : logs) {
                    EventData eventData = JSONObject.parseObject(JSONObject.toJSONString(logResult), EventData.class);
                    data.add(eventData);
                    log.info("Get logs of {}, start:{},end:{} success : {}", addr, fromBlock, fromBlock + 10, eventData.toString());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            //log.error(org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e));
        }
        return data;
    }

    private static void updateConsumeMap(String addr, long block) {
        if (consumeIndexMap.containsKey(addr)) {
            if (block > consumeIndexMap.get(addr)) {
                consumeIndexMap.put(addr, block);
            }
        } else {
            consumeIndexMap.put(addr, block);
        }
    }
}
