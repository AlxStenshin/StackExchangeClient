package ru.alxstn.stackexchangeclient.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alxstn.stackexchangeclient.exception.DataProviderException;
import ru.alxstn.stackexchangeclient.model.taret.TargetDataWrapper;
import ru.alxstn.stackexchangeclient.service.DataProviderService;

@Controller
public class ViewController {

    private final DataProviderService service;
    public ViewController(DataProviderService service) {
        this.service = service;
    }

    @GetMapping()
    public String search(Model model) {
        return "index";
    }

    @GetMapping(value="/questions", produces = MediaType.TEXT_HTML_VALUE)
    public String search(Model model,
                         @RequestParam(required = false) String title,
                         @RequestParam(required = false) Integer page) {
        try {
            var result = service.searchByTitle(title, java.util.Optional.ofNullable(page));
            if (result.isPresent()) {
                var data = new TargetDataWrapper(result.get());
                model.addAttribute("questions", data.getQuestions());
                model.addAttribute("quota_max", data.getQuota_max());
                model.addAttribute("quota_remaining", data.getQuotaRemaining());
                model.addAttribute("has_more", data.getHasMore());
            }
        }
        catch (DataProviderException e) {
            model.addAttribute("error", e.getMessage());
            System.out.println("Error searching for \"" + title + "\" With page -> " + page + ". Message: " + e.getMessage());
        }
        return "index";
    }
}
