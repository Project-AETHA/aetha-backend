package com.nighthawk.aetha_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class StatDTO {

    private long totalUsers;
    private long totalComplaints;
    private long pendingNovelApprovals;

    //complaints chart
    private long completedComplaints;
    private long pendingComplaints;

    //weekly publishes barchart
    private  Map<DayOfWeek, Long> NovelCounts;
    private  Map<DayOfWeek, Long> PoemCounts;
    private  Map<DayOfWeek, Long> ShortStoryCounts;

    //monthly user Registration



}
