package com.site;

import com.security.payload.LoginRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model,
                              HttpServletRequest httpServletRequest,
                              @ModelAttribute LoginRequest loginRequest) {
        model.addAttribute("httpServletRequest", httpServletRequest);
        model.addAttribute("loginRequest", loginRequest);
        //do something like logging
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}