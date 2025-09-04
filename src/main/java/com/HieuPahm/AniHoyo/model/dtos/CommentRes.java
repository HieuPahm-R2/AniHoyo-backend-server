package com.HieuPahm.AniHoyo.model.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRes {
    private Long id;
    private Long userId;
    private String username;
    private String content;
    private int likeCount;
    private List<CommentRes> replies;
    private LocalDateTime createdAt;
}
