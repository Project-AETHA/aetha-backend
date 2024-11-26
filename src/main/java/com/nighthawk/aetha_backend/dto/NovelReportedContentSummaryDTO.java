package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.Poem;
import lombok.Data;

import java.util.Date;

@Data
public class NovelReportedContentSummaryDTO {

    private String id;         // ID of the novel
    private Novel novel;
    private String reason;
    private long count;
    private Date createdAt;

}
