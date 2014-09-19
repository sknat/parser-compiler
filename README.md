# Auto generation of syntaxic analysers

This project is a java grammar parser that is intended to show the ability of a complier to compile itself (YACC - like)

This code is distributed under the GPL3 licence,
Original authors being Nathan Skrzypczak and Alexandre Nolin, June 2013


# Code documentation (in french)

## Interpréteur pour la calculatrice de base

L'objectif est d'interpréter une grammaire du type suivant (implémentant une calculatrice simple), et de réussir à parser des expressions de la forme `1+2*2`
```
expr ::=
prod '+ expr -> { $1 + $3 }
| prod '- expr -> { $1 - $3 }
| prod -> { $1 }
;; 

prod ::=
fun '* prod -> { $1 * $3 }
| fun '/ prod -> { $1 / $3 }
| fun -> { $1 }
;; 

fun ::=
int -> { $1 }
| '( expr ') -> { $2 }
;;
```


### Lexeur de la calculatrice de Base


La toute première étape aura été de réaliser un lexeur adapté aux besoins du code. Nous n'avons en effet pas besoin ici d'investir dans l'utilisation de bibliothèques plus évoluées, à moins de se lancer dans un usage plus approfondi de l'interpréteur.

Avec ce souci de simplicité, il a été rapidement fait le choix d'utiliser les espaces comme séparateurs entre les différents lexèmes. Cela génère de la complexité à l'entrée (il faut donner `1 + 2 * 2 et non 1+2*2`) mais permet la gestion d'expressions plus complexes sans avoir besoin de s'attarder sur le Lexer, qui n'est ici pas le point central.

Les objets choisis pour les entrées/sorties seront tout d'abord des BufferReader (java.io) construits à partir de StringReader, et leurs équivalents en écriture. Ce pour des raisons de facilité d'écriture et de rapidité sur la manipulation de chaînes avec l'opérateur « + ». 

Par la suite, pour la gestion de fichiers, le type retenu a été RandomAccessFile, qui a été étendu pour lui ajouter les capacités de marquage des BufferedReader d'une manière plus simple et compréhensible. L'interprétation des grammaires en l’absence d'une possibilité de faire usage d'un oracle nécessitait en effet une capacité de marquage étendue (en particulier réaliser plusieurs marques sur le flux, stockées dans une pile, et la capacité de revenir à la dernière marque connue).

### Parseur de la calculatrice de base

Une des premières remarques que l'on peut réaliser et que cette grammaire est assez ambiguë, en particulier sur la prévalence des opérateurs `–` et `/` qui ne jouissent pas du même caractère commutatif que `*` et `+`, alors qu'ils sont placés au même statut dans la grammaire. Une expression comme `1 – 2 + 3` est donc équivalente à `1 – (2 + 3)` dans ce contexte. C'est un problème qui s'est posé dès ce stade de la programmation, mais il s'est avéré qu'étant intrinsèque à la grammaire, il n'est pas du ressort de l'interpréteur que nous écrivons de s'en préoccuper.

La réalisation du parseur sera donc faite avec l'idée d'une généralisation de ce traitement pour pouvoir l'automatiser dans un deuxième temps. On s'accorde ainsi sur une syntaxe stricte des objets et procédures considérées, pour l'itérée expr de la grammaire :

* L'objet java correspondant sera Eexpr
* Les objets correspondants aux différents types d'Eexpr : Eexpr_i
* La procédure qui le prélève au niveau du lexeur : Eexpr consumeexpr ()
* Les sous procédures qui prélèvent expr dans le cas des lignes 1 à n : Eexpr consumeexpr_i ()

Tout ceci en gardant à l'esprit que le code doit pouvoir être généré le plus simplement possible par des boucles et de la récursivité structurelle.

## Interpréteur pour la grammaire des grammaires

La première étape de cette réalisation est l'écriture de la grammaire des grammaires :
```
EGram ::=
EEs EGramNxt
;;

EGramNxt ::=
';; EEs EGramNxt
| ';; -> {}
;;

EEs ::=
String '::= EBody
;;

EBody ::=
ELine EBodyNxt
;;

EbodyNxt ::=
'| ELine EBodyNxt
| ----
;;

ELine ::=
;;

EScheme
String
;;

EScheme ::=
ETerminal ESchemeNxt
;;

ESchemeNxt ::=
–

Eterminal
ESchemeNxt '->
;;

ETerminal ::=
String
;;
```

### Réalisation du parseur des grammaires

L'analyse des grammaires ont donné lieu à de nombreuses modifications du code, même si les bases sont restées globalement identiques.

* Le Lexer a été conservé, même si certaines fonctions ont du faire leur apparition pour parer à certaines particularités de la syntaxe des grammaires : on trouve ici des portions de texte (typiquement le code contenu entre accolades} qui ne doit pas être parsé. On ajoute donc au code le type chaîne de caractère pour une plus liberté dans la définition des grammaires, et donc la réalisation de l’objectif final, l’auto-génération du
code.
* La méthode mise en place précédemment pour réaliser le code de la calculatrice simple est ici étendue au cas général, d'où un net avantage de la syntaxe stricte précédente, qui permet de plus de comparer les résultats. 

Un point décisif, et qui aura suscité quelques difficultés est celui de l’absence d'oracle. N'imposant aucun condition générique sur le contenu d'une itérée de la grammaire, cette dernière peut contenir des schémas entièrement différents, qui ne permettrons pas de déterminer au préalable le type d'un texte rencontré. Il est également impossible de savoir à quel niveau le programme se rendant compte que le texte qu'il lit n'est pas du type qu'il considère actuellement.

Pour ceci, le code utilise les Exceptions, qui sont interceptées par les procédures génériques de type Eexpr. On y teste dans l'ordre de lecture de la source (de haut en bas) les différents schémas `(consumeEexpr_1, consumeEexpr_2, ...)` en changeant à toute rencontre d'exception. Le parseur retourne ainsi le premier schéma correspondant dans son sens de lecture (de haut en bas donc). Cela permet de donner une interprétation aux résultats obtenus avec une grammaire ambiguë, qui est interprétée selon la première solution valide rencontrée. 

Par ailleurs, pour permettre l'utilisation de différents types (Booléens, Chaînes de caractères et entiers, la syntaxe a été légèrement modifiée de manière à indiquer (par B,S ou I devant chaque nom d'itérée) le type dans lequel il doit être évalué. 

De la même manière, pour ne pas entrer en conflit avec la syntaxe java, les accolades de code ont été remplacées par des doubles crochets, ce qui donne la syntaxe suivante pour la calculatrice de
base :
```
Iexpr ::=
prod '+ expr -> [[ $1 + $3 ]]
| prod '- expr -> [[ $1 - $3 ]]
| prod -> [[ $1 ]]
;;


Iprod ::=
| fun '* prod -> [[ $1 * $3 ]]
| fun '/ prod -> [[ $1 / $3 ]]
| fun -> [[ $1 / $3 ]]
;;

Ifun ::=
int -> [[ $1 ]]
| '( expr ') -> [[ $2 ]]
;;
```

## Utilisation

### Structure du projet

Le projet contient différents packages :

* calculator contient les sources du parseur pour la calculatrice simple
 - ExpressionFactory, définissant les types.
 - Parser s'occupant du processus de parsing.
 - RunCalculator, un code de test qui permet de charger des fichiers (&f « nom du fichier ») ou de lancer le parseur sur une chaîne rentrée au clavier.
* Grammar : les sources du parseur de grammaires
 - ExpressionFactory, définissant les types, et donnant le code interprété.
 - Parser s'occupant du processus de parsing
 - GrammarExpression, type générique vide créé pour des facilités d'écriture.
 - RunGrammar, code de test
* shared qui contient les librairires partagées
 - Le lexer (Lexer pour les fichiers, StringLexer pour les chaînes)
 - Les exceptions : EvalException et SyntaxError
 - La définition d'expression générique (qui contient une méthode d'évaluation)
 - MarktRandomAccessFile : classe étendant RandomAccessFile, fournissant un accès en lecture et écriture aux fichiers, avec la possibilité de déplacer la tête de lecture et de marquer les positions.
 - Value, conteneurs de valeurs terminales
* out, qui contient la sortie de l'interpréteur (rassemblé en un fichier pour plus de simplicité).
Le fichier java construit par RunGrammar admet des dépendance vers `shared.*`

### Mode d'emploi

#### Pour compiler et lancer l'interpréteur
* Se placer dans le dossier du projet
```
javac -d bin -sourcepath src src/grammar/RunGrammar.java
java -cp bin grammar.RunGrammar
```
* Une fois dans le programme, faire `&f gr.txt` pour la grammaire d'exemple

Pour compiler et lancer le résultat, faire
```
javac -d bin -sourcepath src src/out/RunOutput.java
java -cp bin out.RunOutput
```
On peut alors lancer le programme sur une entrée clavier ou fichier ( &f nomdufichier )
Les scripts run.bat et run.sh exécutent ce traitement.


### Exemples de grammaires

On pourra tester les grammaires :

* grammar_calc.txt sur une entrée de la forme `1 + 2 * ( 3 + 4 )`
* grammar_bool.txt sur une entrée de la forme `! false & true`
* grammar_string.txt sur une entrée de la forme `" foo " 0 :: " oobar "`
