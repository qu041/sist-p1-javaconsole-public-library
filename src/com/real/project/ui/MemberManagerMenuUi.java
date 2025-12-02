	package com.real.project.ui;
	
	import java.util.Scanner;
	
	/**
	 * 관리자의 '회원 관리' 기능 중 '회원 검색' 메뉴의 UI를 담당하는 클래스입니다.
	 */	
	public class MemberManagerMenuUi {
		
		private static Scanner scan = new Scanner(System.in);
		 /**
	     * '회원 검색' 메뉴 화면을 출력하고 사용자의 입력을 받습니다.
	     * @return 사용자가 선택한 메뉴 번호 (String)
	     */
	    public static String adminSearchrScreen() {
	    	System.out.println();
	    	System.out.println("""
	    			╔═════════════════════════════ 회원검색 ═════════════════════════════╗
	    			║                                                                    ║
	    			║                            1. 회원 검색                            ║
	    			║                                                                    ║
	    			║                            0. 이전 메뉴                            ║
	    			║                                                                    ║
	    			╚════════════════════════════════════════════════════════════════════╝
	    			""");			
	        System.out.print(">> 메뉴 선택: ");
	        return scan.nextLine();
	    }
	}
