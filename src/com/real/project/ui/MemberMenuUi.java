package com.real.project.ui;

import java.util.Scanner;
/**
 * 일반 회원 로그인 후 표시되는 메인 메뉴의 UI를 담당하는 클래스입니다.
 */
public class MemberMenuUi {
    private static Scanner scan = new Scanner(System.in);
    
    /**
     * '회원 메뉴' 화면을 출력하고 사용자의 입력을 받습니다.
     * 도서 검색/대여, 도서 추천, 내 서재, 챌린지, 로그아웃 옵션을 제공합니다.
     * @return 사용자가 선택한 메뉴 번호 (String)
     */
    public static String MemberMenuScreen() {
        System.out.println();
        System.out.println("""
            ╔═════════════════════════════ 회원메뉴 ═════════════════════════════╗
            ║                                                                    ║
            ║                        1. 도서 검색 및 대여                        ║
            ║                        2. 도서 추천                                ║
            ║                        3. 내 서재(반납/연장)                       ║
            ║                        4. 챌린지 모드                              ║
            ║                                                                    ║
            ║                            0. 로그아웃                             ║
            ║                                                                    ║
            ╚════════════════════════════════════════════════════════════════════╝
            """);
            
        System.out.print(">> 메뉴 선택: ");
        return scan.nextLine();
    }

    public void showMessage(String message) {
        System.out.println("\n" + message);
    }
}