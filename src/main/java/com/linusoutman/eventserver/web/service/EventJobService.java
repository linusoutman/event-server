package com.linusoutman.eventserver.web.service;

import com.linusoutman.eventserver.web.entity.EventJob;

import java.util.List;

public interface EventJobService {
    List<EventJob> getAllJob();
    int insert(EventJob eventJob);
}
