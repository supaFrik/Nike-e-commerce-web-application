package vn.demo.nike.features.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.demo.nike.features.home.service.HomeService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("runningSection", homeService.getRunningSection());
        return "user/index";
    }
}
