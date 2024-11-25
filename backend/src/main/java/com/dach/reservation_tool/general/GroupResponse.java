package com.dach.reservation_tool.general;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class GroupResponse {

    private String group;
    private List<String> items;

    public GroupResponse(String group, List<String> items) {
        this.group = group;
        this.items = items;
    }
}
