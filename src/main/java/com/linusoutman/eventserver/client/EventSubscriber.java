package com.linusoutman.eventserver.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linusoutman.eventserver.web.entity.EventJob;
import com.linusoutman.eventserver.web.service.EventJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EventSubscriber {

    @Autowired
    private EventJobService eventJobService;

    public void setup() {
        List<EventJob> eventJobs = eventJobService.getAllJob();
        for (EventJob eventJob : eventJobs) {
            JSONObject param = JSONObject.parseObject(eventJob.getParams());
            JSONArray topicArray = param.getJSONArray("topics");
            String[] topics = new String[topicArray.size()];
            for (int i = 0;i < topicArray.size(); i++) {
                topics[i] = topicArray.getString(i);
            }
            EthereumEventClient.registerJob(param.getString("contract"), topics);
            log.info("Event job setup - {}", eventJob.getParams());
        }
    }
}
