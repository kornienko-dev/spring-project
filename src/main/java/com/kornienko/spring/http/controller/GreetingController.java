package com.kornienko.spring.http.controller;

import com.kornienko.spring.database.entity.Role;
import com.kornienko.spring.dto.UserReadDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
@SessionAttributes("user")
public class GreetingController {

    @ModelAttribute("roles")
    public List<Role> roles() {
        return Arrays.asList(Role.values());
    }

    @GetMapping(value = "/hello")
    public String hello(Model model,
                        HttpServletRequest request,
                        @ModelAttribute UserReadDto userReadDto) {
        // request.setAttribute("user", new UserReadDto(1L, "Ivan"));
        // request.getSession().setAttribute("user", new UserReadDto(1L, "Ivan"));
        model.addAttribute("user", userReadDto);

        return "greeting/hello";
    }

    @GetMapping(value = "/bye")
    public String bye(@SessionAttribute UserReadDto user, Model model) {
        // request.getSession().getAttribute("user");
        return "greeting/bye";
    }

    @GetMapping(value = "/hello/{id}")
    public ModelAndView hello2(ModelAndView modelAndView, HttpServletRequest request,
                               @RequestParam Integer age,
                               @RequestHeader String accept,
                               @CookieValue("JSESSIONID") String jsessionId,
                               @PathVariable Integer id) {
        String ageParamValue = request.getParameter("age");
        String acceptHeader = request.getHeader("accept");
        Cookie[] cookies = request.getCookies();

        modelAndView.setViewName("greeting/hello");
        return modelAndView;
    }
}
