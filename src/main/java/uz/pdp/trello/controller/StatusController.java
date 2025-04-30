package uz.pdp.trello.controller;

import org.springframework.data.repository.core.support.IncompleteRepositoryCompositionException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.trello.entity.Status;
import uz.pdp.trello.repo.StatusRepository;

@Controller
@RequestMapping("/status")
public class StatusController {

    private final StatusRepository statusRepository;

    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @GetMapping("/addStatusPage")
    public String addStatusPage(@RequestParam Integer maxPosition , Model model) {
          model.addAttribute("maxPosition", maxPosition);
          return "addStatusPage";
    }

    @Transactional
    @PostMapping("/addStatus")
    public String addStatus(@RequestParam Integer maxPosition ,@RequestParam String name , Model model) {
        Status status = new Status();
        status.setName(name);
        status.setPositionNumber(maxPosition + 1);
        status.setIsActive(true);
        statusRepository.save(status);
        return "redirect:/task";
    }

}
