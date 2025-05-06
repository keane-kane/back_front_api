package com.uchk.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertionStatisticsDto {
    
    private long employedCount;
    private long selfEmployedCount;
    private long seekingCount;
    private long furtherStudiesCount;
    private long internshipCount;
    private long unknownCount;
    
    public long getTotal() {
        return employedCount + selfEmployedCount + seekingCount + 
               furtherStudiesCount + internshipCount + unknownCount;
    }
}
