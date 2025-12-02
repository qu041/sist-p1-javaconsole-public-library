package com.real.project.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 회원 등급 데이터를 관리하는 클래스입니다.
 * CSV 파일로부터 등급 정보를 로드하고 저장하는 기능을 제공합니다.
 * 등급에 따라 혜택 계산, 다음 등급 확인 등의 로직도 포함합니다.
 */
public class MemberGradeData {
	
	/**
	 * 회원 등급 정보가 저장된 리스트 입니다.
	 */
	public static ArrayList<MemberGrade> memberGradeList;

	static {
		memberGradeList = new ArrayList<MemberGrade>();
	}
	
	/**
	 * 회원 등급 CSV 파일을 읽어 memberGradeList에 데이터를 로드합니다.
	 * 파일 경로: dat\\회원등급.csv
	 */
	public static void load() {
		
		

		try {
			BufferedReader reader = new BufferedReader(new FileReader("dat\\회원등급.csv"));

			String line = null;
			reader.readLine();//헤더버리기.

			while ((line = reader.readLine()) != null) {

				String[] temp = line.split(",");

				MemberGrade mg = new MemberGrade(temp[0]
						, temp[1]
						, temp[2]
						, temp[3]
						, temp[4]
						, temp[5]);

				memberGradeList.add(mg);
			}

			reader.close();

		} catch (Exception e) {
			System.out.println("MemberGradeData.load");
			e.printStackTrace();
		}

	}//load
	
	/**
	 * memberGradeList에 저장된 회원 등급 정보를 CSV 파일로 저장합니다.
	 * 파일 경로: dat\\회원등급.csv
	 */
	public static void save() {
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("dat\\회원등급.csv"));
			
			for (MemberGrade meg : MemberGradeData.memberGradeList) {
				
				writer.write(String.format("%s,%s,%s,%s,%s,%s\r\n", meg.getGradeName()
																, meg.getRentalCount()
																, meg.getRentalBonusCount()
																, meg.getExtensionBonusCount()
																, meg.getCongratsMessage()
																, meg.getResetMessage()));
			}
			
			writer.close();
			
		} catch (IOException e) {
			System.out.println("MemberGradeData.save");
			e.printStackTrace();
		}
		
	}//save
	
	/**
	 * 누적 대여권수를 기준으로 현재 회원의 등급을 반환합니다.
	 * 
	 * @param totalCount	회원의 누적 대여권수
	 * @return 해당 누적권수에 해당하는 MemberGrade 객체 (없으면 null)
	 */
	public static MemberGrade findGradeByCount(int totalCount) {
	    MemberGrade result = null;
	    for (MemberGrade grade : memberGradeList) {
	        int min = Integer.parseInt(grade.getRentalCount());
	        if (totalCount >= min) {
	            result = grade;
	        }
	    }
	    return result;
	}

	/**
	 * 현재 등급을 기준으로 다음 등급이 있으면 반환합니다.
	 * 
	 * @param current	현재 회원의 MemberGrade
	 * @return 다음 등급 MemberGrade (없으면 null)
	 */
	public static MemberGrade getNextGrade(MemberGrade current) {
	    for (int i = 0; i < memberGradeList.size(); i++) {
	        if (memberGradeList.get(i).getGradeName().equals(current.getGradeName())) {
	            if (i + 1 < memberGradeList.size()) {
	                return memberGradeList.get(i + 1);
	            }
	        }
	    }
	    return null;
	}

	/**
	 * 현재 등급 기준으로 다음 등급이 존재하는지 여부를 반환합니다.
	 * 
	 * @param current	현재 등급
	 * @return 다음 등급이 존재하면 true, 없으면 false
	 */
	public static boolean hasNextGrade(MemberGrade current) {
	    return getNextGrade(current) != null;
	}


}//class
