package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealStateData {

    private String fullTitle;
    private String rooms;
    private String size;
    private String price;
    private String location;
    private String type;
    private String url;

}
