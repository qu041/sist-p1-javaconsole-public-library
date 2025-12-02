package com.real.project.statistics;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.real.project.data.Book;
import com.real.project.data.Member;
import com.real.project.data.MemberData;
import com.real.project.data.Rental;
import com.real.project.data.RentalData;
import com.real.project.rental.RentalService;

/**
 * 관리자 통계 기능을 제공하는 서비스 클래스입니다.
 * 연체 회원, 등급별 회원, 대여 도서, 연체 도서 등의 정보를 출력합니다.
 */
public class AdminStatisticsService {

	/**
	 * 연체 회원 목록을 출력합니다.
	 * 대여 목록에서 반납기한이 지났고 아직 반납되지 않은 도서를 기준으로 연체 회원을 판별합니다.
	 * 회원번호, 회원명, 연락처, 도서명, 반납기한, 연체일수 출력
	 * 탈퇴 회원이나 삭제된 도서도 처리 가능하도록 예외 처리하여 출력합니다.
	 */
	public void showOverdueMembers() {

		System.out.println("연체 회원 목록");
		System.out.println(padRightDisplayWidth("[회원번호]", 13) 
				+ padRightDisplayWidth("[회원명]", 16)
				+ padRightDisplayWidth("[연락처]", 21) 
				+ padRightDisplayWidth("[도서명]", 21)
				+ padRightDisplayWidth("[반납기한]", 14) + "[연체일수]");

		boolean hasOverdue = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		Set<String> printedSet = new HashSet<>();
		
		for (Rental r : RentalData.rentalList) {

			// 조건: 반납 안 됐고 && 반납기한이 있고 && 반납기한 < 오늘
			if ((r.getReturnDate() == null || r.getReturnDate().isEmpty()) && r.getReturnDeadline() != null
					&& !r.getReturnDeadline().isEmpty()) {

				LocalDate deadline = LocalDate.parse(r.getReturnDeadline(), formatter);

				if (deadline.isBefore(LocalDate.now())) {
					hasOverdue = true;
					
					// 고유 키 생성
					String uniqueKey = r.getBookId() + "-" + r.getMemberNumber() + "-" + r.getReturnDeadline();

					// 중복이면 건너뜀
					if (printedSet.contains(uniqueKey)) continue;
					printedSet.add(uniqueKey);


					Member member = RentalService.findMemberByNum(r.getMemberNumber());
					Book book = RentalService.findBookByNumber(r.getBookId());

					long overdueDays = ChronoUnit.DAYS.between(deadline, LocalDate.now());

					String memberName = (member != null) ? member.getMemberName() : "탈퇴회원";
					String memberTel = (member != null) ? member.getMemberPhone() : "-";
					String bookTitle = (book != null) ? 
							formatTitle(book.getTitle(), 15) : "삭제된도서";

					System.out.println(
					        padRightDisplayWidth(r.getMemberNumber(), 14) +
					        padRightDisplayWidth(memberName, 12) +
					        padRightDisplayWidth(memberTel, 17) +
					        padRightDisplayWidth(bookTitle, 28) +
					        padRightDisplayWidth(r.getReturnDeadline(), 16) +
					        overdueDays + "일");
					
				}
			}
		}

		if (!hasOverdue) {
			System.out.println("현재 연체된 회원이 없습니다.");
		}
	}

	/**
	 * 등급별 회원 목록을 출력합니다.
	 * 누적 대여 횟수를 기준으로 회원을 등급(씨앗, 새싹, 묘목, 나무, 숲)으로 분류합니다.
	 */
	public void showMembersByGrade() {

		// 1. 회원별 누적 대여 횟수 계산
		Map<String, Integer> rentalCountMap = new HashMap<>();
		for (Rental rental : RentalData.rentalList) {
			String memberNum = rental.getMemberNumber();
			rentalCountMap.put(memberNum, rentalCountMap.getOrDefault(memberNum, 0) + 1);
		}

		// 2. 등급별 회원 분류 (등급명 -> 회원 목록)
		Map<String, List<Member>> gradeMap = new LinkedHashMap<>();
		gradeMap.put("씨앗", new ArrayList<>());
		gradeMap.put("새싹", new ArrayList<>());
		gradeMap.put("묘목", new ArrayList<>());
		gradeMap.put("나무", new ArrayList<>());
		gradeMap.put("숲", new ArrayList<>());

		for (Member member : MemberData.memberDataList) {
			int count = rentalCountMap.getOrDefault(member.memberNumber, 0);
			String grade;

			if (count >= 50) {
				grade = "숲";
			} else if (count >= 30) {
				grade = "나무";
			} else if (count >= 15) {
				grade = "묘목";
			} else if (count >= 5) {
				grade = "새싹";
			} else {
				grade = "씨앗";
			}
			gradeMap.get(grade).add(member);
		}

		// 3. 출력 (등급 순서대로)
		System.out.println("===등급별 회원===\r\n");
		System.out.println(
				padRightDisplayWidth("[회원번호]", 13) +
				padRightDisplayWidth("[이름]", 9) +
				padRightDisplayWidth("[누적 대여 수]", 16) + "[등급]");
		
		for (String grade : gradeMap.keySet()) {
			List<Member> members = gradeMap.get(grade);
			for (Member m : members) {
				int count = rentalCountMap.getOrDefault(m.memberNumber, 0);
				String str = Integer.toString(count);
//				System.out.printf("%s\t%s\t\t%d\t%s\r\n", m.memberNumber, m.memberName, count, grade);
				System.out.println(
						padRightDisplayWidth(m.memberNumber, 13) +
						padRightDisplayWidth(m.memberName, 15) +
						padRightDisplayWidth(str, 11) + grade);
			}
		}

	}

	/**
	 * 현재 대여 중인 도서 목록을 출력합니다.
	 * 반납되지 않은 도서만 표시합니다.
	 * 삭제된 도서나 탈퇴한 회원도 예외 처리하여 출력합니다.
	 */
	public void showRentedBooks() {
		System.out.println("===대여 도서 목록===\r\n");
//		System.out.printf("%-10s %-25s %-10s %-10s %-10s %-12s %-12s\n", "도서번호", "제목", "저자", "출판사", "대여자", "대여일", "반납기한");
		System.out.println(
				padRightDisplayWidth("[도서번호]", 20) +
				padRightDisplayWidth("[제목]", 28) +
				padRightDisplayWidth("[저자]", 20) +
				padRightDisplayWidth("[출판사]", 15) +
				padRightDisplayWidth("[대여자]", 13) +
				padRightDisplayWidth("[대여일]", 13) + "[반납기한]");

		boolean hasRental = false;

		for (Rental rental : RentalData.rentalList) {

			// 반납되지 않은 도서만 표시
			if (rental.getReturnDate() == null || rental.getReturnDate().isEmpty()) {
				hasRental = true;
			}
			
			Book book = RentalService.findBookByNumber(rental.getBookId());
			Member member = RentalService.findMemberByNum(rental.getMemberNumber());

			String bookId = rental.getBookId();
			String title = book != null ?
					formatTitle(book.getTitle(), 15) : "삭제된 도서";
			String author = book != null ? 
					formatTitle(book.getAuthor(),10) : "-";
			String publisher = book != null ? 
					formatTitle(book.getPublisher(), 10) : "-";
			String memberName = member != null ? 
					member.getMemberName() : "탈퇴회원";
			String rentalDate = rental.getRentalDate();
			String returnDeadline = rental.getReturnDeadline() != null ? 
					rental.getReturnDeadline() : "-";

//			System.out.printf("%-8s %s%s%s%s%-12s %s\n", bookId, title, author, publisher, memberName, rentalDate, returnDeadline);
			System.out.println(
					padRightDisplayWidth(bookId, 12) +
					padRightDisplayWidth(title, 30) +
					padRightDisplayWidth(author, 21) +
					padRightDisplayWidth(publisher, 21) +
					padRightDisplayWidth(memberName, 11) +
					padRightDisplayWidth(rentalDate, 14) + returnDeadline);
			
			
		}
	}

	/**
	 * 연체 도서 목록을 출력합니다.
	 * 반납기한이 지났음에도 반납되지 않은 도서를 기준으로 연체 도서를 판별합니다.
	 * 탈퇴 회원이나 삭제된 도서도 예외 처리하여 출력합니다.
	 */
	public void showOverdueBooks() {
		System.out.println("연체 도서 목록");
		System.out.println();
		System.out.println(
				padRightDisplayWidth("[도서번호]", 19) +
				padRightDisplayWidth("[제목]", 25) +
				padRightDisplayWidth("[저자]", 16) +
				padRightDisplayWidth("[대여자]", 17) +
				padRightDisplayWidth("[연락처]", 18) +
				padRightDisplayWidth("[반납기한]", 14) + "[연체일수]");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		boolean found = false;
		Set<String> printedSet = new HashSet<>();
		for (Rental rental : RentalData.rentalList) {

			// 아직 반납되지 않았고 반납 기한이 존재해야 함
			if ((rental.getReturnDate() == null || rental.getReturnDate().isEmpty())
					&& rental.getReturnDeadline() != null && !rental.getReturnDeadline().isEmpty()) {

				LocalDate deadline = LocalDate.parse(rental.getReturnDeadline(), formatter);

				if (deadline.isBefore(LocalDate.now())) {
					found = true;
					
					// 고유 키 생성
					String uniqueKey = rental.getBookId() + "-" + rental.getMemberNumber() + "-" + rental.getReturnDeadline();

					// 중복이면 건너뜀
					if (printedSet.contains(uniqueKey)) continue;
					printedSet.add(uniqueKey);


					Book book = RentalService.findBookByNumber(rental.getBookId());
					Member member = RentalService.findMemberByNum(rental.getMemberNumber());

					String bookId = rental.getBookId();
					String title = book != null ? formatTitle(book.getTitle(), 15) : "삭제된도서";
					String author = book != null ? formatTitle(book.getAuthor(), 10) : "-";
					String memberName = member != null ? member.getMemberName() : "탈퇴회원";
					String memberPhone = member != null ? member.getMemberPhone() : "-";
					String deadlineStr = rental.getReturnDeadline();

					long overdueDays = ChronoUnit.DAYS.between(deadline, LocalDate.now());

					//System.out.printf("%-10s %-20s %-10s %-10s %-15s %-12s %d일\n", bookId, title, author, memberName,memberPhone, deadlineStr, overdueDays);
					System.out.println(
							padRightDisplayWidth(bookId, 12) +
							padRightDisplayWidth(title, 28) +
							padRightDisplayWidth(author, 21) +
							padRightDisplayWidth(memberName, 13) +
							padRightDisplayWidth(memberPhone, 21) +
							padRightDisplayWidth(deadlineStr, 17) + overdueDays);
				}
			}
		}

		if (!found) {
			System.out.println("현재 연체 중인 도서가 없습니다.");
		}
	}

	/**
	 * 제목 문자열을 최대 길이에 맞게 잘라서 반환합니다.
	 * 길이가 초과되면 "..."으로 말줄임 처리됩니다.
	 * 
	 * @param title			원래 제목 문자열
	 * @param maxLength		최대 허용 길이
	 * @return 자른 제목 문자열
	 */
	private String formatTitle(String title, int maxLength) {
		return title.length() > maxLength ? title.substring(0, maxLength - 3) + "..." : title;
	}
	
	/**
	 * 주어진 문자열을 지정한 총 표시 폭에 맞춰 오른쪽으로 공백을 채워 정렬합니다.
	 * 한글은 2칸, 영어 및 숫자는 1칸의 너비로 계산됩니다.
	 * 
	 * @param text			정렬한 문자열
	 * @param totalWidth	총 표시 폭
	 * @return	오른쪽 정렬된 문자열
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

		// padding
		while (displayWidth < totalWidth) {
			result.append(" ");
			displayWidth++;
		}

		return result.toString();
	}
	
	/**
	 * 주어진 문자가 한글인지 여부를 반환합니다.
	 * 
	 * @param c		검사할 문자
	 * @return	한글이면 trun, 아니면 false
	 */
	private static boolean isKorean(char c) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		return block == Character.UnicodeBlock.HANGUL_SYLLABLES 
				|| block == Character.UnicodeBlock.HANGUL_JAMO
				|| block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO;
	}

}
