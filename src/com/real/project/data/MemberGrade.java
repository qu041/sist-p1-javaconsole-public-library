package com.real.project.data;

/**
 * 회원 등급 정보를 담는 데이터 클래스입니다.
 * 등급명, 누적 대여권수, 대여권수 혜택, 연장횟수 혜택, 축하 및 초기화 메시지를 포함합니다.
 */
public class MemberGrade {
	
	private String gradeName; 			//등급명
	private String rentalCount;			//누적대여권수
	private String rentalBonusCount;	//대여권수혜택
	private String extensionBonusCount;	//연장횟수혜택
	private String congratsMessage;		//축하메시지
	private String resetMessage;		//초기화메시지
	
	
	/**
	 * MemberGrade 객체를 생성합니다.
	 * 
	 * @param gradeName				등급명 (예: 씨앗, 새싹 등)
	 * @param rentalCount			등급 적용 기준 누적 대여권수
	 * @param rentalBonusCount		등급별 대여권수 혜택
	 * @param extensionBonusCount	등급별 연장횟수 혜택
	 * @param congratsMessage		등급 달성 시 출력할 축하 메시지
	 * @param resetMessage			등급 초기화 시 출력할 메시지
	 */
	public MemberGrade(String gradeName, String rentalCount, String rentalBonusCount, String extensionBonusCount,
			String congratsMessage, String resetMessage) {
		super();
		this.gradeName = gradeName;
		this.rentalCount = rentalCount;
		this.rentalBonusCount = rentalBonusCount;
		this.extensionBonusCount = extensionBonusCount;
		this.congratsMessage = congratsMessage;
		this.resetMessage = resetMessage;
	}

	
	public String getGradeName() {
		return gradeName;
	}


	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}


	public String getRentalCount() {
		return rentalCount;
	}


	public void setRentalCount(String rentalCount) {
		this.rentalCount = rentalCount;
	}


	public String getRentalBonusCount() {
		return rentalBonusCount;
	}


	public void setRentalBonusCount(String rentalBonusCount) {
		this.rentalBonusCount = rentalBonusCount;
	}


	public String getExtensionBonusCount() {
		return extensionBonusCount;
	}


	public void setExtensionBonusCount(String extensionBonusCount) {
		this.extensionBonusCount = extensionBonusCount;
	}


	public String getCongratsMessage() {
		return congratsMessage;
	}


	public void setCongratsMessage(String congratsMessage) {
		this.congratsMessage = congratsMessage;
	}


	public String getResetMessage() {
		return resetMessage;
	}


	public void setResetMessage(String resetMessage) {
		this.resetMessage = resetMessage;
	}


	@Override
	public String toString() {
		return "MemberGrade [gradeName=" + gradeName + ", rentalCount=" + rentalCount + ", rentalBonusCount="
				+ rentalBonusCount + ", extensionBonusCount=" + extensionBonusCount + ", congratsMessage="
				+ congratsMessage + ", resetMessage=" + resetMessage + "]";
	}
	
	
	
	
}
