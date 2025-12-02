package com.real.project.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * {@code BookData} 클래스는 도서 정보의 데이터 입출력을 담당합니다.
 * <p>
 * CSV 파일로부터 도서 목록을 로드하고, 현재 메모리에 존재하는 도서 정보를 저장할 수 있습니다.
 * 도서 목록은 정적 리스트 {@code booklist}에 저장되며,
 * 애플리케이션 전반에서 공통적으로 접근 가능합니다.
 * </p>
 *
 * <p><strong>파일 경로:</strong> {@code dat\\도서데이터.csv}</p>
 *
 * @author
 */
public class BookData {

	 /** 애플리케이션 전체에서 사용하는 도서 목록 (메모리 저장용) */
    public static ArrayList<Book> booklist = new ArrayList<>();

    /**
     * CSV 파일로부터 도서 데이터를 읽어 {@code booklist}에 로드합니다.
     * <ul>
     *     <li>헤더 라인은 무시됩니다.</li>
     *     <li>필드 수가 부족한 레코드는 건너뜁니다.</li>
     * </ul>
     * 파일이 없거나 읽기 오류가 발생할 경우 오류 메시지를 출력합니다.
     */
    public static void load() {
        try (CSVReader reader = new CSVReader(new FileReader("dat\\도서데이터.csv"))) {
            reader.readNext(); // 헤더 스킵
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 6) continue; // 필드 누락 방지

                Book book = new Book(
                    line[0], // bookNumber
                    line[1], // title
                    line[2], // author
                    line[3], // publisher
                    line[4], // category
                    line[5]  // publishedDate
                );
                booklist.add(book);
            }
        } catch (Exception e) {
            System.out.println("📂 BookData.load 오류");
            e.printStackTrace();
        }
    }

    /**
     * 현재 {@code booklist}의 도서 정보를 CSV 파일로 저장합니다.
     * <ul>
     *     <li>기존 파일이 덮어쓰기 됩니다.</li>
     *     <li>첫 줄에 헤더가 작성되며, 이후 각 도서 정보가 한 줄씩 기록됩니다.</li>
     * </ul>
     * 저장 중 오류가 발생할 경우 예외 메시지를 출력합니다.
     */
    public static void save() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("dat\\도서데이터.csv"))) {
            // 헤더 작성
            String[] header = { "bookNumber", "title", "author", "publisher", "category", "publishedDate" };
            writer.writeNext(header);

            for (Book b : booklist) {
                String[] line = {
                    b.getBookNumber(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getPublisher(),
                    b.getCategory(),
                    b.getPublishedDate()
                };
                writer.writeNext(line);
            }
        } catch (Exception e) {
            System.out.println("📁 BookData.save 오류");
            e.printStackTrace();
        }
    }
}
