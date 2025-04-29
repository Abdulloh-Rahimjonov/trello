package uz.pdp.trello.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.pdp.trello.entity.Attachment;
import uz.pdp.trello.repo.AttachmentRepository;

@Controller
public class AttachmentController {

    private final AttachmentRepository attachmentRepository;

    public AttachmentController(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping("/attachment/{id}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable Integer id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(attachment.getContent());
    }

}
