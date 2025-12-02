package com.real.project.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.real.project.data.Member;

/**
 * 회원 데이터 처리를 담당하는 클래스입니다.
 * '회원.csv' 파일에서 데이터를 읽어오거나(load), 파일에 데이터를 저장(save)하는
 * 데이터 접근(Data Access) 관련 기능을 제공합니다.
 */
public class MemberData {
	
	/**
     * 모든 회원 정보를 메모리에 저장하는 정적 리스트입니다.
     * 프로그램 시작 시 load() 메서드를 통해 데이터가 채워집니다.
     */
	public static ArrayList<Member> memberDataList;
	
	static {
		memberDataList = new ArrayList<Member>();
	}
	
	/**
     * 회원 번호를 사용하여 리스트에서 특정 회원을 찾습니다.
     * @param memberNumber 검색할 회원의 고유 번호
     * @return 회원을 찾았으면 해당 Member 객체를, 찾지 못했으면 null을 반환합니다.
     */
	public static Member findMemberByNumber(String memberNumber) {
	    for (Member m : memberDataList) {
	        if (m.getMemberNumber().equals(memberNumber)) {
	            return m;
	        }
	    }
	    return null;
	}
	
	/**
     * 'dat/회원.csv' 파일로부터 회원 정보를 읽어와 memberDataList에 저장합니다.
     * 프로그램 시작 시 호출되어야 합니다.
     * @throws IOException 파일 읽기 중 오류가 발생할 경우
     */
	public static void load() throws IOException {
		
		//ArrayList<MemberData> 가변데이터를 컬렉션으로 메모리에 불러옴
		try {
			BufferedReader reader = new BufferedReader(new FileReader("dat\\회원.csv"));
			
			//reader > list
			String line = null;
			reader.readLine();
			while ((line = reader.readLine()) !=null) {
				//System.out.println(line);
				//line 1개 == 회원 1명
				String[] temp = line.split(",");
				
				Member mem = new Member(temp[0]
														, temp[1]
														, temp[2]
														, temp[3]
														, temp[4]
														, temp[5]);
				memberDataList.add(mem);
			}
			
			
			reader.close();
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
     * 현재 메모리에 있는 모든 회원 정보(memberDataList)를 'dat/회원.csv' 파일에 덮어쓰기 방식으로 저장합니다.
     * 프로그램 종료 시 호출되어 데이터의 일관성을 유지합니다.
     */
	public static void save() {
		
		//처음시작(6건) > 나중에 추가(3건)
		//데이터(txt) 저장시 스트림은 일부내용 수정 불가 -> 수정이든 뭐든 새로운파일로 덮어쓰는 작업
		try {
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("dat\\회원.csv"));
			
			//offlineProductList > (덮어쓰기) > 오프라인상품.txt
			
			for (Member mb : MemberData.memberDataList) {
				//mb 1개 > 텍스트 파일 1줄
				writer.write(String.format("%s,%s,%s,%s,%s,%s\r\n"
															, mb.getMemberNumber()
															, mb.getMemberId()
															, mb.getMemberPw()
															, mb.getMemberName()
															, mb.getMemberJumin()
															, mb.getMemberPhone()));
				
			}
			
			writer.close(); //기록작업이 끝나면 항상 닫아줘야됨
							//자원해제코드 ,Clean Code
			
		} catch (Exception e) {
			System.out.println("Data.save");
			e.printStackTrace();
		}
		
		
		
		
	}
	
	/**
     * MemberData 클래스의 파일 로드 기능을 테스트하기 위한 main 메서드입니다.
     * @param args 프로그램 실행 인자 (사용되지 않음)
     */
	public static void main(String[] args) {
		System.out.println("현재 작업 디렉토리: " + System.getProperty("user.dir"));
	
	    try {
	        MemberData.load();
	        System.out.println("✅ 회원 목록 읽기 성공!");
	        
	        for (Member m : memberDataList) {
	            System.out.println(
	                m.getMemberNumber() + ", " +
	                m.getMemberId() + ", " +
	                m.getMemberPw() + ", " +
	                m.getMemberName() + ", " +
	                m.getMemberJumin() + ", " +
	                m.getMemberPhone()
	            );
	        }

	    } catch (IOException e) {
	        System.out.println("❌ 파일 읽기 실패");
	        e.printStackTrace();
	    }
	}
	
	
	

}
