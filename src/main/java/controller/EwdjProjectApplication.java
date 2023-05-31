package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import repository.BookRepository;
import validator.AuthorValidator;
import validator.BookLocationValidator;
import validator.BookValidator;

import java.util.Locale;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"repository"})
@EntityScan(basePackages = {"domain"})
@ComponentScans({
//        @ComponentScan("service"),
        @ComponentScan("domain"),
        @ComponentScan("repository"),
        @ComponentScan("validator")
})
public class EwdjProjectApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(EwdjProjectApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/registerbook");
    }

    @Bean
    LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Bean
    BookValidator bookValidator() {
        return new BookValidator();
    }

    @Bean
    BookLocationValidator bookLocationValidator() {
        return new BookLocationValidator();
    }

    @Bean
    AuthorValidator authorValidator() {
        return new AuthorValidator();
    }

}

