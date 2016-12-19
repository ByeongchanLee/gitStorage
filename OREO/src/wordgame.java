
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class wordgame {
	private Vector<String> db = new Vector<String>();
	private String beforeString = "";

	private boolean input(String data) throws IOException {
		if (check(data)) {
			db.add(data);
			beforeString = data;
			return true;
		} else
			return false;
	}

	private String[] output(String data) throws IOException {
		char wordInputData = data.charAt(data.length() - 1);
		int i = 0;
		String tempOutputData[] = new String[100];
		Document doc = Jsoup.connect("http://krdic.naver.com/search.nhn?query="
				+ URLEncoder.encode(String.valueOf(wordInputData), "utf-8") + "*&kind=keyword").get();
		Elements E = doc.select("a");
		for (Element e : E) {
			String temp = e.html();
			if (temp.contains("strong")) {
				if (!cut(temp).isEmpty()) {
					tempOutputData[i] = String.valueOf(wordInputData) + cut(temp);
					i++;
				}
			}
		}

		String wordOutputData[] = new String[i];
		for (int j = 0; j < i; j++) {
			wordOutputData[j] = tempOutputData[j];
		}
		return wordOutputData;
	}

	private boolean check(String data) throws IOException {
		if (data.length() > 1) {
			if ((beforeString.equals("")) || String.valueOf(beforeString.charAt(beforeString.length() - 1))
					.equals(String.valueOf(data.charAt(0)))) {
				Document doc = Jsoup.connect(
						"http://krdic.naver.com/search.nhn?query=" + URLEncoder.encode(data, "utf-8") + "&kind=keyword")
						.get();
				Elements E = doc.select("h4");
				for (Element e : E) {
					String temp = e.html();
					if (temp.contains("없습니다")) {
						return false;
					} else
						return true;
				}
			}
		}
		return false;
	}

	private String select(String arr[]) {
		Random random = new Random();
		int i = 0;
		int tempNum;
		while (true) {
			if (arr.length == 0)
				return "-1";
			tempNum = random.nextInt(arr.length);
			if (!db.contains(arr[tempNum])) {
				break;
			} else {
				i++;
			}
			if (i >= 7) {
				return "-1";
			}
		}
		beforeString = arr[tempNum];
		db.add(beforeString);
		return arr[tempNum];

	}

	private String cut(String stringData) {
		char[] inputData = new char[10];
		String outputData = "";
		inputData = stringData.toCharArray();
		for (int i = stringData.indexOf("</strong>") + 9; i < stringData.indexOf("<sup>"); i++) {
			outputData += inputData[i];
		}
		return outputData;
	}

	public String WG(String data) throws IOException {
		while (true) {
			if (input(data)) {
				String outData = select(output(data));
				return outData;
			}
			else
				return "사전에 없거나 단어가 아닙니다 다시 입력하세요";
		}
	}
	// 최종 실행 메소드

	public static void main(String args[]) throws IOException {
		Scanner scan = new Scanner(System.in);
		wordgame wg = new wordgame();
		while (true) {
			String outData = wg.WG(scan.next());
			if (outData.equals("-1")) {
				System.out.println("computer resign");
				break;
			} else {
				System.out.println(outData);
			}
		}
	}
}