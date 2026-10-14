# Calculatrice de polynômes

## 1. Découpage

Le programme suit l'ordre du sujet.

1. Demander combien de polynômes.
2. Lire chaque polynôme.
3. Demander les opérations, dans l'ordre.
4. Calculer et afficher le polynôme réduit.

Si une opération est `/`, on refuse autre chose que 2 polynômes : c'est la contrainte du sujet.

Avec plusieurs polynômes, le calcul se fait de gauche à droite. Si on ne donne qu'une opération, elle est répétée : `+` avec trois polynômes fait P1 + P2 + P3.

## 2. Structure de données

Un tableau `coefficient[degré]` ne marche pas ici. Il suppose une seule variable. Le sujet demande aussi `2x^2y^3z + 2xy`.

On coupe en deux classes.

**Monome** — un terme.

- `coefficient` : le nombre devant les lettres (`2` dans `2x^2y`)
- `exposants` : un tableau de 26 entiers. La case 0 est l'exposant de `a`, la case 23 est celui de `x`.

Pour `2x^2y^3z` : coefficient `2`, case de `x` = 2, case de `y` = 3, case de `z` = 1, le reste à 0.

Deux termes sont semblables quand les 26 cases sont égales. `2xy` et `yx` ont les mêmes cases, donc on additionne les coefficients.

**Polynome** — la somme.

- `termes` : un tableau de monômes
- `nb` : le nombre de cases vraiment utilisées

On ne garde pas les termes de coefficient 0. Après `x - x`, `nb` vaut 0 et on affiche `0`.

Avant d'afficher, on trie : plus haut degré d'abord, puis `x` avant `y` avant `z`. D'où `x^2 + 2x + 2` et `2x^2y^3z + 2xy`.

Si le tableau est plein, on en crée un deux fois plus grand et on recopie. C'est le seul agrandissement, il reste un tableau.

## 3. Algorithmes

**Ajouter un terme.** On parcourt le tableau. Si un terme a les mêmes exposants, on additionne les coefficients. Si la somme vaut 0, on décale les cases suivantes pour boucher le trou. Sinon on ajoute le terme à la fin.

**Addition.** On copie le premier polynôme, puis on ajoute chaque terme du second.

**Soustraction.** Même chose, avec l'opposé de chaque coefficient.

**Multiplication.** Double boucle : chaque terme du premier par chaque terme du second. On multiplie les coefficients et on additionne les exposants, lettre par lettre. `x^2y * xy^3z` donne `x^3y^4z`.

**Division.** On veut `P = quotient × diviseur + reste`.

```text
tant que le dividende n'est pas nul :
    A = son plus grand terme
    B = le plus grand terme du diviseur
    si chaque exposant de B est <= celui de A :
        T = A / B
        quotient = quotient + T
        dividende = dividende - T * diviseur
    sinon :
        ce terme passe dans le reste
        on l'enlève du dividende
```

Si le diviseur est 0, on lève `CalculException`.

Exemple : `(x^2 + 1) / (x + 1)`.

| Dividende | On ajoute au quotient | Reste |
| --- | --- | --- |
| `x^2 + 1` | `x` | `0` |
| `-x + 1` | `-1` | `0` |
| `2` | B ne divise pas 2 | `2` |

Quotient `x - 1`, reste `2`. Vérification : `(x - 1)(x + 1) + 2 = x^2 + 1`.

Si le reste est nul, on n'affiche que le quotient. Sinon on affiche les deux : le quotient seul ne serait pas égal au dividende.

## 4. Classes et cours

| Classe | Rôle |
| --- | --- |
| `Monome` | un terme |
| `Polynome` | la liste, `+` `-` `*` `/`, et `lire` |
| `Calculatrice` | les questions, le `main` |
| `CalculException` | polynôme vide, exposant négatif, division par zéro, mauvaise opération |

Ce qui vient du cours, et rien d'autre :

- attributs `private`, accesseurs `get` / `set`
- constructeur, `this`
- `toString` pour l'affichage
- méthode `static` pour `main` et pour `lire`, qui n'a pas besoin d'un polynôme déjà créé
- deux méthodes `multiplier` : une pour un polynôme, une pour un seul terme (surcharge)
- `CalculException extends Exception`, `throw`, `throws`, `try` / `catch` / `finally`

`finally` ferme le `Scanner` même si une erreur a eu lieu.

On n'a pas fait d'héritage entre monôme et polynôme : un polynôme contient des monômes, il n'est pas « un genre de monôme ».

## 5. Essais

| Calcul | Résultat |
| --- | --- |
| `(x^2 + x + 1) + (x + 1)` | `x^2 + 2x + 2` |
| `2x^2y^3z + 2xy` | `2x^2y^3z + 2xy` |
| `2yx + xy` | `3xy` |
| `(x + 1) * (x + 1)` | `x^2 + 2x + 1` |
| `(x + y) * (x - y)` | `x^2 - y^2` |
| `(x + 1) - (x + 1)` | `0` |
| `(x^2 - 1) / (x - 1)` | `x + 1` |
| `(x^2 + 1) / (x + 1)` | quotient `x - 1`, reste `2` |
| `(2x^2y^3z + 2xy) / (2xy)` | `xy^2z + 1` |
| division par `0` | erreur |
| `/` avec 3 polynômes | erreur |

## 6. Questions

**Pourquoi un tableau de 26 cases ?**
Pour ne pas fixer le nombre de variables à l'avance, et pour reconnaître `xy` et `yx` : les deux remplissent les mêmes cases.

**Pourquoi ne pas garder les coefficients nuls ?**
Le sujet demande la forme réduite. `x + 1 - x` doit s'afficher `1`, pas `0x + 1`.

**Pourquoi la division est limitée à 2 ?**
Le sujet le demande. Avec 3 polynômes, on ne saurait pas où mettre le reste.

**`throw` et `throws` ?**
`throws CalculException` se met sur la méthode qui peut échouer. `throw new CalculException(...)` crée l'erreur à l'endroit du problème. Le `catch` du `main` affiche le message.

**À quoi sert `private` ?**
Pour empêcher de modifier le tableau des exposants directement et de casser la forme réduite. On passe par `setCoefficient` et `setExposant`.
