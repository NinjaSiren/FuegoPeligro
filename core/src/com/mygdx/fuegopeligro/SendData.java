package com.mygdx.fuegopeligro;

public class SendData {
    private static int number;
    private static String answer1;
    private static String answer2;
    private static String answer3;
    private static String question;

    public SendData(int num, String ans1, String ans2, String ans3, String ans4, String ans5, String que) {
        number = num;
        answer1 = ans1;
        answer2 = ans2;
        answer3 = ans3;
        question = que;
    }

    public static int getNumber() {
        return number;
    }

    public static String getAnswer1() {
        return answer1;
    }

    public static String getAnswer2() {
        return answer2;
    }

    public static String getAnswer3() {
        return answer3;
    }

    public static String getQuestion() {
        return question;
    }
}
