package com.real.project.data;

/**
 * 도서 대여 정보를 담는 클래스입니다.
 * 대여번호, 회원번호, 도서번호, 대여일, 반납기한, 반납일, 별점 정보를 포함합니다.
 */
public class Rental {

	private String rentalId; 		// 대여번호
	private String memberNumber; 	// 회원번호
	private String bookId; 			// 도서번호
	private String rentalDate; 		// 대여일
	private String returnDeadline; 	// 반납기한
	private String returnDate; 		// 반납일
	private String starRating; 		// 별점
	
	/**
	 * Rental 객체를 생성합니다.
	 * 
	 * @param rentalId			대여번호
	 * @param memberNumber		회원번호
	 * @param bookId			도서번호
	 * @param rentalDate		대여일(yyyy-MM-dd)
	 * @param returnDeadline	반납기한(yyyy-MM-dd)
	 * @param returnDate		반납일(yyyy-MM-dd, 없으면 빈 문자열)
	 * @param starRating		사용자가 남긴 별점(없으면 빈 문자열)
	 */
	public Rental(String rentalId, String memberNumber, String bookId, String rentalDate, String returnDeadline,
			String returnDate, String starRating) {
		super();
		this.rentalId = rentalId;
		this.memberNumber = memberNumber;
		this.bookId = bookId;
		this.rentalDate = rentalDate;
		this.returnDeadline = returnDeadline;
		this.returnDate = returnDate;
		this.starRating = starRating;
	}


	public String getRentalId() {
		return rentalId;
	}


	public void setRentalId(String rentalId) {
		this.rentalId = rentalId;
	}


	public String getMemberNumber() {
		return memberNumber;
	}


	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}


	public String getBookId() {
		return bookId;
	}


	public void setBookId(String bookId) {
		this.bookId = bookId;
	}


	public String getRentalDate() {
		return rentalDate;
	}


	public void setRentalDate(String rentalDate) {
		this.rentalDate = rentalDate;
	}


	public String getReturnDeadline() {
		return returnDeadline;
	}


	public void setReturnDeadline(String returnDeadline) {
		this.returnDeadline = returnDeadline;
	}


	public String getReturnDate() {
		return returnDate;
	}


	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}


	public String getStarRating() {
		return starRating;
	}


	public void setStarRating(String starRating) {
		this.starRating = starRating;
	}


	@Override
	public String toString() {
		return "Rental [rentalId=" + rentalId + ", memberNumber=" + memberNumber + ", bookId=" + bookId + ", rentalDate=" + rentalDate + ", returnDeadline=" + returnDeadline + ", returnDate=" + returnDate + ", starRating=" + starRating + "]";
	}
	
}