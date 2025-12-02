package com.real.project.data;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * 챌린지 데이터 처리를 담당하는 클래스입니다.
 * '챌린지.csv' 파일과의 데이터 입출력 및 관련 유틸리티 기능을 제공합니다.
 */
public class ChallengeData {
    
    /** 모든 챌린지 정보를 메모리에 저장하는 정적 리스트입니다. */
    public static ArrayList<Challenge> challengeList;
    /** 날짜 문자열을 파싱하기 위한 포맷터 */
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        challengeList = new ArrayList<>();
    }

    /**
     * 'dat/챌린지.csv' 파일로부터 챌린지 정보를 읽어와 challengeList에 저장합니다.
     */
    public static void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dat\\챌린지.csv"))) {
           
            reader.readLine(); // 헤더 라인 건너뛰기
            String line;
            while ((line = reader.readLine()) != null) {
                String[] temp = line.split(",");
                Challenge chal = new Challenge(
                        temp[0],
                        temp[1],
                        temp[2],
                        Integer.parseInt(temp[3]),  // String을 int로 변환
                        temp[4]
                );
                challengeList.add(chal);
            }
        } catch (Exception e) {
            System.out.println("챌린지 데이터 로딩 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    /**
     * 현재 메모리에 있는 모든 챌린지 정보를 'dat/챌린지.csv' 파일에 덮어쓰기 방식으로 저장합니다.
     */
    public static void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dat\\챌린지.csv"))) {
            writer.write("챌린지번호,회원번호,챌린지종류,목표권수,시작일\r\n"); //헤더 작성
            for (Challenge ch : challengeList) {
                writer.write(String.format("%s,%s,%s,%d,%s\r\n",
                        ch.getChallengeNumber(),
                        ch.getMemberNumber(),
                        ch.getChallengeKind(),
                        ch.getGoalBook(),
                        ch.getStartDate()));
            }
        } catch (Exception e) {
            System.out.println("챌린지 데이터 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    /**
     * 새로운 챌린지 번호를 생성하여 반환합니다.
     * @return "CHAL" 접두사와 8자리 숫자로 조합된 새로운 챌린지 번호 (예: "CHAL00000001")
     */
    public static String generateNewChallengeNumber() {
        if (challengeList.isEmpty()) {
            return "CHAL00000001";
        }
        String lastNumber = challengeList.get(challengeList.size() - 1).getChallengeNumber();
        int nextNum = Integer.parseInt(lastNumber.substring(4)) + 1;
        return String.format("CHAL%08d", nextNum);
    }

    /**
     * 특정 회원의 모든 챌린지 목록을 반환합니다.
     * @param memberNumber 조회할 회원의 고유 번호
     * @return 해당 회원의 Challenge 객체가 담긴 ArrayList
     */
    public static ArrayList<Challenge> getChallengesByMember(String memberNumber) {
        ArrayList<Challenge> result = new ArrayList<>();
        for (Challenge ch : challengeList) {
            if (ch.getMemberNumber().equals(memberNumber)) {
                result.add(ch);
            }
        }
        return result;
    }

    /**
     * 특정 회원이 주어진 기간 내에 반납(완독)한 도서의 수를 계산합니다.
     * @param memberNumber 조회할 회원의 고유 번호
     * @param start 기간 시작일
     * @param end 기간 종료일
     * @return 기간 내에 완독한 도서의 수
     */
    public static int countReadBooksFromRental(String memberNumber, LocalDate start, LocalDate end) {
        int count = 0;
        for (Rental r : RentalData.rentalList) {
            if (r.getMemberNumber().equals(memberNumber)) {
                String returnDateStr = r.getReturnDate();
                if (returnDateStr != null && !returnDateStr.trim().isEmpty()) {
                    LocalDate returnDate = LocalDate.parse(returnDateStr.trim(), formatter);
                    if (!returnDate.isBefore(start) && !returnDate.isAfter(end)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}