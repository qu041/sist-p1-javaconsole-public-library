package com.real.project.ui;

import java.util.Scanner;
/**
 * 일반 사용자를 위한 '도서 검색 및 대여' 메뉴 UI를 담당하는 클래스입니다.
 */
public class BookMenuUi {

    private static Scanner scan = new Scanner(System.in);

    /**
     * '도서 메뉴' UI를 화면에 보여주고, 사용자의 입력을 받아서 반환합니다.
     * @return 사용자가 선택한 메뉴 번호 (String)
     */

    public static String BookMenuScreen() {
        System.out.println(); // 메뉴 상단에 공백을 주어 가독성을 높입니다.
        System.out.println("""
                ╔═════════════════════════ 도서검색 및 대여 ═════════════════════════╗
                ║                                                                    ║
                ║                            1. 도서 검색                            ║
                ║                            2. 도서 대여                            ║
                ║                                                                    ║
                ║                            0. 이전 메뉴                            ║
                ║                                                                    ║
                ╚════════════════════════════════════════════════════════════════════╝
                """);

        System.out.print(">> 메뉴 선택: ");
        return scan.nextLine();
    }
}