// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        String[] spieler = new String[4];
        spieler[0] = "Hans";
        spieler[1] = "Peter";
        spieler[2] = "Klaus";
        spieler[3] = "Günther";

        for (int i = 0; i < spieler.length; i++) {
            if (spieler[i].equals("Peter")) {
                System.out.println("Peter ist an Stelle " + i);
            }
        }
    }
}
