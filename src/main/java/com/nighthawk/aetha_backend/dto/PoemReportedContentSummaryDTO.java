package com.nighthawk.aetha_backend.dto;


import com.nighthawk.aetha_backend.entity.Poem;
import lombok.Data;

import java.util.Date;

@Data
public class PoemReportedContentSummaryDTO {

    private String id;         // ID of the poem
    private Poem poem;
    private String reason;
    private long count;
    private Date createdAt;

}
