package com.linusoutman.eventserver.web.mapper;

import com.linusoutman.eventserver.web.entity.EventJob;

import java.util.List;

public interface EventJobMapper {

    int insert(EventJob eventJob);

    List<EventJob> getAll();
}
