package perform;

import dto.BookDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class PerformRest {

    private final String SERVER_URI = "http://localhost:8080/api";
    private final WebClient webClient = WebClient.create();

    public PerformRest() {
        getBook("9781603093255");
        getBook("9783453435773");
        getBooksFromAuthorName("Stephen King");
        getBooksFromAuthorName("Marcus Aurelius");
    }

    private void getBook(String isbn) {
        webClient.get()
                .uri(SERVER_URI + "/book/" + isbn)
                .retrieve()
                .bodyToMono(BookDTO.class)
                .doOnSuccess(this::printBook)
                .block();
    }

    private void getBooksFromAuthorName(String authorName) {
        webClient.get().uri(SERVER_URI + "/author/" + authorName).retrieve()
                .bodyToFlux(BookDTO.class)
                .flatMap(book -> {
                    printBook(book);
                    return Flux.empty();
                })
                .blockLast();
    }

    private void printBook(BookDTO book) {
        System.out.printf("-------------------------%n");
        System.out.printf("Name=%s, ISBN=%s, PriceInEuro=%s%n", book.getName(), book.getIsbn13(), book.getPriceInEuro().toString());
        book.getAuthorNames().forEach(author -> System.out.printf("AuthorName=%s%n", author));
        book.getBookLocations().forEach(location -> System.out.printf("LocationName=%s, PlaceCode1=%s, PlaceCode2=%s%n", location.getLocationName(), location.getPlaceCode1(), location.getPlaceCode2()));
        System.out.printf("-------------------------%n");
    }
}
