package com.github.itellijlover;

import com.github.itellijlover.model.Equipe;
import com.github.itellijlover.model.MatchM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JOptionPane;

public class Tournoi {

	String statuttnom;
	String nt;

	int    statut;
	int    id_tournoi;

	private Vector<Equipe> dataeq = null;
	private Vector<MatchM> datam  = null;
	private Vector<Integer>ideqs  = null; 

	Statement st;
		
	public Tournoi(String nt, Statement s) {
		st = s;

		try {
			ResultSet rs = s.executeQuery("SELECT * FROM tournois WHERE nom_tournoi = '" + Tournoi.mysql_real_escape_string(nt) + "';");
			if (!rs.next()) {
				return;
			}
			this.statut = rs.getInt("statut");

			this.id_tournoi = rs.getInt("id_tournoi");
			rs.close();

		} catch (SQLException e) {
			System.out.println("Erreur SQL: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		statuttnom = "Inconnu";
		switch(this.statut) {
		case 0:
			statuttnom = "Inscription des joueurs";
		break;
		case 1:
			statuttnom = "Génération des matchs";
		break;
		case 2:
			statuttnom = "Matchs en cours";
		break;
		case 3:
			statuttnom = "Terminé";
		break;
		}
		this.nt = nt;

	}

	public void majEquipes() {
		dataeq = new Vector<>();
		ideqs = new Vector<>();
		try {
			ResultSet rs = st.executeQuery("SELECT * FROM equipes WHERE id_tournoi = " + id_tournoi + " ORDER BY num_equipe;");
			while (rs.next()) {
				dataeq.add(new Equipe(rs.getInt("id_equipe"),rs.getInt("num_equipe"), rs.getString("nom_j1"), rs.getString("nom_j2")));
				ideqs.add(rs.getInt("num_equipe"));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void majMatch() {
		datam = new Vector<>();
		try {
			ResultSet rs= st.executeQuery("SELECT * FROM matchs WHERE id_tournoi="+ id_tournoi + ";");
			while (rs.next()) {
				datam.add(new MatchM(rs.getInt("id_match"), rs.getInt("equipe1"), rs.getInt("equipe2"), rs.getInt("score1"), rs.getInt("score2"), rs.getInt("num_tour"), Objects.equals(rs.getString("termine"), "oui")));
			}
			//public MatchM(int _idmatch,int _e1,int _e2,int _score1, int _score2, int _num_tour, boolean _termine)
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public MatchM getMatch(int index) {
		if (datam == null) majMatch();
		return datam.get(index);
	}

	public int getNbMatchs() {
		if (datam == null) majMatch();
		return datam.size();
	}

	public Equipe getEquipe(int index) {
		if (dataeq == null) majEquipes();
		return dataeq.get(index);
	}

	public int getNbEquipes() {
		if (dataeq == null) majEquipes();
		return dataeq.size();
	}
	
	public int getStatut() {
		return statut;
	}

	public String getNStatut() {
		return statuttnom;
	}

	public String getNom() {
		return nt;
	}

	public int getNbTours() {
		try {
			ResultSet rs = st.executeQuery("SELECT MAX (num_tour)  FROM matchs WHERE id_tournoi="+id_tournoi+"; ");
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}

	public void genererMatchs() {
		int nbt = 1;

		System.out.println("Nombre d'équipes : " + getNbEquipes());
		System.out.println("Nombre de tours  : " + nbt);
		StringBuilder req = new StringBuilder("INSERT INTO matchs ( id_match, id_tournoi, num_tour, equipe1, equipe2, termine ) VALUES\n");
		Vector<Vector<MatchM>> ms;
		ms = Tournoi.getMatchsToDo(getNbEquipes(), nbt);
		int z = 1;
		char v = ' ';
		assert ms != null;
		for (Vector<MatchM> t : ms) {
			for (MatchM m:t) {
				req.append(v).append("(NULL,").append(id_tournoi).append(", ").append(z).append(", ").append(m.eq1).append(", ").append(m.eq2).append(", 'non')");
				v = ',';
			}
			req.append("\n");
			z++;
		}
		System.out.println(req);
		try {
			st.executeUpdate(req.toString());
			st.executeUpdate("UPDATE tournois SET statut=2 WHERE id_tournoi=" + id_tournoi + ";");
			this.statut = 2;
		} catch(SQLException e) {
			System.out.println("Erreur validation �quipes : " + e.getMessage());
		}
	}

	public void ajouterTour() {
		// Recherche du nombre de tours actuel
		int nbtoursav;
		if (getNbTours() >= (getNbEquipes() -1)) return;
		System.out.println("Eq:" + getNbEquipes() + "  tours" + getNbTours());
		try {
			ResultSet rs = st.executeQuery("SELECT MAX (num_tour)  FROM matchs WHERE id_tournoi="+id_tournoi+"; ");
			rs.next();
			nbtoursav = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("Nombre de tours avant:" + nbtoursav);


		if (nbtoursav == 0) {
			Vector<MatchM> ms;

			ms = Objects.requireNonNull(Tournoi.getMatchsToDo(getNbEquipes(), nbtoursav + 1)).lastElement();

			StringBuilder req = new StringBuilder("INSERT INTO matchs ( id_match, id_tournoi, num_tour, equipe1, equipe2, termine ) VALUES\n");
			char v = ' ';
			for (MatchM m:ms) {
				req.append(v).append("(NULL,").append(id_tournoi).append(", ").append(nbtoursav + 1).append(", ").append(m.eq1).append(", ").append(m.eq2).append(", 'non')");
				v = ',';
			}
			req.append("\n");

			try {
				st.executeUpdate(req.toString());
			} catch(SQLException e) {
				System.out.println("Erreur ajout tour : " + e.getMessage());
			}		
		} else {
			try {
				ResultSet rs;
				//rs = st.executeQuery("SELECT equipe, (SELECT count(*) FROM matchs m WHERE (m.equipe1 = equipe AND m.score1 > m.score2 AND m.id_tournoi = id_tournoi) OR (m.equipe2 = equipe AND m.score2 > m.score1 AND m.id_tournoi = id_tournoi )) as matchs_gagnes FROM  (select equipe1 as equipe,score1 as score from matchs where id_tournoi=" + this.id_tournoi + " UNION select equipe2 as equipe,score2 as score from matchs where id_tournoi=" + this.id_tournoi + ") GROUP BY equipe ORDER BY matchs_gagnes DESC;");

				rs = st.executeQuery("SELECT equipe, (SELECT count(*) FROM matchs m WHERE (m.equipe1 = equipe AND m.score1 > m.score2  AND m.id_tournoi = id_tournoi) OR (m.equipe2 = equipe AND m.score2 > m.score1 )) as matchs_gagnes FROM  (select equipe1 as equipe,score1 as score from matchs where id_tournoi=" + this.id_tournoi + " UNION select equipe2 as equipe,score2 as score from matchs where id_tournoi=" + this.id_tournoi + ") GROUP BY equipe  ORDER BY matchs_gagnes DESC;");


				ArrayList<Integer> ordreeq= new ArrayList<>();
				while (rs.next()) {
					ordreeq.add(rs.getInt("equipe"));
					System.out.println(rs.getInt(1) +" _ " + rs.getString(2));
				}
				System.out.println("Taille"+ordreeq.size());
				int i;
				boolean fini;
				StringBuilder req = new StringBuilder("INSERT INTO matchs ( id_match, id_tournoi, num_tour, equipe1, equipe2, termine ) VALUES\n");
				char v = ' ';
				while (ordreeq.size() > 1) {
					System.out.println("Taille " + ordreeq.size());
					int j=0;
					while (j<ordreeq.size()) {
						System.out.println(ordreeq.get(j));
						j++;
					}
					i=1;
					do {
						rs = st.executeQuery("SELECT COUNT(*) FROM matchs m WHERE ( (m.equipe1 = " + ordreeq.get(0) + " AND m.equipe2 = " + ordreeq.get(i) + ") OR (m.equipe2 = " + ordreeq.get(0) + " AND m.equipe1 = " + ordreeq.get(i) + ")  )");  
						rs.next();
						if (rs.getInt(1) > 0) {
							// Le match est d�j� jou�
							i++;
							fini = false;

						} else {
							fini = true;
							req.append(v).append("(NULL,").append(id_tournoi).append(", ").append(nbtoursav + 1).append(", ").append(ordreeq.get(0)).append(", ").append(ordreeq.get(i)).append(", 'non')");
							System.out.println(ordreeq.get(0) + ", " +  ordreeq.get(i));
							ordreeq.remove(0);
							ordreeq.remove(i-1);
							v = ',';
						}
					} while(!fini);
				}
				System.out.println(req);
				st.executeUpdate(req.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void supprimerTour() {
		int nbtoursav;
		try {
			ResultSet rs = st.executeQuery("SELECT MAX (num_tour)  FROM matchs WHERE id_tournoi="+id_tournoi+"; ");
			rs.next();
			nbtoursav = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return ;
		}
		//if(tour != nbtoursav) return ;

		try {
			st.executeUpdate("DELETE FROM matchs WHERE id_tournoi="+ id_tournoi+" AND num_tour=" + nbtoursav);
		} catch (SQLException e) {
			System.out.println("Erreur del tour : " + e.getMessage());
		}
	}
		
	public static void deleteTournoi(Statement s2, String nomtournoi) {
		try {
			int idt;
			ResultSet rs = s2.executeQuery("SELECT id_tournoi FROM tournois WHERE nom_tournoi = '" + mysql_real_escape_string(nomtournoi) + "';");
			rs.next();
			idt = rs.getInt(1);
			rs.close();
			System.out.println("ID du tournoi à supprimer:" + idt);
			s2.executeUpdate("DELETE FROM matchs   WHERE id_tournoi = " + idt);
			s2.executeUpdate("DELETE FROM equipes  WHERE id_tournoi = " + idt);
			s2.executeUpdate("DELETE FROM tournois WHERE id_tournoi = " + idt);
		} catch (SQLException e) {
			System.out.println("Erreur suppression" + e.getMessage());

		} catch (Exception e) {
			System.out.println("Erreur inconnue");
		} 
	}

	public static void creerTournoi(Statement statement) {
		String string = JOptionPane.showInputDialog(
                null,
                "Entrez le nom du tournoi",
                "Nom du tournoi",
                JOptionPane.PLAIN_MESSAGE);


		if (string != null && !string.isEmpty()) {
			try {
				string =  mysql_real_escape_string(string);
				if (string.length() < 3) {
					JOptionPane.showMessageDialog(null, "Le tournoi n'a pas �t� cr��. Nom trop court.");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (string.equals("")) {
				JOptionPane.showMessageDialog(null, "Le tournoi n'a pas �t� cr��. Ne pas mettre de caract�res sp�ciaux ou accents dans le nom");
			} else {
				ResultSet rs;
				try {
					rs = statement.executeQuery("SELECT id_tournoi FROM tournois WHERE nom_tournoi = '" + string + "';");
					if (rs.next()) {
						JOptionPane.showMessageDialog(null, "Le tournoi n'a pas �t� cr��. Un tournoi du m�me nom existe d�j�");
						return;
					}
					System.out.println("INSERT INTO tournois (id_tournoi, nb_matchs, nom_tournoi, statut) VALUES (NULL, 10, '"+string+"', 0)");
				statement.executeUpdate("INSERT INTO tournois (id_tournoi, nb_matchs, nom_tournoi, statut) VALUES (NULL, 10, '"+string+"', 0)");
				} catch (SQLException e) {
					System.out.println("Erreur requete insertion nouveau tournoi:" + e.getMessage());
				}
			}
		}
	}
	
	public void ajouterEquipe() {
		int a_aj = this.dataeq.size() + 1;
		for (int i=1;i <= this.dataeq.size(); i++) {
			if (!ideqs.contains(i)) {
				a_aj=i;
				break;
			}
		}
		try {
			st.executeUpdate("INSERT INTO equipes (id_equipe,num_equipe,id_tournoi,nom_j1,nom_j2) VALUES (NULL,"+a_aj+", "+id_tournoi + ",'\"Joueur 1\"', '\"Joueur 2\"');");
		    majEquipes();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void majEquipe(int index) {
		try {
			String req = "UPDATE equipes SET nom_j1 = '" + mysql_real_escape_string(getEquipe(index).getEq1()) + "', nom_j2 = '" + mysql_real_escape_string(getEquipe(index).getEq2()) + "' WHERE id_equipe = " + getEquipe(index).getId() + ";";
			System.out.println(req);
			st.executeUpdate(req);
		    majEquipes();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void majMatch(int index) {
		String termine = (getMatch(index).score1 > 0 || getMatch(index).score2 > 0) ? "oui":"non";
		System.out.println(termine);
		String req = "UPDATE matchs SET equipe1='" + getMatch(index).eq1 + "', equipe2='" + getMatch(index).eq2 + "',  score1='" + getMatch(index).score1 + "',  score2='" +getMatch(index).score2 + "', termine='" + termine + "' WHERE id_match = " + getMatch(index).idmatch + ";";
		try {
			st.executeUpdate(req);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		majMatch();
	}

	public void supprimerEquipe(int ideq) {
		try {
			int numeq;
			ResultSet rs = st.executeQuery("SELECT num_equipe FROM equipes WHERE id_equipe = " + ideq);
			rs.next();
			numeq = rs.getInt(1);
			rs.close();
			st.executeUpdate("DELETE FROM equipes WHERE id_tournoi = " + id_tournoi+ " AND id_equipe = " + ideq);
			st.executeUpdate("UPDATE equipes SET num_equipe = num_equipe - 1 WHERE id_tournoi = " + id_tournoi + " AND num_equipe > " + numeq);
		    majEquipes();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

    public static String mysql_real_escape_string(String str) {
          if (str == null) {
              return null;
          }

          if (str.replaceAll("[a-zA-Z\\d_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}/? ]","").length() < 1) {
              return str;
          }

          String clean_string = str;
          clean_string = clean_string.replaceAll("\\n","\\\\n");
          clean_string = clean_string.replaceAll("\\r", "\\\\r");
          clean_string = clean_string.replaceAll("\\t", "\\\\t");
          clean_string = clean_string.replaceAll("\\00", "\\\\0");
          clean_string = clean_string.replaceAll("'", "''");
          return clean_string;

      }

	public static Vector<Vector<MatchM>> getMatchsToDo(int nbJoueurs, int nbTours) {
		if (nbTours  >= nbJoueurs) {
			System.out.println("Erreur tours < equipes");
			return null;
		}

		int[] tabJoueurs;
		if ((nbJoueurs % 2) == 1) {
			// Nombre impair de joueurs, on rajoute une �quipe fictive
			tabJoueurs   = new int[nbJoueurs+1];
			tabJoueurs[nbJoueurs] = -1;
			for (int z = 0; z < nbJoueurs;z++) {
				tabJoueurs[z] = z+1;
			}
			nbJoueurs++;
		} else {
			tabJoueurs   = new int[nbJoueurs];
			for (int z = 0; z < nbJoueurs;z++) {
				tabJoueurs[z] = z+1;
			}
		}

		boolean quitter;
		int i, increment = 1, temp;

		Vector<Vector<MatchM>> retour = new Vector<>();

		Vector<MatchM> vm;

		for (int r = 1; r <= nbTours;r++) {
			if (r > 1) {
				temp = tabJoueurs[nbJoueurs - 2];
				for (i = (nbJoueurs - 2) ; i > 0; i--) {
					tabJoueurs[i] = tabJoueurs[i-1];
				}
				tabJoueurs[0] = temp;
			}
			i       = 0;
			quitter = false;
			vm = new Vector<>();
			while (!quitter) {
				if (tabJoueurs[i] != -1 && tabJoueurs[nbJoueurs - 1  - i] != -1) {
					vm.add(new MatchM(tabJoueurs[i], tabJoueurs[nbJoueurs - 1  - i]));
				}
				// Sinon : Nombre impair de joueur, le joueur n'a pas d'adversaire

		        i += increment;
				if (i >= nbJoueurs / 2) {
					//if (increment == 1) {
					quitter = true;
						//break;
					/*} else {
						increment = -2;
						if (i > nbJoueurs / 2) {
							i = ((i > nbJoueurs / 2) ? i - 3 : --i) ;
						}
						if ((i < 1) && (increment == -2)) {
							quitter = true;
							//break;
						}
					}*/
				}
			}
			retour.add(vm);
		}
		return retour;
	}

}
