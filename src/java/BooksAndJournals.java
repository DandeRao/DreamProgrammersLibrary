/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mallikharjuna Rao Dande
 */
public class BooksAndJournals {
    String title;
    String author;
    String volume;
    String bookId;
    int pages;
    String ISBN;
    String type;
    String checkedOutTo;

    public BooksAndJournals(String title, String author, String volume, String bookId, int pages, String ISBN, String type, String checkedOutTo) {
        this.title = title;
        this.author = author;
        this.volume = volume;
        this.bookId = bookId;
        this.pages = pages;
        this.ISBN = ISBN;
        this.type = type;
        this.checkedOutTo = checkedOutTo;
    }

    public String getCheckedOutTo() {
        return checkedOutTo;
    }

    public void setCheckedOutTo(String checkedOutTo) {
        this.checkedOutTo = checkedOutTo;
    }

    
    
    public BooksAndJournals(String title, String author, String volume, String bookId, int pages, String ISBN, String type) {
        this.title = title;
        this.author = author;
        this.volume = volume;
        this.bookId = bookId;
        this.pages = pages;
        this.ISBN = ISBN;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    
    
    
}
