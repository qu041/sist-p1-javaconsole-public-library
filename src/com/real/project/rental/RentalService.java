package com.real.project.rental;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

import com.real.project.data.Book;
import com.real.project.data.BookData;
import com.real.project.data.Member;
import com.real.project.data.MemberData;
import com.real.project.data.MemberGrade;
import com.real.project.data.MemberGradeData;
import com.real.project.data.Rental;
import com.real.project.data.RentalData;
import com.real.project.member.MemberService;


/**
 * <b>RentalService</b><br>
 * <br>
 * 도서관 시스템에서 도서 대여/반납/연장, 대여내역(내 서재) 조회 등<br>
 * 대여 관련 모든 비즈니스 로직을 담당하는 서비스 클래스입니다.<br>
 * <ul>
 *   <li>도서 대여/반납, 연장, 별점 평가 기능</li>
 *   <li>회원의 등급별 대여 한도/연장 한도 관리</li>
 *   <li>대여 내역 및 반납 이력(내 서재) 조회</li>
 *   <li>대여 중복/연체 체크, 각종 데이터 유효성 검사 등</li>
 * </ul>
 * <p>
 * 본 클래스는 {@link MemberService}를 생성자 주입받아, 로그인 상태에 따라<br>
 * 회원별 대여/반납 서비스를 제공합니다.<br>
 * </p>
 */
public class RentalService {

	Scanner scan = new Scanner(System.in);
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private MemberService memberService;
	
	/**
	 * RentakService 생성자
	 * @param memberService 로그인 등 회원 관련 서비스를 처리하는 객체
	 */

	public RentalService(MemberService memberService) {
		this.memberService = memberService; // 주입받은 MemberService를 저장
		this.scan = new Scanner(System.in);
	}

	// - 메인 메소드 -
	
	/**
	 * 도서 대여의 전체 과정을 처리합니다.
	 */
	// 책 대여
	public void rentBook() {
		
		Member currentUser = this.memberService.getLoggedInUser();
		String myId = currentUser.getMemberNumber();
		if (myId == null) {
			System.out.println("❌ 회원을 찾을 수 없습니다. 회원.csv 파일을 확인해주세요.");
			return;
		}

		// 대여전 현재 회원의 누적 대여권수를 미리 계산
		int beforeRentalCount = 0;
		for (Rental r : RentalData.rentalList) {
			if (r.getMemberNumber().equals(currentUser.getMemberNumber())) {
				beforeRentalCount++;
			}
		}

		// 1. 연체 상태인지 확인
		Rental overdueRental = findOverdueRental(currentUser.getMemberNumber()); // 테스트데이터
		if (overdueRental != null) {
			handOverdueUser(overdueRental);
			return;
		}

		if (!checkRentalLimit(currentUser)) {
			return;
		}

		// 2. 사용자에게 대여할 도서번호를 입력받고 책이 존재하고 대여가능한 상태인지 확인
		System.out.println("✅ 연체 내역이 없습니다. 도서 대여를 진행합니다.");
		System.out.print("대여할 도서번호 입력하세요:");
		String inputBookNumber = scan.nextLine();

		if(inputBookNumber == null || inputBookNumber.trim().isEmpty()) {
			System.out.println("❌ 도서번호가 입력되지 않았습니다.");
			return;
		}
		
		String normalizedinputBookNumber = inputBookNumber.toUpperCase();
		Book targetBook = findBookByNumber(normalizedinputBookNumber);
		
		if (targetBook == null) {
			System.out.println("❌ 존재하지 않는 도서번호입니다. 도서번호를 다시입력해주세요.");

			return;
		}

		if (!isBookAvailable(normalizedinputBookNumber)) {
			System.out.println("❌ 현재 다른 회원이 대여 중인 도서입니다.");
			return;
		}

		String rentalId;

		if (RentalData.rentalList.isEmpty()) {
			rentalId = "RENT0000000001";
		} else {
			// 마지막 대여 기록을 가져옴
			Rental lastRental = RentalData.rentalList.get(RentalData.rentalList.size() - 1);

			String lastIdNumberStr = lastRental.getRentalId().substring(4);
			int nextIdNumber = Integer.parseInt(lastIdNumberStr) + 1;
			rentalId = "RENT" + String.format("%010d", nextIdNumber);
		}

		// 3. 대여 기록 생성

		String memberNumber = currentUser.getMemberNumber(); 
		String rentalDate = LocalDate.now().format(formatter);
		String dueDate = LocalDate.now().plusDays(14).format(formatter); // 반납기한 계산

		Rental newRental = new Rental(rentalId, memberNumber, normalizedinputBookNumber, rentalDate, dueDate, "", "");

		// 4. 데이터 업데이트
		RentalData.rentalList.add(newRental);

		System.out.printf("✅ '%s' 도서대여가 완료되었습니다!\n (반납기한: %s)", targetBook.getTitle(), dueDate);

		// 5. 등급 상승 체크 및 메세지 출력
		int afterRentalCount = beforeRentalCount + 1;
		checkAndDisplayLevelUpMsg(currentUser, beforeRentalCount, afterRentalCount);

		RentalData.save();

	}

	// 내 서재
	/**
	 * '내 서재'메뉴를 표시하고, 사용자의 선택에 따라 대여중/반납완료 도서 목록을 보여줍니다.
	 */
	public void showMyRentals() {
		Member currentUser = this.memberService.getLoggedInUser();
		String myId = currentUser.getMemberNumber();

		String sel = scan.nextLine();
		if (sel.equals("1")) {
			showReturnedBooks();
		} else if (sel.equals("2")) {
			showRentingBook();
		} else {

		}

		System.out.println();

	}

	// 대여 중
	/**
	 * 현재 대여 중인 책 목록을 출력합니다. 
	 * @return 대여 중인 도서가 한권이라도 있으면 true, 없으면 false를 반환합니다.
	 */
	public boolean showRentingBook() {
		Member currentUser = this.memberService.getLoggedInUser();
		String myId = currentUser.getMemberNumber();

		System.out.println();
		System.out.println("\n<대여중인 책>");
		System.out.println("=============================================================================");
		System.out.println(padRightDisplayWidth("No", 4) + padRightDisplayWidth("도서번호", 12)
				+ padRightDisplayWidth("도서명", 28) + padRightDisplayWidth("저자", 21) + "반납예정일");
		System.out.println("-----------------------------------------------------------------------------"); // ui부분

		int count = 1;
		boolean hasBooks = false;
		for (Rental r : RentalData.rentalList) {
			if (r.getMemberNumber().equals(myId) && r.getReturnDate().isEmpty()) {
				hasBooks = true;
				Book book = findBookByNumber(r.getBookId());

				if (book != null) {
					System.out.println(padRightDisplayWidth(String.valueOf(count++), 4)
							+ padRightDisplayWidth(r.getBookId(), 12) + padRightDisplayWidth(book.getTitle(), 28)
							+ padRightDisplayWidth(book.getAuthor(), 21) + r.getReturnDeadline());
				} else {
					// 도서.csv에 책 정보가 없을 경우를 대비
					System.out.println(padRightDisplayWidth(String.valueOf(count++), 4)
							+ padRightDisplayWidth("[삭제된 도서]", 12) + padRightDisplayWidth("-", 28)
							+ padRightDisplayWidth("-", 21) + r.getReturnDeadline());
				}
			}
 
		}

		if (!hasBooks)
			System.out.println("현재 대여중인 도서가 없습니다.");
		return hasBooks;
	}

	// 대여 했던 도서
	/**
	 * 과거에 대여했던 (반납 완료한) 책 목록을 출력합니다.
	 */
	public void showReturnedBooks() {
		Member currentUser = this.memberService.getLoggedInUser();
		String memberId = currentUser.getMemberNumber();

		System.out.println("\n<대여했던 책>");
		System.out.println(
				"=========================================================================================================");
		System.out.println(padRightDisplayWidth("No", 4) + padRightDisplayWidth("도서번호", 12)
				+ padRightDisplayWidth("도서명", 28) + padRightDisplayWidth("저자", 21) + padRightDisplayWidth("대여일", 15)
				+ padRightDisplayWidth("반납일", 15) + "나의 별점");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------");

		int count = 1;
		boolean hasBooks = false;

		for (Rental r : RentalData.rentalList) {
			// 내 아이디에 반납일이 비어야함
			if (r.getMemberNumber().equals(memberId) && r.getReturnDate() != null && !r.getReturnDate().isEmpty()) {
				hasBooks = true;

				Book book = findBookByNumber(r.getBookId());// bookdata에서 책 정보 검색

				if (book != null) {
					System.out.println(padRightDisplayWidth(String.valueOf(count++), 4)
							+ padRightDisplayWidth(r.getBookId(), 12)
							+ padRightDisplayWidth(formatTitle(book.getTitle(), 15), 28)
							+ padRightDisplayWidth(formatTitle(book.getAuthor(), 10), 21)
							+ padRightDisplayWidth(r.getRentalDate(), 15) + padRightDisplayWidth(r.getReturnDate(), 15)
							+ convertScoreToStars(r.getStarRating()));
				} else {
					System.out.println(padRightDisplayWidth(String.valueOf(count++), 4)
							+ padRightDisplayWidth("[삭제된 도서]", 12) + padRightDisplayWidth("-", 28)
							+ padRightDisplayWidth("-", 21) + padRightDisplayWidth(r.getRentalDate(), 15)
							+ padRightDisplayWidth(r.getReturnDate(), 15) + convertScoreToStars(r.getStarRating()));
				}
			}
		}

		if (!hasBooks)
			System.out.println("과거 대여 이력이 없습니다.");
		// Ui.pause;

	}
	
	// 책 반납 고침
	/**
	 * 도서 반납의 전체 과정을 처리합니다.
	 */
	public void returnBook() {

		Member currentUser = this.memberService.getLoggedInUser();

		Scanner scan = new Scanner(System.in);
		boolean hasRental = false;
		String memberNumber = currentUser.getMemberNumber();

		for (Rental rental : RentalData.rentalList) {
			if (rental.getMemberNumber().equals(memberNumber)
					&& (rental.getReturnDate() == null || rental.getReturnDate().isEmpty())) {
				hasRental = true;
				break;
			}
		}

		if (!hasRental) {
			System.out.println("대여 중인 도서가 없습니다.");
			return;
		}

	
		System.out.print("반납할 도서번호를 입력하세요: ");
		String inputBookId = scan.nextLine();
		
		String normalizedinputBookNumber = inputBookId.toUpperCase();

		for (Rental rental : RentalData.rentalList) {
			if (rental.getMemberNumber().equals(memberNumber) && rental.getBookId().equals(normalizedinputBookNumber)
					&& (rental.getReturnDate() == null || rental.getReturnDate().isEmpty())) {

				// 오늘 날짜를 반납일로 설정
				String today = LocalDate.now().format(formatter);
				rental.setReturnDate(today);

				// 도서 이름 가져오기
				String bookTitle = getBookTitleById(inputBookId);

				// 연체일수 계산
				int overdueDays = 0;
				String returnDeadlineStr = rental.getReturnDeadline();

				if (returnDeadlineStr != null && !returnDeadlineStr.isEmpty()) {
					try {
						LocalDate returnDeadline = LocalDate.parse(returnDeadlineStr, formatter);
						LocalDate currentDate = LocalDate.now();
						long daysOverdue = ChronoUnit.DAYS.between(returnDeadline, currentDate);

						if (daysOverdue > 0) {
							overdueDays = (int) daysOverdue;
						}
					} catch (DateTimeParseException e) {
						// 날짜 형식 오류일 경우 연체일수는 0으로 유지
					}
				}

				// 결과 출력
				if (overdueDays > 0) {
					System.out.printf("[%s] 해당 도서는 연체되었습니다.\n", bookTitle);
					System.out.printf("%d일 동안 대여가 제한됩니다.\n", overdueDays);
				} else {
					System.out.println("반납이 완료되었습니다.");
				}

				// 별점 입력 받기
				int star = 0;
				while (true) {
					System.out.print("이 도서에 대한 별점을 1~10 사이로 입력하세요: ");
					try {
						star = Integer.parseInt(scan.nextLine());
						if (star >= 1 && star <= 10) {
							rental.setStarRating(String.valueOf(star));
							break;
						} else {
							System.out.println("별점은 1부터 10 사이로 입력해야 합니다.");
						}
					} catch (NumberFormatException e) {
						System.out.println("숫자를 입력해주세요.");
					}
				}

				RentalData.save();
				return;
			}
		}

		System.out.println("해당 도서를 찾을 수 없습니다.");

	}

	// 책 연장 고침
	/**
	 * 대여 기간 연장의 전체 과정을 처리합니다
	 */
	public void extendDueDate() {

		Scanner scan = new Scanner(System.in);
		Member currentUser = this.memberService.getLoggedInUser();
		String memberNumber = currentUser.getMemberNumber();

		// 1. 회원의 누적 대여 권수 계산
		int totalRentals = 0;
		for (Rental rental : RentalData.rentalList) {
			if (rental.getMemberNumber().equals(memberNumber)) {
				totalRentals++;
			}
		}

		// 2. 누적 대여 권수에 따라 연장 가능 횟수 계산
		int extensionLimit = 0;
		if (totalRentals >= 15 && totalRentals < 50) {
			extensionLimit = 1;
		} else if (totalRentals >= 50) {
			extensionLimit = 2;
		}

		// 3. 회원이 현재 연장 가능한 대여 건이 있는지 확인
		boolean found = false;

		for (Rental rental : RentalData.rentalList) {
			if (rental.getMemberNumber().equals(memberNumber)
					&& (rental.getReturnDate() == null || rental.getReturnDate().isEmpty())) {
				found = true;
				break;
			}
		}

		if (!found) {
			System.out.println("현재 연장 가능한 도서가 없습니다.");
			return;
		}

		// 4. 연장할 도서 선택
		System.out.print("연장할 도서번호를 입력하세요: ");
		String inputBookId = scan.nextLine();

		// 연장 대상 찾기
		for (Rental rental : RentalData.rentalList) {
			if (rental.getMemberNumber().equals(memberNumber) && rental.getBookId().equals(inputBookId)
					&& (rental.getReturnDate() == null || rental.getReturnDate().isEmpty())) {

				String returnDeadlineStr = rental.getReturnDeadline();
				String rentalDateStr = rental.getRentalDate();

				try {

					if (rentalDateStr == null || returnDeadlineStr == null || rentalDateStr.isEmpty()
							|| returnDeadlineStr.isEmpty()) {
						System.out.println("대여일 또는 반납기한 정보가 없습니다.");
						return;
					}

					// 연체일 계산
					LocalDate returnDeadline = LocalDate.parse(returnDeadlineStr, formatter);
					LocalDate today = LocalDate.now();

					long overdueDays = ChronoUnit.DAYS.between(returnDeadline, today);
					if (overdueDays > 0) {
						System.out.println("연체 중인 도서는 연장할 수 없습니다.");
						return;
					}

				} catch (DateTimeParseException e) {
					System.out.println("반납기한 날짜 형식 오류로 연장할 수 없습니다.");
					return;
				}

				try {

					LocalDate rentalDate = LocalDate.parse(rentalDateStr, formatter);
					LocalDate returnDeadline = LocalDate.parse(returnDeadlineStr, formatter);

					// 사용한 연장 횟수 계산
					long totalDays = ChronoUnit.DAYS.between(rentalDate, returnDeadline);
					long extensionDays = totalDays - 14;
					if (extensionDays < 0)
						extensionDays = 0;
					int usedExtensions = (int) (extensionDays / 7);

					if (usedExtensions >= extensionLimit) {
						System.out.println("더 이상 연장할 수 없습니다.");
						return;
					}

					// 연장 처리
					LocalDate newDeadline = returnDeadline.plusDays(7);
					rental.setReturnDeadline(newDeadline.format(formatter));
					System.out.println("대여 기간이 7일 연장되었습니다. 새로운 반납 날짜는 " + newDeadline.format(formatter) + "입니다.");
					System.out.println("(남은 연장 가능 횟수: " + (extensionLimit - usedExtensions - 1) + "회)");

					RentalData.save();

				} catch (Exception e) {
					System.out.println("날짜 형식 오류로 연장할 수 없습니다.");
				}
				return;
			}
		}

		System.out.println("해당 도서를 찾을 수 없거나 연장할 수 없습니다.");

	}

	// 부가적인 메소드
	
	/**
	 * 회원의 등급에 따른 최대 대여 가능 권수를 계산합니다. 
	 * @param totalRentalCount 총 누적 대여 횟수
	 * @return 최대 대여 가능 권수
	 */
	private int getMaxRentalLimit(int totalRentalCount) {
		// 1. 누적 대여량으로 등급 정보 찾기
		MemberGrade grade = MemberGradeData.findGradeByCount(totalRentalCount);

		// 2. 기본 3권 + 등급 보너스로 최종 대여 한도 결정
		int baseRentalLimit = 3;
		int bonusBooks = Integer.parseInt(grade.getRentalBonusCount());

		return baseRentalLimit + bonusBooks;
	}

	// 등급 레벨업시
	/**
	 * 대여 전후의 횟수를 비교하여 등급 상승 시 축하 메세지를 출력합니다.
	 * @param currentUser 현재 로그인한 회원 객체
	 * @param beforeRentalCount	대여 전 총 누적 대여 횟수
	 * @param afterRentalCount	대여 후 총 누적 대여 횟수
	 */
	private void checkAndDisplayLevelUpMsg(Member currentUser, int beforeRentalCount, int afterRentalCount ) {
		MemberGrade beforeGrade = MemberGradeData.findGradeByCount(beforeRentalCount);
		MemberGrade afterGrade = MemberGradeData.findGradeByCount(afterRentalCount);
		
		if (!beforeGrade.getGradeName().equals(afterGrade.getGradeName())) {

			String newGradeName = afterGrade.getGradeName();

			System.out.println("\n\n===============================================");
			System.out.printf("🎉  %s 님의 등급 상승! [%s] -> [%s] 🎉\n",currentUser.getMemberName(),beforeGrade.getGradeName(), newGradeName);
			System.out.println("---------------------------------------------------");
			
			// 새로 달성항 등급에 따라 다른 축하 메세지를 출력
			switch (newGradeName) {
			case "새싹":
				System.out.println("🌱 새싹 등급 달성을 축하합니다! 꾸준한 독서의 시작을 응원합니다.");
				break;
			case "묘목":
				System.out.println("🌿 묘목 등급으로 성장하셨습니다! 독서가 즐거운 습관으로 자리 잡고 있네요. 멋진 발전입니다.");
				break;
			case "나무":
				System.out.println("🌳 나무 등급이 되신 것을 축하합니다! 이제 회원님은 자신만의 독서 세계를 가진 단단한 독서가입니다.\n");
				break;
			case "숲":
				System.out.println("🌲🌲 최고 등급인 숲 등급에 오르셨습니다! 회원님의 깊이 있는 독서 경험이 다른 분들에게 좋은 길잡이가 될 것입니다. 진심으로 축하드립니다.");
				break;
			}

			System.out.println("\n[새로운 등급 혜택");
			System.out.printf("🎁 이제 최대 %d권까지 대여할 수 있습니다!\n", getMaxRentalLimit(afterRentalCount));
			
			int newMaxExtensions = 0;
			String extensionCountStr = afterGrade.getExtensionBonusCount();
			if(extensionCountStr != null && ! extensionCountStr.isEmpty()) {
				newMaxExtensions = Integer.parseInt(extensionCountStr);
				
				System.out.printf("🎁 최대 연장 %d회 가능합니다.\n", newMaxExtensions);
			}

		}

	}
	
	/**
	 * 숫자 점수를 별점 문자열로 변환합니다.
	 * @param starRating "1" ~ "10" 사이의 점수 문자열
	 * @return "★", "☆"로 변환된 별점 문자열
	 */
	private String convertScoreToStars(String starRating) {
		if (starRating == null || starRating.isEmpty()) {
			return "";
		}
		try {

			int numScore = Integer.parseInt(starRating);

			StringBuilder stars = new StringBuilder();

			int fullStars = numScore / 2; // 2점당 꽉 찬 별(★) 1개
			int halfStars = numScore % 2; // 나머지가 1이면 반쪽 별(☆) 1개

			for (int i = 0; i < fullStars; i++) {
				stars.append("★");
			}

			if (halfStars == 1) {
				stars.append("☆");
			}

			return stars.toString();

		} catch (NumberFormatException e) {

			return "평가없음";

		}

	}
	
	/**
	 * 회원의 대여 한도를 확인합니다.
	 * @param currentUser 현재 로그인한 회원 객체
	 * @return 대여 가능하면 true, 불가능하면 false
	 */
	private boolean checkRentalLimit(Member currentUser) {

		// 1. 회원 총 누적 대여량 계산
		int totalRentalCount = 0;
		int maxRentalLimit = getMaxRentalLimit(totalRentalCount);

		for (Rental r : RentalData.rentalList) {
			if (r.getMemberNumber().equals(currentUser.getMemberNumber())) {
				totalRentalCount++;
			}
		}

		MemberGrade currentGrade = MemberGradeData.findGradeByCount(totalRentalCount);
		if (currentUser == null) {
			System.out.println("❌ 회원 등급 정보를 찾을 수 없습니다.");
			return false;
		}

		// 기본 3권 + 등급 보너스로 최종 대여 한도 결정
		int baseRentalLimit = 3;
		int bonusBooks = Integer.parseInt(currentGrade.getRentalBonusCount());
		maxRentalLimit = baseRentalLimit + bonusBooks;

		// 현재 대여중인 책(미반납)의 수 계산
		long currentlyRentingCount = RentalData.rentalList.stream()
				.filter(r -> r.getMemberNumber().equals(currentUser.getMemberNumber())
						&& (r.getReturnDate() == null || r.getReturnDate().isEmpty()))
				.count();

		if (currentlyRentingCount >= maxRentalLimit) {
			System.out.println("\n❌ 대여 한도 초과");
			System.out.println("--------------------------");
			System.out.printf("회원님은 [%s] 등급으로 최대 %d권까지 대여 가능합니다\n", currentGrade.getGradeName(), maxRentalLimit);
			System.out.printf("현재 %d권을 대여 중이므로 추가 대여가 불가능합니다\n", currentlyRentingCount);
			System.out.println("기존 도서를 반납 후 이용해주세요.");
			System.out.println("--------------------------");
			return false;// 대여불가
		}

		// 대여 한도 통과시
		System.out.printf("✅ 현재 %d권 대여중 (최대 %d권 가능)\n", currentlyRentingCount, maxRentalLimit);
		return true; // 대여 가능
	}
	
	/**
	 * 도서 번호로 BookData에서 책을 찾아 그 제목을 반환합니다
	 * @param inputBookId 찾고자 하는 도서의 고유 번호
	 * @return 	찾은 도서의 제목, 만약 도서를 찾지 못하면 "(도서 정보 없음)"을 반환합니다.
	 */
	public String getBookTitleById(String inputBookId) {
		for (Book book : BookData.booklist) {
			if (book.getBookNumber().equals(inputBookId)) {
				return book.getTitle();
			}
		}
		return "(도서 정보 없음)";
	}
	
	/**
	 * 도서 번호로 BookData에서 해당 Book 객체를 찾습니다.
	 * @param bookNumber 찾은 도서의 고유 번호
	 * @return 찾은 Book 객체 (없으면 null)
	 */
	public static Book findBookByNumber(String bookNumber) {
		for (Book b : BookData.booklist) {
			if (b.getBookNumber().equals(bookNumber)) {
				return b;
			}else {
			}
		}
		return null;
	}
	
	/**
	 * 회원 번호로 MemberData에서 해당 Member 객체를 찾습니다. 
	 * @param memberNumber 찾은 회원의 고유 번호
	 * @return 찾은 Member 객체 (없으면 null)
	 */
	public static Member findMemberByNum(String memberNumber) {
		for (Member m : MemberData.memberDataList) {
			if (m.getMemberNumber().equals(memberNumber)) {
				return m;
			}
		}
		return null;
	}

	/**
	 * 특정 도서가 현재 대여 가능한 상태인지 확인합니다.
	 * @param nomalizedinputBookNumber 확인할 도서의 소문자상관없는 고유 번호
	 * @return 대여가능하면 true, 불가능하면 false
	 */
	private boolean isBookAvailable(String nomalizedinputBookNumber) {
		for (Rental rental : RentalData.rentalList) {
			if (rental.getBookId().equals(nomalizedinputBookNumber) && rental.getReturnDate().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 특정 회원의 현재 연체된 대여 기록을 찾습니다.
	 * @param memberId 확인할 회원의 Id
	 * @return 연체된 Rental 객체 (없으면 null)
	 */
	private Rental findOverdueRental(String memberId) {
		for (Rental r : RentalData.rentalList) {
			if (r.getMemberNumber().equals(memberId) && r.getReturnDate().isEmpty()
					&& LocalDate.parse(r.getReturnDeadline(), formatter).isBefore(LocalDate.now())) {
				return r;
			}
		}
		return null;
	}

	/**
	 * 연체된 사용자에게 대여 불가 안내 메세지를 출력합니다.
	 * @param overdueRental 연체된 대여 정보 객체
	 */
	private void handOverdueUser(Rental overdueRental) {
		Book overdueBook = findBookByNumber(overdueRental.getBookId());
		if (overdueBook == null) {
			System.out.printf("❌ 연체 기록은 있으나, 해당 도서(번호: %s)의 정보를 찾을 수 없습니다.\n", overdueRental.getBookId());
			return;
		}

		// 필요한 날짜 정보
		LocalDate today = LocalDate.now();
		LocalDate dueDate = LocalDate.parse(overdueRental.getReturnDeadline(), formatter);

		// 현재 연체 일수 계산
		long overdueDays = ChronoUnit.DAYS.between(dueDate, today);

		// 예상 대여 가능일 계산
		// '오늘'날짜에 '연체된 일수'만큼 더해서 계산하기
		LocalDate availableDate = today.plusDays(overdueDays);
		String formattedAvailableDate = availableDate.format(formatter);

		System.out.println("====================================================================");
		System.out.println("  ❌ 대여 불가 안내 ❌");
		System.out.printf("  연체 도서: %s (반납 예정일: %s)\n", overdueBook.getTitle(), overdueRental.getReturnDeadline());
		System.out.printf("  현재 %d일 연체되어 대여가 불가능합니다.\n", overdueDays);
		System.out.printf("  지금 반납 시, %s 이후부터 대여 가능합니다.\n", formattedAvailableDate);
		System.out.println("====================================================================");
		// Ui.pause();
		return;

	}

	// 생략
	private String formatTitle(String title, int maxLength) {
		return title.length() > maxLength ? title.substring(0, maxLength - 3) + "..." : title;
	}

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

	private static boolean isKorean(char c) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		return block == Character.UnicodeBlock.HANGUL_SYLLABLES || block == Character.UnicodeBlock.HANGUL_JAMO
				|| block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO;
	}

}