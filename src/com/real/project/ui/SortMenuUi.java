package com.real.project.ui;

import java.util.Scanner;
/**
 * 도서 추천 시, 정렬 기준(별점순, 대여순)을 선택하는 메뉴 UI를 담당하는 클래스입니다.
 */
public class SortMenuUi {
    private static Scanner scan = new Scanner(System.in);

    /**
     * '정렬 기준 선택' UI를 화면에 보여주고, 사용자의 입력을 받아서 반환합니다.
     * @return 사용자가 선택한 메뉴 번호 (String)
     */
    public static String SortMenuScreen() {
        System.out.println();
        System.out.println("""
            ╔═════════════════════════════ 추천메뉴 ═════════════════════════════╗
            ║                                                                    ║
            ║                             1. 별점순                              ║
            ║                             2. 대여순                              ║
            ║                                                                    ║
            ║                            0. 이전 메뉴                            ║
            ║                                                                    ║
            ╚════════════════════════════════════════════════════════════════════╝
            """);
        System.out.print(">> 번호를 선택하세요: "); // 사용자 입력 프롬프트 추가
        return scan.nextLine();
    }
}