/**
 * Un polynôme est une liste de monômes non nuls.
 * On les range dans un tableau, du plus haut degré au plus bas.
 * Exemple : x^2 + x + 1  a trois cases.
 */
public class Polynome {

    private Monome[] termes;
    private int nb;

    public Polynome() {
        termes = new Monome[50];
        nb = 0;
    }

    public int getNbTermes() {
        return nb;
    }

    public Monome getTerme(int i) {
        return termes[i].copie();
    }

    public boolean estNul() {
        return nb == 0;
    }

    /** Ajoute un terme et regroupe tout de suite les termes semblables. */
    public void ajouterTerme(Monome monome) {
        if (monome == null || monome.estNul()) {
            return;
        }
        for (int i = 0; i < nb; i++) {
            if (termes[i].memesExposants(monome)) {
                termes[i].setCoefficient(termes[i].getCoefficient() + monome.getCoefficient());
                if (termes[i].estNul()) {
                    supprimer(i);
                }
                return;
            }
        }
        if (nb == termes.length) {
            Monome[] plusGrand = new Monome[termes.length * 2];
            for (int i = 0; i < nb; i++) {
                plusGrand[i] = termes[i];
            }
            termes = plusGrand;
        }
        termes[nb] = monome.copie();
        nb = nb + 1;
    }

    public Polynome copie() {
        Polynome copie = new Polynome();
        for (int i = 0; i < nb; i++) {
            copie.ajouterTerme(termes[i]);
        }
        return copie;
    }

    public Polynome ajouter(Polynome autre) {
        Polynome somme = copie();
        for (int i = 0; i < autre.nb; i++) {
            somme.ajouterTerme(autre.termes[i]);
        }
        somme.trier();
        return somme;
    }

    public Polynome soustraire(Polynome autre) {
        Polynome difference = copie();
        for (int i = 0; i < autre.nb; i++) {
            Monome oppose = autre.termes[i].copie();
            oppose.setCoefficient(-oppose.getCoefficient());
            difference.ajouterTerme(oppose);
        }
        difference.trier();
        return difference;
    }

    public Polynome multiplier(Polynome autre) {
        Polynome produit = new Polynome();
        for (int i = 0; i < nb; i++) {
            for (int j = 0; j < autre.nb; j++) {
                produit.ajouterTerme(termes[i].multiplier(autre.termes[j]));
            }
        }
        produit.trier();
        return produit;
    }

    public Polynome multiplier(Monome monome) {
        Polynome produit = new Polynome();
        for (int i = 0; i < nb; i++) {
            produit.ajouterTerme(termes[i].multiplier(monome));
        }
        produit.trier();
        return produit;
    }

    /**
     * Division : P = quotient * diviseur + reste.
     * On renvoie un tableau de 2 cases : [0] quotient, [1] reste.
     */
    public Polynome[] diviser(Polynome diviseur) throws CalculException {
        if (diviseur == null || diviseur.estNul()) {
            throw new CalculException("Division par le polynôme nul.");
        }
        Polynome quotient = new Polynome();
        Polynome reste = new Polynome();
        Polynome courant = copie();
        Monome dominantDiviseur = diviseur.termes[0];
        int etapes = 0;
        while (!courant.estNul()) {
            etapes = etapes + 1;
            if (etapes > 10000) {
                throw new CalculException("Division impossible.");
            }
            Monome dominant = courant.termes[0];
            if (dominantDiviseur.divise(dominant)) {
                Monome terme = dominant.diviserPar(dominantDiviseur);
                quotient.ajouterTerme(terme);
                courant = courant.soustraire(diviseur.multiplier(terme));
            } else {
                reste.ajouterTerme(dominant);
                courant.supprimer(0);
            }
        }
        quotient.trier();
        reste.trier();
        Polynome[] resultat = new Polynome[2];
        resultat[0] = quotient;
        resultat[1] = reste;
        return resultat;
    }

    public String toString() {
        if (nb == 0) {
            return "0";
        }
        String texte = "";
        for (int i = 0; i < nb; i++) {
            double coefficient = termes[i].getCoefficient();
            if (i == 0) {
                if (coefficient < 0) {
                    texte = texte + "-";
                }
            } else if (coefficient < 0) {
                texte = texte + " - ";
            } else {
                texte = texte + " + ";
            }
            texte = texte + termes[i].sansSigne();
        }
        return texte;
    }

    /**
     * Lit un polynôme : x^2 + x + 1, 2x^2y^3z + 2xy, x² + 3.
     * L'exposant s'écrit avec ^ (ou ² et ³).
     */
    public static Polynome lire(String texte) throws CalculException {
        if (texte == null) {
            throw new CalculException("Polynôme vide.");
        }
        String s = texte.replace(" ", "").replace("*", "").replace(",", ".");
        s = s.replace("²", "^2").replace("³", "^3");
        if (s.length() == 0) {
            throw new CalculException("Polynôme vide.");
        }
        Polynome polynome = new Polynome();
        int i = 0;
        int signe = 1;
        if (s.charAt(0) == '+') {
            i = 1;
        } else if (s.charAt(0) == '-') {
            signe = -1;
            i = 1;
        }
        if (i >= s.length()) {
            throw new CalculException("Polynôme incomplet.");
        }
        while (i < s.length()) {
            double coefficient = 1;
            boolean coefficientLu = false;
            char c = s.charAt(i);
            if ((c >= '0' && c <= '9') || c == '.') {
                int debut = i;
                while (i < s.length() && ((s.charAt(i) >= '0' && s.charAt(i) <= '9') || s.charAt(i) == '.')) {
                    i = i + 1;
                }
                try {
                    coefficient = Double.parseDouble(s.substring(debut, i));
                } catch (NumberFormatException e) {
                    throw new CalculException("Coefficient invalide.");
                }
                coefficientLu = true;
            }
            Monome monome = new Monome(signe * coefficient);
            if (i < s.length() && estLettre(s.charAt(i))) {
                while (i < s.length() && estLettre(s.charAt(i))) {
                    char variable = s.charAt(i);
                    i = i + 1;
                    int exposant = 1;
                    if (i < s.length() && s.charAt(i) == '^') {
                        i = i + 1;
                        if (i < s.length() && s.charAt(i) == '-') {
                            throw new CalculException("Exposant négatif interdit.");
                        }
                        int debut = i;
                        while (i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                            i = i + 1;
                        }
                        if (debut == i) {
                            throw new CalculException("Exposant manquant.");
                        }
                        exposant = Integer.parseInt(s.substring(debut, i));
                    }
                    if (exposant != 0) {
                        monome.setExposant(variable, monome.getExposant(variable) + exposant);
                    }
                }
            } else if (!coefficientLu) {
                throw new CalculException("Terme invalide.");
            }
            polynome.ajouterTerme(monome);
            if (i >= s.length()) {
                break;
            }
            char separateur = s.charAt(i);
            if (separateur == '+') {
                signe = 1;
            } else if (separateur == '-') {
                signe = -1;
            } else {
                throw new CalculException("Symbole inattendu : " + separateur);
            }
            i = i + 1;
            if (i >= s.length()) {
                throw new CalculException("Polynôme incomplet.");
            }
        }
        polynome.trier();
        return polynome;
    }

    private void supprimer(int index) {
        for (int j = index; j < nb - 1; j++) {
            termes[j] = termes[j + 1];
        }
        nb = nb - 1;
    }

    /** Tri par insertion : le plus grand monôme passe devant. */
    private void trier() {
        for (int i = 1; i < nb; i++) {
            Monome courant = termes[i];
            int j = i - 1;
            while (j >= 0 && termes[j].comparer(courant) < 0) {
                termes[j + 1] = termes[j];
                j = j - 1;
            }
            termes[j + 1] = courant;
        }
    }

    private static boolean estLettre(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
}
