package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.dto.StatDTO;
import com.nighthawk.aetha_backend.repository.*;
import com.nighthawk.aetha_backend.utils.StatusList;
import com.nighthawk.aetha_backend.utils.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


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

    @Autowired
    private ShortStoryRepository shortStoryRepository;


    public ResponseDTO getStatistics(){

        try {
            //cards
            long totalUsers = repository.count();
            long totalComplaints = supportTicketRepository.count();
            long pendingNovelApprovals = novelRepository.countByStatus(StatusList.PENDING);

            statDTO.setTotalUsers(totalUsers);
            statDTO.setTotalComplaints(totalComplaints);
            statDTO.setPendingNovelApprovals(pendingNovelApprovals);

            //complaints donut chart
            long completedComplaints = supportTicketRepository.countByStatus(StatusList.COMPLETED);
            long pendingComplaints = supportTicketRepository.countByStatus(StatusList.PENDING);

            statDTO.setCompletedComplaints(completedComplaints);
            statDTO.setPendingComplaints(pendingComplaints);

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

            //user registration trend
            LocalDate sixMonthsAgo = today.minusMonths(6);

            List<Map<String, Long>> cumulativeUserCounts = new ArrayList<>();

            long cumulativeReaders = 0;
            long cumulativeWriters = 0;

            for(int i=0; i<6 ; i++){
                LocalDate startOfMonth = sixMonthsAgo.plusMonths(i);
                LocalDate endOfMonth = startOfMonth.plusMonths(1);

                long readersCount = Optional.ofNullable(repository.countByCreatedAtBetweenAndRole(startOfMonth, endOfMonth, "READER")).orElse(0);
                long writersCount = Optional.ofNullable(repository.countByCreatedAtBetweenAndRole(startOfMonth, endOfMonth, "WRITER")).orElse(0);

                cumulativeReaders += readersCount;
                cumulativeWriters += writersCount;

                Map<String, Long> monthlyData = new HashMap<>();
                monthlyData.put("month", Long.valueOf(startOfMonth.getMonthValue()));
                monthlyData.put("year", Long.valueOf(startOfMonth.getYear()));
                monthlyData.put("cumulativeReaders", cumulativeReaders);
                monthlyData.put("cumulativeWriters", cumulativeWriters);

                cumulativeUserCounts.add(monthlyData);
            }


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

    public ResponseDTO getContentCount(){

        try{
            long novelCount = novelRepository.count();
            long poemCount = poemRepository.count();
            long shortStoryCount = shortStoryRepository.count();

            HashMap<String,Object> contentCount = new HashMap<>();

            contentCount.put("novels",novelCount);
            contentCount.put("poems",poemCount);
            contentCount.put("shortstories",shortStoryCount);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("successful");
            responseDTO.setContent(contentCount);

        }catch (Exception e){

            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage(e.getMessage());
        }


        return responseDTO;
    }

}
