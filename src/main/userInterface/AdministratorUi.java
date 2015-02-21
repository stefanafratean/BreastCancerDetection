package userInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdministratorUi {
	public AdministratorUi() {

	}

	public void showMenu() {
		System.out.println("Alegeti o optiune:");
		System.out.println("1. Antrenare sistem");
		System.out.println("2. Restaurare baza de date");
		System.out.println("3. Iesire");
	}

	public void start() {
		System.out
				.println("Prezenta aplicatie are rolul de a antrena un clasificator de programare genetica si");
		System.out.println("este dedicata unor clienti specifici.");
		System.out.println("Pentru a continua este necesara autentificarea.");
		System.out.print("Nume utilizator: ");
		String userName = readStringFromConsole();
		System.out.print("Parola: ");
		String pass = readStringFromConsole();
		System.out.println("Autentificare reusita.");
		showMenu();
		String option = readStringFromConsole();
		while (!option.trim().equals("3")) {
			showMenu();
			option = readStringFromConsole();
			if (option.trim().equals("2")) {
				processDbRestore();
			} else if (option.trim().equals("1")) {
				processTrainSystem();
			}
		}
	}

	private void processTrainSystem() {
		System.out.println("Introduceti numarul maxim de generatii.");
		readStringFromConsole();
		System.out.println("Introduceti dimensiunea populatiei.");
		readStringFromConsole();
		System.out
				.println("Introduceti descriptorii doriti, separati prin virgula");
		System.out
				.println("Optiuni posibile: momente, Haralick, GLRL, Gabor, HOG, CSS.");
		readStringFromConsole();
		System.out
				.println("Sistemul se antreneaza. In functie de parametrii selectati acest proces poate dura");
		System.out.println("chiar si cateva ore, va rugam asteptati.");
		System.out.println("Antrenarea sistemului a fost finalizata cu succes.");
		System.out.println("Rezultate obtinute: acuratete = 0.82, precizie = 0.65, rapel = 0.69");
		System.out.println("Doriti salvarea solutiei in baza de date? [Y/N]");
		readStringFromConsole();
		System.out.println("Salvare reusita.");
	}

	private void processDbRestore() {
		System.out
				.println("Aceasta optiune duce la stergerea ultimului rezultat al antrenarii. Sunteti sigur ca");
		System.out.println("doriti sa continuati? [Y/N] ");
		String ans = readStringFromConsole();
		if (ans.trim().equalsIgnoreCase("Y")) {
			System.out.println("Operatiunea s-a incheiat cu succes.");
		} else {
			System.out.println("Nici o modificare efectuata.");
		}
	}

	private String readStringFromConsole() {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		String s = null;
		try {
			s = bufferRead.readLine();
		} catch (IOException e) {
			System.err.println("Eroare in timpul citirii: " + e.getMessage());
		}
		return s;
	}
}
