/*
 *  Książka telefoniczna
 *   - klasa testująca
 *
 *  Autor: Tymoteusz Frankiewicz
 *   Data: 24 grudnia 2018 r.
 *
 */


public class Tester{
	public static void main(String[] args) {
		Server server = new Server();
		new Client("X", "localhost");
        new Client("Y", "localhost");
    }
}