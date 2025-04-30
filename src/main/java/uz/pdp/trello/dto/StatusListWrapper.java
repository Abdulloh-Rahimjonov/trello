package uz.pdp.trello.dto;

import lombok.Data;
import uz.pdp.trello.entity.Status;

import java.util.List;

@Data
public class StatusListWrapper {
    private List<Status> statuses;
}
