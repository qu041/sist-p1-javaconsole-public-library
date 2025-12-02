package com.real.project.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * 대여 데이터(Rental)를 관리하는 클래스입니다.
 * CSV 파일로부터 대여 정보를 로드하고, 저장하는 기능을 제공합니다.
 */
public class RentalData {

	/**
	 * 전체 대여 정보를 담고 있는 리스트입니다.
	 */
	public static ArrayList<Rental> rentalList;

	static {
		rentalList = new ArrayList<Rental>();
	}
	
	/**
	 * "dat\\대여.csv" 파일에서 대여 정보를 읽어 rentalList에 로드합니다.
	 * CSV는 다음 형식을 가집니다:
	 * 대여번호, 회원번호, 도서번호, 대여일, 반납기한, 반납일, 별점
	 */
	public static void load() {
		
		//기존 데이터 초기화
		rentalList.clear();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("dat\\대여.csv")));
			
			reader.readLine();
			String line = null;

			while ((line = reader.readLine()) != null) {

				String[] temp = line.split(",",-1);

				Rental nt = new Rental (temp[0]
										,temp[1]
										,temp[2]
										,temp[3]
										,temp[4]
										,temp[5]
										,temp[6]);

				rentalList.add(nt);
			}

			reader.close();

		} catch (Exception e) {
			System.out.println("RentalData.load 오류");
			e.printStackTrace();
		}

	}//load
	
	/**
	 * rentalList에 저장된 대여 정보를 "dat\\대여.csv" 파일에 저장합니다.
	 */
	public static void save() {
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("dat\\대여.csv")));
			
			for (Rental ren : RentalData.rentalList) {
				
				writer.write(String.format("%s,%s,%s,%s,%s,%s,%s\r\n"
						, ren.getRentalId()
						,ren.getMemberNumber()
						,ren.getBookId()
						,ren.getRentalDate()
						,ren.getReturnDeadline()
						,ren.getReturnDate()
						,ren.getStarRating()));
			}
			
			writer.close();
			
		} catch (IOException e) {
			System.out.println("RentalData.save 오류");
			e.printStackTrace();
		}
		
	}

}//class
