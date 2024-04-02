package com.ras.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AFCLog {

    private Integer id;
    private String nickname;
    private Integer donationCount;
    private Date donationDate;


    public void setStarCount(int starCount) {
    }
    public void setTimeStamp(String timeStamp) {
    }
}
