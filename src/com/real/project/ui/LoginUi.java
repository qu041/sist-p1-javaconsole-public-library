package com.real.project.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * '로그인' UI를 담당하는 클래스입니다.
 */
public class LoginUi {

    public static void main(String[] args) {
    	  /**
         * 로그인 화면을 출력하고, 사용자로부터 아이디와 비밀번호를 입력받습니다.
         * @return 사용자가 입력한 아이디와 비밀번호가 담긴 List<String>
         */
    	loginScreen();
    }

    public static List<String> loginScreen() {

        Scanner scan = new Scanner(System.in);

        System.out.println();
        
        System.out.println("╔══════════════════════════════ 로그인 ══════════════════════════════╗");
        System.out.println("║                                                                    ║");
        System.out.print(  "║                         아이디 : ");
        String id = scan.nextLine();
        System.out.println("║                                                                    ║");
        System.out.print(  "║                       비밀번호 : ");
        String pw = scan.nextLine();
        System.out.println("║                                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println(">> 로그인을 시도합니다...");
        
        List<String> loginInfo = new ArrayList<>();
        loginInfo.add(id);
        loginInfo.add(pw);
        
        // List를 반환합니다. 이 부분이 가장 중요합니다.
        return loginInfo;
    
    }

    /**
     * 화면 전환 전에 잠시 멈춰서 메시지를 확인할 시간을 주는 메소드
     */
    private static void pause() {
        System.out.println("계속하시려면 엔터를 입력하세요.");
        // 새 Scanner 객체를 만들어야 버퍼 문제가 생기지 않습니다.
        new Scanner(System.in).nextLine();
    }
}