package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.Comment;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovelChapterOverview {

    private Novel novel;
    private List<ChapterDTO> chapters;
    private List<Review> reviews;

}
