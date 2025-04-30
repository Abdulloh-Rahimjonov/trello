package uz.pdp.trello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import uz.pdp.trello.dto.StatusListWrapper;
import uz.pdp.trello.entity.Status;
import uz.pdp.trello.repo.StatusRepository;

import java.util.List;

@Controller
@RequestMapping("/status")
public class StatusController {

    private final StatusRepository statusRepository;

    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @GetMapping("/addStatusPage")
    public String addStatusPage(@RequestParam Integer maxPosition, Model model) {
        model.addAttribute("maxPosition", maxPosition);
        return "addStatusPage";
    }

    @Transactional
    @PostMapping("/addStatus")
    public String addStatus(@RequestParam Integer maxPosition, @RequestParam String name, Model model) {
        Status status = new Status();
        status.setName(name);
        status.setPositionNumber(maxPosition + 1);
        status.setIsActive(true);
        statusRepository.save(status);
        return "redirect:/task";
    }

    @GetMapping("/manageOrders")
    public String manageOrders(Model model) {
        List<Status> allStatuses = statusRepository.findOrderByPositionNumber();
        StatusListWrapper wrapper = new StatusListWrapper();
        wrapper.setStatuses(allStatuses);
        model.addAttribute("statusesWrapper", wrapper);
        return "EditStatus";
    }

    @PostMapping("/update")
    @Transactional
    public RedirectView updateStatuses(
            @ModelAttribute("statusesWrapper") StatusListWrapper wrapper
    ) {
        List<Status> statuses = wrapper.getStatuses();
        for (Status s : statuses) {
            if (s.getIsActive() == null) {
                s.setIsActive(false);
            }
        }
        statusRepository.saveAll(statuses);
        return new RedirectView("/task");
    }
}
