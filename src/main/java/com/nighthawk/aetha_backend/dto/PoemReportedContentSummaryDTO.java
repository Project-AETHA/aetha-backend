package com.nighthawk.aetha_backend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PoemReportedContentSummaryDTO {

    private String id;         // ID of the poem
    private String title;
    private String reason;
    private long count;
    private Date createdAt;

}
