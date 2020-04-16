package chat7;

import java.net.Socket;
import java.util.Scanner;

import chat7.Receiver;

public class MultiClient extends IConnectImpl {

	public MultiClient() {
		super("kosmo", "1234");
	}

	public static void main(String[] args) {

		try {
			IConnectImpl ic = new IConnectImpl("kosmo", "1234");
			
			String s_name = null;
			System.out.println("이름을 입력하세요:");
			Scanner scanner = new Scanner(System.in);
			
			while(true) {
				s_name = scanner.nextLine();

				String query = "SELECT COUNT(*) FROM user_tb WHERE NAME = ? ";
				psmt = con.prepareStatement(query);
				psmt.setString(1, s_name);
				int affected = psmt.executeUpdate();

				rs = psmt.executeQuery(query);
				String count = null;
				while (rs.next()) {
					count = rs.getString(1);
					System.out.println(count);
				}
				if (!(count.equals("0"))) {// 중복일때
					System.out.println(count);
					System.out.println("이미 있는 접속자입니다. 다른 이름으로 접속하세요.");
					System.out.println("다른 이름을 입력하세요:");
					continue;
				}
				break;
			}

//			String query = "SELECT COUNT(*) FROM user_tb WHERE NAME = ? ";
//			psmt = con.prepareStatement(query);
//			psmt.setString(1, s_name);
//			int affected = psmt.executeUpdate();
//		
//			rs = psmt.executeQuery(query);
//			
//			while (rs.next()) {
//				String count = rs.getString(1);
//				System.out.println(count);
//				while (!((count.equals("0")))) {//중복일때
//					System.out.println(count);
//					System.out.println("이미 있는 접속자입니다. 다른 이름으로 접속하세요.");
//					System.out.println("다른 이름을 입력하세요:");
//					s_name = scanner.nextLine();
//					
//					String query3 = "SELECT COUNT(*) FROM user_tb WHERE NAME = ? ";
//					psmt = con.prepareStatement(query);
//					psmt.setString(1, s_name);
//					int affected3 = psmt.executeUpdate();
//					rs = psmt.executeQuery(query);
//					break;
//				}
//				
//			}

			String query1 = "INSERT INTO user_tb VALUES (user_seq.nextval, ?, to_char(sysdate, 'hh:mi'))";
			psmt = con.prepareStatement(query1);
			psmt.setString(1, s_name);
			int affected1 = psmt.executeUpdate();

			String ServerIP = "localhost";
			if (args.length > 0) {
				ServerIP = args[0];
			}
			Socket socket = new Socket(ServerIP, 9999);
			System.out.println("서버와 연결되었습니다...");

			// 서버에서 보내는 Echo메세지를 클라이언트에 출력하기 위한 쓰레드 생성
			Thread receiver = new Receiver(socket);
			receiver.start();

			// 클라이언트의 메세지를 서버로 전송해주는 쓰레드 생성
			Thread sender = new Sender(socket, s_name);
			sender.start();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
