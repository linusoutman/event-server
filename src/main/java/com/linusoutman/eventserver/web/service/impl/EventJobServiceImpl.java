package com.linusoutman.eventserver.web.service.impl;

import com.linusoutman.eventserver.web.entity.EventJob;
import com.linusoutman.eventserver.web.mapper.EventJobMapper;
import com.linusoutman.eventserver.web.service.EventJobService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@AllArgsConstructor
public class EventJobServiceImpl implements EventJobService {

    private EventJobMapper eventJobMapper;

    @Override
    public List<EventJob> getAllJob() {
        return eventJobMapper.getAll();
    }

    @Override
    public int insert(EventJob eventJob) {
        return eventJobMapper.insert(eventJob);
    }
}
