package com.real.project.challenge;

import com.real.project.data.Member;
import com.real.project.data.RentalData;
import com.real.project.data.Challenge;
import com.real.project.data.ChallengeData;
import com.real.project.member.MemberService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * 회원별 챌린지(월간/연간 목표) 설정, 진행률/종료 이력 조회 등
 * 도서 챌린지 관련 기능을 담당하는 서비스 클래스입니다.
 */
public class ChallengeService {
    /** 콘솔 입력을 위한 Scanner (static, 공유) */
    private static final Scanner sc = new Scanner(System.in);

    /** 날짜 포맷터 (yyyy-MM-dd) */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** 회원 서비스 참조 (생성자 주입) */
    private final MemberService memberService;

    /**
     * ChallengeService 생성자
     * @param memberService 회원 서비스 객체
     */
    public ChallengeService(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 회원의 월간/연간 챌린지 목표를 신규 설정하거나 수정합니다.
     * 동일한 종류의 챌린지가 진행 중이면 수정, 아니면 신규 생성합니다.
     * 중복 챌린지(진행 중)가 여러 개 있다면 최근 것만 남기고 나머지는 자동 제거합니다.
     */
    public void setGoal() {
        Member user = memberService.getLoggedInUser();
        if (user == null) {
            System.out.println("\n❌ 로그인 정보가 없습니다. 먼저 로그인해주세요!");
            return;
        }

        System.out.println("\n=== 📌 [챌린지 목표 설정 화면] ===");
        System.out.println("로그인한 회원: " + user.getMemberName() + " (" + user.getMemberId() + ")");

        // --- 챌린지 종류 선택
        String type = "";
        while (true) {
            System.out.println("\n[챌린지 종류 선택]");
            System.out.println("1. 월간");
            System.out.println("2. 연간");
            System.out.print("선택: ");
            String input = sc.nextLine().trim();

            if (input.equals("1")) {
                type = "월간";
                break;
            } else if (input.equals("2")) {
                type = "연간";
                break;
            } else {
                System.out.println("❌ 1 또는 2를 입력해주세요.");
            }
        }

        // --- 목표 권수 입력
        int goalCount;
        while (true) {
            System.out.print("\n목표 권수 입력 (1권 이상): ");
            String input = sc.nextLine().trim();
            try {
                goalCount = Integer.parseInt(input);
                if (goalCount >= 1) break;
                System.out.println("❌ 1권 이상 입력해야 합니다.");
            } catch (Exception e) {
                System.out.println("❌ 숫자만 입력해주세요.");
            }
        }

        // --- 기존 진행 중 챌린지 찾기 & 중복 자동 제거
        LocalDate today = LocalDate.now();
        List<Challenge> runningChallenges = new ArrayList<>();
        for (Challenge challenge : ChallengeData.challengeList) {
            if (challenge.getMemberNumber().equals(user.getMemberNumber()) &&
                challenge.getChallengeKind().equals(type)) {
                LocalDate startDate = LocalDate.parse(challenge.getStartDate(), formatter);
                LocalDate endDate = calculateEndDate(startDate, type);
                if (!today.isAfter(endDate)) {
                    runningChallenges.add(challenge);
                }
            }
        }

        // 중복 제거: 가장 최신 챌린지만 남기고 나머지 제거
        Challenge existingChallenge = null;
        if (!runningChallenges.isEmpty()) {
            existingChallenge = runningChallenges.get(0);
            for (Challenge ch : runningChallenges) {
                LocalDate chStart = LocalDate.parse(ch.getStartDate(), formatter);
                LocalDate exStart = LocalDate.parse(existingChallenge.getStartDate(), formatter);
                if (chStart.isAfter(exStart)) {
                    existingChallenge = ch;
                }
            }
            // 최신 챌린지 빼고 나머지는 삭제
            for (Challenge ch : runningChallenges) {
                if (ch != existingChallenge) {
                    ChallengeData.challengeList.remove(ch);
                }
            }
        }

        LocalDate startDate = LocalDate.now();
        String startDateStr = startDate.format(formatter);

        if (existingChallenge != null) {
            // 기존 챌린지 수정
            System.out.println("\n⚠️ 현재 진행 중인 " + type + " 챌린지가 있습니다.");
            System.out.println("기존 챌린지 내용이 새로운 내용으로 수정됩니다.");
            existingChallenge.setGoalBook(goalCount);
            existingChallenge.setStartDate(startDateStr);
            System.out.println("✅ 기존 챌린지가 수정되었습니다!");
        } else {
            // 새로운 챌린지 생성
            String challengeNumber = ChallengeData.generateNewChallengeNumber();
            Challenge newChallenge = new Challenge(
                challengeNumber,
                user.getMemberNumber(),
                type,
                goalCount,
                startDateStr
            );
            ChallengeData.challengeList.add(newChallenge);
            System.out.println("✅ 새로운 챌린지가 등록되었습니다!");
        }

        // 데이터 저장
        ChallengeData.save();

        // 완료 메시지
        System.out.printf("- 종류: %s\n", type);
        System.out.printf("- 목표 권수: %d권\n", goalCount);
        System.out.printf("- 시작일: %s\n", startDateStr);

        LocalDate endDate = calculateEndDate(startDate, type);
        System.out.printf("- 종료일: %s\n", endDate.format(formatter));
        System.out.println("\n📌 목표는 재설정 시 초기화됩니다.");
    }

    /**
     * 챌린지 시작일과 종류(월간/연간)에 따라 종료일을 계산합니다.
     * @param startDate 시작일
     * @param type "월간" 또는 "연간"
     * @return 종료일(LocalDate)
     */
    private LocalDate calculateEndDate(LocalDate startDate, String type) {
        if (type.equals("월간")) {
            return startDate.plusMonths(1).minusDays(1); // 한 달 후 -1일 (마지막 날)
        } else if (type.equals("연간")) {
            return startDate.plusYears(1).minusDays(1);  // 일 년 후 -1일 (마지막 날)
        }
        return startDate; // 예외 방지 (실제로 이럴 일 없음)
    }

    /**
     * 로그인한 회원의 현재 진행 중 챌린지(달성률, 남은 권수 등) 정보를 출력합니다.
     */
    public void viewProgress() {
        Member user = memberService.getLoggedInUser();
        if (user == null) {
            System.out.println("\n❌ 로그인 정보가 없습니다. 먼저 로그인해주세요!");
            return;
        }

        System.out.println("\n=== 📌 [챌린지 달성률 확인 화면] ===");
        System.out.println("회원명: " + user.getMemberName());

        // --- 내 챌린지 불러오기
        List<Challenge> userChallenges = ChallengeData.getChallengesByMember(user.getMemberNumber());
        if (userChallenges.isEmpty()) {
            System.out.println("\n❌ 등록된 챌린지 목표가 없습니다. 먼저 설정해주세요.");
            return;
        }

        for (Challenge ch : userChallenges) {
            // 1️⃣ 시작일, 종료일 계산
            LocalDate startDate = LocalDate.parse(ch.getStartDate(), formatter);
            LocalDate endDate = calculateEndDate(startDate, ch.getChallengeKind());

            // 2️⃣ 오늘 날짜
            LocalDate today = LocalDate.now();

            // 3️⃣ 오늘 날짜가 챌린지 기간 안에 있는지 확인
            if (today.isBefore(startDate) || today.isAfter(endDate)) {
                continue;  // 진행중이 아니면 건너뛰기
            }

            // 4️⃣ 출력
            System.out.println("\n------------------------------");
            System.out.printf("▶ 챌린지 종류: %s\n", ch.getChallengeKind());
            System.out.printf("▶ 목표 권수: %d권\n", ch.getGoalBook());
            System.out.printf("▶ 시작일: %s\n", ch.getStartDate());
            System.out.printf("▶ 종료일: %s\n", endDate.format(formatter));

            // ✅ 기간 내 반납 완료 도서 카운트
            int readCount = 0;
            for (com.real.project.data.Rental r : RentalData.rentalList) {
                if (!r.getMemberNumber().equals(user.getMemberNumber())) continue;
                String returnDateStr = r.getReturnDate();
                if (returnDateStr == null || returnDateStr.trim().isEmpty()) continue;
                LocalDate returnDate = LocalDate.parse(returnDateStr.trim(), formatter);
                if (!returnDate.isBefore(startDate) && !returnDate.isAfter(endDate)) {
                    readCount++;
                }
            }

            // --- 달성률 계산
            double rate = (ch.getGoalBook() <= 0) ? 0.0 : (readCount * 100.0) / ch.getGoalBook();
            int left = Math.max(ch.getGoalBook() - readCount, 0);

            System.out.printf("- 📚 달성률: %.1f%%\n", rate);
            System.out.printf("- 📚 남은 권수: %d권\n", left);
        }
    }

    /**
     * 로그인한 회원의 종료된(기간 지난) 챌린지 이력을 출력합니다.
     */
    public void viewEndedChallenges() {
        Member user = memberService.getLoggedInUser();
        if (user == null) {
            System.out.println("\n❌ 로그인 정보가 없습니다. 먼저 로그인해주세요!");
            return;
        }

        System.out.println("\n=== 📌 [종료된 챌린지 이력 확인 화면] ===");
        System.out.println("회원명: " + user.getMemberName());

        // --- 내 챌린지 불러오기
        List<Challenge> userChallenges = ChallengeData.getChallengesByMember(user.getMemberNumber());
        if (userChallenges.isEmpty()) {
            System.out.println("\n❌ 등록된 챌린지 목표가 없습니다.");
            return;
        }

        boolean hasEndedChallenge = false;
        LocalDate today = LocalDate.now();

        for (Challenge ch : userChallenges) {
            // 1️⃣ 시작일, 종료일 계산
            LocalDate startDate = LocalDate.parse(ch.getStartDate(), formatter);
            LocalDate endDate = calculateEndDate(startDate, ch.getChallengeKind());

            // 2️⃣ 오늘 날짜가 종료일 이후인지 확인
            if (!today.isAfter(endDate)) {
                continue;  // 종료되지 않은 건 건너뛰기
            }
            hasEndedChallenge = true;

            // 3️⃣ 출력
            System.out.println("\n------------------------------");
            System.out.printf("▶ 챌린지 종류: %s\n", ch.getChallengeKind());
            System.out.printf("▶ 목표 권수: %d권\n", ch.getGoalBook());
            System.out.printf("▶ 시작일: %s\n", ch.getStartDate());
            System.out.printf("▶ 종료일: %s\n", endDate.format(formatter));

            // ✅ 기간 내 반납 완료 도서 카운트
            int readCount = 0;
            for (com.real.project.data.Rental r : RentalData.rentalList) {
                if (!r.getMemberNumber().equals(user.getMemberNumber())) continue;
                String returnDateStr = r.getReturnDate();
                if (returnDateStr == null || returnDateStr.trim().isEmpty()) continue;
                LocalDate returnDate = LocalDate.parse(returnDateStr.trim(), formatter);
                if (!returnDate.isBefore(startDate) && !returnDate.isAfter(endDate)) {
                    readCount++;
                }
            }

            // --- 달성률 계산
            double rate = (ch.getGoalBook() <= 0) ? 0.0 : (readCount * 100.0) / ch.getGoalBook();
            int left = Math.max(ch.getGoalBook() - readCount, 0);

            System.out.printf("- 📚 최종 달성률: %.1f%%\n", rate);
            System.out.printf("- 📚 목표 대비 미달성 권수: %d권\n", left);
        }

        if (!hasEndedChallenge) {
            System.out.println("\n✅ 종료된 챌린지 이력이 없습니다.");
        }
    }
}
