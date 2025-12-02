package com.real.project.data;

/**
 * 사용자 한 명의 독서 챌린지 정보를 저장하는 데이터 클래스(DTO)입니다.
 */
public class Challenge {
    
    /** 챌린지 고유 번호 */
    private String challengeNumber;
    /** 챌린지를 진행하는 회원의 고유 번호 */
    private String memberNumber;
    /** 챌린지 종류 (예: "기간", "권수") */
    private String challengeKind;
    /** 목표 도서 권수 */
    private int goalBook;
    /** 챌린지 시작일 (yyyy-MM-dd 형식) */
    private String startDate;

    /**
     * Challenge 객체의 모든 필드 값을 초기화하는 생성자입니다.
     * @param challengeNumber 챌린지 번호
     * @param memberNumber 회원 번호
     * @param challengeKind 챌린지 종류
     * @param goalBook 목표 권수
     * @param startDate 시작일
     */
    public Challenge(String challengeNumber, String memberNumber, String challengeKind, int goalBook, String startDate) {
        this.challengeNumber = challengeNumber;
        this.memberNumber = memberNumber;
        this.challengeKind = challengeKind;
        this.goalBook = goalBook;
        this.startDate = startDate;
    }

    /**
     * 챌린지 번호를 반환합니다.
     * @return 챌린지 번호
     */
    public String getChallengeNumber() {
        return challengeNumber;
    }

    /**
     * 회원 번호를 반환합니다.
     * @return 회원 번호
     */
    public String getMemberNumber() {
        return memberNumber;
    }
    
    /**
     * 챌린지 종류를 반환합니다.
     * @return 챌린지 종류
     */
    public String getChallengeKind() {
        return challengeKind;
    }

    /**
     * 목표 도서 권수를 반환합니다.
     * @return 목표 도서 권수
     */
    public int getGoalBook() {
        return goalBook;
    }
    
    /**
     * 챌린지 시작일을 반환합니다.
     * @return 챌린지 시작일
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 목표 도서 권수를 설정합니다.
     * @param goalBook 새로 설정할 목표 도서 권수
     */
    public void setGoalBook(int goalBook) {
        this.goalBook = goalBook;  
    }
    
    /**
     * 챌린지 시작일을 설정합니다.
     * @param startDate 새로 설정할 챌린지 시작일
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;  
    }
    
    /**
	 * Challenge 객체의 모든 필드 값을 문자열 형태로 반환합니다.
	 * 디버깅 및 로깅 용도로 사용됩니다.
	 * @return 객체의 상태를 나타내는 문자열
	 */
    @Override
	public String toString() {
		return "Challenge [challengeNumber=" + challengeNumber + ", memberNumber=" + memberNumber + ", challengeKind="
				+ challengeKind + ", goalBook=" + goalBook + ", startDate=" + startDate + "]";
	}
}