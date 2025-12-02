package com.real.project.ui;

import java.util.Scanner;

/**
 * 관리자 로그인 후 표시되는 메인 메뉴의 UI를 담당하는 클래스입니다.
 */
public class MemberAdminMenuUi {

	 private static Scanner scan = new Scanner(System.in);
	 /**
	     * '관리자 메뉴' 화면을 출력하고 사용자의 입력을 받습니다.
      * 도서 관리, 회원 관리, 통계 조회, 로그아웃 옵션을 제공합니다.
	     * @return 사용자가 선택한 메뉴 번호 (String)
	     */	
	    public static String ManagerMenuScreen() {
	    	System.out.println();
	        System.out.println("""
	        		╔════════════════════════════ 관리자메뉴 ════════════════════════════╗
	        		║                                                                    ║
	        		║                            1. 도서 관리                            ║
	        		║                            2. 회원 관리                            ║
	        		║                            3. 통계 조회                            ║
	        		║                                                                    ║
	        		║                            0. 로그아웃                             ║
	        		║                                                                    ║
	        		╚════════════════════════════════════════════════════════════════════╝
	        		""");
	    			
	        System.out.print(">> 메뉴 선택: "); // 사용자 입력을 받기 전 안내 메시지 추가
	        return scan.nextLine();
	    } 
	    
	    public void showMessage(String message) {
	        System.out.println("\n" + message);
	    }
	}
