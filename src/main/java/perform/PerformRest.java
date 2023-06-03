package perform;

import domain.Book;
import org.springframework.web.reactive.function.client.WebClient;

public class PerformRest {

    private final String SERVER_URI = "http://localhost:8080/api";
    private final WebClient webClient = WebClient.create();

    public PerformRest() {
//        getBook("9781603093255");
    }

    private void getBook(String isbn) {
        webClient.get()
                .uri(SERVER_URI + "/book/" + isbn)
                .retrieve()
                .bodyToMono(Book.class)
                .doOnSuccess(this::printBook)
                .block();
    }

//    private void getAllFruits() {
//        webClient.get().uri(SERVER_URI + "/all").retrieve()
//                .bodyToFlux(Fruit.class)
//                .flatMap(fruit -> {
//                    printBook(fruit);
//                    return Mono.empty();
//                })
//                .blockLast();
//    }

    private void printBook(Book book) {
        System.out.printf("UUID=%s, Name=%s, ISBN=%s%n", book.getBookId().toString(), book.getName(), book.getIsbn13());
        System.out.printf("-------------------------%n");
        book.getAuthors().forEach(author -> System.out.printf("UUID=%s, FirstName%s, LastName=%s%n", author.getAuthorId().toString(), author.getFirstName(), author.getLastName()));
        System.out.printf("-------------------------%n");
        book.getBookLocations().forEach(location -> System.out.printf("UUID=%s, LocationName=%s, PlaceCode1=%s, PlaceCode2=%s%n", location.getLocationId().toString(), location.getLocationName(), location.getPlaceCode1(), location.getPlaceCode2()));
    }
}
