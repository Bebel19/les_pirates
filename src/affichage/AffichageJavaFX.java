package affichage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import entites.Case;
import entites.Pirate;
import entites.Plateau;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import utils.Couleur;
import utils.PirateNom;

public class AffichageJavaFX implements IAffichage {

	private GridPane plateauGrid;
	private List<PirateNom> piratesDejaChoisis = new ArrayList<>();
	private List<Couleur> couleursDejaChoisies = new ArrayList<>();

	// Constructeur
	public AffichageJavaFX(GridPane plateauGrid) {
		this.plateauGrid = plateauGrid;
	}

	@Override
	public void afficherPlateau(Plateau plateau) {
		// Effacer le plateau existant
		plateauGrid.getChildren().clear();

		int nbCases = Plateau.getNbCases();
		int size = 100; // Taille de chaque case

		// Calculate the number of rows and columns based on the total number of cells
		int numRows = (int) Math.ceil(Math.sqrt(nbCases));
		int numCols = (int) Math.ceil((double) nbCases / numRows);

		int row = 0, col = 0;
		int dx = 1, dy = 0;
		int steps = numCols;
		int stepCounter = 0;

		for (int i = 0; i < nbCases; i++) {
			Case caseCourante = plateau.getCase(i); // Get the current case from the plateau

			Rectangle rectangle = new Rectangle(size, size);

			// Fill the rectangle based on the type of the current case
			if (caseCourante == null) {
				rectangle.setFill(Color.GREEN);
			} else {
				switch (caseCourante.getEffet()) {
				case ARME:
					rectangle.setFill(Color.BLUE);
					break;
				case RHUM:
					rectangle.setFill(Color.YELLOW);
					break;
				default:
					rectangle.setFill(Color.LIGHTGRAY);
					break;
				}
			}
			// Add a border to the rectangles
			rectangle.setStroke(Color.BLACK);
			rectangle.setStrokeWidth(1);

			// Add the rectangle to the grid pane
			plateauGrid.add(rectangle, col, row);

			row += dy;
			col += dx;
			stepCounter++;

			// Change direction when reaching the end of steps in one direction
			if (stepCounter == steps) {
				stepCounter = 0;
				int temp = dx;
				dx = -dy;
				dy = temp;
				if (dy == 0) {
					steps--; // Reduce the number of steps after changing direction horizontally
				}
			}
		}
	}

	@Override
	public void afficherMessage(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Message");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public void afficherPosition(int positionAfficher, Case caseCourante) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afficherGagnant(Pirate pirate) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("F�licitations !");
		alert.setHeaderText(null);
		alert.setContentText("Le vaillant pirate " + pirate.getNom() + " remporte la partie !");
		alert.showAndWait();
	}

	@Override
	public void afficherDebutTour(Pirate pirate) {
		afficherPopup("D�but du tour de " + pirate.getNom(), "Le tour du pirate " + pirate.getNom() + " commence.");
	}

	@Override
	public void afficherFinTour(Pirate pirate) {
		afficherPopup("Fin du tour de " + pirate.getNom(), "Le tour du pirate " + pirate.getNom() + " se termine.");
	}

	// M�thode utilitaire pour afficher une popup avec un titre et un message
	private void afficherPopup(String titre, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titre);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public int demanderNombreJoueurs() {
		int nombreJoueurs = 0;
		int maxJoueurs = Math.min(PirateNom.values().length, Couleur.values().length);
		// Cr�er une bo�te de dialogue pour demander le nombre de joueurs
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Nombre de Joueurs");
		dialog.setHeaderText("Entrez le nombre de joueurs :");
		dialog.setContentText("Nombre de joueurs :");

		// Boucle pour demander le nombre de joueurs jusqu'� ce qu'une valeur valide
		// soit entr�e
		boolean valide = false;
		while (!valide) {
			// Afficher la bo�te de dialogue et attendre la r�ponse de l'utilisateur
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				try {
					nombreJoueurs = Integer.parseInt(result.get());
					if (nombreJoueurs >= 2 && nombreJoueurs <= maxJoueurs) {
						valide = true; // La valeur est valide, sortir de la boucle
					} else {
						System.err.println("Erreur : Nombre de joueurs invalide. Veuillez entrer un nombre entre 2 et "
								+ maxJoueurs + ".");
					}
				} catch (NumberFormatException e) {
					System.err.println("Erreur : Entr�e invalide pour le nombre de joueurs.");
				}
			} else {
				// Si l'utilisateur annule la bo�te de dialogue, fermer l'application
				Platform.exit();
			}
		}

		return nombreJoueurs;
	}
	@Override
	public PirateNom choisirPirate() {
	    // Filtrer les options disponibles pour enlever celles d�j� choisies
	    List<PirateNom> optionsDisponibles = new ArrayList<>();
	    for (PirateNom pirate : PirateNom.values()) {
	        if (!piratesDejaChoisis.contains(pirate)) {
	            optionsDisponibles.add(pirate);
	        }
	    }

	    // Cr�er une bo�te de dialogue pour choisir le pirate
	    ChoiceDialog<PirateNom> dialog = new ChoiceDialog<>(optionsDisponibles.get(0), optionsDisponibles);
	    dialog.setTitle("Choix du Pirate");
	    dialog.setHeaderText("Choisissez le pirate :");
	    dialog.setContentText("Pirate :");

	    // Afficher la bo�te de dialogue et attendre la r�ponse de l'utilisateur
	    Optional<PirateNom> result = dialog.showAndWait();
	    if (result.isPresent()) {
	        PirateNom pirateChoisi = result.get();
	        // Mettre � jour la liste des pirates d�j� choisis
	        piratesDejaChoisis.add(pirateChoisi);
	        return pirateChoisi;
	    } else {
	        // Si l'utilisateur annule la bo�te de dialogue, ne rien faire
	        return null; // Retourner null pour indiquer une annulation
	    }
	}

	
	@Override
	public Couleur choisirCouleur() {
	    // Filtrer les options disponibles pour enlever celles d�j� choisies
	    List<Couleur> optionsDisponibles = new ArrayList<>();
	    for (Couleur couleur : Couleur.values()) {
	        if (!couleursDejaChoisies.contains(couleur)) {
	            optionsDisponibles.add(couleur);
	        }
	    }

	    // Cr�er une liste personnalis�e d'options pour le ChoiceDialog
	    List<CouleurOption> couleurOptions = new ArrayList<>();
	    for (Couleur couleur : optionsDisponibles) {
	        couleurOptions.add(new CouleurOption(couleur));
	    }

	    // Cr�er une bo�te de dialogue pour choisir la couleur
	    ChoiceDialog<CouleurOption> dialog = new ChoiceDialog<>(couleurOptions.get(0), couleurOptions);
	    dialog.setTitle("Choix de la Couleur");
	    dialog.setHeaderText("Choisissez une couleur :");
	    dialog.setContentText("Couleur :");

	    // Afficher la bo�te de dialogue et attendre la r�ponse de l'utilisateur
	    Optional<CouleurOption> result = dialog.showAndWait();
	    if (result.isPresent()) {
	        Couleur couleurChoisie = result.get().getCouleur();
	        // Mettre � jour la liste des couleurs d�j� choisies si n�cessaire
	        couleursDejaChoisies.add(couleurChoisie);
	        return couleurChoisie;
	    } else {
	        // Si l'utilisateur annule la bo�te de dialogue, ne rien faire
	        return null;
	    }
	}

	// Classe pour repr�senter une option de couleur avec un nom convivial
	class CouleurOption {
	    private Couleur couleur;

	    public CouleurOption(Couleur couleur) {
	        this.couleur = couleur;
	    }

	    public Couleur getCouleur() {
	        return couleur;
	    }

	    @Override
	    public String toString() {
	        return couleur.getNom();
	    }
	}






}