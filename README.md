# Calculatrice de polynômes

Devoir de Java : on saisit des polynômes, on choisit des opérations, le programme affiche le résultat réduit. Plusieurs variables sont possibles (`2x^2y^3z + 2xy`). La division n'accepte que 2 polynômes.

## Lancer

```bash
javac -encoding UTF-8 *.java
java Calculatrice
```

## Saisie

```text
Nombre de polynômes : 2
Polynôme 1 : x^2 + x + 1
Polynôme 2 : x + 1
Opérations : +

Résultat : x^2 + 2x + 2
```

Un exposant s'écrit `x^2` (ou `x²`, `x³`). Les termes se séparent avec `+` et `-`.

Avec 3 polynômes ou plus, une seule opération est répétée : `+` calcule P1 + P2 + P3. On peut aussi écrire les opérations une par une, de gauche à droite : `* +` calcule (P1 * P2) + P3.

## Fichiers

- `Monome.java` : un terme, coefficient + exposants
- `Polynome.java` : la liste de termes, les opérations, la lecture
- `Calculatrice.java` : les questions et le `main`
- `CalculException.java` : les erreurs
- `RAPPORT.md` : analyse, structure, algorithmes
