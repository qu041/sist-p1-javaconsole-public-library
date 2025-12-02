package com.real.project.ui;

import java.util.Scanner;
/**
 * 도서 추천 및 검색 시 사용될 도서 분야(카테고리) 선택 메뉴 UI를 담당하는 클래스입니다.
 */
public class CategoryMenuUi {
    private Scanner scan = new Scanner(System.in);

    /**
     * 도서 분야 선택 UI를 화면에 보여주고, 사용자의 입력을 받아 해당 분야 이름(String)으로 변환하여 반환합니다.
     * @return 사용자가 선택한 분야 이름. 잘못된 입력 시 "INVALID", 종료 시 "EXIT"를 반환합니다.
     */
    public String CategoryMenuScreen() { // 메소드 이름을 다른 UI와 통일했습니다.
        System.out.println();
    	System.out.println("""
╔═══════════════════════════════ 분야 ═══════════════════════════════╗
║                                                                    ║
║      1. 경제/경영          5. 소설               9. 자기계발       ║
║      2. 과학               6. 시/에세이          10. 정치/사회     ║
║      3. 기술/공학          7. 역사/문화          11. 컴퓨터/IT     ║ 
║      4. 만화               8. 유아(0~7세)        12. 인문          ║
║                                                                    ║
║                            0. 이전 메뉴                            ║
║                                                                    ║
╚════════════════════════════════════════════════════════════════════╝
    			""");
        
        System.out.print(">> 분야 번호를 선택하세요: ");
        String input = scan.nextLine();
        
        // 사용자가 입력한 번호에 해당하는 카테고리 이름(문자열)으로 바꿔서 반환합니다.
        return switch (input) {
            case "1" -> "경제/경영";
            case "2" -> "과학";
            case "3" -> "기술/공학";
            case "4" -> "만화";
            case "5" -> "소설";
            case "6" -> "시/에세이";
            case "7" -> "역사/문화";
            case "8" -> "유아(0~7세)";
            case "9" -> "자기계발";
            case "10" -> "정치/사회";
            case "11" -> "컴퓨터/IT";
            case "12" -> "인문";
            case "0" -> "EXIT"; // '종료'를 의미하는 약속된 문자열
            default -> "INVALID"; // '잘못된 입력'을 의미하는 약속된 문자열
        };
    }
}
