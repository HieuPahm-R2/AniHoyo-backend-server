package com.HieuPahm.AniHoyo.model.dtos;

import com.HieuPahm.AniHoyo.model.entities.Season;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {
    private Long id;

    @NotBlank(message = "Tiêu đề tập phim không được để trống")
    @Size(max = 150, message = "Tiêu đề tập phim tối đa 150 ký tự")
    private String title;

    @Size(max = 1000, message = "Đường dẫn tệp tối đa 1000 ký tự")
    private String filePath;
    @Size(max = 255, message = "contentType tối đa 255 ký tự")
    private String contentType;

    private Season season;
}
