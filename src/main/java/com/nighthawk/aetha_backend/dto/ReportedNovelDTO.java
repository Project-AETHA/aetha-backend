package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.utils.StatusList;

import java.util.Date;

public class ReportedNovelDTO {

    private String reportId;
    private AuthUser reportedUser;  // The user who reported the poem
    private Date reportedDate;        // Date when the report was made
    private String novelId;          // The ID of the novel being reported
    private String reason;          // Reason for the report
    private StatusList status;
}
