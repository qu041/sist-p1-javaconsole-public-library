# 📚 도서관 관리 시스템 (자바 콘솔 프로젝트)

## 📖 프로젝트 소개
**도서관 관리 시스템**은 자바 콘솔 기반의 도서관 관리 프로그램입니다.
도서 대여 및 반납, 회원 관리, 도서 추천, 독서 챌린지 등 다양한 기능을 제공하여 도서관 운영을 효율적으로 돕습니다.

## ✨ 주요 기능

### 👤 일반 사용자 (Member)
- **로그인/회원가입**: 아이디/비밀번호 찾기 기능 포함.
- **도서 검색**: 제목, 저자, 출판사 등으로 도서 검색.
- **도서 대여/반납**: 원하는 도서를 대여하고 반납 기한 내에 반납.
- **내 서재**: 대여 중인 도서 및 반납 이력 확인.
- **도서 추천**: 별점순, 대여순 등 다양한 기준으로 도서 추천.
- **독서 챌린지**: 목표 독서량을 설정하고 달성 현황 확인.

### 🛡️ 관리자 (Admin)
- **회원 관리**: 전체 회원 조회, 연체 회원 관리, 등급별 회원 조회.
- **도서 관리**: 신규 도서 등록, 도서 정보 수정 및 삭제.
- **통계 관리**: 대여 중인 도서, 연체 도서 현황 파악.

## 🛠️ 기술 스택
- **Language**: Java 17
- **Build Tool**: Maven
- **Data Persistence**: OpenCSV (CSV 파일로 데이터 저장 및 로드)
- **IDE**: Eclipse / IntelliJ IDEA

## 📂 프로젝트 구조
```
src/com/real/project
├── Main.java           # 프로그램 진입점 (Entry Point)
├── book                # 도서 관련 서비스 로직
├── member              # 회원 관련 서비스 로직
├── rental              # 대여/반납 관련 서비스 로직
├── challenge           # 챌린지 관련 서비스 로직
├── recommend           # 추천 시스템 로직
├── statistics          # 통계 및 관리자 기능 로직
├── data                # 데이터 모델 (VO/DTO) 및 파일 입출력 (DAO)
└── ui                  # 콘솔 UI 클래스 모음
```

## 🚀 실행 방법

### 1. 프로젝트 클론
```bash
git clone <repository-url>
```

### 2. 프로젝트 가져오기 (Import)
- **Eclipse**: `File` > `Import` > `Maven` > `Existing Maven Projects`
- **IntelliJ IDEA**: `File` > `Open` > `pom.xml` 선택 > `Open as Project`

### 3. 의존성 설치
Maven 프로젝트이므로 `pom.xml`에 정의된 의존성(OpenCSV 등)이 자동으로 다운로드됩니다.
만약 다운로드되지 않는다면 프로젝트 우클릭 > `Maven` > `Update Project`를 실행하세요.

### 4. 실행
`src/com/real/project/Main.java` 파일을 실행합니다.

## 💾 데이터 파일
데이터는 프로젝트 루트의 `dat` 폴더(또는 설정된 경로)에 CSV 파일로 저장됩니다.
- `Books.csv`: 도서 정보
- `Members.csv`: 회원 정보
- `Rentals.csv`: 대여 기록
- `Challenges.csv`: 챌린지 정보

---
💡 **Tip**: 관리자 계정(`admin`)으로 로그인하면 관리자 전용 메뉴에 접근할 수 있습니다.
