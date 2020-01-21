package com.mygdx.fuegopeligro;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QAReader {
    private int qaNumber;
    private String qaAnswerT;
    private String qaAnswerF1;
    private String qaAnswerF2;
    private String qaAnswerF3;
    private String qaAnswerF4;
    private String qaQuestion;
    private String[] data;

    public QAReader(String fileName, int qaNumber) {
        readBooksFromCSV(fileName, qaNumber);
    }

    private void readBooksFromCSV(String fileName, int number) {
        List<SendData> dates = new ArrayList<>();

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            // read the first line from the text file
            for (int i = 0; i < number; i++) {
                br.readLine();
            }
            String line = br.readLine();

            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");
                SendData createData2 = createData2(attributes);
                createData(attributes);

                // adding book into ArrayList
                dates.add(createData2);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private SendData createData2(String[] metadata) {
        // create and return book of this metadata
        qaNumber = Integer.parseInt(metadata[0]);
        qaAnswerT = metadata[1];
        qaAnswerF1 = metadata[2];
        qaAnswerF2 = metadata[3];
        qaAnswerF3 = metadata[4];
        qaAnswerF4 = metadata[5];
        qaQuestion = metadata[6];

        return new SendData(qaNumber, qaAnswerT, qaAnswerF1, qaAnswerF2, qaAnswerF3, qaAnswerF4, qaQuestion);
    }

    private void createData(String[] metadata) {
        // create and return book of this metadata
        qaNumber = Integer.parseInt(metadata[0]);
        qaAnswerT = metadata[1];
        qaAnswerF1 = metadata[2];
        qaAnswerF2 = metadata[3];
        qaAnswerF3 = metadata[4];
        qaAnswerF4 = metadata[5];
        qaQuestion = metadata[6];
        data = metadata;
    }

    public int getQaNumber() {
        return qaNumber;
    }

    public void setQaNumber(int value) {
        qaNumber = value;
    }

    public String getQaAnswerT() {
        return qaAnswerT;
    }

    public String getQaAnswerF1() {
        return qaAnswerF1;
    }

    public String getQaAnswerF2() {
        return qaAnswerF2;
    }

    public String getQaAnswerF3() {
        return qaAnswerF3;
    }

    public String getQaAnswerF4() {
        return qaAnswerF4;
    }

    public String getQaQuestion() {
        return qaQuestion;
    }

    public String[] getData() {
        return data;
    }
}