import java.util.*;

/**
 * Solves a Simple expression(Ex: 4+2*(10+2))
 * Only capable of solving a maximum of 9 numbers
 * Operators allowed: +,-,*,/.
 *  Note: If your expression contains '(', make sure you have an operator before.
 * @author Michael Romero
 *
 */
public class ExpressionSolver {
	private Stack<Node> stack;//ExpressionTree
	private Stack<Object> sum;//Hold solution
	private String[] inputs;//Hold Inputs
	
	public ExpressionSolver(){
		System.out.println("Please enter an expression below.");
		sum = new Stack<>();
		stack = new Stack<>();
		inputs = new String[10];
	}
	
	public String singleInputs(String input){
		int count = 0;
		String current = "";
		String finalInput = "";
		for(int i = 0; i < input.length();i++){
			char character = input.charAt(i);
			switch(character){
			case '*': 
				if(current.length()>0){
				inputs[count] = current;
				finalInput = finalInput + count;
				finalInput = finalInput + '*';
				current = "";
				count++;
				}
				else{
					finalInput = finalInput + '*';
				}
				break;
				 case '/':
					 if(current.length()>0){
						inputs[count] = current;
						finalInput = finalInput + count;
						finalInput = finalInput + '/';
						current = "";
						count++;
					 }
					 else{
						 finalInput = finalInput + '/';
					 }
					 break;
				 case '+':
					 if(current.length()>0) {
					 	inputs[count] = current;
						finalInput = finalInput + count;
						finalInput = finalInput + '+';
						current = "";
						count++;
					 }
					 else{
						 finalInput = finalInput + '+';
					 }
					 break;
				 case '-':
					 if(current.length()>0){
					 	inputs[count] = current;
						finalInput = finalInput + count;
						finalInput = finalInput + '-';
						current = "";
						count++;
					 }
					 else {
						 finalInput = finalInput + '-';
					 }
					 break;	
				 case '(':
					 finalInput = finalInput + '(';
				 break;
				 case')': 
					 inputs[count] = current;
					 finalInput = finalInput + count;
					 current = "";
					count++;
					 finalInput = finalInput + ')';
					 break;			 
			default:				
				current = current + character;				
			break;
		   }			
		}
		if(current.length()!= 0){
			inputs[count] = current;
			finalInput = finalInput + count;
		}
		return finalInput;
	}
	/**
	 * Converts the infix expression to a postfix expression
	 * @param String infix
	 * @return String postfix
	 */
	public String convertToPostfix(String infix) {	
		String input = singleInputs(infix);
    	Stack<Character> operatorStack = new Stack<>();
    	String postfix = "";  
    	char topOperator;
  
    	int i = 0;	
    	int length = input.length();
    	while(length != 0){
    		char nextCharacter = input.charAt(i);
    		switch(nextCharacter){
    			case '+' : case '-' : 
    				while(!operatorStack.isEmpty() && operatorStack.peek() != '('){
    					postfix = postfix + operatorStack.pop();
    				}
    				operatorStack.push(nextCharacter);
    			break;
    			 
    			case '*': case '/':
    				if(!operatorStack.isEmpty()){					
    					if(operatorStack.peek() == '+' || operatorStack.peek() == '-'){
    						operatorStack.push(nextCharacter); 
						}
    					else
        				{
        					while(!operatorStack.isEmpty() && operatorStack.peek() != '('){
            					postfix = postfix + operatorStack.pop();
            				}
            				operatorStack.push(nextCharacter);
        				}    	
    				}
    				else{
    					operatorStack.push(nextCharacter);
    				}
    				 				
    			break;
   
    			case '(':
    				operatorStack.push(nextCharacter);
    				break;
    			
    			case ')':
    				while(!operatorStack.isEmpty() && operatorStack.peek() != '(')
    				{
    					postfix = postfix + operatorStack.pop();	 					
    				}
    				if(operatorStack.peek() == '(')
    				{
    					operatorStack.pop();
    				}
    				break;					
    			default:  				
    				postfix = postfix + nextCharacter;
    			break;
    		}
    		i++;
    		length--;
    	}	
    	while(!operatorStack.isEmpty()){
    			topOperator = operatorStack.pop();
    			postfix = postfix + topOperator;
    	}  	
    	return postfix;
    }
	
	public Node expressionTree(String expression){
		if(expression.length() != 0 ){
			String postfix = convertToPostfix(expression);
			return expressionTreeHelper(postfix);
		}
		return null;
	}
	/**
	 * Makes an expression tree
	 * @param String input
	 * @return A Expression Tree
	 */
	private Node expressionTreeHelper(String input){
		int i = 0;//loop invariant
		int j = 0;//next index
		while(i < input.length()){
			char character = input.charAt(j);
				switch(character){
					case '*': case '/': case '+': case '-':	
					Node right = stack.pop();
					Node left =  stack.pop();
					stack.push(new Node(character, left, right));		
					break;					
					default: 		
						stack.push(new Node(character)); 
					break;
				}
				i++;
				j++;	 
		}
		Node last = stack.pop();		
		return last;
	}
	/**
	 * Takes in an expression
	 * Converts the expression to postfix 
	 * Makes an expression tree out of the postfix expression
	 * Evaluates the expression tree using PostOrder
	 * @param String infix
	 * @return an integer 
	 */
	public Object evaluateExpression(String infix){
		Node tree = expressionTree(infix);
		if(tree != null){
		evaluateExpressionHelper(tree);
		return sum.pop();
		}
		
		return "No expression entered";
	}
	/**
	 * Evaluates an Expression Tree
	 * Post Order Traversal
	 * Puts it on Stack
	 * @param Node tree
	 */
	public void evaluateExpressionHelper(Node tree){	
		//recurse if else, postfix - left right root
		if(tree.left != null){
			evaluateExpressionHelper(tree.left);
		}
		if(tree.right!= null){
			 evaluateExpressionHelper(tree.right);
		}
		char character = tree.data;
		switch(character){
			case '*': 
				Object right1 = sum.pop();
				Object left1 = sum.pop();
				//if both are strings, I need to get their input
				if(right1 instanceof Character && left1 instanceof Character){
					//cast OBJ to string
					char rightPop = (char) right1;
					//get index
					int rightCount = Character.getNumericValue(rightPop);
					
					
					char leftPop = (char) left1;
					int leftCount = Character.getNumericValue(leftPop);
					
					
					//get data
					String rightInput = inputs[rightCount];
					String leftInput = inputs[leftCount];
					
					int intRight = Integer.parseInt(rightInput);
					int intLeft = Integer.parseInt(leftInput);
					
					int intSum = intLeft * intRight;
					sum.push(intSum);
					
				}
				//only right is String
				else if(right1 instanceof Character && !(left1 instanceof Character)){
					char rightPop = (char) right1;
					int rightCount = Character.getNumericValue(rightPop);		
					String rightInput = inputs[rightCount];
					int parseInputs = Integer.parseInt(rightInput);
					
					
					int leftPop = (int)left1;
					
					int intSum = leftPop * parseInputs;
					
					sum.push(intSum);
				}
				
				//only left is String
				else if(left1 instanceof Character && !(right1 instanceof Character)){
					//you cant cast an int to an int
					int rightPop = (int)right1;
					
					
					char leftPop = (char)left1;		
					int leftCount= Character.getNumericValue(leftPop);
					String leftInput = inputs[leftCount];
					int parseInputs = Integer.parseInt(leftInput);
					
					int intSum = parseInputs * rightPop;
					
					sum.push(intSum);
				}
				//both are int
				else{
					int rightPop = (int)right1;
					int leftPop = (int) left1;
					int intSum = leftPop * rightPop;
					sum.push(intSum);	
				}	
				
			break;
			case '/':
				Object right2 = sum.pop();
				Object left2 = sum.pop();
				//if both are strings, I need to get their input
				if(right2 instanceof Character && left2 instanceof Character){
					//cast OBJ to string
					char rightPop = (char) right2;
					//get index
					int rightCount = Character.getNumericValue(rightPop);
					
					
					char leftPop = (char) left2;
					int leftCount = Character.getNumericValue(leftPop);
					
					
					//get data
					String rightInput = inputs[rightCount];
					String leftInput = inputs[leftCount];
					
					int intRight = Integer.parseInt(rightInput);
					int intLeft = Integer.parseInt(leftInput);
					
					int intSum = intLeft / intRight;
					sum.push(intSum);
					
				}
				//only right is String
				else if(right2 instanceof Character && !(left2 instanceof Character)){
					char rightPop = (char) right2;
					int rightCount = Character.getNumericValue(rightPop);		
					String rightInput = inputs[rightCount];
					int rightInputs = Integer.parseInt(rightInput);
					
					
					int leftPop = (int)left2;
					
					int intSum = leftPop / rightInputs;
					
					sum.push(intSum);
				}
				
				//only left is String
				else if(left2 instanceof Character && !(right2 instanceof Character)){
					//you cant cast an int to an int
					int rightPop = (int)right2;
					
					
					char leftPop = (char)left2;		
					int leftCount= Character.getNumericValue(leftPop);
					String leftInput = inputs[leftCount];
					int leftInputs = Integer.parseInt(leftInput);
					
					int intSum = leftInputs / rightPop;
					
					sum.push(intSum);
				}
				//both are int
				else{
					int rightPop = (int)right2;
					int leftPop = (int) left2;
					int intSum = leftPop / rightPop;
					sum.push(intSum);	
				}
				
			break;
			case '+':
				Object right = sum.pop();
				Object left = sum.pop();
				//if both are strings, I need to get their input
				if(right instanceof Character && left instanceof Character){
					//cast OBJ to string
					char rightPop = (char) right;
					//get index
					int rightCount = Character.getNumericValue(rightPop);
					
					
					char leftPop = (char) left;
					int leftCount = Character.getNumericValue(leftPop);
					
					
					//get data
					String rightInput = inputs[rightCount];
					String leftInput = inputs[leftCount];
					
					int intRight = Integer.parseInt(rightInput);
					int intLeft = Integer.parseInt(leftInput);
					
					int intSum = intLeft + intRight;
					sum.push(intSum);
					
				}
				//only right is String
				else if(right instanceof Character && !(left instanceof Character)){
					char rightPop = (char) right;
					int rightCount = Character.getNumericValue(rightPop);		
					String rightInput = inputs[rightCount];
					int rightInputs = Integer.parseInt(rightInput);
					
					
					int leftPop = (int)left;
					
					int intSum = leftPop + rightInputs;
					
					sum.push(intSum);
				}
				
				//only left is String
				else if(left instanceof Character && !(right instanceof Character)){
					//you cant cast an int to an int
					int rightPop = (int)right;
					
					
					char leftPop = (char)left;		
					int leftCount= Character.getNumericValue(leftPop);
					String leftInput = inputs[leftCount];
					int leftInputs = Integer.parseInt(leftInput);
					
					int intSum = leftInputs + rightPop;
					
					sum.push(intSum);
				}
				//both are int
				else{
					int rightPop = (int)right;
					int leftPop = (int) left;
					int intSum = leftPop + rightPop;
					sum.push(intSum);	
				}
			break;
			case '-':
				Object right4 = sum.pop();
				Object left4 = sum.pop();
				//if both are strings, I need to get their input
				if(right4 instanceof Character && left4 instanceof Character){
					//cast OBJ to string
					char rightPop = (char) right4;
					//get index
					int rightCount = Character.getNumericValue(rightPop);
					
					
					char leftPop = (char) left4;
					int leftCount = Character.getNumericValue(leftPop);
					
					
					//get data
					String rightInput = inputs[rightCount];
					String leftInput = inputs[leftCount];
					
					int intRight = Integer.parseInt(rightInput);
					int intLeft = Integer.parseInt(leftInput);
					
					int intSum = intLeft - intRight;
					sum.push(intSum);
					
				}
				//only right is String
				else if(right4 instanceof Character && !(left4 instanceof Character)){
					char rightPop = (char) right4;
					int rightCount = Character.getNumericValue(rightPop);		
					String rightInput = inputs[rightCount];
					int rightInputs = Integer.parseInt(rightInput);
					
					
					int leftPop = (int)left4;
					
					int intSum = leftPop - rightInputs;
					
					sum.push(intSum);
				}
				
				//only left is String
				else if(left4 instanceof Character && !(right4 instanceof Character)){
					//you cant cast an int to an int
					int rightPop = (int)right4;
					
					
					char leftPop = (char)left4;		
					int leftCount= Character.getNumericValue(leftPop);
					String leftInput = inputs[leftCount];
					int leftInputs = Integer.parseInt(leftInput);
					
					int intSum = leftInputs - rightPop;
					
					sum.push(intSum);
				}
				//both are int
				else{
					int rightPop = (int)right4;
					int leftPop = (int) left4;
					int intSum = leftPop - rightPop;
					sum.push(intSum);	
				}
			break;	
			
			default:
				sum.push(character);
			break;		
		
		}		
	}	
	public class Node {
		private Node right;
		private Node left;
		private char data;
		
		public Node(char data, Node lf, Node rt){
			this.data = data;
			this.right = rt;
			this.left = lf;
		}
		public Node(char data){
			this(data,null,null);			
		}
	}	
	public static void main(String[] args){	
		ExpressionSolver et = new ExpressionSolver();
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		System.out.println(et.evaluateExpression(input));
		scanner.close();		
	}	
}

