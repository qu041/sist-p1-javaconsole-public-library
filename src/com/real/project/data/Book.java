package com.real.project.data;

/**
 * {@code Book} 클래스는 도서의 주요 정보를 담는 데이터 모델입니다.
 * <p>
 * 도서 번호, 제목, 저자, 출판사, 출판일, 분야 등의 정보를 보유하며,
 * 도서 관리, 검색, 추천 등의 기능에서 사용됩니다.
 * </p>
 *
 * @author 
 */
public class Book {

    /** 도서 번호 (고유 식별자) */
    private String bookNumber;

    /** 도서 제목 */
    private String title;

    /** 저자명 */
    private String author;

    /** 출판사명 */
    private String publisher;

    /** 도서 분야 (카테고리) */
    private String category;

    /** 출판일 */
    private String publishedDate;

    /**
     * Book 객체 생성자.
     *
     * @param bookNumber     도서 번호
     * @param title          도서 제목
     * @param author         저자명
     * @param publisher      출판사명
     * @param category       도서 분야
     * @param publishedDate  출판일
     */
    public Book(String bookNumber, String title, String author, String publisher,
                String category, String publishedDate) {
        this.bookNumber = bookNumber;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.publishedDate = publishedDate;
    }

    /** @return 도서 번호 */
    public String getBookNumber() { return bookNumber; }

    /** @param bookNumber 도서 번호 설정 */
    public void setBookNumber(String bookNumber) { this.bookNumber = bookNumber; }

    /** @return 도서 제목 */
    public String getTitle() { return title; }

    /** @param title 도서 제목 설정 */
    public void setTitle(String title) { this.title = title; }

    /** @return 저자명 */
    public String getAuthor() { return author; }

    /** @param author 저자명 설정 */
    public void setAuthor(String author) { this.author = author; }

    /** @return 출판사명 */
    public String getPublisher() { return publisher; }

    /** @param publisher 출판사명 설정 */
    public void setPublisher(String publisher) { this.publisher = publisher; }

    /** @return 출판일 */
    public String getPublishedDate() { return publishedDate; }

    /** @param publishedDate 출판일 설정 */
    public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }

    /** @return 도서 분야 */
    public String getCategory() { return category; }

    /** @param category 도서 분야 설정 */
    public void setCategory(String category) { this.category = category; }

    /**
     * 도서 정보를 문자열 형태로 반환합니다.
     *
     * @return 도서 정보 문자열
     */
    @Override
    public String toString() {
        return "[" + bookNumber + "] [제목:" + title + "]  [저자:" + author + "]  [출판사:" + publisher
            + "]    [분야:" + category + "] [출판일:" + publishedDate + "]";
    }
}
