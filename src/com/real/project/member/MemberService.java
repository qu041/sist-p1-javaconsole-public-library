package com.real.project.member;

import java.util.Scanner;

import com.real.project.data.Member;
import com.real.project.data.MemberData;
import com.real.project.data.MemberGrade;
import com.real.project.data.MemberGradeData;
import com.real.project.data.Rental;
import com.real.project.data.RentalData;

/**
 * 회원 관련 서비스(회원가입, 로그인, 회원정보 검색 등)를 담당하는 클래스입니다.
 * <br>입력 검증, 등급조회, 아이디/비밀번호 찾기 등 다양한 멤버 관련 기능을 제공합니다.
 */
public class MemberService {

    /** 입력 처리를 위한 Scanner 객체 */
    private Scanner scan = new Scanner(System.in);

    /** 로그인한 사용자 상태 저장 */
    private static Member loggedInUser;

    // ========== [입력값 검증 메서드] ==========

    /**
     * 아이디가 유효한지 검사합니다.
     * @param id 입력한 아이디
     * @return 유효하면 true
     */
    private boolean isValidId(String id) {
        return id.matches("^[a-z][a-z0-9_]{3,15}$");
    }

    /**
     * 비밀번호가 유효한지 검사합니다.
     * @param pw 입력한 비밀번호
     * @return 유효하면 true
     */
    private boolean isValidPassword(String pw) {
        return pw.matches("^(?=(?:.*[a-z])(?=.*[A-Z])|(?:.*[a-z])(?=.*\\d)|(?:.*[A-Z])(?=.*\\d)).{5,}$");
    }

    /**
     * 이름이 유효한지 검사합니다.
     * @param name 입력한 이름
     * @return 유효하면 true
     */
    private boolean isValidName(String name) {
        return name.matches("^[가-힣]{2,5}$");
    }

    /**
     * 주민등록번호가 유효한지 검사합니다.
     * @param jumin 입력한 주민등록번호
     * @return 유효하면 true
     */
    private boolean isValidJumin(String jumin) {
        return jumin.matches("^[0-9]{6}-[1-4][0-9]{6}$");
    }

    /**
     * 전화번호가 유효한지 검사합니다.
     * @param phone 입력한 전화번호
     * @return 유효하면 true
     */
    private boolean isValidPhone(String phone) {
        return phone.matches("^01[016789]-[0-9]{3,4}-[0-9]{4}$");
    }

    // ========== [주요 기능 메서드] ==========

    /**
     * 현재 로그인한 회원 정보를 반환합니다.
     * @return 로그인한 Member 객체 (없으면 null)
     */
    public Member getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * 로그아웃 처리(로그인 정보 해제)
     */
    public void logout() {
        loggedInUser = null;
    }

    /**
     * 전체 회원 목록을 출력합니다. (관리자 전용)
     */
    public static void showAllMembers() {
        System.out.println("\n=== [전체 회원 목록] ===\n");
        System.out.printf("%-8s %-6s %-10s %-8s %-13s\n", "회원번호", "이름", "아이디", "생년월일", "전화번호");
        System.out.println("----------------------------------------------");

        for (Member m : MemberData.memberDataList) {
            String birth = m.getMemberJumin().length() >= 6
                    ? m.getMemberJumin().substring(0, 6)
                    : "------";

            System.out.printf("%-8s %-6s %-10s %-8s %-13s\n",
                    m.getMemberNumber(),
                    m.getMemberName(),
                    m.getMemberId(),
                    birth,
                    m.getMemberPhone());
        }
    }

    /**
     * 회원 정보를 조건별로 검색하고 결과를 출력합니다.
     * @return 검색 처리 결과 메시지
     */
    public String searchMember() {
        System.out.println("\n=== [회원 검색] ===");
        System.out.println("검색 조건 선택");
        System.out.println("1.이름 | 2.아이디 | 3.생년월일(6자리) | 4.전화번호");
        System.out.print("선택: ");
        String choice = scan.nextLine().trim();

        System.out.print("검색어 입력: ");
        String keyword = scan.nextLine().trim();

        System.out.println("\n[검색 결과]");
        System.out.println(
            padRightDisplayWidth("회원번호", 12) +
            padRightDisplayWidth("이름", 18) +
            padRightDisplayWidth("아이디", 21) +
            padRightDisplayWidth("생년월일", 21) +
            padRightDisplayWidth("전화번호", 11)
        );
        System.out.println("--------------------------------------------------------------------------------------------");

        boolean found = false;
        for (Member m : MemberData.memberDataList) {
            String birth = m.getMemberJumin().length() >= 6
                    ? m.getMemberJumin().substring(0, 6)
                    : "------";

            boolean match = false;

            switch (choice) {
                case "1":
                    match = m.getMemberName().contains(keyword);
                    break;
                case "2":
                    match = m.getMemberId().contains(keyword);
                    break;
                case "3":
                    match = birth.contains(keyword);
                    break;
                case "4":
                    match = m.getMemberPhone().contains(keyword);
                    break;
                default:
                    System.out.println("❌ 잘못된 선택입니다.");
                    return "❌ 잘못된 선택입니다.";
            }

            if (match) {
                found = true;
                System.out.println(
                    padRightDisplayWidth(m.getMemberNumber(), 12) +
                    padRightDisplayWidth(m.getMemberName(), 18) +
                    padRightDisplayWidth(m.getMemberId(), 22) +
                    padRightDisplayWidth(birth, 21) +
                    padRightDisplayWidth(m.getMemberPhone(), 11)
                );
            }
        }

        if (!found) {
            return "❌ 일치하는 회원이 없습니다.";
        }

        return "✅ 검색이 완료되었습니다.";
    }

    /**
     * 로그인한 회원의 누적 대여 권수 및 등급을 출력합니다.
     * @return 등급 조회 결과 메시지
     */
    public String gradeInfo() {
        if (loggedInUser == null) {
            System.out.println("\n❌ 로그인하지 않았습니다! 먼저 로그인해주세요.");
            return "❌ 로그인 필요";
        }

        String memberNumber = loggedInUser.getMemberNumber();
        int totalCount = 0;

        for (Rental r : RentalData.rentalList) {
            if (r.getMemberNumber().equals(memberNumber)) {
                totalCount++;
            }
        }

        MemberGrade currentGrade = MemberGradeData.findGradeByCount(totalCount);

        System.out.println("\n=== [내 등급 알림] ===");
        System.out.println("현재 누적 권수는 " + totalCount + "권으로 " + currentGrade.getGradeName() + " 등급입니다.");

        if (MemberGradeData.hasNextGrade(currentGrade)) {
            MemberGrade next = MemberGradeData.getNextGrade(currentGrade);
            int remain = Integer.parseInt(next.getRentalCount()) - totalCount;
            System.out.println("다음 등급인 " + next.getGradeName() + "까지 " + remain + "권 남았습니다.");
        } else {
            System.out.println("최고 등급입니다! 축하드립니다.");
        }

        return "✅ 등급 조회 완료";
    }

    /**
     * 회원, 도서 정보 출력 시 한글과 영문 길이 차이를 고려해서 우측에 공백을 추가합니다.
     * @param str 출력할 문자열
     * @param displayWidth 고정 폭
     * @return 폭 맞춰진 문자열
     */
    private static String padRightDisplayWidth(String str, int displayWidth) {
        if (str == null) str = "";

        int currentWidth = 0;
        for (char c : str.toCharArray()) {
            // 한글, 한자 등은 폭이 2, 영문/숫자는 폭이 1
            if (c >= 0xAC00 && c <= 0xD7AF) { // 한글 범위
                currentWidth += 2;
            } else if (c > 127) { // 기타 멀티바이트 문자
                currentWidth += 2;
            } else { // 영문, 숫자, 기호
                currentWidth += 1;
            }
        }

        StringBuilder sb = new StringBuilder(str);
        int padding = displayWidth - currentWidth;
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 회원가입 절차를 진행합니다. (아이디/비밀번호/이름/주민번호/전화번호 입력 및 유효성 검사)
     */
    public void registerMember() {
        String id;
        String pw;
        String name;
        String jumin;
        String phone;

        System.out.println("\n=== [회원가입 화면] ===");

        // 아이디 입력 + 검사
        while (true) {
            System.out.print("아이디 입력: ");
            id = scan.nextLine().trim();

            if (!isValidId(id)) {
                System.out.println("❌ 아이디는 영어 소문자, 숫자, _ 로 4~16자여야 합니다.");
                continue;
            }

            boolean duplicate = false;
            for (Member m : MemberData.memberDataList) {
                if (m.getMemberId().equals(id)) {
                    System.out.println("❌ 이미 사용 중인 아이디입니다.");
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) break;
        }

        // 비밀번호 입력 + 검사
        while (true) {
            System.out.print("비밀번호 입력: ");
            pw = scan.nextLine().trim();

            if (!isValidPassword(pw)) {
                System.out.println("❌ 영어 대문자, 소문자, 숫자 중 2종 이상 조합으로 8자 이상 입력해야 합니다.");
                continue;
            }
            break;
        }

        // 이름 입력 + 검사
        while (true) {
            System.out.print("이름 입력: ");
            name = scan.nextLine().trim();

            if (!isValidName(name)) {
                System.out.println("❌ 이름은 한글 2~5자로 입력해야 합니다.");
                continue;
            }
            break;
        }

        // 주민번호 입력 + 검사
        while (true) {
            System.out.print("주민등록번호 입력 (YYMMDD-XXXXXXX): ");
            jumin = scan.nextLine().trim();

            if (!isValidJumin(jumin)) {
                System.out.println("❌ 주민등록번호는 YYMMDD-XXXXXXX 형식이어야 합니다.");
                continue;
            }

            boolean duplicate = false;
            for (Member m : MemberData.memberDataList) {
                if (m.getMemberJumin().equals(jumin)) {
                    System.out.println("❌ 이미 가입된 주민등록번호입니다.");
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) break;
        }

        // 전화번호 입력 + 검사
        while (true) {
            System.out.print("전화번호 입력 (010-XXXX-XXXX): ");
            phone = scan.nextLine().trim();

            if (!isValidPhone(phone)) {
                System.out.println("❌ 전화번호는 010-XXXX-XXXX 형식이어야 합니다.");
                continue;
            }

            boolean duplicate = false;
            for (Member m : MemberData.memberDataList) {
                if (m.getMemberPhone().equals(phone)) {
                    System.out.println("❌ 이미 가입된 전화번호입니다.");
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) break;
        }

        // DB에 저장
        String memberNumber = "IDS" + String.format("%05d", MemberData.memberDataList.size() + 1);
        Member newMember = new Member(memberNumber, id, pw, name, jumin, phone);
        MemberData.memberDataList.add(newMember);
        MemberData.save();

        System.out.println("✅ 회원가입이 완료되었습니다.");
    }

    /**
     * 로그인 절차를 진행합니다. (아이디/비밀번호 입력, 검증, 로그인 정보 저장)
     */
    public void logIn() {
        String id;
        String pw;

        System.out.println("\n=== [로그인 화면] ===");

        // 아이디 입력 및 존재 검사
        while (true) {
            System.out.print("아이디 입력: ");
            id = scan.nextLine().trim();

            if (id.isEmpty()) {
                System.out.println("❌ 아이디는 비어있을 수 없습니다.");
                continue;
            }

            boolean exists = false;
            for (Member m : MemberData.memberDataList) {
                if (m.getMemberId().equals(id)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                System.out.println("❌ 존재하지 않는 아이디입니다. 다시 입력해주세요.");
                continue;
            }
            break;
        }

        // 비밀번호 입력 → 유효성검사 → DB검사
        while (true) {
            System.out.print("비밀번호 입력: ");
            pw = scan.nextLine().trim();

            if (pw.isEmpty()) {
                System.out.println("❌ 비밀번호는 비어있을 수 없습니다.");
                continue;
            }

            if (!isValidPassword(pw)) {
                System.out.println("❌ 비밀번호 형식 오류! 영어 대문자, 소문자, 숫자 중 2종 이상 조합, 8자 이상.");
                continue;
            }

            boolean correct = false;
            for (Member m : MemberData.memberDataList) {
                if (m.getMemberId().equals(id) && m.getMemberPw().equals(pw)) {
                    loggedInUser = m;
                    correct = true;
                    break;
                }
            }

            if (!correct) {
                System.out.println("❌ 비밀번호가 틀렸습니다. 다시 입력해주세요.");
                continue;
            }
            break;
        }

        // 로그인 성공 메시지
        if (loggedInUser != null) {
            if (loggedInUser.getMemberId().equals("admin")) {
                System.out.println("\n✅ 로그인 성공! (관리자 계정)");
                System.out.println("✅ 관리자 메뉴로 이동합니다.");
            } else {
                System.out.printf("\n✅ 로그인 성공!\n✅ [%s]님 환영합니다.\n✅ 회원 메뉴로 이동합니다.\n", loggedInUser.getMemberName());
            }
        }
    }

    /**
     * 이름+주민등록번호로 아이디를 찾아서 출력합니다.
     */
    public void findId() {
        String name;
        String jumin;

        System.out.println("\n=== [아이디 찾기 화면] ===");

        // 이름 입력
        while (true) {
            System.out.print("이름 입력 (한글 2~5자): ");
            name = scan.nextLine().trim();

            if (!isValidName(name)) {
                System.out.println("❌ 이름 형식이 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }
            break;
        }

        // 주민등록번호 입력
        while (true) {
            System.out.print("주민등록번호 입력 (YYMMDD-XXXXXXX): ");
            jumin = scan.nextLine().trim();

            if (!isValidJumin(jumin)) {
                System.out.println("❌ 주민등록번호 형식이 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }
            break;
        }

        // DB조회
        boolean found = false;
        for (Member m : MemberData.memberDataList) {
            if (m.getMemberName().equals(name) && m.getMemberJumin().equals(jumin)) {
                System.out.printf("\n✅ %s님의 아이디는 [%s]입니다.\n", name, m.getMemberId());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("\n❌ 입력하신 정보로 아이디를 찾을 수 없습니다.");
        }
    }

    /**
     * 아이디+주민등록번호로 비밀번호를 찾아서 출력합니다.
     */
    public void findPw() {
        String id;
        String jumin;

        System.out.println("\n=== [비밀번호 찾기 화면] ===");

        // 아이디 입력
        while (true) {
            System.out.print("아이디 입력: ");
            id = scan.nextLine().trim();

            if (id.isEmpty()) {
                System.out.println("❌ 아이디는 비어있을 수 없습니다. 다시 입력해주세요.");
                continue;
            }
            break;
        }

        // 주민등록번호 입력
        while (true) {
            System.out.print("주민등록번호 입력 (YYMMDD-XXXXXXX): ");
            jumin = scan.nextLine().trim();

            if (!isValidJumin(jumin)) {
                System.out.println("❌ 주민등록번호 형식이 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }
            break;
        }

        // DB조회
        boolean found = false;
        for (Member m : MemberData.memberDataList) {
            if (m.getMemberId().equals(id) && m.getMemberJumin().equals(jumin)) {
                System.out.printf("\n✅ %s님의 비밀번호는 [%s]입니다.\n", id, m.getMemberPw());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("\n❌ 입력하신 정보로 비밀번호를 찾을 수 없습니다.");
        }
    }
}
