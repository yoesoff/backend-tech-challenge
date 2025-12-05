package com.mhyusuf.user.controller.html;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class GreetingController {
    @GetMapping(value = {"/"})
    public String greeting(@RequestParam(value = "name", defaultValue = "Reviewer") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}