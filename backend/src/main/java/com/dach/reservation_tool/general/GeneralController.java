package com.dach.reservation_tool.general;

import com.dach.reservation_tool.conference.CONF_TYPE;
import com.dach.reservation_tool.parkinglot.PARKINGLOT_NUMBER;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static com.dach.reservation_tool.conference.CONF_TYPE.CONFERENCE_OG2;
import static com.dach.reservation_tool.conference.CONF_TYPE.CONFERENCE_OG3;
import static com.dach.reservation_tool.parkinglot.PARKINGLOT_NUMBER.*;

@RestController
@RequestMapping("/api/v1")
public class GeneralController {

    @GetMapping("/reserve-types")
    public List<GroupResponse> getAllReserveTypes() {
        return Arrays.asList(
                new GroupResponse("Conference Room", Arrays.asList(CONFERENCE_OG2.toString(), CONFERENCE_OG3.toString())),
                new GroupResponse("Parking Lot", Arrays.asList(PARKING_LOT12.toString(), PARKING_LOT13.toString(), PARKING_LOT14.toString(),
                                                        PARKING_LOT15.toString(), PARKING_LOT16.toString(), PARKING_LOT37.toString()))
        );
    }
}
