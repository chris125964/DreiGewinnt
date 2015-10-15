package com.todd;

public class SpielzugRO {

	private int wertung;
	private Zug gewinnZug;

	public int getWertung() {
		return this.wertung;
	}

	public void setWertung(final int wertung) {
		this.wertung = wertung;
	}

	public Zug getGewinnZug() {
		return this.gewinnZug;
	}

	public void setGewinnZug(final Zug gewinnZug) {
		this.gewinnZug = gewinnZug;
	}

}
