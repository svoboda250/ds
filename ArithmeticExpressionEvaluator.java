/*
 * CIS 27: Lab 1
 * @author Jenny Hamer
 * Program 2: Evaluate arithmethic expressions using a stack
 * Step 1: create a filter InfixToPostfix to convert and print the expression --> postfix 
 * Step 2: create an evaluator EvaluatePostfix that inputs a postfix expression, calculates & prints its evaluation
 * (Print infix expression, postfix expression, and final result)
 */
package lab1;

import java.util.*;

public class ArithmeticExpressionEvaluator {

    String infix; // input --> expression to convert
    String postfix; // output expression in postfix / reverse Polish notation
    double result; // the evaluation of the expression
    StringBuffer createPost; // to build the output expression in postfix
    Stack<String> operatorStack = new Stack<>(); // will store operators during expression conversion
    Stack<Double> numberStack = new Stack<>(); // will store values while evaluating the postfix expr

    public ArithmeticExpressionEvaluator(String input) { // constructor for A.E.E. which takes an infix expression as input
        infix = input;
    }

    int precedence(String t) {
        // takes an operator token and returns its precendence
        // precedence is as follows:"*" & "/" --> 2, "+" & "-" --> 1
        switch (t) {
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            case "(":
            case ")":
                return -1;
            case " ": // to ignore spaces in inputted expression
                return -1;
        }
        return -1;
    }

    boolean isOperator(String t) {
        // takes a token and determines if it is a valid operator
        switch (t) {
            case "*":
            case "/":
            case "+":
            case "-":
            case "(":
            case ")":
                return true;
            default:
                break;
        }
        return false;
    }

    boolean isDouble(String t) { // check if the current element is parsable as a double
        try {
            Double.parseDouble(t);
            return true;
        } catch (NumberFormatException exc) {
            return false;
        }
    }
    // filter method: converts infix expression to its postfix equivalent 
    // & prints both expressions to the console

    public String InfixToPostfix(String in) {
        infix = in;
        String noSpaces = infix.replaceAll(" ", ""); // to determine length of expr
        int infixLen = noSpaces.length();
        createPost = new StringBuffer(infixLen); // used to create the postfix expr
        Scanner scan = new Scanner(infix);

        while (scan.hasNext()) { // check if there are more tokens to scan
            String token = scan.next(); // gets the next token in the scanner's input
            if (isOperator(token)) {
                /* if the expression contains parens, push left parens onto the 
                 * operator stack; once the right paren is found, pop the ops from
                 * the stack and add them to the postfix expression until the 
                 * left paren is on top, then pop it from the stack (so no parens in final expr)
                 */
                if (token.equals("(")) {
                    operatorStack.push(token);
                } // push the beginning left paren onto the stack 
                else if (token.equals(")")) {
                    while (!operatorStack.peek().equals("(")) {
                        createPost.append(operatorStack.pop());
                    }
                } else {
                    /* while there are elems in the op stack & the stack's top elem has 
                     * higher precedence than the current scanned op, pop top elem 
                     * & add it to the postfix expression (except for parentheses)
                     */
                    while (!operatorStack.isEmpty() && (precedence(operatorStack.peek()) >= precedence(token))) {
                        if (precedence(operatorStack.peek()) > 0) {
                            createPost.append(operatorStack.pop());
                        } else {
                            operatorStack.pop();
                        }
                    }
                } // if no tokens in op stack or lower prec, push to the op stack
                operatorStack.push(token);
            } else { // if the token is an operand
                createPost.append(token);
            }
        }
        // while the operator stack has elems left, they are added one by one to 
        // the postfix expression, except for parentheses
        while (!operatorStack.isEmpty()) {
            if (precedence(operatorStack.peek()) > 0) {
                createPost.append(operatorStack.pop());
            } else {
                operatorStack.pop();
            } // parens are removed from the stack
        }
        scan.close(); // no more elems to scan --> close the Scanner
        postfix = createPost.toString();
        postfix = postfix.replaceAll("", " "); // legible formatting
        return postfix; // returns the reformatted postfix expression
    }

    public double PostfixEvaluator(String result) {
        postfix = result;
        Scanner pscan = new Scanner(postfix); // postfix Scanner
        while (pscan.hasNext()) { // while there are still elements of expr to be scanned
            // if an operator was scanned, pop the two top numbers on the stack 
            // and apply the operator to them; update the result with this value
            String currElem = pscan.next();
            if (isDouble(currElem)) {
                numberStack.push(Double.parseDouble(currElem));
            } else if (isOperator(currElem)) {
                double num = numberStack.pop();
                if (currElem.equals("*")) {
                    num = numberStack.pop() * num;
                } else if (currElem.equals("/")) {
                    num = numberStack.pop() / num;
                } else if (currElem.equals("+")) {
                    num = numberStack.pop() + num;
                } else if (currElem.equals("-")) {
                    num = numberStack.pop() - num;
                }
                numberStack.push(num);
            }
        }
        return numberStack.pop();
    }

    public static void main(String[] args) {
        // testing the postfix expression translation
        // entered expressions must have a space in between each element
        String input1 = "( 1 + 2 ) * ( 3 / 4 )";
        String input2 = "( 5 * 5 ) + ( 2 + 3 ) / ( 6 - 1 )";
        String input3 = "1 + 2 * 3 / 4 - 5";
        String input4 = "5 * 2 + 3 / 4 - 2";
        String input5 = "4 + ( 8 / 2 ) * 2";
        ArithmeticExpressionEvaluator test1 = new ArithmeticExpressionEvaluator(input1);
        ArithmeticExpressionEvaluator test2 = new ArithmeticExpressionEvaluator(input2);
        ArithmeticExpressionEvaluator test3 = new ArithmeticExpressionEvaluator(input3);
        ArithmeticExpressionEvaluator test4 = new ArithmeticExpressionEvaluator(input4);
        ArithmeticExpressionEvaluator test5 = new ArithmeticExpressionEvaluator(input5);
        //String evaluation = EvaluatePostfix(expression);
        //System.out.println("Final result: " + evaluation);
        System.out.println("Before, in infix: " + input1);
        String result1 = test1.InfixToPostfix(input1);
        System.out.println("After, in postfix: " + result1 + "\n================");
        double result12 = test1.PostfixEvaluator(result1);
        System.out.println("Evaluation result --> " + result12 + "\n================");

        System.out.println("Before, in infix: " + input2);
        String result2 = test2.InfixToPostfix(input2);
        System.out.println("After, in postfix: " + result2 + "\n================");
        double result22 = test2.PostfixEvaluator(result2);
        System.out.println("Evaluation result --> " + result22 + "\n================");

        System.out.println("Before, in infix: " + input3);
        String result3 = test3.InfixToPostfix(input3);
        System.out.println("After, in postfix: " + result3 + "\n================");
        double result32 = test3.PostfixEvaluator(result3);
        System.out.println("Evaluation result --> " + result32 + "\n================");

        System.out.println("Before, in infix: " + input4);
        String result4 = test4.InfixToPostfix(input4);
        System.out.println("After, in postfix: " + result4 + "\n================");
        double result42 = test4.PostfixEvaluator(result4);
        System.out.println("Evaluation result --> " + result42 + "\n================");

        System.out.println("Before, in infix: " + input5);
        String result5 = test5.InfixToPostfix(input5);
        System.out.println("After, in postfix: " + result5 + "\n================");
        double result52 = test5.PostfixEvaluator(result5);
        System.out.println("Evaluation result --> " + result52 + "\n================");
    }
}

/* OUTPUT: 	
 Before, in infix: ( 1 + 2 ) * ( 3 / 4 )
After, in postfix:  1 2 + 3 4 / * 
================
Evaluation result --> 2.25
================
Before, in infix: ( 5 * 5 ) + ( 2 + 3 ) / ( 6 - 1 )
After, in postfix:  5 5 * 2 3 + 6 1 - / + 
================
Evaluation result --> 26.0
================
Before, in infix: 1 + 2 * 3 / 4 - 5
After, in postfix:  1 2 3 * 4 / + 5 - 
================
Evaluation result --> -2.5
================
Before, in infix: 5 * 2 + 3 / 4 - 2
After, in postfix:  5 2 * 3 4 / + 2 - 
================
Evaluation result --> 8.75
================
Before, in infix: 4 + ( 8 / 2 ) * 2
After, in postfix:  4 8 2 / 2 * + 
================
Evaluation result --> 12.0
================
BUILD SUCCESSFUL (total time: 2 seconds)
 */
