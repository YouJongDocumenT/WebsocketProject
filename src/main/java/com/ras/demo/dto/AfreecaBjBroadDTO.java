package com.ras.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AfreecaBjBroadDTO {
    private String bjId;
    private int broadNo;

    public AfreecaBjBroadDTO(String bjId, int broadNo) {
        this.bjId = bjId;
        this.broadNo = broadNo;
    }
}
