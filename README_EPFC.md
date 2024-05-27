# Projet ANC3 2324 - Groupe g01 - Sokoban

## Notes de version itération 1

## Notes de version itération 2
### bug itération 1 corrigé :
* Artefact artefact.create est maintenant une méthode statique.
* Artefact artefact.create ne recoit pas un string mais un enum ArtefactMode.
* Artefact toString a été supprimé puisque inutile.
* le chemin d'accès à l'image d'un Artefact et son utilisation dans la vue a été découplé,
  utilisation d'un switch en fonction de l'enum.

### Liste des fonctionnalités supplémentaires

* à la construction d'un niveau, une partie des possibilitées qui empecheraient un
  niveau de pouvoir être completée sont envisagées et empêche le joueur de lancer le niveau.


## Notes de version itération 3

### Liste des bugs connus
* en mode grid4Design quand on retire le player et qu'on ajoute un autre artefact dans le grid, il reapparait a sa position precedente.

## Nota Bene 
* diagrammes de classes et de séquence disponibles dans le dossier "docs"