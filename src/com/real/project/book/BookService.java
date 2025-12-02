package com.real.project.book;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.real.project.data.Book;
import com.real.project.data.BookData;
import com.real.project.ui.BookSearchMenuUi;

/**
 * 📚 BookService 클래스
 * 
 * 도서 등록, 수정, 삭제, 검색, 전체 조회 등 도서 관리 전반의 기능을 제공하는 서비스 클래스입니다.
 * 
 * <p>이 클래스는 {@link com.real.project.data.BookData}의 정적 리스트를 기반으로 작동하며,
 * 사용자의 콘솔 입력을 통해 도서 정보를 추가하거나 수정하고, 조건에 따라 도서 목록을 검색하거나 출력할 수 있습니다.
 * 또한, 도서의 대여 여부를 확인하여 대여 중인 도서에 대한 수정/삭제 제한도 처리합니다.</p>
 * 
 * <p>주요 기능:</p>
 * <ul>
 *   <li>도서 등록 기능: 사용자 입력을 받아 새로운 도서 객체를 생성 및 저장</li>
 *   <li>도서 수정 기능: 기존 도서 정보를 수정</li>
 *   <li>도서 삭제 기능: 지정된 도서번호의 도서 삭제</li>
 *   <li>도서 검색 기능: 제목, 저자, 출판사, 분야별 검색 제공</li>
 *   <li>전체 도서 목록 출력 기능</li>
 *   <li>도서 대여 가능 여부 확인 기능</li>
 * </ul>
 * 
 * @author [작성자]
 * @version 1.0
 * @since 2025-07-14
 */
public class BookService {

	/**
	 * BookService 클래스는 도서 등록, 수정, 삭제, 검색, 전체 조회, 대여 여부 확인 등의
	 * 도서 관리 기능을 담당하는 서비스 클래스입니다.
	 */
	public BookService() {}

	/**
	 * 도서 등록
	 * 사용자 입력을 통해 도서를 등록하는 메서드입니다.
	 * 도서번호 형식 및 중복 여부, 입력값의 유효성을 검증합니다.
	 */
	public void addBookFromUserInput() {
		Scanner sc = new Scanner(System.in);
		System.out.println("도서 등록 정보를 입력하세요.");

		System.out.print("도서번호: ");
		String number = sc.nextLine();

		if (!number.matches("S\\d{5}")) {
			System.out.println("❌ 도서번호 형식이 잘못되었습니다. (예: S12345)");
			return;
		}

		for (Book b : BookData.booklist) {
			if (b.getBookNumber().equals(number)) {
				System.out.println("❌ 이미 존재하는 도서번호입니다.");
				return;
			}
		}

		System.out.print("제목: ");
		String title = sc.nextLine().trim();
		if (title == null || title.isEmpty()) {
			System.out.println("⚠️ 제목은 비워둘 수 없습니다.");
			return;
		}

		System.out.print("저자: ");
		String author = sc.nextLine().trim();
		if (author == null || author.isEmpty()) {
			System.out.println("⚠️ 저자는 비워둘 수 없습니다.");
			return;
		}

		System.out.print("출판사: ");
		String publisher = sc.nextLine().trim();
		if (publisher == null || publisher.isEmpty()) {
			System.out.println("⚠️ 출판사는 비워둘 수 없습니다.");
			return;
		}
		
		System.out.println();
		System.out.println("""
				╔═══════════════════════════════ 분야 ═══════════════════════════════╗
				║                                                                    ║
				║       경제/경영             소설                   자기계발        ║
				║       과학                  시/에세이              정치/사회       ║
				║       기술/공학             역사/문화              컴퓨터/IT       ║ 
				║       만화                  유아(0~7세)            인문            ║
				║                                                                    ║
				╚════════════════════════════════════════════════════════════════════╝
				    			""");
		System.out.print("분야: ");
		String category = sc.nextLine();
		String categoryId = getCategoryIdByCategoryName(category);

		if (categoryId.equals("Unknown")) {
			System.out.println("❌ 유효하지 않은 분야입니다. 아래 중 하나를 입력하세요:");
			System.out.println("경제/경영, 과학, 기술/공학, 만화, 소설, 시/에세이, 역사/문화, 유아, 인문, 자기계발, 정치/사회, 컴퓨터/IT");
			return;
		}

		System.out.print("발행일자 (예: 20250701): ");
		String publishedDate = sc.nextLine();

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate.parse(publishedDate, formatter);
		} catch (DateTimeParseException e) {
			System.out.println("❌ 존재하지 않는 날짜입니다. (예: 20250701)");
			return;
		}

		Book book = new Book(number, title, author, publisher, category, publishedDate);
		BookData.booklist.add(book);
		BookData.save();
		System.out.println("✅ 도서 등록 완료: " + title);	
	}
	
	/**
	 * 도서 수정
	 * 사용자가 입력한 도서번호에 해당하는 도서 정보를 수정합니다.
	 * 대여 중인 도서는 수정이 불가능합니다.
	 */
	public void updateBook() {
	Scanner sc = new Scanner(System.in);
	System.out.println("수정할 도서 정보를 입력하세요.");
	System.out.print("도서번호: ");
	String number = sc.nextLine();

	Book target = null;
	for (Book b : BookData.booklist) {
		if (b.getBookNumber().equals(number)) {
			target = b;
			break;
		}
	}

	if (target == null) {
		System.out.println("❌ 해당 도서번호를 찾을 수 없습니다.");
		return;
	}

	// ✅ 대여 중인지 확인
	if (!isBookAvailable(number)) {
		System.out.println("⚠️ 현재 대여중입니다. 수정/삭제가 불가능합니다.");
		return;
	}

	System.out.println("새로운 도서 정보를 입력하세요.");
	System.out.print("제목: ");
	String title = sc.nextLine().trim();
	if (title == null || title.isEmpty()) {
		System.out.println("⚠️ 제목은 비워둘 수 없습니다.");
		return;
	}

	System.out.print("저자: ");
	String author = sc.nextLine().trim();
	if (author == null || author.isEmpty()) {
		System.out.println("⚠️ 저자는 비워둘 수 없습니다.");
		return;
	}

	System.out.print("출판사: ");
	String publisher = sc.nextLine().trim();
	if (publisher == null || publisher.isEmpty()) {
		System.out.println("⚠️ 출판사는 비워둘 수 없습니다.");
		return;
	}

	System.out.println();
	System.out.println("""
			╔═══════════════════════════════ 분야 ═══════════════════════════════╗
			║                                                                    ║
			║       경제/경영             소설                   자기계발        ║
			║       과학                  시/에세이              정치/사회       ║
			║       기술/공학             역사/문화              컴퓨터/IT       ║ 
			║       만화                  유아(0~7세)            인문            ║
			║                                                                    ║
			╚════════════════════════════════════════════════════════════════════╝
						""");
	System.out.print("분야: ");
	String category = sc.nextLine().trim();
	String categoryId = getCategoryIdByCategoryName(category);
	if (categoryId.equals("Unknown")) {
		System.out.println("⚠️ 유효하지 않은 분야입니다. 아래 중 하나를 입력하세요:");
		System.out.println("👉 경제/경영, 과학, 기술/공학, 만화, 소설, 시/에세이, 역사/문화, 유아, 인문, 자기계발, 정치/사회, 컴퓨터/IT");
		return;
	}

	System.out.print("발행일자 (예: 20250701): ");
	String publishedDate = sc.nextLine().trim();
	try {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate.parse(publishedDate, formatter);
	} catch (DateTimeParseException e) {
		System.out.println("⚠️ 존재하지 않는 날짜입니다. (예: 20250701)");
		return;
	}

	target.setTitle(title);
	target.setAuthor(author);
	target.setPublisher(publisher);
	target.setCategory(category);
	target.setPublishedDate(publishedDate);

	System.out.println("✅ 도서 수정 완료: " + target.getTitle());
	BookData.save();
	}

	/**
	 * 도서 삭제
	 * 지정된 도서번호에 해당하는 도서를 삭제합니다.
	 * 대여 중인 도서는 삭제가 불가능합니다.
	 *
	 * @param bookNumber 삭제할 도서의 도서번호
	 */
	public void deleteBook(String bookNumber) {
	// ✅ 대여 중인지 확인
	if (!isBookAvailable(bookNumber)) {
		System.out.println("⚠️ 현재 대여중입니다. 수정/삭제가 불가능합니다.");
		return;
	}

	Iterator<Book> iterator = BookData.booklist.iterator();
	boolean deleted = false;

	while (iterator.hasNext()) {
		Book book = iterator.next();
		if (book.getBookNumber().equals(bookNumber)) {
			iterator.remove();
			deleted = true;
			System.out.println("✅ 도서 삭제 완료: " + book.getTitle());
			break;
		}
	}

	if (!deleted) {
		System.out.println("❌ 해당 도서번호를 찾을 수 없습니다.");
	} else {
		BookData.save();
	}
	}

	/**
	 * 도서 검색
	 *
	 * @param searchType 검색 유형 (1: 제목, 2: 저자, 3: 출판사, 4: 분야)
	 * @param keyword    검색어
	 * @return 검색 결과에 해당하는 도서 목록
	 */
	public List<Book> searchBooks(String searchType, String keyword) { //컬렉션 즉 리스트 반환하는 클래스 생성 
																		//searchType은 1,2,3,4 즉 어떤 종류로 검색할지 타입을 받고 
																		//그에 따른 검색어 키워드 입력 받음
       
		List<Book> results = new ArrayList<>(); //검색 결과를 받을 컬렉션 ArrayList 생성

        if (keyword == null || keyword.trim().isEmpty()) { // 검색어가 비어있거나 공백이면 빈 리스트를 즉시 반환

            return results;
        }

        	for (Book b : BookData.booklist) { //도서 목록 데이터 조건문
        	
            String lowerKeyword = keyword.toLowerCase();//대소문자 구분 없이 받기 위한 코드

            switch (searchType) {
                case "1": // 제목으로 검색
                    if (b.getTitle().toLowerCase().contains(lowerKeyword)) { //모든 문자를 소문자로 바꾸는 toLowercase 
                        													 //사용후 사용자가 입력한 값이 데이터에 있는지 확인 후 반환
                        results.add(b);
                    }
                    break;
                case "2": // 저자로 검색
                    if (b.getAuthor().toLowerCase().contains(lowerKeyword)) { //case 1번과 같은 맥락으로 저자 검색
                        results.add(b);
                    }
                    break;
                case "3": // 출판사로 검색
                    if (b.getPublisher().toLowerCase().contains(lowerKeyword)) {
                        results.add(b);
                    }
                    break;
                case "4": // 분야로 검색
                    if (b.getCategory().toLowerCase().contains(lowerKeyword)) {
                        results.add(b);
                    }
                    break;
            }
        }
        return results; // 검색 결과를 반환
    }

	/**
	 * 검색된 도서 리스트를 포맷에 맞춰 출력합니다.
	 *
	 * @param bookList 검색 결과 리스트
	 */
	public void printSearchedBooks(List<Book> bookList) {
	    if (bookList == null || bookList.isEmpty()) {
	        System.out.println("\n>> 📚 검색 결과가 없습니다.");
	        return;
	    }

	    // 보기 좋게 한 줄 띄우고, viewAllBooks와 동일한 헤더를 출력합니다.
	    System.out.println();
	    System.out.printf("%-18s %-16s %-17s %-16s %-15s %-9s %s\n",
	            "[도서번호]", "[제목]", "[저자]", "[출판사]", "[출판일]", "[분야]", "[대여 여부]");

		for (Book b : bookList) {
			String status = isBookAvailable(b.getBookNumber()) ? "대여 가능" : "대여 불가";
			System.out.println(
		            "  " +
		            padRightDisplayWidth(b.getBookNumber(), 12) +
		            padRightDisplayWidth(formatTitle(b.getTitle(), 15), 28) +
		            padRightDisplayWidth(formatTitle(b.getAuthor(), 10), 20) +
		            padRightDisplayWidth(formatTitle(b.getPublisher(), 10), 20) +
		            padRightDisplayWidth(b.getPublishedDate(), 17) +
		            padRightDisplayWidth(String.format("%s", b.getCategory()), 15) +
		            status
		        );
	    }
	    System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
	    System.out.printf(">> 총 %d권의 도서가 검색되었습니다.\n", bookList.size());
	}
	
	/**
	 * 전체 도서 목록을 출력합니다.
	 */
	public void viewAllBooks() {
		List<Book> bookList = BookData.booklist;
		if (bookList.isEmpty()) {
			System.out.println("❌ 등록된 도서가 없습니다.");
			return;
		}
		
		System.out.printf("%-18s %-16s %-17s %-16s %-15s %-9s %s\n",
	            "[도서번호]", "[제목]", "[저자]", "[출판사]", "[출판일]", "[분야]", "[대여 여부]");

		for (Book b : bookList) {
			String status = isBookAvailable(b.getBookNumber()) ? "대여 가능" : "대여 불가";
			System.out.println(
		            "  " +
		            padRightDisplayWidth(b.getBookNumber(), 12) +
		            padRightDisplayWidth(formatTitle(b.getTitle(), 15), 28) +
		            padRightDisplayWidth(formatTitle(b.getAuthor(), 10), 20) +
		            padRightDisplayWidth(formatTitle(b.getPublisher(), 10), 20) +
		            padRightDisplayWidth(b.getPublishedDate(), 17) +
		            padRightDisplayWidth(String.format("%s", b.getCategory()), 15) +
		            status
		        );
		}
	}

	/**
	 * 대여 가능 여부 확인
	 * 해당 도서번호의 도서가 대여 가능한 상태인지 확인합니다.
	 *
	 * @param bookNumber 확인할 도서번호
	 * @return true: 대여 가능, false: 대여 중
	 */
	private boolean isBookAvailable(String bookNumber) {
		try (BufferedReader reader = new BufferedReader(new FileReader("dat\\대여.csv"))) {
			reader.readLine(); // skip header
			String line;
			while ((line = reader.readLine()) != null) {
				String[] temp = line.split(",", -1); // 빈 칸도 유지!
				if (temp.length < 6) continue;

				if (temp[2].equals(bookNumber)) {
					String returnDate = temp[5];
					if (returnDate == null || returnDate.trim().isEmpty() || returnDate.equalsIgnoreCase("null")) {
						return false; // 대여 중
					}
				}
			}
		} catch (Exception e) {
			System.out.println("❌ 대여 여부 확인 실패: " + e.getMessage());
		}
		return true; // 대여 가능
	}

	/**
	 * 분야명 → 분야ID 자동 매칭
	 * 분야명을 기반으로 분야 ID를 반환합니다.
	 * 존재하지 않는 분야명일 경우 "Unknown"을 반환합니다.
	 *
	 * @param categoryName 분야명 (예: "경제/경영")
	 * @return 분야 ID (예: "A1") 또는 "Unknown"
	 */
	private String getCategoryIdByCategoryName(String categoryName) {
	    String input = categoryName.trim();
	    if (input == null || input.isEmpty()) return "Unknown";

	    String[][] categories = {
	        { "A1", "경제/경영" }, { "A2", "과학" }, { "A3", "기술/공학" }, { "A4", "만화" },
	        { "A5", "소설" }, { "A6", "시/에세이" }, { "A7", "역사/문화" }, { "A8", "유아" },
	        { "A9", "인문" }, { "A10", "자기계발" }, { "A11", "정치/사회" }, { "A12", "컴퓨터/IT" }
	    };

	    for (String[] pair : categories) {
	        if (pair[1].equalsIgnoreCase(input)) {
	            return pair[0]; // 분야명이 유효하면 그에 해당하는 ID 반환
	        }
	    }

	    return "Unknown"; // 일치하는 분야명이 없으면 Unknown 반환
	}

	/**
	 * ================== 줄 정렬 관련 유틸 ==================
	 * 문자열의 디스플레이 너비를 기준으로 우측 공백을 채워 정렬된 문자열을 반환합니다.
	 * 한글은 2칸으로 계산합니다.
	 *
	 * @param text        출력할 문자열
	 * @param totalWidth  원하는 총 너비
	 * @return 정렬된 문자열
	 */
	public static String padRightDisplayWidth(String text, int totalWidth) {
		if (text == null) text = "";
		int displayWidth = 0;
		StringBuilder result = new StringBuilder();

		for (char c : text.toCharArray()) {
			int charWidth = isKorean(c) ? 2 : 1;
			if (displayWidth + charWidth > totalWidth) break;
			result.append(c);
			displayWidth += charWidth;
		}
		while (displayWidth < totalWidth) {
			result.append(" ");
			displayWidth++;
		}
		return result.toString();
	}

	/**
	 * 주어진 문자가 한글인지 여부를 판단합니다.
	 *
	 * @param c 판단할 문자
	 * @return true: 한글, false: 기타
	 */
	private static boolean isKorean(char c) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		return block == Character.UnicodeBlock.HANGUL_SYLLABLES ||
			   block == Character.UnicodeBlock.HANGUL_JAMO ||
			   block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO;
	}

	/**
	 * 도서 제목이 길 경우, 지정된 길이까지만 잘라서 출력합니다.
	 *
	 * @param title     제목
	 * @param maxLength 최대 길이
	 * @return 잘린 제목 + … (생략 부호)
	 */
	public static String formatTitle(String title, int maxLength) {
		if (title == null) return "";
		return title.length() <= maxLength ? title : title.substring(0, maxLength - 1) + "…";
	}
}
