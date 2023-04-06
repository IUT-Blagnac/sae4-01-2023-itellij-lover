package com.github.itellijlover.vue;

import com.github.itellijlover.Tournoi;
import com.github.itellijlover.dialog.DialogMatch;
import com.github.itellijlover.model.Equipe;
import com.github.itellijlover.model.MatchM;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JPanel c;
	private final Statement s;

	private JList<String> list;
	private JButton selectTournoi;
	private JButton deleteTournoi;
	private final JButton btournois;
	private final JButton bequipes;
	private final JButton btours;
	private final JButton bmatchs;
	private final JButton bresultats;
	private final JButton bparams;

	private boolean tournois_trace  = false;
	private boolean equipes_trace   = false;
	private boolean tours_trace     = false;
	private boolean match_trace     = false;
	private boolean resultats_trace = false;

	private final CardLayout fen;

	private final static String TOURNOIS = "Tournois";
	private final static String DETAIL   = "Paramètres du tournoi";
	private final static String EQUIPES  = "Equipes";
	private final static String TOURS    = "Tours";
	private final static String MATCHS   = "Matchs";
	private final static String RESULTATS= "Resultats";

	private Tournoi t = null;

    private final JLabel statut_slect;

	public Fenetre(Statement st) {
		s = st;
		setTitle("Gestion de tournoi de Belote");
		setSize(800,400);
		setVisible(true);
		setLocationRelativeTo(getParent());


		JPanel contenu = new JPanel();
		contenu.setLayout(new BorderLayout());
		setContentPane(contenu);


		JPanel phaut = new JPanel();
		contenu.add(phaut,BorderLayout.NORTH);

		phaut.add(statut_slect = new JLabel());
		setStatutSelect("Pas de tournoi sélectionné");

		JPanel pgauche = new JPanel();
		pgauche.setBackground(Color.RED);
		pgauche.setPreferredSize(new Dimension(130,0));
		contenu.add(pgauche,BorderLayout.WEST);


		btournois    = new JButton("Tournois");
		bparams      = new JButton("Paramètres");
		bequipes     = new JButton("Equipes");
		btours       = new JButton("Tours");
		bmatchs      = new JButton("Matchs");
		bresultats   = new JButton("Résultats");


		int taille_boutons = 100;
		int hauteur_boutons = 30;

		btournois.setPreferredSize(new Dimension(taille_boutons,hauteur_boutons));
		bparams.setPreferredSize(new Dimension(taille_boutons,hauteur_boutons));
		bequipes.setPreferredSize(new Dimension(taille_boutons,hauteur_boutons));
		btours.setPreferredSize(new Dimension(taille_boutons,hauteur_boutons));
		bmatchs.setPreferredSize(new Dimension(taille_boutons,hauteur_boutons));
		bresultats.setPreferredSize(new Dimension(taille_boutons,hauteur_boutons));

		pgauche.add(btournois);
		pgauche.add(bparams);
		pgauche.add(bequipes);
		pgauche.add(btours);
		pgauche.add(bmatchs);
		pgauche.add(bresultats);
		fen = new CardLayout();
		c = new JPanel(fen);

		contenu.add(c,BorderLayout.CENTER);

		btournois.addActionListener(arg0 -> tracer_select_tournoi());
		btours.addActionListener(arg0 -> tracer_tours_tournoi());
		bparams.addActionListener(arg0 -> tracer_details_tournoi());
		bequipes.addActionListener(e -> tracer_tournoi_equipes());
		bmatchs.addActionListener(e -> tracer_tournoi_matchs());
		bresultats.addActionListener(arg0 -> tracer_tournoi_resultats());

		tracer_select_tournoi();
	}

	private void setStatutSelect(String t) {
		statut_slect.setText("Gestion de tournois de Belote v1.0 - " + t);
	}

	private void majboutons() {
		if (t == null) {
			btournois.setEnabled(true);
			bequipes.setEnabled(false);
			bmatchs.setEnabled(false);
			btours.setEnabled(false);
			bresultats.setEnabled(false);
			bparams.setEnabled(false);			
		} else {
			switch(t.getStatut()) {
			case 0:
				btournois.setEnabled(true);
				bequipes.setEnabled(true);
				bmatchs.setEnabled(false);
				btours.setEnabled(false);
				bresultats.setEnabled(false);
				bparams.setEnabled(true);	
			break;
			case 2:
				btournois.setEnabled(true);
				bequipes.setEnabled(true);
				bmatchs.setEnabled(t.getNbTours() > 0);
				btours.setEnabled(true);

				int total, termines;
				try {
					ResultSet rs = DialogMatch.getMatchTermines(t.getId());
					rs.next();
					total    = rs.getInt(1);
					termines = rs.getInt(2);
				} catch (SQLException e) {
					e.printStackTrace();
					return ;
				}
				bresultats.setEnabled(total == termines && total > 0);			
				bparams.setEnabled(true);					
			break;
			}
		}
	}

	private void tracer_select_tournoi() {

		t = null;
		majboutons();

		int nbdeLignes = 0;
		Vector<String> noms_tournois = new Vector<>();
        setStatutSelect("sélection d'un tournoi");
		ResultSet rs;
		try {
			rs = DialogMatch.getTournois();

			while (rs.next()) {
				nbdeLignes++;
				noms_tournois.add(rs.getString("nom_tournoi"));
			}

			rs.close();
		} catch (SQLException e) {
			afficherErreur("Erreur lors de la requète :" + e.getMessage());
		}
		
		if (tournois_trace) {
			list.setListData(noms_tournois);

	        if (nbdeLignes == 0) {
	        	selectTournoi.setEnabled(false);
	        	deleteTournoi.setEnabled(false);
	        } else {
	        	selectTournoi.setEnabled(true);
	        	deleteTournoi.setEnabled(true);
	        	list.setSelectedIndex(0);
	        }
			fen.show(c, TOURNOIS);


		} else {
		    tournois_trace = true;
			JPanel t = new JPanel();

			t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
			c.add(t,TOURNOIS);
			JTextArea gt = new JTextArea("Gestion des tournois\nXXXXX XXXXXXXX, juillet 2012");
			gt.setAlignmentX(Component.CENTER_ALIGNMENT);
			gt.setEditable(false);
			t.add(gt);

			// Recherche de la liste des tournois
			JPanel ListeTournois = new JPanel();

			t.add(ListeTournois);

			list = new JList<>(noms_tournois);
			list.setAlignmentX(Component.LEFT_ALIGNMENT); 
			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		    list.setVisibleRowCount(-1);
		    JScrollPane listScroller = new JScrollPane(list);
	        listScroller.setPreferredSize(new Dimension(250, 180));

			JLabel label = new JLabel("Liste des tournois");
	        label.setLabelFor(list);
	        label.setAlignmentX(Component.LEFT_ALIGNMENT);
	        t.add(label);
	        t.add(listScroller);
	        t.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));



	        Box bh = Box.createHorizontalBox();
	        t.add(bh);
			JButton creerTournoi = new JButton("Créer un nouveau tournoi");
			selectTournoi = new JButton("Sélectionner le tournoi");
			deleteTournoi = new JButton("Supprimer le tournoi");
			bh.add(creerTournoi);
			bh.add(selectTournoi);	
			bh.add(deleteTournoi);

			t.updateUI();
	        if (nbdeLignes == 0) {
	        	selectTournoi.setEnabled(false);
	        	deleteTournoi.setEnabled(false);
	        } else {
	        	list.setSelectedIndex(0);
	        }

	        creerTournoi.addActionListener( e -> {
				Tournoi.creerTournoi();
				tracer_select_tournoi();
			});

	        deleteTournoi.addActionListener( e -> {
				Tournoi.deleteTournoi(list.getSelectedValue());
				tracer_select_tournoi();
			});
	        selectTournoi.addActionListener( arg0 -> {
				String nt = list.getSelectedValue();
				this.t = new Tournoi(nt);
				tracer_details_tournoi();
				setStatutSelect("Tournoi \" " + nt + " \"");
			});
	        fen.show(c, TOURNOIS);
		}
	}

	private void tracer_details_tournoi() {
		if (t == null) {
			return;
		}
		majboutons();

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(new JLabel("Détail du tournoi"));
		c.add(p, DETAIL);

		JPanel tab = new JPanel( new GridLayout(4,2));
		JLabel detailt_nom = new JLabel(t.getNom());
		tab.add(new JLabel("Nom du tournoi"));
		tab.add(detailt_nom);

		JLabel detailt_statut = new JLabel(t.getStatutString());
		tab.add(new JLabel("Statut"));
		tab.add(detailt_statut);

		JLabel detailt_nbtours = new JLabel(Integer.toString(t.getNbTours()));
		tab.add(new JLabel("Nombre de tours:"));
		tab.add(detailt_nbtours);

		p.add(tab);

		p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

		fen.show(c, DETAIL);
	}

    private AbstractTableModel eq_modele;
    private JButton eq_ajouter;
    private JButton eq_supprimer;
    private JButton eq_valider;
    private JTable eq_jt;

	private void tracer_tournoi_equipes() {
		if (t == null) {
			return;
		}
		majboutons();
		if (equipes_trace) {
			t.getEquipes();
			eq_modele.fireTableDataChanged();
		} else {
			equipes_trace = true;
			JPanel eq_p = new JPanel();
			BoxLayout eq_layout = new BoxLayout(eq_p, BoxLayout.Y_AXIS);
			eq_p.setLayout(eq_layout);
			JLabel eq_desc = new JLabel("Equipes du tournoi");
			eq_p.add(eq_desc);
			eq_p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
			c.add(eq_p, EQUIPES);

			eq_modele = new AbstractTableModel() {

				private static final long serialVersionUID = 1L;

				@Override
				public Object getValueAt(int arg0, int arg1) {
					Object r = null;
					switch(arg1) {
					case 0:
						r = t.getEquipe(arg0).getNum();
					break;
					case 1:
						r = t.getEquipe(arg0).getNomJ1();
					break;
					case 2:
						r = t.getEquipe(arg0).getNomJ2();
					break;
					}
					return r;

				}
				public String getColumnName(int col) {
					if (col == 0) {
						return "Numéro d'équipe";
					} else if(col == 1) {
						return "Joueur 1";
					} else if(col == 2) {
						return "Joueur 2";
					} else {
						return "??";
					}
				}

				@Override
				public int getRowCount() {
					if (t == null) return 0;
					return t.getNbEquipes();
				}

				@Override
				public int getColumnCount() {
					return 3;
				}

				public boolean isCellEditable(int x, int y) {
					if(t.getStatut() != 0) return false;
					return y > 0;
				}

				public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
					Equipe e = t.getEquipe(rowIndex);
					if (columnIndex == 1) {
						e.setNomJ1((String)aValue);
					} else if (columnIndex == 2) {
						e.setNomJ2((String)aValue);
					}
					t.updateEquipe(rowIndex);
					fireTableDataChanged();
				}
			};
			eq_jt = new JTable(eq_modele);
			JScrollPane eq_js = new JScrollPane(eq_jt);
			eq_p.add(eq_js);

			JPanel bt    = new JPanel();
			eq_ajouter   = new JButton("Ajouter une équipe");
			eq_supprimer = new JButton("Supprimer une équipe");
			eq_valider   = new JButton("Valider les équipes");

			eq_ajouter.addActionListener(arg0 -> {
				t.addEquipe();
				eq_valider.setEnabled(t.getNbEquipes() > 0 && t.getNbEquipes() % 2 == 0) ;
				eq_modele.fireTableDataChanged();
				if (t.getNbEquipes() > 0) {
					eq_jt.getSelectionModel().setSelectionInterval(0, 0);
				}
			});

			eq_supprimer.addActionListener(e -> {
				if (Fenetre.this.eq_jt.getSelectedRow() != -1) {
					t.deleteEquipe(t.getEquipe(Fenetre.this.eq_jt.getSelectedRow()).getId());
				}
				eq_valider.setEnabled(t.getNbEquipes() > 0 && t.getNbEquipes() % 2 == 0) ;
				eq_modele.fireTableDataChanged();
				if (t.getNbEquipes() > 0) {
					eq_jt.getSelectionModel().setSelectionInterval(0, 0);
				}
			});

			eq_valider.addActionListener(e -> {
				t.genererMatchs();
				Fenetre.this.majboutons();
				Fenetre.this.tracer_tournoi_matchs();
			});
			if (t.getNbEquipes() > 0) {
				eq_jt.getSelectionModel().setSelectionInterval(0, 0);
			}
			bt.add(eq_ajouter);
			bt.add(eq_supprimer);
			bt.add(eq_valider);
			eq_p.add(bt);
			eq_p.add(new JLabel("Dans le cas de nombre d'équipes impair, créer une équipe virtuelle"));
		}
		if (t.getStatut() != 0) {
			eq_ajouter.setEnabled(false);
			eq_supprimer.setEnabled(false);
			eq_valider.setEnabled(t.getStatut() == 1);
		} else {
			eq_ajouter.setEnabled(true);
			eq_supprimer.setEnabled(true);	
			eq_valider.setEnabled(t.getNbEquipes() > 0) ;
		}
		fen.show(c, EQUIPES);

	}

	private JScrollPane tours_js;

	private JButton tours_ajouter;
	private JButton tours_supprimer;

	private void tracer_tours_tournoi(){
		if (t == null) {
			return;
		}
		majboutons();
		Vector<Vector<Object>> to =new Vector<>();
		Vector<Object> v;
		boolean peutajouter = true;
		try {
			ResultSet rs = DialogMatch.getToursParMatch(t.getId());
			while (rs.next()) {
				v = new Vector<>();
				v.add(rs.getInt("num_tour"));
				v.add(rs.getInt("tmatchs"));
				v.add(rs.getString("termines"));
				to.add(v);
				peutajouter = peutajouter && rs.getInt("tmatchs") == rs.getInt("termines");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Vector<String> columnNames = new Vector<>();
		columnNames.add("Numéro du tour");
		columnNames.add("Nombre de matchs");
		columnNames.add("Matchs joués");
		JTable tours_t = new JTable(to, columnNames);
		if (tours_trace) {
			tours_js.setViewportView(tours_t);
		} else {
			tours_trace  = true;
			JPanel tours_p = new JPanel();
			BoxLayout tours_layout = new BoxLayout(tours_p, BoxLayout.Y_AXIS);
			tours_p.setLayout(tours_layout);
			JLabel tours_desc = new JLabel("Tours");
			tours_p.add(tours_desc);
			tours_p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
			c.add(tours_p, TOURS);



			tours_js = new JScrollPane();
			tours_js.setViewportView(tours_t);
			tours_p.add(tours_js);

			JPanel bt    = new JPanel();
			tours_ajouter   = new JButton("Ajouter un tour");
			tours_supprimer = new JButton("Supprimer le dernier tour");
			bt.add(tours_ajouter);
			bt.add(tours_supprimer);
			tours_p.add(bt);
			tours_p.add(new JLabel("Pour pouvoir ajouter un tour, terminez tous les matchs du précédent."));
			tours_p.add(new JLabel("Le nombre maximum de tours est \"le nombre total d'équipes - 1\""));
			tours_ajouter.addActionListener( arg0 -> {
				t.ajouterTour();
				Fenetre.this.tracer_tours_tournoi();
			});
			tours_supprimer.addActionListener( e -> {
				t.supprimerTour();
				Fenetre.this.tracer_tours_tournoi();
			});
		}
		if (to.size() == 0) {
			tours_supprimer.setEnabled(false);
			tours_ajouter.setEnabled(true);
		} else {
			tours_supprimer.setEnabled(t.getNbTours() > 1);
			tours_ajouter.setEnabled(peutajouter && t.getNbTours() < t.getNbEquipes() - 1);
		}

		fen.show(c, TOURS);
	}

	private AbstractTableModel match_modele;

	private JLabel match_statut;
	private JButton match_valider;

	private void tracer_tournoi_matchs() {
		if (t == null) {
			return ;
		}
		majboutons();
		if (match_trace) {
			t.majMatch();
			match_modele.fireTableDataChanged();
			majStatutM();
		} else {
			match_trace = true;
			JPanel match_p = new JPanel();
			BoxLayout match_layout = new BoxLayout(match_p, BoxLayout.Y_AXIS);
			match_p.setLayout(match_layout);
			JLabel match_desc = new JLabel("Matchs du tournoi");
			match_p.add(match_desc);
			match_p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
			c.add(match_p, MATCHS );

			match_modele = new AbstractTableModel() {
				private static final long serialVersionUID = 1L;
				@Override
				public Object getValueAt(int arg0, int arg1) {
					Object r=null;
					switch(arg1){
					case 0:
						r= t.getMatch(arg0).getNum_tour();
					break;
					case 1:
						r= t.getMatch(arg0).getEq1();
					break;
					case 2:
						r= t.getMatch(arg0).getEq2();
					break;
					case 3:
						r= t.getMatch(arg0).getScore1();
					break;
					case 4:
						r= t.getMatch(arg0).getScore2();
					break;
					}
					return r;

				}
				public String getColumnName(int col) {
					if (col == 0) {
						return "Tour";
					} else if (col == 1) {
						return "Équipe 1";
					} else if (col == 2) {
						return "Équipe 2";
					} else if (col == 3) {
						return "Score équipe 1";
					} else if (col == 4) {
						return "Score équipe 2";
					} else {
						return "??";
					}
				}
				@Override
				public int getRowCount() {
					if (t == null) return 0;
					return t.getNbMatchs();
				}

				@Override
				public int getColumnCount() {
					return 5;
				}
				public boolean isCellEditable(int x, int y) {
					return y > 2 && t.getMatch(x).getNum_tour() == t.getNbTours();
				}
				public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
					MatchM m = t.getMatch(rowIndex);
					int sco;
					if (columnIndex == 3) {
						try {
							sco = Integer.parseInt((String)aValue);
							m.setScore1(sco);
							t.majMatch(rowIndex);

						} catch(Exception e) {
							return ;
						}

					} else if (columnIndex == 4) {
						try {
							sco = Integer.parseInt((String)aValue);
							m.setScore2(sco);
							t.majMatch(rowIndex);

						} catch(Exception e) {
							return ;
						}
					}

					fireTableDataChanged();
					Fenetre.this.majStatutM();
					Fenetre.this.majboutons();
				}
			};
			JTable match_jt = new JTable(match_modele);

			JScrollPane match_js = new JScrollPane(match_jt);
			match_p.add(match_js);

			JPanel match_bas = new JPanel();
			match_bas.add(match_statut = new JLabel("?? Matchs joués"));
			match_bas.add(match_valider = new JButton("Afficher les résultats"));
			match_valider.setEnabled(false);

			match_p.add(match_bas);
			majStatutM();

		}

		fen.show(c, MATCHS);

	}

    private JScrollPane resultats_js;

	private void tracer_tournoi_resultats() {
		if (t == null) {
			return ;
		}

		Vector<Vector<Object>> to = new Vector<>();
		Vector<Object> v;
		try {
			ResultSet rs = DialogMatch.getResultMatch(t.getId());
			while(rs.next()){
				v = new Vector<>();
				v.add(rs.getInt("equipe"));
				v.add(rs.getString("joueur1"));
				v.add(rs.getString("joueur2"));
				v.add(rs.getInt("score"));
				v.add(rs.getInt("matchs_gagnes"));
				v.add(rs.getInt("matchs_joues"));
				to.add(v);
			}
		} catch (SQLException e) {
			afficherErreur("Erreur lors de la récupération des résultats du match de ce tournoi");
			System.out.println(e.getMessage());
		}
		Vector<String> columnNames = new Vector<>();
		columnNames.add("Numéro d'équipe");
		columnNames.add("Nom joueur 1");
		columnNames.add("Nom joueur 2");
		columnNames.add("Score");
		columnNames.add("Matchs gagnés");
		columnNames.add("Matchs joués");
		JTable resultats_jt = new JTable(to, columnNames);
		resultats_jt.setAutoCreateRowSorter(true);

		if (resultats_trace) {
			resultats_js.setViewportView(resultats_jt);
		} else {
			resultats_trace  = true;
			JPanel resultats_p = new JPanel();
			BoxLayout resultats_layout = new BoxLayout(resultats_p, BoxLayout.Y_AXIS);

			resultats_p.setLayout(resultats_layout);
			JLabel resultats_desc = new JLabel("Résultats du tournoi");
			resultats_p.add(resultats_desc);
			resultats_p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
			c.add(resultats_p, RESULTATS );

			resultats_js = new JScrollPane(resultats_jt);
			resultats_p.add(resultats_js);

			JPanel resultats_bas = new JPanel();
			resultats_bas.add(new JLabel("Gagnant:"));
			
			resultats_p.add(resultats_bas);
		}

		fen.show(c, RESULTATS);

	}

	private void majStatutM() {
		int total, termines;
		try {
			ResultSet rs = DialogMatch.getMatchTermines(t.getId());
			rs.next();
			total    = rs.getInt(1);
			termines = rs.getInt(2);
		} catch (SQLException e) {
			e.printStackTrace();
			return ;
		}
		match_statut.setText(termines + "/" + total + " matchs terminés");
		match_valider.setEnabled(total == termines);
	}

	public static void afficherErreur(String message) {
		JOptionPane.showMessageDialog(null, message, "ERREUR", JOptionPane.ERROR_MESSAGE);
	}

}
