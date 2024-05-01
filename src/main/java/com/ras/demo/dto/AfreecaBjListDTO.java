package com.ras.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AfreecaBjListDTO {
    private int result;
    private List<AfreecaBjDTO> allBj;
}
