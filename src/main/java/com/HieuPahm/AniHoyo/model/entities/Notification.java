package com.HieuPahm.AniHoyo.model.entities;

import java.time.Instant;

import com.HieuPahm.AniHoyo.utils.constant.NotificationEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverId;
    private Long senderId;

    // @Enumerated(EnumType.STRING)
    private String type;

    private String message;
    private Boolean isRead = false;

    private Long referId;

    private Instant createdAt = Instant.now();

}
