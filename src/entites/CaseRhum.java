package entites;


import utils.Effet;

public class CaseRhum extends Case {

	public CaseRhum(int numero) {
		super(numero, Effet.RHUM);
	}

	@Override
	public void appliquerEffet(Pirate pirate, Jeu jeu) {
		De de = new De();
		int valRecul =de.lancerDe();
		jeu.getAffichage().afficherIvresse(valRecul);
		jeu.deplacerPirate(pirate, -valRecul);
		
	}

}