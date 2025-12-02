package com.real.project.data;
/**
 * 회원 한 명의 정보를 저장하는 데이터 클래스(DTO, Data Transfer Object)입니다.
 */
public class Member {
	
	public String memberNumber; //회원번호
	public String memberId;     //아이디	
	public String memberPw;		//비밀번호
	public String memberName;	//이름
	public String memberJumin;  //Idcard 주민번호
	public String memberPhone; // 핸드폰번호
	
	/**
	 * 모든 필드 값을 초기화하는 생성자입니다.
	 * * @param memberNumber 회원 번호
	 * @param memberId     아이디
	 * @param memberPw     비밀번호
	 * @param memberName   이름
	 * @param memberJumin  주민등록번호
	 * @param memberPhone  핸드폰번호
	 */
	public Member(String memberNumber, String memberId, String memberPw, String memberName, String memberJumin,
			String memberPhone) {
		
		this.memberNumber = memberNumber;
		this.memberId = memberId;
		this.memberPw = memberPw;
		this.memberName = memberName;
		this.memberJumin = memberJumin;
		this.memberPhone = memberPhone;
	}

	public String getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberPw() {
		return memberPw;
	}

	public void setMemberPw(String memberPw) {
		this.memberPw = memberPw;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberJumin() {
		return memberJumin;
	}

	public void setMemberJumin(String memberJumin) {
		this.memberJumin = memberJumin;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	@Override
	public String toString() {
		return "Member [memberNumber=" + memberNumber + ", memberId=" + memberId + ", memberPw=" + memberPw
				+ ", memberName=" + memberName + ", memberJumin=" + memberJumin + ", memberPhone=" + memberPhone + "]";
	}
	
	
	
	
	
}