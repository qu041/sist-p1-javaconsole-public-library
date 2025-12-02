package com.real.project.ui;

import java.util.Scanner;
/**
 * 현재 대여 중인 도서에 대한 '반납 및 연장' 메뉴 UI를 담당하는 클래스입니다.
 */
public class RentalMenu {
    private static Scanner scan = new Scanner(System.in);
    /**
     * '반납 및 연장' 메뉴 화면을 출력하고 사용자의 입력을 받습니다.
     * @return 사용자가 선택한 메뉴 번호 (String)
     */
    public static String RentalBookScreen() {
    	System.out.println();
    	System.out.println("""
    	        ╔═══════════════════════════ 반납 및 연장 ═══════════════════════════╗
    	        ║                                                                    ║
    	        ║                            1. 도서 반납                            ║
    	        ║                            2. 도서 연장                            ║
    	        ║                                                                    ║
    	        ║                            0. 이전 메뉴                            ║
    	        ║                                                                    ║
    	        ╚════════════════════════════════════════════════════════════════════╝
    	        """);
        System.out.print(">> 메뉴 선택: ");
        return scan.nextLine();
    }
}
