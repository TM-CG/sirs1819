package pt.ulisboa.tecnico.sirs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BoxUI {

    private String question;

    public BoxUI(String question) {
        this.question = question;
    }

    public void show() {
        System.out.println();
        System.out.print("+");

        for (int i = 0; i < question.length(); i++)
            System.out.print("-");
        System.out.println("+");

        System.out.println(question);

        System.out.print("+");
        for (int i = 0; i < question.length(); i++)
            System.out.print("-");
        System.out.println("+");

        System.out.flush();

    }

    public String showAndGet() {
        String inputString = null;

        System.out.println();
        System.out.print("Your answer is: ");

        System.out.flush();

        show();

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            return bufferRead.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
