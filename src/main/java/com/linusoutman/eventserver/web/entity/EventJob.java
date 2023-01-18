package com.linusoutman.eventserver.web.entity;

import lombok.Data;

import java.util.Date;

@Data
public class EventJob {

    private long id;
    private String params;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
