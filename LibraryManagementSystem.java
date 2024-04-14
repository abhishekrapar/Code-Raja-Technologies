import java.util.*;

class Book {
    private String title;
    private String author;
    private String genre;
    private boolean isAvailable;
    
    public Book(String title, String author, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
    }
    
    // Getters and setters
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Genre: " + genre + ", Available: " + isAvailable;
    }
}

class Patron {
    private String name;
    private String contactInfo;
    private List<Book> borrowedBooks;
    private Map<Book, Date> borrowedDates; // Track borrowed books and their borrow dates

    public Patron(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.borrowedBooks = new ArrayList<>();
        this.borrowedDates = new HashMap<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    // Borrowing methods
    public void borrowBook(Book book, Date borrowDate) {
        borrowedBooks.add(book);
        borrowedDates.put(book, borrowDate);
    }
    
    public void returnBook(Book book) {
        borrowedBooks.remove(book);
        borrowedDates.remove(book);
    }
    
    public Date getBorrowDate(Book book) {
        return borrowedDates.get(book);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Contact: " + contactInfo + ", Borrowed Books: " + borrowedBooks.size();
    }
}

class Library {
    private List<Book> books;
    private List<Patron> patrons;
    
    public Library() {
        books = new ArrayList<>();
        patrons = new ArrayList<>();
    }
    
    // Book Management methods
    public void addBook(Book book) {
        books.add(book);
    }
    
    public void removeBook(Book book) {
        books.remove(book);
    }
    
    public Book searchBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }
    
    // Patron Management methods
    public void addPatron(Patron patron) {
        patrons.add(patron);
    }
    
    public void removePatron(Patron patron) {
        patrons.remove(patron);
    }
    
    public Patron searchPatronByName(String name) {
        for (Patron patron : patrons) {
            if (patron.getName().equalsIgnoreCase(name)) {
                return patron;
            }
        }
        return null;
    }
    
    // Borrowing System methods
    public void borrowBook(String patronName, String bookTitle) {
        Patron patron = searchPatronByName(patronName);
        Book book = searchBookByTitle(bookTitle);
        
        if (patron != null && book != null && book.isAvailable()) {
            book.setAvailable(false);
            patron.borrowBook(book, new Date());
            System.out.println(patronName + " borrowed " + bookTitle);
        } else {
            System.out.println("Book is not available or patron not found.");
        }
    }
    
    public void returnBook(String patronName, String bookTitle) {
        Patron patron = searchPatronByName(patronName);
        Book book = searchBookByTitle(bookTitle);
        
        if (patron != null && book != null && patron.getBorrowedBooks().contains(book)) {
            book.setAvailable(true);
            patron.returnBook(book);
            System.out.println(patronName + " returned " + bookTitle);
        } else {
            System.out.println("Book is not borrowed by the patron or patron/book not found.");
        }
    }
    
    // Fine Calculation method
    public double calculateFine(String patronName, String bookTitle) {
        Patron patron = searchPatronByName(patronName);
        Book book = searchBookByTitle(bookTitle);
        
        if (patron != null && book != null && patron.getBorrowedBooks().contains(book)) {
            Date borrowDate = patron.getBorrowDate(book);
            long diffInMillis = new Date().getTime() - borrowDate.getTime();
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
            if (diffInDays > 14) { // Assume a borrowing period of 14 days
                return (diffInDays - 14) * 0.50; // Assume a fine of $0.50 per day overdue
            }
        }
        return 0.0;
    }
    
    // Report Generation methods
    public void generateBookAvailabilityReport() {
        System.out.println("Book Availability Report:");
        for (Book book : books) {
            System.out.println(book);
        }
    }
    
    public void generateBorrowingHistoryReport(String patronName) {
        Patron patron = searchPatronByName(patronName);
        if (patron != null) {
            System.out.println("Borrowing History Report for " + patronName + ":");
            for (Book book : patron.getBorrowedBooks()) {
                System.out.println(" - " + book.getTitle() + " (Borrowed on: " + patron.getBorrowDate(book) + ")");
            }
        } else {
            System.out.println("Patron not found.");
        }
    }
    
    public void generateFineReport() {
        System.out.println("Fine Report:");
        for (Patron patron : patrons) {
            for (Book book : patron.getBorrowedBooks()) {
                double fine = calculateFine(patron.getName(), book.getTitle());
                if (fine > 0.0) {
                    System.out.println(patron.getName() + " has a fine of $" + fine + " for the book: " + book.getTitle());
                }
            }
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        // Add books
        library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Classic"));
        library.addBook(new Book("1984", "George Orwell", "Dystopian"));
        library.addBook(new Book("To Kill a Mockingbird", "Harper Lee", "Classic"));

        // Add patrons
        library.addPatron(new Patron("John Doe", "john.doe@example.com"));
        library.addPatron(new Patron("Jane Smith", "jane.smith@example.com"));

        // Borrow and return books
        library.borrowBook("John Doe", "The Great Gatsby");
        library.borrowBook("Jane Smith", "1984");

        // Generate reports
        library.generateBookAvailabilityReport();
        library.generateBorrowingHistoryReport("John Doe");
        library.generateFineReport();

        // Return books and calculate fines
        library.returnBook("John Doe", "The Great Gatsby");
        library.calculateFine("Jane Smith", "1984");
    }
}