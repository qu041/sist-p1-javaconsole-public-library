package com.real.project.ui;

import java.util.Scanner;


/**
 * 관리자 통계 메뉴 중, 도서 관련 통계(대여 및 연체 도서 조회) 메뉴 UI를 담당하는 클래스입니다.
 */

public class BookSortMenuUi {
  	
	private static Scanner scan = new Scanner(System.in);
	/**
     * '대여 및 연체 도서 조회' 메뉴 화면을 출력하고 사용자의 입력을 받습니다.
     * 1. 대여 도서 조회, 2. 연체 도서 조회, 0. 이전 메뉴 옵션을 제공합니다.
     * @return 사용자가 선택한 메뉴 번호 (문자열)
     */
	public static String BookSortScreen() {
    	
		System.out.println();
        System.out.println("""
        		
        		╔══════════════════════ 대여 및 연체 도서 조회 ══════════════════════╗
        		║                                                                    ║
        		║                         1. 대여 도서 조회                          ║
        		║                         2. 연체 도서 조회                          ║
        		║                                                                    ║
        		║                            0. 이전 메뉴                            ║
        		║                                                                    ║
        		╚════════════════════════════════════════════════════════════════════╝
        		""");
    			
        System.out.print(">> 메뉴 선택: "); // 사용자 입력을 받기 전 안내 메시지 추가
        return scan.nextLine();
    }
    
}
