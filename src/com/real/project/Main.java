package com.real.project;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// 필요한 모든 UI와 Service 클래스를 import 합니다.
import com.real.project.book.BookService;
import com.real.project.challenge.ChallengeService;
import com.real.project.statistics.AdminStatisticsService;
import com.real.project.data.Book;
import com.real.project.data.BookData;
import com.real.project.data.ChallengeData;
import com.real.project.data.Member;
import com.real.project.data.MemberData;
import com.real.project.data.MemberGradeData;
import com.real.project.data.RentalData;
import com.real.project.member.MemberService;
import com.real.project.recommend.RecommendationService;
import com.real.project.rental.RentalService;
import com.real.project.ui.BookAdminMenuUi;
import com.real.project.ui.BookMenuUi;
import com.real.project.ui.BookSearchMenuUi;
import com.real.project.ui.BookSortMenuUi;
import com.real.project.ui.CategoryMenuUi;
import com.real.project.ui.ChallengeUi;
import com.real.project.ui.FindIdUi;
import com.real.project.ui.FindPasswordUi;
import com.real.project.ui.LoginUi;
import com.real.project.ui.MainUi;
import com.real.project.ui.MemberAdminMenuUi;
import com.real.project.ui.MemberManagerMenuUi;
import com.real.project.ui.MemberMenuUi;
import com.real.project.ui.MemberSortMenuUi;
import com.real.project.ui.MyLibraryUi;
import com.real.project.ui.RegisterMemberUi;
import com.real.project.ui.RentalMenu;
import com.real.project.ui.SortMenuUi;
import com.real.project.ui.StatisticsMenuUi;

/**
 * 도서관 관리 프로그램의 메인 클래스입니다.
 * 프로그램의 시작점으로, 각종 데이터 파일을 로드하고
 * 전체적인 애플리케이션 흐름을 제어합니다.
 */
public class Main {

    // --- 사용할 모든 UI와 Service 객체를 클래스 상단에 미리 생성합니다. ---
    private final MainUi mainUi = new MainUi();
    private final BookService bookService = new BookService();
	private final MemberService memberService = new MemberService();
    private final RentalService rentalService = new RentalService(memberService);
	private final ChallengeService challengeService = new ChallengeService(memberService);
    private final RecommendationService recommendationService = new RecommendationService(); // [추가] MemberAdminMenuUi 객체 생성
    private final AdminStatisticsService adminStatisticsService = new AdminStatisticsService();
    private final RegisterMemberUi registerMemberUi = new RegisterMemberUi();
    
    private final LoginUi loginUi = new LoginUi();
    private final FindIdUi findIdUi = new FindIdUi();
    private final FindPasswordUi findPasswordUi = new FindPasswordUi();
    private final MemberMenuUi memberMenuUi = new MemberMenuUi();
    private final BookMenuUi bookMenuUi = new BookMenuUi();
    private final BookSearchMenuUi bookSearchMenuUi = new BookSearchMenuUi();
    private final CategoryMenuUi categoryMenuUi = new CategoryMenuUi();
    private final SortMenuUi sortMenuUi = new SortMenuUi();
    private final MyLibraryUi myLibraryUi = new MyLibraryUi();
    private final MemberAdminMenuUi mangerMenuUi = new MemberAdminMenuUi();
    private final BookAdminMenuUi bookAdminMenuUi = new BookAdminMenuUi(); // [추가]
    private final MemberAdminMenuUi memberAdminMenuUi = new MemberAdminMenuUi(); // [추가] MemberAdminMenuUi 객체 생성

    private final Scanner scan = new Scanner(System.in);

    /**
     * 프로그램의 주 실행 메서드(main method)입니다.
     * 애플리케이션 시작 시 필요한 데이터들을 로드하고, run() 메서드를 호출하여 프로그램을 실행합니다.
     * @param args 프로그램 실행 시 전달되는 인자 (현재 코드에서는 사용되지 않음)
     * @throws IOException 데이터 로드/저장 시 발생할 수 있는 입출력 예외
     */
    public static void main(String[] args) throws IOException {
        
    	MemberData.load();
        ChallengeData.load();
        MemberGradeData.load();
        BookData.load();
        RentalData.load();
        Main app = new Main();
        app.run();
        
    }

    
    /**
     * 프로그램의 초기 메인 루프를 실행합니다.
     * 사용자가 로그인, 회원가입, 아이디/비밀번호 찾기, 종료 등의 기능을 선택할 수 있는
     * 메인 메뉴를 반복해서 표시합니다.
     */
    public void run() {
        boolean loop = true;
        while (loop) {
            String sel = MainUi.mainmenu();
            
            if (sel.equals("1")) {
                memberService.logIn();

                if (memberService.getLoggedInUser() != null) {
                    if (memberService.getLoggedInUser().getMemberId().equals("admin")) {
                        runManagerMenu();
                    } else {
                        memberService.gradeInfo();
                        runMemberMenu();
                    }
                }
                MainUi.pause();

            } else if (sel.equals("2")) {
                memberService.registerMember();
                MainUi.pause();

            } else if (sel.equals("3")) {
                memberService.findId();
                MainUi.pause();

            } else if (sel.equals("4")) {
                memberService.findPw();
                MainUi.pause();

            } else if (sel.equals("0")) {
                loop = false;

            } else {
                mainUi.showMessage("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
        MemberData.save();
        mainUi.showMessage("✅ 프로그램을 종료합니다.");
    }
    
    //관리자 아이디로 로그인 한 후 보이는 메뉴탭
    private void runManagerMenu() { 
    	boolean isLoggedIn = true;
        while(isLoggedIn) {
            String sel = MemberAdminMenuUi.ManagerMenuScreen();

            if (sel.equals("1")) { // 도서 관리
            	runBookAdminMenu();
            	
            } else if (sel.equals("2")) {// 회원 관리
                runMemberAdminMenu(); 
                
            } else if (sel.equals("3")) { //통계 관리
            	runAdminStaticMenu();
            	
            } else if (sel.equals("0")) {// 로그아웃
                isLoggedIn = false; 
                
            } else {
                mainUi.showMessage("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
        mainUi.showMessage("✅ 관리자 계정에서 로그아웃 되었습니다.");
    }

	
    //관리자 - 통계
	private void runAdminStaticMenu() { 
		
	        while (true) {
	            String sel = StatisticsMenuUi.StatisticsScreen(); // UI 클래스 호출

	            if (sel.equals("1")) {   //관리자- 회원 목록 조회
	                runMemberSortMenu();
	                
	            } else if (sel.equals("2")) { //관리자 도서 관리 조회
	                runBookSortMenu();

	            } else if (sel.equals("0")) {
	                // 0. 이전 메뉴로 돌아가기
	                System.out.println(">> 이전 메뉴로 돌아갑니다.");
	                return; // 메소드를 종료하여 이전 메뉴로 복귀

	            } else {
	                // 그 외 잘못된 입력
	                mainUi.showMessage("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
	            }
	        }
	    
		}
	
	//관리자- 도서관리
	private void runBookSortMenu() { 
		
		while (true) {
	        // 1. 아래에서 만들 UI 클래스를 호출해 메뉴를 보여줍니다.
	        String sel = BookSortMenuUi	.BookSortScreen();

	        if (sel.equals("1")) {   //대여중인 도서 출력
	          adminStatisticsService.showRentedBooks();

	        } else if (sel.equals("2")) { //연체중인 도서 출력
	            adminStatisticsService.showOverdueBooks();

	        } else if (sel.equals("0")) {
	            // 1-3. 이전 메뉴(통계 메뉴)로 돌아가기
	            System.out.println(">> 이전 통계 메뉴로 돌아갑니다.");
	            return; // 현재 메소드를 종료하고 호출한 곳으로 복귀

	        } else {
	            mainUi.showMessage("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
	        }
	    }
	}

	//관리자 - 통계 - 회원관리
	private void runMemberSortMenu() { 
		while (true) {
	        // 1. 아래에서 만들 UI 클래스를 호출해 메뉴를 보여줍니다.
	        String sel = MemberSortMenuUi.MemberSortScreen();

	        if (sel.equals("1")) { //관리자(회원관리)- 연체 회원 조회
	        	adminStatisticsService.showOverdueMembers(); 	        	
	        	MainUi.pause();
	        } else if (sel.equals("2")) { //관리자(회원관리)- 회원 등급조회
	        	adminStatisticsService.showMembersByGrade(); 
	        	MainUi.pause();
	        } else if (sel.equals("0")) { //이전 메뉴로 돌아가기

	        	System.out.println(">> 이전 통계 메뉴로 돌아갑니다.");
	            return; // 현재 메소드를 종료하고 호출한 곳으로 복귀

	        } else {
	            mainUi.showMessage("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
	        }
	    }
	}

	//관리자 전용- 회원 검색
	private void runMemberAdminMenu() { 
		boolean loop = true;
        while (loop) {
            // ChallengeUi의 static 메소드를 호출해 메뉴를 보여주고 입력을 받습니다.
            String sel = MemberManagerMenuUi.adminSearchrScreen(); // [수정] ChallengeUi 객체를 통해 호출해야 합니다.

            if (sel.equals("1")) { //관리자 - 회원검색
                memberService.searchMember();
                MainUi.pause();
            } else if (sel.equals("0")) {
                loop = false;
            } else {
                mainUi.showMessage("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
        mainUi.showMessage("✅ 뒤로 돌아갑니다.");
    }		
	
	
	//관리자 전용- 도서관리
	private void runBookAdminMenu() { 
		boolean loop = true;
        while(loop) {
            String sel = bookAdminMenuUi.BookAdminScreen();

            if (sel.equals("1")) {   //관리자 도서 등록
            	bookService.addBookFromUserInput();
                MainUi.pause();

            } else if (sel.equals("2")) {//2. 기존 도서 삭제

                bookService.viewAllBooks(); //도서 삭제 전 전체 도서 보여줌
                System.out.print("\n>> 삭제할 도서의 번호를 입력하세요: ");
                String bookNumber = scan.nextLine();
                bookService.deleteBook(bookNumber);
                MainUi.pause();

            } else if (sel.equals("3")) { //도서 수정
            	bookService.updateBook();
                MainUi.pause();

            } else if (sel.equals("4")) { //도서 검색
            	runBookSearchMenu();
                MainUi.pause();

            } else if (sel.equals("5")) { //전체 도서 보기
                // --- 5. 전체 도서 목록 보기 ---
                bookService.viewAllBooks();
                MainUi.pause();

            } else if (sel.equals("0")) {
                loop = false;
            } else {
                mainUi.showMessage("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    
		
	}

	
    /**
     * 일반 회원으로 로그인한 후의 사용자 메뉴를 실행합니다.
     * 도서 검색/대여, 도서 추천, 내 서재, 챌린지, 로그아웃 등의 기능을 제공합니다.
     */
    public void runMemberMenu() {
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            String sel = MemberMenuUi.MemberMenuScreen();
    
            if (sel.equals("1")) { //도서 검색 및 대여
                runBookMenu();
            } else if (sel.equals("2")) { //도서 추천
                runRecommend(); 
            } else if (sel.equals("3")) { //내 서재(반납)
                runMyLibraryMenu();
            } else if (sel.equals("4")) { //챌린지
            	runChallengeMenu();
            } else if (sel.equals("0")) {
                isLoggedIn = false; 
            } else {
                memberMenuUi.showMessage("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
        memberMenuUi.showMessage("✅ 로그아웃 되었습니다. 메인 화면으로 돌아갑니다.");
    }

    //도서 검색 및 대여
    private void runBookMenu() {
    	boolean loop = true;
        while (loop) {
            String sel = BookMenuUi.BookMenuScreen(); 
            
            if (sel.equals("1")) { //1.도서 검색
                // 1. 도서 검색
                runBookSearchMenu();
            } else if (sel.equals("2")) { // 2. 도서 대여
				rentalService.rentBook();
				
            } else if (sel.equals("3")) { // 3. 도서 반납
                rentalService.returnBook();
                
            } else if (sel.equals("0")) {                
                loop = false;
                
            } else {
            	System.out.println("❗️ 잘못된 입력입니다.");
            }
        }
    }
     //내 서재 
      private void runMyLibraryMenu() {
	        boolean loop = true;
	        while(loop) {
	            String sel = MyLibraryUi.MyLibraryScreen(); //

	            if (sel.equals("1")) { //대여했던 도서 보기
	                rentalService.showReturnedBooks();
	                MainUi.pause();
	            } else if (sel.equals("2")) { //대여중인 도서 보기
	            	runRentingBookActionMenu();
	            	
	            } else if (sel.equals("0")) {
	                loop = false;
	            } else {
	                mainUi.showMessage("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
	            }
	        }
	    }

      //챌린지
      private void runChallengeMenu() {
          boolean loop = true;
          while (loop) {

        	  String sel = ChallengeUi.ChallengeScreen();

              if (sel.equals("1")) { //챌린지 목표 설정
                  challengeService.setGoal(); 
                  MainUi.pause();

              } else if (sel.equals("2")) {
                  challengeService.viewProgress(); //도전중인챌린지 내역 보기
                  MainUi.pause();

              } else if (sel.equals("3")) {
                  challengeService.viewEndedChallenges(); //도전중인챌린지 내역 보기 
                  MainUi.pause();
              } else if (sel.equals("0")) {
                  loop = false;
              } else {
                  mainUi.showMessage("❌ 잘못된 입력입니다. 다시 선택해주세요.");
              }
          }
      
      }

    
    //대여 중인 도서 관리 메뉴 (반납/연장)
    private void runRentingBookActionMenu() {
    	
    	 boolean hasRentingBooks = rentalService.showRentingBook();

    	    // 2. 만약 대여중인 책이 없다면, 메뉴를 보여주지 않고 바로 메소드를 종료합니다.
    	    if (!hasRentingBooks) {
    	        // Ui.pause(); 와 같은 일시정지 기능 후 종료하면 더 자연스럽습니다.
    	        return;
    	    }
    	 boolean loop = true;
         while(loop) {
        	 
            String sel = RentalMenu.RentalBookScreen();
            
      		if (sel.equals("1")) {//도서 반납
                 rentalService.returnBook(); 
                 MainUi.pause();
             } else if (sel.equals("2")) {//도서 연장
                 rentalService.extendDueDate(); 
                 MainUi.pause();
             } else if (sel.equals("0")) {
                 loop = false;
                 
             } else {
                 mainUi.showMessage("❗️ 잘못된 입력입니다.");
             }
        }
    }
   
    //도서 검색 

	private void runBookSearchMenu() {
		boolean loop = true;
		while (loop) {
			String searchType = bookSearchMenuUi.BookSearchScreen(); // bookservice에서 선언한 searchType(1,2,3,4등 번호)를 변수에
																		// 저장
			String keyword = "";

			if (searchType.equals("1") || searchType.equals("2") || searchType.equals("3") || searchType.equals("4")) {
				System.out.print(">> ✅ 검색할 키워드를 입력하세요: ");
				keyword = scan.nextLine(); // 사용자에게 키워드 입력 받기

				List<Book> results = bookService.searchBooks(searchType, keyword);// 사용자가 입력한 searchType과 keyword를 그대로
																					// 전달 후 result 변수에 저장
				bookService.printSearchedBooks(results); // 방금 사용자가 받은 result 변수에 저장 된 값을 printSearchedBooks 메서드로 출력

				if (!results.isEmpty()) {
					MainUi.pause(); // ❗ 추가: 검색 결과를 확인하도록 잠시 멈춤
				}

			} else if (searchType.equals("0")) {
				loop = false;

			} else {
				System.out.println("❗️ 잘못된 입력입니다. 다시 선택해주세요.");
			}
		}
	}

    //분야별 추천 도서
     private void runRecommend() {
    	while (true) {
    		
            String selectedCategory = categoryMenuUi.CategoryMenuScreen();

            if (selectedCategory.equals("EXIT")) break; // 0번 입력 시 루프 종료
            if (selectedCategory.equals("INVALID")) {
                System.out.println("❗️ 잘못된 번호입니다. 다시 입력해주세요.");
                continue; // 루프 처음으로
            }

            // 2. 정렬 기준 UI를 보여주고 선택값을 받음
            String sortInput = SortMenuUi.SortMenuScreen(); //분야 선택 후 별점순/대여순으로 선택 받음
            if (sortInput.equals("0")) {
                continue; // 카테고리 선택으로 돌아가기
            }
            recommendationService.setCategory(selectedCategory);

            if (sortInput.equals("1")) { //별점순으로 추천 도서 출력
            	recommendationService.setCategory(selectedCategory); //사용자가 선택한 카테고리를 저장
                recommendationService.recommendByRating(); 
                MainUi.pause();
            } else if (sortInput.equals("2")) {//대여 많은 순으로 추천 도서 출력
                recommendationService.recommendByRentalCount();
                MainUi.pause();
            } else {
                 System.out.println("❗️ 잘못된 번호입니다. 다시 입력해주세요.");
                 continue; // 루프 처음으로 (카테고리 선택부터)
            }
               
        }
        System.out.println(">> 이전 메뉴로 돌아갑니다.");
    }
}