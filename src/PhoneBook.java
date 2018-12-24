import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 *  Książka telefoniczna
 *   - Model danych książki telefonicznej
 *
 *  Autor: Tymoteusz Frankiewicz
 *   Data: 24 grudnia 2018 r.
 *
 */

public class PhoneBook {
    //klucz to imie, numer to wartosc
    private ConcurrentMap<String, String> contacts;


    public PhoneBook() {
        contacts = new ConcurrentHashMap<String, String>();
    }

    public String load(String fileName) {
        if (fileName != null || !fileName.equals("")) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
                contacts = (ConcurrentMap<String, String>) inputStream.readObject();
                return "OK";
            } catch (FileNotFoundException e) {
                return "ERROR " + e.getMessage();
            } catch (IOException e) {
                return "ERROR " + e.getMessage();
            } catch (ClassNotFoundException e) {
                return "ERROR " + e.getMessage();
            }
        } else return "ERROR - pusta nazwa pliku";

    }

    public String save(String fileName) {
        if (fileName != null || !fileName.equals("")) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
                outputStream.writeObject(contacts);
                return "OK";
            } catch (FileNotFoundException e) {
                return "ERROR " + e.getMessage();
            } catch (IOException e) {
                return "ERROR " + e.getMessage();
            }
        } else return "ERROR - pusta nazwa pliku";
    }

    public String get(String name) {
        if (!contacts.containsKey(name)) return "ERROR: Nie ma takiej osoby";
        else return "OK: " + contacts.get(name);
    }

    public String put(String name, String number) {
        if(name.equals("") || name == null || number.equals("") || number == null) return "ERROR: PUSTE POLA";
        if (contacts.containsKey(name)) {
            return "ERROR: Taka osoba istnieje. Wprowadz poprawne imie";
        } else {
            contacts.put(name, number);
            return "OK";
        }
    }

    public String replace(String name, String number) {
        if(name.equals("") || name == null || number.equals("") || number == null) return "ERROR: PUSTE POLA";
        if (!contacts.containsKey(name)) {
            return "ERROR: Nie ma takiej osoby";
        } else {
            contacts.put(name, number);
            return "OK";
        }
    }

    public String delete(String name) {
        if(name.equals("") || name == null) return "ERROR: PUSTE POLE IMIENIA";
        if (!contacts.containsKey(name)) {
            return "ERROR: Nie ma takiej osoby";
        } else {
            contacts.remove(name);
            return "OK";
        }
    }

    public String list() {
        StringBuilder sb = new StringBuilder("OK: ");
        for (String key : contacts.keySet()) {
            sb.append(key + " ");
        }
        return sb.toString();
    }

}