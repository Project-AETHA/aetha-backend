package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.utils.predefined.ContentType;

import java.util.Date;

public class ReportedContentDTO {

    private String id;

    private String sourceId;

    private String chapterId;

    private AuthUser author;

    private ContentType type;

    private Date createdAt;

    private String reason;

}
