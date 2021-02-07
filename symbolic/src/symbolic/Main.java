package symbolic;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		while (true) {
			String input = scan.nextLine();
			Lexer lexer = new Lexer(input);
			Parser p = new Parser(lexer);

			try {
				ASTNode tree = p.parse();
				tree = tree.eval("", new Double_(1));
				System.out.println(tree.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (LexException e) {
				e.printStackTrace();
			}
		}
	}

}
