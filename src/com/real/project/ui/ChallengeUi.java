package com.real.project.ui;

import java.util.Scanner;
/**
 * 사용자의 독서 챌린지 관련 메뉴 UI를 담당하는 클래스입니다.
 */
public class ChallengeUi {

	 private static Scanner scan = new Scanner(System.in);

	    /**
	     * '도서 메뉴' UI를 화면에 보여주고, 사용자의 입력을 받아서 반환합니다.
	     * @return 사용자가 선택한 메뉴 번호 (String)
	     */
	    public static String ChallengeScreen() {
	    	System.out.println(); // 메뉴 상단에 공백을 주어 가독성을 높입니다.
	    	System.out.println("""
	                ╔════════════════════════ 챌린지 설정과 내역 ════════════════════════╗
	                ║                                                                    ║
	                ║                          1. 챌린지 설정                            ║ 
	                ║                          2. 도전중인 챌린지                        ║
	                ║                          3. 챌린지 내역                            ║
	                ║                                                                    ║
	                ║                            0. 이전 메뉴                            ║ 
	                ║                                                                    ║
	                ╚════════════════════════════════════════════════════════════════════╝
	                """);
	                
	        System.out.print(">> 메뉴 선택: ");
	        return scan.nextLine();
	    }
}