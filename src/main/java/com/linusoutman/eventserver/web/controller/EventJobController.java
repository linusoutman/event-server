package com.linusoutman.eventserver.web.controller;

import com.linusoutman.eventserver.web.common.ResultStatus;
import com.linusoutman.eventserver.web.common.utils.ResponseInfo;
import com.linusoutman.eventserver.web.entity.EventJob;
import com.linusoutman.eventserver.web.service.EventJobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/event")
@AllArgsConstructor
@CrossOrigin
public class EventJobController {

    @Autowired
    private EventJobService eventJobService;

    @GetMapping("/list")
    public ResponseInfo index() {
        try {
            List<EventJob> result = eventJobService.getAllJob();
            return ResponseInfo.ok().put("data", result);
        } catch (Exception e) {
            log.error("get event list failed, error : " + e.getMessage());
            return ResponseInfo.error(ResultStatus.GET_EVENT_LIST_FAILED);
        }
    }

    @PostMapping("/add")
    public ResponseInfo create(@RequestBody EventJob eventJob) {
        try {
            int count = eventJobService.insert(eventJob);
            if (count > 0) {
                return ResponseInfo.ok().put("data", "");
            } else {
                return ResponseInfo.error(ResultStatus.CREATE_EVENT_FAILED);
            }
        } catch (Exception e) {
            log.error("create event failed, error : " + e.getMessage());
            return ResponseInfo.error(ResultStatus.CREATE_EVENT_FAILED);
        }
    }

}
