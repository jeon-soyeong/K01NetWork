package chat7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class IConnectImpl implements IConnect{
	
	//동적쿼리 처리를 위한 객체
	public static PreparedStatement psmt;
	public static Connection con;
	public static ResultSet rs;
	
	public IConnectImpl() {
		System.out.println("IConnectImpl 기본생성자 호출");
	}
	public IConnectImpl(String user, String pass) {
		System.out.println("IConnectImpl 인자생성자 호출");
		try {
			//드라이버로드
			Class.forName(ORACLE_DRIVER);
			//DB연결
			connect(user, pass);
		} 
		catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
			e.printStackTrace();
		}
	}
	
	//오라클 DB에 실제 접속
	@Override
	public void connect(String user, String pass) {
		try {
			con = DriverManager.getConnection(ORACLE_URL, user, pass);
		}
		catch (SQLException e) {
			System.out.println("데이터베이스 연결 오류");
			e.printStackTrace();
		}
	}
	
	/*
	오버라이딩의 목적으로만 정의한 메소드. 쿼리실행은 각 클래스에서
	진행한다.
	 */
	@Override
	public void execute() {
		//하는 일 없음
	}

	@Override
	public void close() {
		try {
			if(con!=null) con.close();
			if(psmt!=null) psmt.close();
			if(rs!=null) rs.close();
			System.out.println("자원 반납 완료");
		} 
		catch (Exception e) {
			System.out.println("자원 반납 시 오류 발생");
			e.printStackTrace();
		}
	}
	
	//사용자로부터 입력값을 받기 위한 메소드
	@Override
	public String scanValue(String title) {
	
		Scanner scan = new Scanner(System.in);
		System.out.print(title+"을(를) 입력(exit->종료):");
		String inputStr =  scan.nextLine();

		/*
		 equalsIgnoreCase() 
		  : equals()메소드와 동일하게 문자열이 같은지를 비교하는 메소드로
		  다른점은 대소문자를 구분하지 않는다.
		 */
		if("EXIT".equalsIgnoreCase(inputStr)) {
			System.out.println("프로그램을 종료합니다.");
			close();
			//프로그램 자체가 즉시 종료된다.
			System.exit(0);
		}
		
		return inputStr;
	}
}
