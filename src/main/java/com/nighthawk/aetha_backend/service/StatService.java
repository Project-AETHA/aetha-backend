package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.dto.StatDTO;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.repository.PoemRepository;
import com.nighthawk.aetha_backend.repository.SupportTicketRepository;
import com.nighthawk.aetha_backend.utils.StatusList;
import com.nighthawk.aetha_backend.utils.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;


@Service
public class StatService {

    @Autowired
    private AuthUserRepository repository;

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private PoemRepository poemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private StatDTO statDTO;


    public ResponseDTO getStatistics(){

        try {
            //cards
            long totalUsers = repository.count();
            long totalComplaints = supportTicketRepository.count();
            long pendingNovelApprovals = novelRepository.countByStatus(StatusList.PENDING);

            //complaints donut chart
            long completedComplaints = supportTicketRepository.countByStatus(StatusList.COMPLETED);
            long pendingComplaints = supportTicketRepository.countByStatus(StatusList.PENDING);

            //weekly publishes barchart
            // Date calculation: Get date ranges from yesterday to 7 days back
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);

            // Loop from yesterday back to 7 days
            Map<DayOfWeek, Long> novelCountsByDay = new HashMap<>();
            Map<DayOfWeek, Long> poemCountsByDay = new HashMap<>();
//            Map<String, Long> shortStoryCountsByDay = new HashMap<>();

            for (int i = 0; i < 7; i++) {
                LocalDate startDate = yesterday.minusDays(i);
                LocalDate endDate = startDate.plusDays(1);

                // Fetch counts for novels, poems, and short stories on this day
                long novelsCount = novelRepository.countByPublishedAtBetween(startDate, endDate);
                long poemsCount = poemRepository.countByCreatedAtBetween(startDate, endDate);
//                long shortStoriesCount = shortStoryRepository.countByCreatedAtBetween(startDate, endDate);

                // Save counts with the day as key (formatted date string)
                DayOfWeek dayOfWeek = startDate.getDayOfWeek();
                novelCountsByDay.put(dayOfWeek, novelsCount);
                poemCountsByDay.put(dayOfWeek, poemsCount);
//                shortStoryCountsByDay.put(dayOfWeek, shortStoriesCount);
            }

            // Send these maps to the DTO
            statDTO.setNovelCounts(novelCountsByDay);
            statDTO.setPoemCounts(poemCountsByDay);
//            statDTO.setShortStoryCounts(shortStoryCountsByDay);


            statDTO.setTotalUsers(totalUsers);
            statDTO.setTotalComplaints(totalComplaints);
            statDTO.setPendingNovelApprovals(pendingNovelApprovals);
            statDTO.setCompletedComplaints(completedComplaints);
            statDTO.setPendingComplaints(pendingComplaints);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Data fetched successfully");
            responseDTO.setContent(statDTO);

        } catch ( Exception e ) {

            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

}
