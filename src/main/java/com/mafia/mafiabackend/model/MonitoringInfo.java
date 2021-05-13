package com.mafia.mafiabackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.Instant;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringInfo {
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
