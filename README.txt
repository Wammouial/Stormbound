                                                                                                                 
                                                                                                                  
`7MMF'     A     `7MF' db      `7MMF'      `7MMF'      `7MMF'`7MMM.     ,MMF'`7MMF'      `7MM"""YMM    .g8""8q.   
  `MA     ,MA     ,V  ;MM:       MM          MM          MM    MMMb    dPMM    MM          MM    `7  .dP'    `YM. 
   VM:   ,VVM:   ,V  ,V^MM.      MM          MM          MM    M YM   ,M MM    MM          MM   d    dM'      `MM 
    MM.  M' MM.  M' ,M  `MM      MM          MM          MM    M  Mb  M' MM    MM          MMmmMM    MM        MM 
    `MM A'  `MM A'  AbmmmqMA     MM      ,   MM      ,   MM    M  YM.P'  MM    MM      ,   MM   Y  , MM.      ,MP 
     :MM;    :MM;  A'     VML    MM     ,M   MM     ,M   MM    M  `YM'   MM    MM     ,M   MM     ,M `Mb.    ,dP' 
      VF      VF .AMA.   .AMMA..JMMmmmmMMM .JMMmmmmMMM .JMML..JML. `'  .JMML..JMMmmmmMMM .JMMmmmmMMM   `"bmmd"'   
                                                                                                                  
Règles du jeu basées sur StormBound, Par Léo Chardon et William Ammouial.



[]-[]-[]-1-Les Implémentations-[]-[]-[]


Il est maintenant possible de :
-jouer sur console ou sur une interface graphique(en changeant une ligne dans le Main.java)
-jouer soit contre une IA, soit contre un deuxième joueur (même écran)
note : si vous gagnez contre l'IA, alors vous gagnez une nouvelle carte dans le coffre de votre objet save
- Personnaliser et sauvegarder 6 decks ( 3 pour le joueur, et 3 pour l'IA/joueur2)
- changer de nom de joueur ou obtenir d'un coup toute les cartes grace à un code

[*] Les effets ont uniquement été implémentés pour les 12 cartes du jeu de base ainsi que tous
les sorts de faction "Neutral".

[]-[]-[]-2-Organisations-[]-[]-[]

Voici comment le jeux est créé :

le jeu utilise 25 classes pour son bon fonctionnement, parmi elles ont retrouve des classes tels que :
-Deck (création et méthode des decks)
-Card (interface pour les cartes unité et Spell)
-Unit (unité informations et methodes)
-Spell (carte magique informations et methodes)
-Structure (extension des unités)
-4 classes pour les Effets (effet, paramètre des effets, types, description d'eux)
-IA (méthodes de l'IA appelées dans board, extension d'un joueur)
-2 classes d'Exception (en cas d'aucune cible pour un effet ou mauvais paramètre)
-Player (informations et methodes des joueurs)
-Save ( une classe serialisable pour la sauvegarde du coffre, des noms et des decks)

La classe la plus importante est la classe Board car c'est dans cette classe que le jeu se déroule et que 
beaucoup de méthodes sont appelées.
elle est lancé par la classe GameController.
Il existe dans board plusieurs methodes de la gestion des tours (IA, IA graphique, console, graphique).
Le GameData est ainsi créer avant ainsi que le GameView si vous jouez en graphique.
Si vous jouez donc en graphique, il y a un menu géré par la classe Home 
qui vous offre la possibilité de jouer contre l'IA, contre un joueur 2 ou bien personnaliser ses decks ou aller dans les options.

Les options et la personnalisations sont gérer par la classe Edit qui est appelée par le controlleur suivant le choix du joueur
Il est donc possible de personnaliser 6 decks, en faisant attention à ne pas mettre des cartes de meme faction, changer le nom du deck est aussi possible
Dans option on peut entrer un code pour gagner toute les cartes du jeu StormBound, ou changer de nom de joueur.
Une fois que vous etes retournés au menu home alors tout vos changement ont ete sauvegardés dans l'objet save, GameData se sert de l'objet save pour créer la partie

Les images ainsi que la liste de toutes les cartes ont été implémentés grace a un script python ainsi que deux methodes en Java toujours visible 
dans le code de Edit.

La classe GameView implémente toute les méthodes pour dessiner dans l'interface graphique
elle est réalisée pour être responsive. Elle utilise les getters des différentes classes comme 
les cartes ou le joueur.

Il y aussi deux dossier autres qui sont pictures et fonts pour l'interface graphique.

[]-[]-[]-3-Choix techniques-[]-[]-[]

La plupart des méthodes de fonctionnement du jeu sont basées sur une double boucle,
pour parcourir la matrice ainsi que des if pour faire des tests et retourner des valeurs
qui seront à leurs tours interprétées par d'autres méthodes.
Ceci est valable dans le board mais aussi dans le GameView.
Les calculs pour dessiner sont faits à partir de constantes initialisées en fonction de l'écran.
Les effets, les cartes et les images sont créés dans une classe Creator sous 
forme de HashMap pour mieux les utilisés ensuite.
Dans l'interface graphique, on attend un evenement qui peut etre un clique ou une touche du clavier pressée, 
et le jeu reagi ensuite en enregistrant les informations prise en compte pour les reutilisées dans l'evenement d'apres
comme pour la selection de carte par exemple, on clique une fois sur la carte, ensuite on peut cliquer sur une case ou un bouton

[]-[]-[]-4-Problèmes rencontrés-[]-[]-[]

Beaucoup de bugs dû à des manques ou des problèmes de conditions sont parfois apparue au cours de la réalisations du jeu tels que 
les nullPointerExeption, mais heureusement, grâce à nos analyses et nos concertations, nous avons toujours veillé à les corriger.
La gestion des effets était aussi compliquée à gérer, c'est en divisant les effets en plusieurs méthodes que nous avons pu les implémenter correctement.
Il y a une map d'effet qui appelle le bon effet en fonction du nom de la carte, grace à un consumer car il n'est normalement pas possible de mettre des methodes dans une map.
Aussi, il a fallu rendre responsive toutes les méthodes draw à l'aide de calculs qui parfois, étaient difficiles à cerner à cause des nombreuses
informations comme les coordonnées I-J mais aussi les cases de la matrices ou encore les axes X et Y.









