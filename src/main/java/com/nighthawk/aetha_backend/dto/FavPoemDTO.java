package com.nighthawk.aetha_backend.dto;

import com.nighthawk.aetha_backend.entity.Poem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavPoemDTO {
    private String id;
    private String poemName;
    private String coverImage;
    private String userId;
}
