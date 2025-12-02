package com.real.project.recommend;

import java.util.*;
import java.util.stream.Collectors;

import com.real.project.data.Book;
import com.real.project.data.BookData;
import com.real.project.data.Rental;
import com.real.project.data.RentalData;

/**
 * {@code RecommendationService} 클래스는 도서 대여 이력 데이터를 기반으로
 * 사용자에게 도서를 추천하는 기능을 제공합니다.
 *
 * <p>
 * 추천 기준은 다음 두 가지 방식으로 제공됩니다:
 * </p>
 * <ul>
 *   <li><b>별점순 추천:</b> 사용자들의 별점 평균이 높은 도서 우선</li>
 *   <li><b>대여순 추천:</b> 실제 대여 횟수가 많은 도서 우선</li>
 * </ul>
 *
 * <p>
 * 또한, 현재 대여 중인 도서는 추천 목록에서 "대여 불가"로 표시되며,
 * 반납되지 않은 도서를 식별하여 자동으로 필터링합니다.
 * </p>
 *
 * <p>
 * 이 클래스는 다음과 같은 데이터를 사용합니다:
 * </p>
 * <ul>
 *   <li>{@link com.real.project.data.BookData} - 도서 기본 정보</li>
 *   <li>{@link com.real.project.data.RentalData} - 대여 및 별점 기록</li>
 * </ul>
 *
 * <p>
 * 사용자는 추천을 원하는 카테고리를 {@link #setCategory(String)}를 통해 지정한 후,
 * {@link #recommendByRating()} 또는 {@link #recommendByRentalCount()} 메서드를 호출하여
 * 추천 목록을 확인할 수 있습니다.
 * </p>
 *
 * @author 
 * @since 1.0
 */
public class RecommendationService {

	/** 현재 선택된 도서 추천 카테고리 */
	private String selectedCategory;

	/**
	 * 사용자가 선택한 카테고리를 설정합니다.
	 *
	 * @param category 추천 도서 필터링에 사용할 도서 카테고리
	 */
	public void setCategory(String category) {
		this.selectedCategory = category;
	}

	/** 각 도서의 평균 별점 정보 (bookNumber → 평균 별점) */
	private Map<String, Double> bookRatingAvg = new HashMap<>();
	/** 각 도서의 누적 대여 횟수 (bookNumber → 대여 횟수) */
	private Map<String, Integer> bookRentalCount = new HashMap<>();
	/** 현재 대여 중으로, 대여 불가능한 도서 목록 (bookNumber 목록) */
	private Set<String> unavailableBookNumbers = new HashSet<>();

	public RecommendationService() {
		RentalData.load(); // ✅ RentalData.rentalList 사용
		calculateStatistics();
	}

	/**
	 * 별점 평균 및 대여 횟수 계산
	 * 대여 기록을 기반으로 도서별 별점 평균과 대여 횟수를 계산합니다.
	 * <p>
	 * 또한 반납되지 않은 도서(반납일이 비어있는 경우)는 대여 불가 목록에 추가합니다.
	 * 잘못된 형식의 별점 데이터는 무시되고 건너뜁니다.
	 * </p>
	 */
	public void calculateStatistics() {
		Map<String, List<Double>> ratingMap = new HashMap<>();
		Map<String, Integer> rentalMap = new HashMap<>();

		for (Rental r : RentalData.rentalList) {

			String bookId = r.getBookId();

			// 별점 처리
			try {
				double rating = (r.getStarRating() == null || r.getStarRating().trim().isEmpty())
						? 0.0
						: Double.parseDouble(r.getStarRating().trim());

				ratingMap.computeIfAbsent(bookId, k -> new ArrayList<>()).add(rating);

			} catch (NumberFormatException e) {
				System.out.printf("⚠️ 잘못된 별점 형식 포함된 레코드: [%s] - 건너뜁니다.\n", r.toString());
			}

			// 대여 횟수 누적
			rentalMap.put(bookId, rentalMap.getOrDefault(bookId, 0) + 1);

			// ❗ 반납일이 비어있으면 대여 불가 처리
			if (r.getReturnDate() == null || r.getReturnDate().trim().isEmpty()) {
				unavailableBookNumbers.add(bookId);
			}
		}

		// 별점 평균 계산
		for (String bookId : ratingMap.keySet()) {
			List<Double> ratings = ratingMap.get(bookId);
			double avg = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
			bookRatingAvg.put(bookId, avg);
		}

		bookRentalCount = rentalMap;
	}

	/**
	 * 도서 추천 - 별점순
	 * 선택된 카테고리 내에서 별점 평균이 높은 순으로 도서를 추천합니다.
	 * <p>
	 * 도서가 현재 대여 중이라면 '대여 불가'로 표시됩니다.
	 * </p>
	 * 출력 항목: 도서 코드, 제목, 저자, 출판사, 출판일, 분야, 별점, 대여 가능 여부
	 */
	public void recommendByRating() {
		System.out.println("\n🔷 별점순 추천 결과:\n");

		List<Book> filtered = BookData.booklist.stream()
				.filter(b -> b.getCategory().equals(this.selectedCategory)
						&& bookRatingAvg.containsKey(b.getBookNumber()))
				.sorted((b1, b2) -> Double.compare(
						bookRatingAvg.get(b2.getBookNumber()),
						bookRatingAvg.get(b1.getBookNumber())))
				.collect(Collectors.toList());

		if (filtered.isEmpty()) {
			System.out.println("⚠️ 추천 가능한 도서가 없습니다.");
			return;
		}

		System.out.println("📌 도서 추천 목록 (별점순):\n");

		System.out.printf("%-15s %-22s %-16s %-12s %-7s %-8s %-5s %s\n",
				"코드", "제목", "저자", "출판사", "출판일", "분야", "별점", "대여 가능");

		for (Book b : filtered) {
			String bookNum = b.getBookNumber();
			String availability = unavailableBookNumbers.contains(bookNum) ? "❌ 대여 불가" : "📗 대여 가능";

			String title = padRightDisplayWidth(formatTitle(b.getTitle(), 15), 30);
			String author = padRightDisplayWidth(b.getAuthor(), 20);
			String publisher = padRightDisplayWidth(b.getPublisher(), 20);
			String pubDate = padRightDisplayWidth(b.getPublishedDate(), 12);
			String categoryName = padRightDisplayWidth(b.getCategory(), 10);
			double rating = bookRatingAvg.get(bookNum);

			System.out.println(
					bookNum + " " +
							title + author + publisher + pubDate + categoryName +
							String.format("⭐ %.2f  ", rating) +
							availability);
		}
	}

	/**
	 * 도서 추천 - 대여순
	 * 선택된 카테고리 내에서 대여 횟수가 많은 순으로 도서를 추천합니다.
	 * <p>
	 * 도서가 현재 대여 중이라면 '대여 불가'로 표시됩니다.
	 * </p>
	 * 출력 항목: 도서 코드, 제목, 저자, 출판사, 출판일, 분야, 대여 횟수, 대여 가능 여부
	 */
	public void recommendByRentalCount() {
		System.out.println("\n🔷 대여순 추천 결과:\n");

		List<Book> filtered = BookData.booklist.stream()
				.filter(b -> b.getCategory().equals(this.selectedCategory)
						&& bookRentalCount.containsKey(b.getBookNumber()))
				.sorted((b1, b2) -> Integer.compare(
						bookRentalCount.get(b2.getBookNumber()),
						bookRentalCount.get(b1.getBookNumber())))
				.collect(Collectors.toList());

		if (filtered.isEmpty()) {
			System.out.println("⚠️ 추천 가능한 도서가 없습니다.");
			return;
		}

		System.out.println("📌 도서 추천 목록 (대여순):\n");

		System.out.printf("%-8s %-30s %-15s %-14s %-7s %-10s %-13s %s\n",
				"코드", "제목", "저자", "출판사", "출판일", "분야", "대여횟수", "대여 가능");

		for (Book b : filtered) {
			String bookNum = b.getBookNumber();
			String availability = unavailableBookNumbers.contains(bookNum) ? "❌ 대여 불가" : "📗 대여 가능";

			String title = padRightDisplayWidth(formatTitle(b.getTitle(), 15), 30);
			String author = padRightDisplayWidth(b.getAuthor(), 20);
			String publisher = padRightDisplayWidth(b.getPublisher(), 20);
			String pubDate = padRightDisplayWidth(b.getPublishedDate(), 12);
			String categoryName = padRightDisplayWidth(b.getCategory(), 15);
			int rentalCount = bookRentalCount.get(bookNum);

			System.out.printf(
					"%-8s %s%s%s%s%s%-12s %s\n",
					bookNum,
					title, author, publisher, pubDate, categoryName,
					rentalCount + "회",
					availability);
		}
	}

	/**
	 * ======================== 줄 정렬 메서드 ==============================
	 * 문자열의 실제 출력 너비(한글 2칸, 영문 1칸 기준)를 고려하여
	 * 오른쪽으로 공백을 추가하여 정해진 너비에 맞춥니다.
	 *
	 * @param text        출력할 문자열
	 * @param totalWidth  전체 출력 너비(한글은 2칸으로 계산)
	 * @return 지정된 너비에 맞게 오른쪽 공백이 추가된 문자열
	 */
	public static String padRightDisplayWidth(String text, int totalWidth) {
		int displayWidth = 0;
		StringBuilder result = new StringBuilder();

		for (char c : text.toCharArray()) {
			int charWidth = isKorean(c) ? 2 : 1;
			if (displayWidth + charWidth > totalWidth)
				break;

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
	 * @param c 검사할 문자
	 * @return 한글 문자이면 true, 아니면 false
	 */
	private static boolean isKorean(char c) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		return block == Character.UnicodeBlock.HANGUL_SYLLABLES
				|| block == Character.UnicodeBlock.HANGUL_JAMO
				|| block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO;
	}

	/**
	 * 도서 제목을 지정된 최대 길이에 맞게 잘라내고,
	 * 길이를 초과하면 말줄임표(...)를 붙입니다.
	 *
	 * @param title     원본 제목
	 * @param maxLength 최대 허용 길이
	 * @return 포맷된 제목 문자열
	 */
	public static String formatTitle(String title, int maxLength) {
		if (title == null)
			return "";
		return title.length() <= maxLength ? title : title.substring(0, maxLength - 1) + "…";
	}
}
