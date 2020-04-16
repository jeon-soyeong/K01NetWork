package chat7;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Scanner;

//클라이언트가 입력한 메세지를 서버로 전송해주는 쓰레드 클래스
public class Sender extends Thread {

	Socket socket;
	PrintWriter out = null;
	String name;

	// 생성자에서 output스트림을 생성한다.
	public Sender(Socket socket, String name) {
		this.socket = socket;
		try {
			out = new PrintWriter(this.socket.getOutputStream(), true);
			this.name = name;
		} catch (Exception e) {
			System.out.println("예외>Sender>생성자" + e);
		}
	}

	@Override
	public void run() {
		Scanner s = new Scanner(System.in);

		try {
			// 클라이언트가 입력한 "대화명"을 서버로 전송한다.
			
			out.println(URLEncoder.encode(name, "UTF-8"));

			// Q를 입력하기 전까지의 메세지를 서버로 전송한다.
			while (out != null) {
				try {
					String s2 = s.nextLine();
					boolean flag = s2.isEmpty();
					
					if(flag==true) {
						out.println("다시 입력하세요.");
					}
					
					if (s2.equalsIgnoreCase("Q")) {
						break;
					} else {
					out.println(URLEncoder.encode(s2, "UTF-8"));
					}
				}
				catch(NullPointerException e) {
					System.out.println(e.getMessage());
				}
				catch (UnsupportedEncodingException e1) {
				}
				catch (Exception e) {
					System.out.println("예외>Sender>run1" + e);
				}
			}

			// Q를 입력하면 스트림과 소켓을 모두 종료한다.
			out.close();
			socket.close();
		} 
		catch (UnsupportedEncodingException e1) {
		}
		catch (Exception e) {
			System.out.println("예외>Sender>run2" + e);
		}
	}
}
