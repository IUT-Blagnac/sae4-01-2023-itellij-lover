CREATE TABLE IF NOT EXISTS tournoi (
  id_tournoi INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  nb_matchs INT,
  nom_tournoi VARCHAR(30),
  statut INT
);

CREATE TABLE IF NOT EXISTS equipe (
  id_equipe INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  num_equipe INT,
  id_tournoi INT,
  nom_j1 VARCHAR(30),
  nom_j2 VARCHAR(30),
  FOREIGN KEY (id_tournoi) REFERENCES tournoi (id_tournoi)
);

CREATE TABLE IF NOT EXISTS match (
  id_match INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  id_tournoi INT,
  num_tour INT,
  equipe1 INT,
  equipe2 INT,
  score1  INT,
  score2  INT,
  termine VARCHAR(3)
)