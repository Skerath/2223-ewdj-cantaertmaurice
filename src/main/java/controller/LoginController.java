package controller;

import domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import repository.BookRepository;

@Controller
@RequestMapping("/account")
@Slf4j
public class LoginController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping(value = "/login")
    public String login(Model model) {

//        model.addAttribute("book", book);
        return "redirect:/registerbook";
    }

    @GetMapping(value = "/logout")
    public String logout(Model model) {

//        model.addAttribute("book", book);
        return "redirect:/registerbook";
    }

}
