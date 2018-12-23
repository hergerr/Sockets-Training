public class Tester{
	public static void main(String[] args) {
		Server server = new Server();
		new Client("X", "localhost");
        new Client("Y", "localhost");
//
//		server.setServerSocketAccepts(false);
//        new Client("Z", "localhost");

    }
}