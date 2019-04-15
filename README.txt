                                                                                                                 
                                                                                                                  
`7MMF'     A     `7MF' db      `7MMF'      `7MMF'      `7MMF'`7MMM.     ,MMF'`7MMF'      `7MM"""YMM    .g8""8q.   
  `MA     ,MA     ,V  ;MM:       MM          MM          MM    MMMb    dPMM    MM          MM    `7  .dP'    `YM. 
   VM:   ,VVM:   ,V  ,V^MM.      MM          MM          MM    M YM   ,M MM    MM          MM   d    dM'      `MM 
    MM.  M' MM.  M' ,M  `MM      MM          MM          MM    M  Mb  M' MM    MM          MMmmMM    MM        MM 
    `MM A'  `MM A'  AbmmmqMA     MM      ,   MM      ,   MM    M  YM.P'  MM    MM      ,   MM   Y  , MM.      ,MP 
     :MM;    :MM;  A'     VML    MM     ,M   MM     ,M   MM    M  `YM'   MM    MM     ,M   MM     ,M `Mb.    ,dP' 
      VF      VF .AMA.   .AMMA..JMMmmmmMMM .JMMmmmmMMM .JMML..JML. `'  .JMML..JMMmmmmMMM .JMMmmmmMMM   `"bmmd"'   
                                                                                                                  
R�gles du jeu bas�es sur StormBound, Par L�o Chardon et William Ammouial.



[]-[]-[]-1-Les Impl�mentations-[]-[]-[]


Il est maintenant possible de :
-jouer sur console ou sur une interface graphique(en changeant une ligne dans le Main.java)
-jouer soit contre une IA, soit contre un deuxi�me joueur (m�me �cran)
note : si vous gagnez contre l'IA, alors vous gagnez une nouvelle carte dans le coffre de votre objet save
- Personnaliser et sauvegarder 6 decks ( 3 pour le joueur, et 3 pour l'IA/joueur2)
- changer de nom de joueur ou obtenir d'un coup toute les cartes grace � un code

[*] Les effets ont uniquement �t� impl�ment�s pour les 12 cartes du jeu de base ainsi que tous
les sorts de faction "Neutral".

[]-[]-[]-2-Organisations-[]-[]-[]

Voici comment le jeux est cr�� :

le jeu utilise 25 classes pour son bon fonctionnement, parmi elles ont retrouve des classes tels que :
-Deck (cr�ation et m�thode des decks)
-Card (interface pour les cartes unit� et Spell)
-Unit (unit� informations et methodes)
-Spell (carte magique informations et methodes)
-Structure (extension des unit�s)
-4 classes pour les Effets (effet, param�tre des effets, types, description d'eux)
-IA (m�thodes de l'IA appel�es dans board, extension d'un joueur)
-2 classes d'Exception (en cas d'aucune cible pour un effet ou mauvais param�tre)
-Player (informations et methodes des joueurs)
-Save ( une classe serialisable pour la sauvegarde du coffre, des noms et des decks)

La classe la plus importante est la classe Board car c'est dans cette classe que le jeu se d�roule et que 
beaucoup de m�thodes sont appel�es.
elle est lanc� par la classe GameController.
Il existe dans board plusieurs methodes de la gestion des tours (IA, IA graphique, console, graphique).
Le GameData est ainsi cr�er avant ainsi que le GameView si vous jouez en graphique.
Si vous jouez donc en graphique, il y a un menu g�r� par la classe Home 
qui vous offre la possibilit� de jouer contre l'IA, contre un joueur 2 ou bien personnaliser ses decks ou aller dans les options.

Les options et la personnalisations sont g�rer par la classe Edit qui est appel�e par le controlleur suivant le choix du joueur
Il est donc possible de personnaliser 6 decks, en faisant attention � ne pas mettre des cartes de meme faction, changer le nom du deck est aussi possible
Dans option on peut entrer un code pour gagner toute les cartes du jeu StormBound, ou changer de nom de joueur.
Une fois que vous etes retourn�s au menu home alors tout vos changement ont ete sauvegard�s dans l'objet save, GameData se sert de l'objet save pour cr�er la partie

Les images ainsi que la liste de toutes les cartes ont �t� impl�ment�s grace a un script python ainsi que deux methodes en Java toujours visible 
dans le code de Edit.

La classe GameView impl�mente toute les m�thodes pour dessiner dans l'interface graphique
elle est r�alis�e pour �tre responsive. Elle utilise les getters des diff�rentes classes comme 
les cartes ou le joueur.

Il y aussi deux dossier autres qui sont pictures et fonts pour l'interface graphique.

[]-[]-[]-3-Choix techniques-[]-[]-[]

La plupart des m�thodes de fonctionnement du jeu sont bas�es sur une double boucle,
pour parcourir la matrice ainsi que des if pour faire des tests et retourner des valeurs
qui seront � leurs tours interpr�t�es par d'autres m�thodes.
Ceci est valable dans le board mais aussi dans le GameView.
Les calculs pour dessiner sont faits � partir de constantes initialis�es en fonction de l'�cran.
Les effets, les cartes et les images sont cr��s dans une classe Creator sous 
forme de HashMap pour mieux les utilis�s ensuite.
Dans l'interface graphique, on attend un evenement qui peut etre un clique ou une touche du clavier press�e, 
et le jeu reagi ensuite en enregistrant les informations prise en compte pour les reutilis�es dans l'evenement d'apres
comme pour la selection de carte par exemple, on clique une fois sur la carte, ensuite on peut cliquer sur une case ou un bouton

[]-[]-[]-4-Probl�mes rencontr�s-[]-[]-[]

Beaucoup de bugs d� � des manques ou des probl�mes de conditions sont parfois apparue au cours de la r�alisations du jeu tels que 
les nullPointerExeption, mais heureusement, gr�ce � nos analyses et nos concertations, nous avons toujours veill� � les corriger.
La gestion des effets �tait aussi compliqu�e � g�rer, c'est en divisant les effets en plusieurs m�thodes que nous avons pu les impl�menter correctement.
Il y a une map d'effet qui appelle le bon effet en fonction du nom de la carte, grace � un consumer car il n'est normalement pas possible de mettre des methodes dans une map.
Aussi, il a fallu rendre responsive toutes les m�thodes draw � l'aide de calculs qui parfois, �taient difficiles � cerner � cause des nombreuses
informations comme les coordonn�es I-J mais aussi les cases de la matrices ou encore les axes X et Y.









