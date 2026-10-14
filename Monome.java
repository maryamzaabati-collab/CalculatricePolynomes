/**
 * Un terme : 2x^2y^3z.
 * Le coefficient est un nombre.
 * Les exposants sont un tableau de 26 cases : la case 0 est a, la case 23 est x.
 * Deux termes semblables ont exactement le même tableau.
 */
public class Monome {

    private double coefficient;
    private int[] exposants;

    public Monome(double coefficient) {
        exposants = new int[26];
        setCoefficient(coefficient);
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        if (Math.abs(coefficient) < 0.000000001) {
            this.coefficient = 0;
        } else if (Math.abs(coefficient - Math.round(coefficient)) < 0.000000001) {
            this.coefficient = Math.round(coefficient);
        } else {
            this.coefficient = coefficient;
        }
    }

    public int getExposant(char variable) {
        return exposants[indice(variable)];
    }

    public void setExposant(char variable, int exposant) {
        exposants[indice(variable)] = exposant;
    }

    public int getDegre() {
        int degre = 0;
        for (int i = 0; i < 26; i++) {
            degre = degre + exposants[i];
        }
        return degre;
    }

    public boolean estNul() {
        return coefficient == 0;
    }

    public boolean memesExposants(Monome autre) {
        for (int i = 0; i < 26; i++) {
            if (exposants[i] != autre.exposants[i]) {
                return false;
            }
        }
        return true;
    }

    /** Positif si ce monôme est plus grand (degré, puis a, puis b, ...). */
    public int comparer(Monome autre) {
        int ecart = getDegre() - autre.getDegre();
        if (ecart != 0) {
            return ecart;
        }
        for (int i = 0; i < 26; i++) {
            if (exposants[i] != autre.exposants[i]) {
                return exposants[i] - autre.exposants[i];
            }
        }
        return 0;
    }

    public Monome copie() {
        Monome copie = new Monome(coefficient);
        for (int i = 0; i < 26; i++) {
            copie.exposants[i] = exposants[i];
        }
        return copie;
    }

    public Monome multiplier(Monome autre) {
        Monome produit = new Monome(coefficient * autre.coefficient);
        for (int i = 0; i < 26; i++) {
            produit.exposants[i] = exposants[i] + autre.exposants[i];
        }
        return produit;
    }

    /** Vrai si CE monôme divise l'autre (exposants plus petits ou égaux). */
    public boolean divise(Monome autre) {
        if (estNul()) {
            return false;
        }
        for (int i = 0; i < 26; i++) {
            if (exposants[i] > autre.exposants[i]) {
                return false;
            }
        }
        return true;
    }

    public Monome diviserPar(Monome diviseur) {
        Monome quotient = new Monome(coefficient / diviseur.coefficient);
        for (int i = 0; i < 26; i++) {
            quotient.exposants[i] = exposants[i] - diviseur.exposants[i];
        }
        return quotient;
    }

    /** Affichage sans le signe : x^2, 3, 2xy. */
    public String sansSigne() {
        double absolu = Math.abs(coefficient);
        String nombre;
        if (Math.abs(absolu - Math.round(absolu)) < 0.000000001) {
            nombre = "" + (long) Math.round(absolu);
        } else {
            nombre = "" + absolu;
        }
        String variables = "";
        for (int i = 0; i < 26; i++) {
            if (exposants[i] != 0) {
                variables = variables + (char) ('a' + i);
                if (exposants[i] != 1) {
                    variables = variables + "^" + exposants[i];
                }
            }
        }
        if (variables.length() == 0) {
            return nombre;
        }
        if (Math.abs(absolu - 1) < 0.000000001) {
            return variables;
        }
        return nombre + variables;
    }

    public String toString() {
        if (coefficient < 0) {
            return "-" + sansSigne();
        }
        return sansSigne();
    }

    private int indice(char variable) {
        if (variable >= 'A' && variable <= 'Z') {
            variable = (char) (variable - 'A' + 'a');
        }
        return variable - 'a';
    }
}
