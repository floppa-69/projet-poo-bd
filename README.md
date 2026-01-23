# Projet POO - Gestion de Pharmacie

Ce dépôt contient une application Java (Maven) pour la gestion d'une pharmacie : gestion des produits, fournisseurs, clients, ventes et commandes fournisseurs.

Principales informations

- Langage : Java
- Outils : Maven
- Base de données : MySQL (script d'initialisation : schema.sql, init_db.sh)

Installation rapide

1. Cloner le dépôt :
   git clone https://github.com/floppa-69/projet-poo-bd.git
2. Importer le projet dans votre IDE (Eclipse/IntelliJ) en tant que projet Maven.
3. Initialiser la base de données :
   - Exécuter `schema.sql` ou `./init_db.sh` (MySQL local)
4. Compiler et exécuter :
   mvn package
   java -jar target/projet-poo-bd.jar

Fichiers importants

- `src/main/java/pharmacie/model/` : classes du modèle (Client, Product, User, Sale, ...)
- `schema.sql` : script de création de la base de données et données d'exemple
- `init_db.sh` : script d'initialisation (création d'utilisateur MySQL, import)
- `rapport_projet.tex` : rapport existant au format LaTeX


