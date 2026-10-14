import java.util.Scanner;

/**
 * Demande les polynômes et les opérations, puis affiche le résultat réduit.
 * La division n'est acceptée que pour 2 polynômes.
 */
public class Calculatrice {

    public static void main(String[] args) {
        Scanner lecture = new Scanner(System.in);
        try {
            System.out.println("Calculatrice de polynômes");
            System.out.println("Exemple : x^2 + x + 1    ou    2x^2y^3z + 2xy");
            System.out.println("Opérations : +  -  *  /");
            System.out.println();
            boolean encore = true;
            while (encore) {
                try {
                    calculer(lecture);
                } catch (CalculException e) {
                    System.out.println("Erreur : " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : nombre entier attendu.");
                }
                System.out.print("Autre calcul (o/n) ? ");
                String reponse = lecture.nextLine().trim();
                encore = reponse.equals("o") || reponse.equals("oui");
                System.out.println();
            }
        } finally {
            lecture.close();
        }
    }

    private static void calculer(Scanner lecture) throws CalculException {
        System.out.print("Nombre de polynômes : ");
        int nombre = Integer.parseInt(lecture.nextLine().trim());
        if (nombre < 1) {
            throw new CalculException("Il faut au moins un polynôme.");
        }

        Polynome[] polynomes = new Polynome[nombre];
        for (int i = 0; i < nombre; i++) {
            System.out.print("Polynôme " + (i + 1) + " : ");
            polynomes[i] = Polynome.lire(lecture.nextLine());
        }

        if (nombre == 1) {
            System.out.println("Résultat : " + polynomes[0]);
            return;
        }

        System.out.print("Opérations : ");
        String operations = preparer(lecture.nextLine(), nombre);
        System.out.println();

        if (operations.equals("/")) {
            Polynome[] division = polynomes[0].diviser(polynomes[1]);
            if (division[1].estNul()) {
                System.out.println("Résultat : " + division[0]);
            } else {
                System.out.println("Quotient : " + division[0]);
                System.out.println("Reste : " + division[1]);
            }
            return;
        }

        Polynome resultat = polynomes[0];
        for (int i = 0; i < operations.length(); i++) {
            char operation = operations.charAt(i);
            if (operation == '+') {
                resultat = resultat.ajouter(polynomes[i + 1]);
            } else if (operation == '-') {
                resultat = resultat.soustraire(polynomes[i + 1]);
            } else if (operation == '*') {
                resultat = resultat.multiplier(polynomes[i + 1]);
            } else {
                throw new CalculException("Opération inconnue : " + operation);
            }
        }
        System.out.println("Résultat : " + resultat);
    }

    /**
     * Une seule opération est répétée sur tous les polynômes.
     * Sinon il en faut exactement une de moins que de polynômes.
     * La division force ce nombre à 2.
     */
    private static String preparer(String saisie, int nombre) throws CalculException {
        String operations = "";
        if (saisie != null) {
            for (int i = 0; i < saisie.length(); i++) {
                char c = saisie.charAt(i);
                if (c == ' ' || c == ',') {
                    continue;
                }
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    operations = operations + c;
                } else {
                    throw new CalculException("Opération inconnue : " + c);
                }
            }
        }
        boolean division = false;
        for (int i = 0; i < operations.length(); i++) {
            if (operations.charAt(i) == '/') {
                division = true;
            }
        }
        if (division && nombre != 2) {
            throw new CalculException("La division nécessite exactement 2 polynômes.");
        }
        if (operations.length() == 1 && nombre > 2) {
            char operation = operations.charAt(0);
            operations = "";
            for (int i = 1; i < nombre; i++) {
                operations = operations + operation;
            }
        }
        if (operations.length() != nombre - 1) {
            throw new CalculException("Il faut " + (nombre - 1) + " opération(s).");
        }
        return operations;
    }
}
