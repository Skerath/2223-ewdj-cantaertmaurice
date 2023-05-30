package controller;

import domain.Book;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import validator.BookRegistrationValidator;

@Controller
@RequestMapping("/registerbook")
@Slf4j
public class BookRegistrationController {

    @Autowired
    private BookRegistrationValidator bookRegistrationValidator;

    @GetMapping
    public String showRegistration(Model model) {
        model.addAttribute("book", new Book());
        return "bookRegistrationForm";
    }

    @PostMapping
    public String processRegistration(@Valid Book registration, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.error(result.getAllErrors().toString());
            return "bookRegistrationForm";
        }
        String isbn13 = registration.getIsbn13();
        return "redirect:/book/" + isbn13;
    }
}
