package com.polarbookshop.catalog_service.demo;

import com.polarbookshop.catalog_service.domain.Book;
import com.polarbookshop.catalog_service.domain.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("testdata")
public class BookDataLoader {
    private static final Logger log = LoggerFactory.getLogger(BookDataLoader.class);
    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        bookRepository.deleteAll();
        var book1 = Book.of("1234567891", "Northern Lights",
                "Lyra Silverstar", 9.90, "sam");
        var book2 = Book.of("1234567892", "Polar Journey",
                "Iorek Polarson", 12.90, "sam");
        bookRepository.saveAll(List.of(book1, book2));
        log.info("Test Books loaded");
    }
}
