
package com.real.project.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.real.project.member.MemberService;
/**
 * '회원가입' UI를 담당하는 클래스입니다.
 * 사용자로부터 회원가입에 필요한 정보를 입력받는 역할을 합니다.
 */
public class RegisterMemberUi {
	
    private static MemberService service = new MemberService();

    private static Scanner scan = new Scanner(System.in);
    /**
     * 회원가입 폼을 화면에 출력하고 사용자로부터 정보를 입력받습니다.
     * @return 아이디, 비밀번호, 이름, 주민등록번호, 핸드폰번호가 순서대로 담긴 List<String>
     */
    public List<String> getRegistrationInput() {    	
        
    	System.out.println();
        System.out.println("╔═════════════════════════════ 회원가입 ═════════════════════════════╗");
        System.out.println("║                                                                    ║");
        
        System.out.print(  "║                        아이디 : ");
        String id = scan.nextLine();
        System.out.println("║                                                                    ║");
        
        System.out.print(  "║                      비밀번호 : ");
        String pw = scan.nextLine();
        System.out.println("║                                                                    ║");
        
        System.out.print(  "║                          이름 : ");
        String name = scan.nextLine();
        System.out.println("║                                                                    ║");

        System.out.print(  "║                  주민등록번호 : ");
        String jumin = scan.nextLine();
        System.out.println("║                                                                    ║");

        System.out.print(  "║                    핸드폰번호 : ");
        String phone = scan.nextLine();
        System.out.println("║                                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        // 입력받은 정보를 String 배열에 순서대로 저장하여 반환
  
        List<String> registrationInfo = new ArrayList<>();
        registrationInfo.add(id);
        registrationInfo.add(pw);
        registrationInfo.add(name);
        registrationInfo.add(jumin);
        registrationInfo.add(phone);
        
        return registrationInfo;
    
    }


    public void showMessage(String message) {
        System.out.println("\n" + message);
        System.out.print("계속하려면 Enter를 누르세요.");
        scan.nextLine();
    }
}
