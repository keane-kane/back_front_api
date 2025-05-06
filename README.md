# 🎓 Projet d'Évaluation – Technologie d’Application Web

## 📌 Contexte

Ce projet est réalisé dans le cadre du cours de **Technologie d’Application Web**. Il vise à évaluer la maîtrise des technologies front-end, middle-tier (API REST), et back-end, au travers d’une application web complète et modulaire dédiée à l’**Université Cheikh Hamidou Kane**.

---

## 🎯 Objectif

Concevoir et développer une **application web évolutive** pour la **gestion administrative et pédagogique** de l’université, en respectant une architecture en **trois couches** :

* **Frontend** : Interface utilisateur développée avec **Angular**
* **Middle/API REST** : Gestion des échanges, sécurisation des données, intégration d'éléments médias (logos, images, etc.)
* **Backend** : Application **Spring Boot** connectée à une base de données relationnelle (**MySQL**, **PostgreSQL** ou équivalent)

---

## 🏗️ Architecture modulaire

L'application propose un portail web où chaque entité de l’université dispose d’un **profil d’accueil dédié**.

### 👥 Entités concernées :

* **Étudiants**
* **Formateurs** (enseignants permanents, associés, responsables de formation, tuteurs)
* **Administration**

  * Programmation des activités pédagogiques : cours, devoirs, examens
  * Suivi des activités de tutorat
  * Gestion générale des opérations universitaires

---

## 🔧 Technologies utilisées

| Couche          | Technologie                                               |
| --------------- | --------------------------------------------------------- |
| Frontend        | Angular                                                   |
| API / Middle    | RESTful APIs, sécurité (JWT ou autre), gestion des médias |
| Backend         | Spring Boot + JPA                                         |
| Base de données | MySQL / PostgreSQL                                        |

---

## 📦 Modules fonctionnels (en cours)

1. **Module Communication**

   * Messagerie interne entre étudiants, formateurs et administration
   * Annonces et notifications
2. **Module Gestion des Activités**

   * Création et planification de cours
   * Devoirs, examens, calendrier académique
3. **Module Suivi pédagogique**

   * Suivi de tutorat
   * Historique académique
   * Évaluations
4. **Module Authentification & Profils**

   * Connexion sécurisée (JWT, OAuth, etc.)
   * Différents rôles et droits d'accès

---

## 🚧 État du projet

🟡 En cours de développement
✅ Frontend en cours de structuration
✅ Backend partiellement opérationnel
🟡 API REST en intégration continue

---

## 🤝 Contribution

Les contributions sont les bienvenues pour améliorer les fonctionnalités, la documentation ou l’architecture du projet.

