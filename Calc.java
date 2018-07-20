/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

/**
 *
 * @author Nikita
 */
public class Calc {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
    BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        String InputString;
        String Exit;
        do{
            try {
                System.out.println("Введте выражение для расчета. Поддерживаются цифры, операции +,-,*,/,^,% и приоритеты в виде скобок ( и ):");
                InputString = d.readLine();
                InputString = opn(InputString);
                //System.out.print("\n"+InputString);
                System.out.println(calculate(InputString));
                System.out.println("Продолжить?Y/N");
                Exit = d.readLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Продолжить?Y/N");
                Exit = d.readLine();
            }
        } while(Exit.equals("Y"));
    }

    /**
     * Преобразовать строку в обратную польскую нотацию
     * @param InputString Входная строка
     * @return Выходная строка в обратной польской нотации
     */
    private static String opn(String InputString) throws Exception {
        StringBuilder sbStack = new StringBuilder("");
        StringBuilder sbOut = new StringBuilder("");
        char cIn, cTmp;

        for (int i = 0; i < InputString.length(); i++) {
            cIn = InputString.charAt(i);
            if (isOp(cIn)) {
                while (sbStack.length() > 0) {
                    cTmp = sbStack.substring(sbStack.length()-1).charAt(0);
                    //System.out.print(cIn+" cTmp\n");
                    if (isOp(cTmp) && (opPrior(cIn) <= opPrior(cTmp))) {
                        sbOut.append(" ").append(cTmp).append(" ");
                        sbStack.setLength(sbStack.length()-1);
                        //System.out.print(cIn+" 2cIn\n");
                    } else {
                        sbOut.append(" ");
                        break;
                    }
                }
                sbOut.append(" ");
                sbStack.append(cIn);
                // System.out.print(sbOut.toString()+" |||sbOut|||"+sbStack.toString()+"|||sbStack|||\n");
            } else if ('(' == cIn) {
                sbStack.append(cIn);
            } else if (')' == cIn) {
                cTmp = sbStack.substring(sbStack.length()-1).charAt(0);
                while ('(' != cTmp) {
                    if (sbStack.length() < 1) {
                        throw new Exception("Ошибка разбора скобок. Проверьте правильность выражения.");
                    }
                    sbOut.append(" ").append(cTmp);
                    //System.out.print(sbOut.toString()+" ||2 sbOut|||\n");
                    sbStack.setLength(sbStack.length()-1);
                    cTmp = sbStack.substring(sbStack.length()-1).charAt(0);
                }
                sbStack.setLength(sbStack.length()-1);
            } else {
                // Если символ не оператор - добавляем в выходную последовательность
                //System.out.print(cIn+" 2cIn\r");
                sbOut.append(cIn);
                //System.out.print(sbOut.toString()+" ||sbOut|||\r");
            }
        }

        // Если в стеке остались операторы, добавляем их в входную строку
        while (sbStack.length() > 0) {
            sbOut.append(" ").append(sbStack.substring(sbStack.length()-1));
            sbStack.setLength(sbStack.length()-1);
        }
        //System.out.print(sbOut.toString()+" ret sbOut|||");
        return  sbOut.toString();
    }

    /**
     * Функция проверяет, является ли текущий символ оператором
     */
    private static boolean isOp(char c) {
        switch (c) {
            case '-':
            case '+':
            case '*':
            case '/':
            case '^':
                return true;
        }
        return false;
    }

    /**
     * Возвращает приоритет операции
     * @param op char
     * @return byte
     */
    private static byte opPrior(char op) {
        switch (op) {
            case '^':
                return 3;
            case '*':
            case '/':
            case '%':
                return 2;
        }
        return 1; // Тут остается + и -
    }

    /**
     * Считает выражение, записанное в обратной польской нотации
     * @param InputString
     * @return double result
     */
    private static double calculate(String InputString) throws Exception {
        double dA = 0, dB = 0;
        String sTmp;
        Deque<Double> stack = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(InputString);
        while(st.hasMoreTokens()) {
            try {
                sTmp = st.nextToken().trim();
                if (1 == sTmp.length() && isOp(sTmp.charAt(0))) {
                    if (stack.size() < 2) {
                        throw new Exception("Неверное количество данных в стеке для операции " + sTmp);
                    }
                    dB = stack.pop();
                    dA = stack.pop();
                    //System.out.print(dB+" sdb|||"+dA+" da\n");
                    //System.out.print(sTmp+"stmp");
                    //System.out.print(stack+"st");
                    switch (sTmp.charAt(0)) {
                        case '+':
                            dA += dB;
                            break;
                        case '-':
                            dA -= dB;
                            break;
                        case '/':
                            dA /= dB;
                            break;
                        case '*':
                            dA *= dB;
                            break;
                        case '%':
                            dA %= dB;
                            break;
                        case '^':
                            dA = Math.pow(dA, dB);
                            break;
                        default:
                            throw new Exception("Недопустимая операция " + sTmp);
                    }
                    stack.push(dA);
                    //System.out.print(dA+" 1da||\n");
                    //System.out.print(stack+"0st");
                } else {
                    dA = Double.parseDouble(sTmp);
                    stack.push(dA);
                    //System.out.print(dA+" 2da||\n");
                    //System.out.print(stack+"1st");
                }
            } catch (Exception e) {
                throw new Exception("Недопустимый символ в выражении");
            }
            //System.out.print(stack+"st\n");
        }

        if (stack.size() > 1) {
            throw new Exception("Количество операторов не соответствует количеству операндов");
        }

        return stack.pop();
    }
    
}
