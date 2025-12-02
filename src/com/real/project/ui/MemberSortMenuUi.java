package com.real.project.ui;

import java.util.Scanner;
/**
 * 관리자의 '통계 조회' 중 '회원 목록' 메뉴의 상세 UI를 담당하는 클래스입니다.
 */
public class MemberSortMenuUi {
	 private static Scanner scan = new Scanner(System.in);
	   /**
	     * '회원 관리(통계)' 메뉴 화면을 출력하고 사용자의 입력을 받습니다.
      * 연체 회원 조회, 등급별 회원 조회 옵션을 제공합니다.
	     * @return 사용자가 선택한 메뉴 번호 (String)
	     */
	    public static String MemberSortScreen() {
	    	
	        System.out.println("""
	        		╔═════════════════════════════ 회원관리 ═════════════════════════════╗
	        		║                                                                    ║
	        		║                            1. 연체 회원                            ║
	        		║                            2. 등급별 회원                          ║
	        		║                                                                    ║
	        		║                            0. 이전 메뉴                            ║
	        		║                                                                    ║
	        		╚════════════════════════════════════════════════════════════════════╝
	        		""");
	    			
	        System.out.print(">> 메뉴 선택: "); // 사용자 입력을 받기 전 안내 메시지 추가
	        return scan.nextLine();
	    }
	    
}
