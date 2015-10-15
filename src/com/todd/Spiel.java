package com.todd;

import java.util.List;

public class Spiel {

	private final double SPIEL_GEWINN = 1.0;
	private final double SPIEL_VERLUST = -1.0;
	private final double SPIEL_UNENTSCHIEDEN = 0.0;

	public Spiel() {
	}

	public SpielzugRO play(final int pTiefe, final Spielfeld pFeld, final SpielerRing pSpielerRing,
			final Spieler pSpielerAmZug, final Spieler pBewerteterSpieler) {

		SpielzugRO aktuellerSpielzug = new SpielzugRO();
		double maxGewonnen = this.SPIEL_UNENTSCHIEDEN;
		if (pTiefe <= 10) {
			if (pSpielerAmZug.equals(pBewerteterSpieler)) {
				maxGewonnen = this.SPIEL_VERLUST;
			} else {
				maxGewonnen = this.SPIEL_GEWINN;
			}
			List<Zug> zuege = pFeld.getAlleZuege(pSpielerAmZug);
			Zug besterZug = null;
			if (zuege.size() == 0) {
				maxGewonnen = this.SPIEL_UNENTSCHIEDEN;
			} else {
				for (Zug zug : zuege) {
					Spielfeld neuesFeld = zug.doIt(pFeld, pSpielerAmZug);
					double gewonnen = neuesFeld.gewonnen(pSpielerAmZug, pBewerteterSpieler);
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
					if (this.aktuellerSpielerHatGewonnen(pSpielerAmZug, pBewerteterSpieler, maxGewonnen)) {
						if (pTiefe == 1) {
							System.out.println("Siegzug: " + zug.toString());
						}
						break;
					}
					if (this.aktuellerSpielerHatVerloren(pSpielerAmZug, pBewerteterSpieler, maxGewonnen)) {
						break;
					}
				} // for
			}
			this.printSummary(pTiefe, maxGewonnen, besterZug);
		}
		aktuellerSpielzug.setWertung(maxGewonnen);
		return aktuellerSpielzug;
	}

	private boolean aktuellerSpielerHatGewonnen(final Spieler pSpielerAmZug, final Spieler pBewerteterSpieler,
			final double pBewertung) {
		return (pSpielerAmZug.equals(pBewerteterSpieler) && (pBewertung == this.SPIEL_GEWINN));
	}

	private boolean aktuellerSpielerHatVerloren(final Spieler pSpielerAmZug, final Spieler pBewerteterSpieler,
			final double pBewertung) {
		return (!pSpielerAmZug.equals(pBewerteterSpieler) && (pBewertung == this.SPIEL_VERLUST));
	}

	private double wertung(final int pTiefe, final Spieler pSpielerAmZug, final Spieler pBewerteterSpieler,
			final double pGewonnen, final double pMaxGewonnen) {
		double maxGewonnen = 0;
		if (pSpielerAmZug.equals(pBewerteterSpieler)) {
			maxGewonnen = Math.max(pMaxGewonnen, pGewonnen);
		} else {
			maxGewonnen = Math.min(pMaxGewonnen, pGewonnen);
		}
		return maxGewonnen;
	}

	private void printSummary(final int pTiefe, final double pMaxGewonnen, final Zug pBesterZug) {
		if ((pTiefe == 2) && (pMaxGewonnen == 100)) {
			System.out.println();
		}
		this.indent(pTiefe);
		System.out.println(pTiefe + ") ====== max gewonnen: " + pMaxGewonnen + "; bester Zug: "
				+ ((pBesterZug != null) ? pBesterZug.toString() : "---"));
	}

	private void printZug(final int pTiefe, final Zug pZug, final double pGewonnen) {
		this.indent(pTiefe);
		System.out.println(pTiefe + ") Zug: " + pZug.toString() + " - Bewertung: " + pGewonnen);
	}

	private void indent(final int pTiefe) {
		for (int i = 0; i < pTiefe; i++) {
			System.out.print("    ");
		}
	}
}
