package com.SAR.ReservationsSAR.context.topic.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicResponse {
    private UUID id;
    private String name;
}
