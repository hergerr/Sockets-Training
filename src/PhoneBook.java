import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PhoneBook{
	//klucz to imie, numer to wartosc
	private ConcurrentMap<String, String> contacts;

	
	public PhoneBook() {
		contacts = new ConcurrentHashMap<String, String>();
	}

	@SuppressWarnings("unchecked")
	public String load(String fileName) {
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))){
			contacts =  (ConcurrentMap<String, String>) inputStream.readObject();
			return "OK";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "ERROR" + e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR" + e.getMessage();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "ERROR" + e.getMessage();
		}
	}
	
	public String save(String fileName) {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
		    outputStream.writeObject(contacts);
			return "OK";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "ERROR" + e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR" + e.getMessage();
		}
	}
	
	public String get(String name) {
		if(!contacts.containsKey(name)) return "ERROR: Nie ma takiej osoby";
		else return "OK: " + contacts.get(name);
	}
	
	public String put(String name, String number) {
		if(contacts.containsKey(name)) {
			return "ERROR: Taka osoba istnieje. Numer zostanie nadpisany";
		} else {
			contacts.put(name, number);
			return "OK";
		}
	}
	
	public String replace(String name, String number) {
		if(!contacts.containsKey(name)) {
			return "ERROR: Nie ma takiej osoby";
		} else {
			contacts.put(name, number);
			return "OK";
		}
	}
	
	public String delete(String name) {
		if(!contacts.containsKey(name)) {
			return "ERROR: Nie ma takiej osoby";
		} else {
			contacts.remove(name);
			return "OK";
		}
	}
	
	public String list() {
		StringBuilder sb = new StringBuilder("OK: ");
		for(String key: contacts.keySet()) {
			sb.append(key + " ");
		}
		return sb.toString();
	}
	
}