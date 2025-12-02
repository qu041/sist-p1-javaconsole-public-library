package com.real.project.ui;

import java.util.Scanner;

/**
 * 관리자용 '도서 관리' 기능의 메뉴 UI를 담당하는 클래스입니다.
 */
public class BookAdminMenuUi {

    private Scanner scan = new Scanner(System.in);

    /**
     * 관리자용 '도서 관리' 메뉴 UI를 화면에 보여주고, 사용자의 입력을 받아서 반환합니다.
     * @return 사용자가 선택한 메뉴 번호 (String)
     */
    public String BookAdminScreen() {
        System.out.println();
        System.out.println("""
    ╔═════════════════════════════ 도서관리 ═════════════════════════════╗
    ║                                                                    ║
    ║                         1. 신규 도서 등록                          ║
    ║                         2. 기존 도서 삭제                          ║
    ║                         3. 도서 정보 수정                          ║
    ║                         4. 도서 상세 검색                          ║
    ║                         5. 전체 도서 목록 보기                     ║
    ║                                                                    ║
    ║                            0. 이전 메뉴                            ║
    ║                                                                    ║
    ╚════════════════════════════════════════════════════════════════════╝
    			""");
        System.out.print(">> 메뉴 선택: ");
        return scan.nextLine();
    }
}
