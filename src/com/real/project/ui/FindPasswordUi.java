package com.real.project.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * '비밀번호 찾기' UI를 담당하는 클래스입니다.
 */
public class FindPasswordUi {
	   /**
     * 비밀번호 찾기 화면을 출력하고, 사용자로부터 아이디와 주민등록번호를 입력받습니다.
     * @return 사용자가 입력한 아이디와 주민등록번호가 담긴 List<String>
     */
    public static void main(String[] args) {
        FindPasswordScreen();
    }

    public static List<String> FindPasswordScreen() {

        Scanner scan = new Scanner(System.in);
        System.out.println();
        System.out.println("╔═══════════════════════════ 비밀번호찾기 ═══════════════════════════╗");
        System.out.println("║                                                                    ║");
        System.out.print(  "║                          아이디 : ");
        String id = scan.nextLine();
        System.out.println("║                                                                    ║");
        System.out.print(  "║                    주민등록번호 : ");  
        String jumin = scan.nextLine();
        System.out.println("║                                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println(">> 비밀번호를 검색합니다...");
        
        // 여기에 다른 팀원이 실제 로그인 기능을 구현할 예정
        // ex) if (MemberService.login(id, pw)) { ... }

        List<String> FindPwInfo = new ArrayList<>();
        FindPwInfo.add(id);
        FindPwInfo.add(jumin);

        return FindPwInfo;
    }

    public void showMessage(String message) {
        System.out.println(message);
        pause();
    }
    
    private static void pause() {
        System.out.println("계속하시려면 엔터를 입력하세요.");
        // 새 Scanner 객체를 만들어야 버퍼 문제가 생기지 않습니다.
        new Scanner(System.in).nextLine();
    }
}