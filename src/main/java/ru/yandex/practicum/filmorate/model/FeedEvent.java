package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class FeedEvent {
    LocalDateTime timeStamp;
    EventType eventType;
    EventOperation eventOperation;
    int userId;
    int eventId;
    int entityId;
}