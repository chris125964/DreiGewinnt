package com.todd;

import java.util.List;

public class Spiel {

	private final int SPIEL_GEWINN = 100;
	private final int SPIEL_VERLUST = -100;

	public Spiel() {
	}

	public SpielzugRO play(final int pTiefe, final Spielfeld pFeld, final SpielerRing pSpielerRing,
			final Spieler pSpielerAmZug, final Spieler pBewerteterSpieler) {

		SpielzugRO aktuellerSpielzug = new SpielzugRO();
		int maxGewonnen = 0;
		if (pTiefe <= 10) {
			if (pSpielerAmZug.equals(pBewerteterSpieler)) {
				maxGewonnen = this.SPIEL_VERLUST;
			} else {
				maxGewonnen = this.SPIEL_GEWINN;
			}
			List<Zug> zuege = pFeld.getAlleZuege(pSpielerAmZug);
			Zug besterZug = null;
			if (zuege.size() == 0) {
				maxGewonnen = 0; // keine Züge -> unentschieden
			} else {
				for (Zug zug : zuege) {
					Spielfeld neuesFeld = zug.doIt(pFeld, pSpielerAmZug);
					int gewonnen = neuesFeld.gewonnen(pSpielerAmZug, pBewerteterSpieler);
					this.printZug(pTiefe, zug, gewonnen);

					if ((gewonnen == this.SPIEL_GEWINN) || (gewonnen == this.SPIEL_VERLUST)) {
						// gewonnen -> keine weitere Aktion mehr notwendig
						besterZug = zug;
						maxGewonnen = this.wertung(pTiefe, pSpielerAmZug, pBewerteterSpieler, gewonnen, maxGewonnen);
						break;
					} else {
						Spieler naechsterSpieler = pSpielerRing.nextSpieler(pSpielerAmZug);
						SpielzugRO naechsterSpielzug = this.play(pTiefe + 1, neuesFeld, pSpielerRing, naechsterSpieler,
								pBewerteterSpieler);
						gewonnen = naechsterSpielzug.getWertung();
					}
					maxGewonnen = this.wertung(pTiefe, pSpielerAmZug, pBewerteterSpieler, gewonnen, maxGewonnen);
					if (pSpielerAmZug.equals(pBewerteterSpieler) && (maxGewonnen == this.SPIEL_GEWINN)) {
						if (pTiefe == 1) {

							System.out.println("Siegzug: " + zug.toString());
						}
						break;
					}
					if (!pSpielerAmZug.equals(pBewerteterSpieler) && (maxGewonnen == this.SPIEL_VERLUST)) {
						break;
					}
				} // for
			}
			this.printSummary(pTiefe, maxGewonnen, besterZug);
		}
		aktuellerSpielzug.setWertung(maxGewonnen);
		return aktuellerSpielzug;
	}

	private int wertung(final int pTiefe, final Spieler pSpielerAmZug, final Spieler pBewerteterSpieler,
			final int pGewonnen, final int pMaxGewonnen) {
		int maxGewonnen = 0;
		if (pSpielerAmZug.equals(pBewerteterSpieler)) {
			maxGewonnen = Math.max(pMaxGewonnen, pGewonnen);
		} else {
			maxGewonnen = Math.min(pMaxGewonnen, pGewonnen);
		}
		return maxGewonnen;
	}

	private void printSummary(final int pTiefe, final int pMaxGewonnen, final Zug pBesterZug) {
		if ((pTiefe == 2) && (pMaxGewonnen == 100)) {
			System.out.println();
		}
		this.indent(pTiefe);
		System.out.println(pTiefe + ") ====== max gewonnen: " + pMaxGewonnen + "; bester Zug: "
				+ ((pBesterZug != null) ? pBesterZug.toString() : "---"));
	}

	private void printZug(final int pTiefe, final Zug pZug, final int pGewonnen) {
		this.indent(pTiefe);
		System.out.println(pTiefe + ") Zug: " + pZug.toString() + " - Bewertung: " + pGewonnen);
	}

	private void indent(final int pTiefe) {
		for (int i = 0; i < pTiefe; i++) {
			System.out.print("    ");
		}
	}
}
