package STICPinterpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;



public class LanguageInterpreter {

	public static void main(String[] args) {
		
		TreeNode root = new TreeNode(new Token("root"));
		Parser parser = new Parser(new Scan("program1.txt"));
		root = parser.parse(root);
		printTree(root);
		dfs(root);
		
	}
	static Map<String, Integer> map = new HashMap<>();
	private static void dfs(TreeNode n) {
		if(n == null) return;
		System.out.println("DFS: "+n.t.text);
		for(int i = 0; i < n.children.size(); i++) {
			evaluate(n.children.get(i));
		}
	}
	public static void evaluate(TreeNode n) {
		if(n.t.getType().equals(TokenType.CONDITION_BEG)) {
			System.out.println("DFS: "+n.t.text);
			System.out.println("DFS: "+n.children.get(0).t.text);
			String exp = n.children.get(0).children.get(0).t.text;
			
			exp = clean_brackets(exp);
			int total = eval_exp(exp);
			if(total != 0) {
				for(int i = 1 ; i < n.children.get(0).children.size() ; i++) {
					evaluate(n.children.get(0).children.get(i));
				}
			}
			else {
				if(n.children.size()>1){
					System.out.println("DFS: "+n.children.get(1).t.text);
					for(int i = 1 ; i < n.children.size() ; i++) {
						evaluate(n.children.get(1).children.get(i));
					}
				}
			}
		}
		else if(n.t.getType().equals(TokenType.WHILE_BEG)) {
			System.out.println("DFS: "+n.t.text);
			System.out.println("DFS: "+n.children.get(0).t.text);
			String exp = n.children.get(0).children.get(0).t.text;
			System.out.println("DFS: "+n.children.get(0).children.get(0).t.text);
			exp = clean_brackets(exp);
			int total= eval_exp(exp);
			while(total != 0) {
				for(int i = 1 ; i < n.children.get(0).children.size() ; i++) {
					evaluate(n.children.get(0).children.get(i));
				}
				total = eval_exp(exp);
				System.out.println("DFS: "+n.children.get(0).t.text);
				System.out.println("DFS: "+n.children.get(0).children.get(0).t.text);
			}
		}
		else if(n.t.getType().equals(TokenType.ASSIGNMENT)) {
			System.out.println("DFS: "+n.t.text);
			System.out.println("DFS: "+n.children.get(0).t.text);
			String exp = n.children.get(1).t.text;
			System.out.println("DFS: "+n.children.get(1).t.text);
			exp = clean_brackets(exp);
			int total = eval_exp(exp);
			map.put(n.children.get(0).t.text, total);
		}
		else if(n.t.getType().equals(TokenType.OUTPUT)) {
			System.out.println("DFS: "+n.t.text);
			String exp = n.children.get(0).t.text;
			System.out.println("DFS: "+n.children.get(0).t.text);
			exp = clean_brackets(exp);
			int total = eval_exp(exp);
			System.out.println("Output: "+n.children.get(0).t.text+" = "+ total);
		}
		else if(n.t.getType().equals(TokenType.INPUT)) {
			System.out.println("DFS: "+n.t.getType());
			System.out.println("DFS: "+n.children.get(0).t.text);
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter value for "+n.children.get(0).t.text+":");
			int num = scanner.nextInt();
			map.put(n.children.get(0).t.text, num);
			scanner.close();
		}
	}
	private static int eval_exp(String exp) {
		int total = 0;
		char ch;
		for(int i = 0; i < exp.length(); i++) {
			ch = exp.charAt(i);
			if(Character.isAlphabetic(ch)) {
				StringBuffer buffer = new StringBuffer();
				while(exp.length()>i && Character.isAlphabetic(exp.charAt(i))) {
					buffer.append(exp.charAt(i));
					i++;
				}
				i--;
				total = total + map.get(String.valueOf(buffer));
			}
			else if(Character.isDigit(ch)) {
				StringBuffer buffer = new StringBuffer();
				while(exp.length()>i && Character.isDigit(exp.charAt(i))) {
					buffer.append(exp.charAt(i));
					i++;
				}
				i--;
				total = total + Integer.parseInt(String.valueOf(buffer));
			}
			else if(ch == '-') {
				i++;
				StringBuffer buffer = new StringBuffer();
				if(exp.length()>i && Character.isDigit(exp.charAt(i))) {
					while(exp.length()>i && Character.isDigit(exp.charAt(i))) {
						buffer.append(exp.charAt(i));
						i++;
					}
					total = total - Integer.parseInt(String.valueOf(buffer));
				}
				else if(exp.length()>i && Character.isAlphabetic(exp.charAt(i))) {
					while(exp.length()>i && Character.isAlphabetic(exp.charAt(i))) {
						buffer.append(exp.charAt(i));
						i++;
					}
					total = total - map.get(String.valueOf(buffer));
				}
				i--;
			}
			else if(ch == '+') {
				i++;
				StringBuffer buffer = new StringBuffer();
				if(exp.length()>i && Character.isDigit(exp.charAt(i))) {
					while(exp.length()>i && Character.isDigit(exp.charAt(i))) {
						buffer.append(exp.charAt(i));
						i++;
					}
					total = total + Integer.parseInt(String.valueOf(buffer));
				}
				else if(exp.length()>i && Character.isAlphabetic(exp.charAt(i))) {
					while(exp.length()>i && Character.isAlphabetic(exp.charAt(i))) {
						buffer.append(exp.charAt(i));
						i++;
					}
					total = total + map.get(String.valueOf(buffer));
				}
				i--;
			}
			else if(ch == '*') {
				i++;
				StringBuffer buffer = new StringBuffer();
				if(exp.length()>i && Character.isDigit(exp.charAt(i))) {
					while(exp.length()>i && Character.isDigit(exp.charAt(i))) {
						buffer.append(exp.charAt(i));
						i++;
					}
					total = total * Integer.parseInt(String.valueOf(buffer));
				}
				else if(exp.length()>i && Character.isAlphabetic(exp.charAt(i))) {
					while(exp.length()>i && Character.isAlphabetic(exp.charAt(i))) {
						buffer.append(exp.charAt(i));
						i++;
					}
					total = total * map.get(String.valueOf(buffer));
				}
				i--;
			}
			else if(ch == '/') {
				i++;
				StringBuffer buffer = new StringBuffer();
				if(exp.length()>i && Character.isDigit(exp.charAt(i))) {
					while(exp.length()>i && Character.isDigit(exp.charAt(i))) {
						buffer.append(exp.charAt(i));
						i++;
					}
					total = total / Integer.parseInt(String.valueOf(buffer));
				}
				else if(exp.length()>i && Character.isAlphabetic(exp.charAt(i))) {
					while(exp.length()>i && Character.isAlphabetic(exp.charAt(i))) {
						buffer.append(exp.charAt(i));
						i++;
					}
					total = total / map.get(String.valueOf(buffer));
				}
				i--;
			}
		}
		return total;
	}
	private static String clean_brackets(String exp) {
		for(int x = 0 ; x < exp.length() ; x++) {
			
			if(exp.charAt(x) == '^') {
				int val = Character.getNumericValue(exp.charAt(x-1));
				int exponent = Character.getNumericValue(exp.charAt(x-1));
				for(int m = 1 ; m < Character.getNumericValue(exp.charAt(x+1)) ; m++) {
					val = val * exponent;
				}
				exp = exp.substring(0,x-1) + val + exp.substring(x+2);
				x++;
			}
			else if(exp.charAt(x) == '*' || exp.charAt(x) == '/' || exp.charAt(x) == '%') {
				String expleft = exp.substring(0, x-1);
				String expmiddle = exp.substring(x-1, x+2);
				String expright = exp.substring(x+2);
				exp = expleft + "(" + expmiddle + ")" + expright;
				x++;
			}
		}
		while(exp.contains(")")) {
			for(int i = 0; i < exp.length(); i++) {
				if(exp.charAt(i) == ')') {
					int j = i-1;
					boolean found = false;
					while(!found && j>=0) {
						if(exp.charAt(j) == '(') {
							int val = eval_exp(exp.substring(j+1, i));
							exp = exp.substring(0, j) + val + exp.substring(i+1);
							found = true;
						}
						j--;
					}
				}
				if(!exp.contains(")")){
					break;
				}
			}
		}
		return exp;
	}
	private static void printTree(TreeNode root){
	    if(root == null) return;
	    Queue<TreeNode> queue = new LinkedList<>();
	    queue.offer(root);
	    while(!queue.isEmpty()) {
	        int len = queue.size();
	        System.out.println(len);
	        for(int i=0;i<len;i++) { // so that we can reach each level
	            TreeNode node = queue.poll();
	            System.out.print(node.t.text + " ");
	            for (TreeNode item : node.children) { // for-Each loop to iterate over all childrens
	                queue.offer(item);
	            }
	        }
	        System.out.println();
	    }
	}
}

class Scan {
	private String progText;
	private int curPos = 0;
	Scan(String fileName){
		try {
			byte[] allBytes = Files.readAllBytes(Paths.get(fileName));
			progText = new String(allBytes);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	Token nextToken() {
		if(curPos == progText.length())
			return new EOFTOKEN(null);
		
		char curChar;
		while(curPos < progText.length() && Character.isWhitespace(progText.charAt(curPos))) {
			curPos++;
		}
		curChar = progText.charAt(curPos);
		curPos++;
		
		if(Character.isDigit(curChar)) {
			return new NumberToken(String.valueOf(curChar));
		}
		else if(Character.isAlphabetic(curChar)) {
			return new IdentifierToken(String.valueOf(curChar));
		}
		else if(curChar == '{') {
			return new WhileBegToken(String.valueOf(curChar));
		}
		else if(curChar == '}') {
			return new WhileEndToken(String.valueOf(curChar));
		}
		else if(curChar == '[') {
			return new ConditionBegToken(String.valueOf(curChar));
		}
		else if(curChar == ']') {
			return new ConditionEndToken(String.valueOf(curChar));
		}
		else if(curChar == '?') {
			return new TrueFalseToken(String.valueOf(curChar));
		}
		else if(curChar == ':') {
			return new ElseIfToken(String.valueOf(curChar));
		}
		else if(curChar == ';') {
			return new SemicolonToken(String.valueOf(curChar));
		}
		else if(curChar == '=') {
			return new AssignmentToken(String.valueOf(curChar));
		}
		else if(curChar == '<') {
			return new OutputToken(String.valueOf(curChar));
		}
		else if(curChar == '>') {
			return new InputToken(String.valueOf(curChar));
		}
		else if(curChar == '-') {
			return new SubtOpToken(String.valueOf(curChar));
		}
		else if(curChar == '+') {
			return new AddOpToken(String.valueOf(curChar));
		}
		else if(curChar == '*') {
			return new MultOpToken(String.valueOf(curChar));
		}
		else if(curChar == '/') {
			return new DivOpToken(String.valueOf(curChar));
		}
		else if(curChar == '%') {
			return new ModOpToken(String.valueOf(curChar));
		}
		else if(curChar == '^') {
			return new ExponentToken(String.valueOf(curChar));
		}
		else if(curChar == '(') {
			return new BracketBegToken(String.valueOf(curChar));
		}
		else if(curChar == ')') {
			return new BracketEndToken(String.valueOf(curChar));
		}
		else if(curChar == '.') {
			return new EOFTOKEN(String.valueOf(curChar));
		}
		else {
			return new ErrorToken("NotRecognizedToken");
		}
	}
}

class Parser{
	private Scan scan;
	Parser(Scan scan){
		this.scan = scan;
	}
	
	void parseOld() {
		Token token = scan.nextToken();
		while(!token.getType().equals(TokenType.END_OF_FILE)) {
			//print all token texts and their types here
			//for example, program1.txt
			System.out.printf("token text: %s, token type: %s\n", token.text, token.tokenType);
			token = scan.nextToken();
		}
		System.out.printf("%s, %s\n", token.text, token.tokenType);
		System.out.println("Done with reading");
	}
	
	Token token;
	boolean done = false;
	TreeNode parse(TreeNode n) {
		try {
			token = scan.nextToken();
			if(token.text == ".") {
				System.out.println("Program is syntactically correct.");
			}
			else {
				while(done==false)
					S(n);
				System.out.println("Program is syntactically correct.");
			}
		}
		catch(MissingTokenException ex){
			System.out.println(ex.getMessage());
		}
		return n;
	}
	void S(TreeNode n) throws MissingTokenException{
		
		if(token.getType().equals(TokenType.CONDITION_BEG)) {
			C(n);
		}
		else if(token.getType().equals(TokenType.WHILE_BEG)) {
			W(n);
		}
		else if(token.getType().equals(TokenType.IDENTIFIER)) {
			A(n);
		}
		else if(token.getType().equals(TokenType.OUTPUT)) {
			O(n);
		}
		else if(token.getType().equals(TokenType.INPUT)) {
			I(n);
		}
		else if(token.getType().equals(TokenType.END_OF_FILE)) {
			done = true;
		}
		else {
			throw new MissingTokenException(token.text + " found. The beginning of a statement is missing.");
		}
	}
	void C(TreeNode n) throws MissingTokenException {
		if(token.getType().equals(TokenType.CONDITION_BEG)) {
			TreeNode cond_beg = new TreeNode(token);
			token = scan.nextToken();
			String exp = "";
			exp = E(exp);
			TreeNode expression = new TreeNode(new ExpressionToken(exp));
			if(token.getType().equals(TokenType.TRUE_FALSE)) {
				TreeNode qsmark = new TreeNode(token);
				qsmark.children.add(expression);
				token = scan.nextToken();
				while(!(token.getType().equals(TokenType.ELSE_IF)) && !(token.getType().equals(TokenType.CONDITION_END))) {
					S(qsmark);
				}
				if(token.getType().equals(TokenType.ELSE_IF)) {
					TreeNode elseif = new TreeNode(token);
					token = scan.nextToken();
					while(!token.getType().equals(TokenType.CONDITION_END)) {
						S(elseif);
					}
					cond_beg.children.add(elseif);
				}
				token = scan.nextToken();
				cond_beg.children.add(qsmark);
			}
			else {
				throw new MissingTokenException(token.text+  " found. ? is missing.");
			}
			n.children.add(cond_beg);
		}
	}
	void W(TreeNode n) throws MissingTokenException {
		if(token.getType().equals(TokenType.WHILE_BEG)) {
			TreeNode while_beg = new TreeNode(token);
			token = scan.nextToken();
			String exp = "";
			exp = E(exp);
			TreeNode expression = new TreeNode(new ExpressionToken(exp));
			if(token.getType().equals(TokenType.TRUE_FALSE)) {
				TreeNode qsmark = new TreeNode(token);
				qsmark.children.add(expression);
				token = scan.nextToken();
				while(!token.getType().equals(TokenType.WHILE_END)) {
					S(qsmark);
				}
				token = scan.nextToken();
				while_beg.children.add(qsmark);
			}
			else {
				throw new MissingTokenException(token.text+  " found. ? is missing.");
			}
			
			n.children.add(while_beg);
		}
	}
	void A(TreeNode n) throws MissingTokenException {
		if(token.getType().equals(TokenType.IDENTIFIER)) {
			TreeNode identifier = new TreeNode(token);
			token = scan.nextToken();
			if(token.getType().equals(TokenType.ASSIGNMENT)){
				TreeNode assignment = new TreeNode(token);
				token = scan.nextToken();
				String exp = "";
				exp = E(exp);
				TreeNode expression = new TreeNode(new ExpressionToken(exp));
				assignment.children.add(identifier);
				assignment.children.add(expression);
				n.children.add(assignment);
				if(token.getType().equals(TokenType.SEMICOLON)) {
					token = scan.nextToken();
				}
				else {
					throw new MissingTokenException(token.text+  " found. ; is missing.");
				}
			}
			else {
				throw new MissingTokenException(token.text+  " found. = is missing.");
			}
		}
	}
	void O(TreeNode n) throws MissingTokenException {
		if(token.getType().equals(TokenType.OUTPUT)) {
			TreeNode output = new TreeNode(token);
			token = scan.nextToken();
			String exp = "";
			exp = E(exp);
			TreeNode expression = new TreeNode(new ExpressionToken(exp));
			output.children.add(expression);
			n.children.add(output);
			if(token.getType().equals(TokenType.SEMICOLON)) {
				token = scan.nextToken();
			}
			else {
				throw new MissingTokenException(token.text+  " found. ; is missing.");
			}
		}
	}
	void I(TreeNode n) throws MissingTokenException {
		if(token.getType().equals(TokenType.INPUT)) {
			TreeNode input = new TreeNode(token);
			token = scan.nextToken();
			if(token.getType().equals(TokenType.IDENTIFIER)) {
				TreeNode identifier = new TreeNode(token);
				input.children.add(identifier);
				n.children.add(input);
				token = scan.nextToken();
				if(token.getType().equals(TokenType.SEMICOLON)) {
					token = scan.nextToken();
				}
				else {
					throw new MissingTokenException(token.text+  " found. ; is missing.");
				}
			}
			else {
				throw new MissingTokenException(token.text+  " found. Identifier is missing.");
			}
		}
	}
	String E(String str) throws MissingTokenException {
		str = Term(str);
		str = Expr_tail(str);
		return str;
	}
	String Expr_tail(String str) throws MissingTokenException{
		if(token.tokenType.equals(TokenType.ADD_OP)) {
			str = str + token.text;
			token = scan.nextToken();
			str = Term(str);
			str = Expr_tail(str);
		}
		else if(token.tokenType.equals(TokenType.SUBT_OP)) {
			str = str + token.text;
			token = scan.nextToken();
			str = Term(str);
			str = Expr_tail(str);
		}
		return str;
	}
	String Term(String str) throws MissingTokenException {
		str = Factor(str);
		str = Term_tail(str);
		return str;
	}
	String Term_tail(String str) throws MissingTokenException {
		if(token.tokenType.equals(TokenType.MULT_OP)) {
			str = str + token.text;
			token = scan.nextToken();
			str = Factor(str);
			str = Term_tail(str);
		}
		else if(token.tokenType.equals(TokenType.DIV_OP)) {
			str = str + token.text;
			token = scan.nextToken();
			str = Term(str);
			str = Expr_tail(str);
		}
		else if(token.tokenType.equals(TokenType.MOD_OP)) {
			str = str + token.text;
			token = scan.nextToken();
			str = Term(str);
			str = Expr_tail(str);
		}
		else if(token.tokenType.equals(TokenType.EXPONENT)) {
			str = str + token.text;
			token = scan.nextToken();
			str = Term(str);
			str = Expr_tail(str);
		}
		return str;
	}
	String Factor(String str) throws MissingTokenException {
		if(token.tokenType.equals(TokenType.NUMBER)) {
			str = D(str);
			return str;
		}
		else if(token.tokenType.equals(TokenType.BRACKET_BEG)) {
			str = str + token.text;
			token = scan.nextToken();
			str = E(str);
			if(token.tokenType.equals(TokenType.BRACKET_END)) {
				str = str + token.text;
				token = scan.nextToken();
				return str;
			}
			else {
				throw new MissingTokenException(token.text + " found. ) is missing.");
			}
		}
		else if(token.tokenType.equals(TokenType.IDENTIFIER)) {
			str = L(str);
			return str;
		}
		else {
			throw new MissingTokenException(token.text + " found. Factor (number, left par, or identifier) is missing.");
		}
	}
	String L(String str) {
		if(token.tokenType.equals(TokenType.IDENTIFIER)) {
			str = str + token.text;
			token = scan.nextToken();
			
		}
		return str;
	}
	String D(String str) {
		if(token.tokenType.equals(TokenType.NUMBER)) {
			str = str + token.text;
			token = scan.nextToken();
			
		}
		return str;
	}
}

class MissingTokenException extends Exception{
	public MissingTokenException(String message) {
		super(message);
	}
}

//Read the whole program text into a String variable and use it for extracting tokens
class Token {
	protected String text;
	protected TokenType tokenType;
	Token(String text){
		this.text=text;
	}
	TokenType getType() {
		return tokenType;
	}
}

class EOFTOKEN extends Token{
	EOFTOKEN(String text){
		super(text);
		this.tokenType = TokenType.END_OF_FILE;
	}
}
class NumberToken extends Token{
	NumberToken(String text){
		super(text);
		this.tokenType = TokenType.NUMBER;
	}
}
class IdentifierToken extends Token{
	IdentifierToken(String text){
		super(text);
		this.tokenType = TokenType.IDENTIFIER;
	}
}
class WhileBegToken extends Token{
	WhileBegToken(String text){
		super(text);
		this.tokenType = TokenType.WHILE_BEG;
	}
}
class WhileEndToken extends Token{
	WhileEndToken(String text){
		super(text);
		this.tokenType = TokenType.WHILE_END;
	}
}
class ConditionBegToken extends Token{
	ConditionBegToken(String text){
		super(text);
		this.tokenType = TokenType.CONDITION_BEG;
	}
}
class ConditionEndToken extends Token{
	ConditionEndToken(String text){
		super(text);
		this.tokenType = TokenType.CONDITION_END;
	}
}
class AssignmentToken extends Token{
	AssignmentToken(String text){
		super(text);
		this.tokenType = TokenType.ASSIGNMENT;
	}
}
class TrueFalseToken extends Token{
	TrueFalseToken(String text){
		super(text);
		this.tokenType = TokenType.TRUE_FALSE;
	}
}
class ElseIfToken extends Token{
	ElseIfToken(String text){
		super(text);
		this.tokenType = TokenType.ELSE_IF;
	}
}
class SemicolonToken extends Token{
	SemicolonToken(String text){
		super(text);
		this.tokenType = TokenType.SEMICOLON;
	}
}
class OutputToken extends Token{
	OutputToken(String text){
		super(text);
		this.tokenType = TokenType.OUTPUT;
	}
}
class InputToken extends Token{
	InputToken(String text){
		super(text);
		this.tokenType = TokenType.INPUT;
	}
}
class SubtOpToken extends Token{
	SubtOpToken(String text){
		super(text);
		this.tokenType = TokenType.SUBT_OP;
	}
}
class AddOpToken extends Token{
	AddOpToken(String text){
		super(text);
		this.tokenType = TokenType.ADD_OP;
	}
}
class MultOpToken extends Token{
	MultOpToken(String text){
		super(text);
		this.tokenType = TokenType.MULT_OP;
	}
}
class DivOpToken extends Token{
	DivOpToken(String text){
		super(text);
		this.tokenType = TokenType.DIV_OP;
	}
}
class ModOpToken extends Token{
	ModOpToken(String text){
		super(text);
		this.tokenType = TokenType.MOD_OP;
	}
}
class ExponentToken extends Token{
	ExponentToken(String text){
		super(text);
		this.tokenType = TokenType.EXPONENT;
	}
}
class BracketBegToken extends Token{
	BracketBegToken(String text){
		super(text);
		this.tokenType = TokenType.BRACKET_BEG;
	}
}
class BracketEndToken extends Token{
	BracketEndToken(String text){
		super(text);
		this.tokenType = TokenType.BRACKET_END;
	}
}
class ExpressionToken extends Token{
	ExpressionToken(String text){
		super(text);
		this.tokenType = TokenType.EXPRESSION;
	}
}
class ErrorToken extends Token{
	ErrorToken(String text){
		super(text);
		this.tokenType = TokenType.NRT_ERROR; //not recognized token error
	}
}
enum TokenType{
	WHILE_BEG("{"), WHILE_END("}"), CONDITION_BEG("["), CONDITION_END("]"), 
	ASSIGNMENT("="), TRUE_FALSE("?"), ELSE_IF(":"), SEMICOLON(";"), 
	OUTPUT("<"), INPUT(">"), SUBT_OP("-"), ADD_OP("+"), MULT_OP("*"), DIV_OP("/"), 
	MOD_OP("%"), EXPONENT("^"), BRACKET_BEG("("), BRACKET_END(")"), NUMBER, EXPRESSION, 
	IDENTIFIER, NRT_ERROR, END_OF_FILE;
	String text;
	TokenType(){
		this.text = this.toString();
	}
	TokenType(String text) {
		this.text = text;
	}
	
}


