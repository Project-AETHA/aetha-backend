package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.utils.StatusList;
import lombok.Data;

import java.util.Date;

@Data
public class ReportedPoemDTO {

    private String reportId;
    private AuthUser reportedUser;  // The user who reported the poem
    private Date reportedDate;        // Date when the report was made
    private String poemId;          // The ID of the poem being reported
    private String reason;          // Reason for the report
    private StatusList status;
}
