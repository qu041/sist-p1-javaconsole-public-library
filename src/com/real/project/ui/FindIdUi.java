package com.real.project.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * '아이디 찾기' UI를 담당하는 클래스입니다.
 */
public class FindIdUi {
	 /**
     * 아이디 찾기 화면을 출력하고, 사용자로부터 이름과 주민등록번호를 입력받습니다.
     * @return 사용자가 입력한 이름과 주민등록번호가 담긴 List<String>
     */
    public static void main(String[] args) {
        FindIdScreen();
    }

    public static List<String> FindIdScreen() {

        Scanner scan = new Scanner(System.in);

        System.out.println();
        System.out.println("╔════════════════════════════ 아이디찾기 ════════════════════════════╗");
        System.out.println("║                                                                    ║");
        System.out.print(  "║                           이름 : ");
        String name = scan.nextLine();
        System.out.println("║                                                                    ║");
        System.out.print(  "║                   주민등록번호 : ");
        String jumin = scan.nextLine();
        System.out.println("║                                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println(">> 아이디를 검색합니다...");
        
        // 여기에 다른 팀원이 실제 로그인 기능을 구현할 예정
        // ex) if (MemberService.login(id, pw)) { ... }

        List<String> findIdInfo = new ArrayList<>();
        findIdInfo.add(name);
        findIdInfo.add(jumin);

        return findIdInfo;
    }
    /**
     * 사용자에게 특정 메시지를 출력하고, 확인을 위해 잠시 대기합니다.
     * @param message 화면에 출력할 메시지
     */
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