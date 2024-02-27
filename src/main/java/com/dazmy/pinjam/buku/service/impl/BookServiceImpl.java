package com.dazmy.pinjam.buku.service.impl;

import com.dazmy.pinjam.buku.entity.Book;
import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.entity.UserBorrowBook;
import com.dazmy.pinjam.buku.entity.UserBorrowBookId;
import com.dazmy.pinjam.buku.model.request.AddBookRequest;
import com.dazmy.pinjam.buku.model.request.BorrowBookRequest;
import com.dazmy.pinjam.buku.model.response.BookResponse;
import com.dazmy.pinjam.buku.repository.BookRepository;
import com.dazmy.pinjam.buku.repository.UserBorrowBookRepository;
import com.dazmy.pinjam.buku.repository.UserRepository;
import com.dazmy.pinjam.buku.service.BookService;
import com.dazmy.pinjam.buku.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserBorrowBookRepository userBorrowBookRepository;
    private final ValidationService validationService;

    @Override
    @Transactional
    public void addBook(AddBookRequest request) {
        validationService.validate(request);

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setWriter(request.getWriter());
        book.setPublisher(request.getPublisher());
        book.setPage(request.getPage());
        book.setYop(request.getYop());
        book.setStatus(false);

        bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(this::toBookResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBook(Long bookId) {
        Book book = checkExistBook(bookId);
        return toBookResponse(book);
    }

    @Override
    @Transactional
    public void borrowBook(BorrowBookRequest request, Long bookId, User user) {
        Book book = checkExistBook(bookId);
        validationService.validate(request);

        checkAllValidationToCreateRelationship(request, book, user);

        UserBorrowBookId userBorrowBookId = new UserBorrowBookId();
        userBorrowBookId.setUserId(user.getId());
        userBorrowBookId.setBookId(book.getId());

        UserBorrowBook userBorrowBook = new UserBorrowBook();
        userBorrowBook.setId(userBorrowBookId);
        userBorrowBook.setUser(user);
        userBorrowBook.setBook(book);
        userBorrowBook.setDateBorrow(LocalDate.parse(request.getDateBorrow()));
        userBorrowBook.setDateReturn(LocalDate.parse(request.getDateReturn()));
        userBorrowBookRepository.save(userBorrowBook);
    }

    private void checkAllValidationToCreateRelationship(BorrowBookRequest request, Book book, User user) {
        // check age minimum borrow is 15
        if (user.getAge() < 15) {
            throw new ResponseStatusException(406, "Your are not old enough", null);
        }

        // check user must have at least 1 address
        if (user.getAddresses().isEmpty()) {
            throw new ResponseStatusException(406, "Please add address at least one", null);
        }

        if (userBorrowBookRepository.findByUserAndBook(user, book).isPresent()) {
            throw new ResponseStatusException(406, "You already borrow this book", null);
        }

        // check max 3 books user can borrow
        if (user.getBooks().size() == 3) {
            throw new ResponseStatusException(406, "You are already borrow 3 books", null);
        }

        // check max 2 weeks
        int days = LocalDate.parse(request.getDateBorrow()).until(LocalDate.parse(request.getDateReturn())).getDays();
        if (days > 14) {
            throw new ResponseStatusException(406, "Max borrow book is 2 weeks", null);
        }
    }

    private Book checkExistBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(404, "Book Not Found", null));
    }

    private BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .writer(book.getWriter())
                .publisher(book.getPublisher())
                .page(book.getPage())
                .yop(book.getYop())
                .status(book.getStatus())
                .build();
    }

}
