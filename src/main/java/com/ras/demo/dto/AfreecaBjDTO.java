package com.ras.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AfreecaBjDTO {
    private String bjId;
    private String bjNick;
    private String totalRank;

    public AfreecaBjDTO(String bjId, String bjNick, String totalRank) {
        this.bjId = bjId;
        this.bjNick = bjNick;
        this.totalRank = totalRank;
    }

    public String getBjId() {
        return bjId;
    }

    public void setBjId(String bjId) {
        this.bjId = bjId;
    }

    public String getBjNick() {
        return bjNick;
    }

    public void setBjNick(String bjNick) {
        this.bjNick = bjNick;
    }

    public String getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(String totalRank) {
        this.totalRank = totalRank;
    }
}
