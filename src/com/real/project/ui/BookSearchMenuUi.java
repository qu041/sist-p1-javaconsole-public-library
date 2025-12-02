package com.real.project.ui;

import java.util.Scanner;
/**
 * '도서 검색'의 상세 옵션 메뉴 UI를 담당하는 클래스입니다.
 */
public class BookSearchMenuUi {

    private static Scanner scan = new Scanner(System.in);

    /**
     * '도서 검색 상세 메뉴' UI를 화면에 보여주고, 사용자의 입력을 받아서 반환합니다.
     * @return 사용자가 선택한 메뉴 번호 (String)
     */
    public String BookSearchScreen() {
    	System.out.println();
    	System.out.println("""
╔═════════════════════════════ 도서검색 ═════════════════════════════╗
║                                                                    ║
║                          1. 제목으로 검색                          ║
║                          2. 저자로 검색                            ║
║                          3. 출판사로 검색                          ║
║                          4. 분야로 검색                            ║
║                                                                    ║
║                            0. 이전 메뉴                            ║
║                                                                    ║
╚════════════════════════════════════════════════════════════════════╝
    			""");
    			
        System.out.print(">> 메뉴 선택: ");
        return scan.nextLine();
    }
}
