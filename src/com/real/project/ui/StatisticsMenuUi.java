package com.real.project.ui;

import java.util.Scanner;
/**
 * 관리자의 '통계 조회' 메인 메뉴 UI를 담당하는 클래스입니다.
 */
public class StatisticsMenuUi {
	
	 private static Scanner scan = new Scanner(System.in);
	  /**
	     * '통계 관리' 메뉴 화면을 출력하고 사용자의 입력을 받습니다.
      * 회원 목록 조회, 도서 목록 조회 옵션을 제공합니다.
	     * @return 사용자가 선택한 메뉴 번호 (String)
	     */	
	    public static String StatisticsScreen() {
	    	
	        System.out.println("""
	        		╔═════════════════════════════ 통계관리 ═════════════════════════════╗
	        		║                                                                    ║
	        		║                            1. 회원 목록                            ║
	        		║                            2. 도서 목록                            ║
	        		║                                                                    ║
	        		║                            0. 이전 메뉴                            ║
	        		║                                                                    ║
	        		╚════════════════════════════════════════════════════════════════════╝
	        		""");
	    			
	        System.out.print(">> 메뉴 선택: "); // 사용자 입력을 받기 전 안내 메시지 추가
	        return scan.nextLine();
	    }
	    
}
