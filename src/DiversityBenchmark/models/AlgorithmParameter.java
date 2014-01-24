package DiversityBenchmark.models;

public class AlgorithmParameter extends AbstractModel {
	private boolean ag;
	private boolean grassHopper;
	private boolean motley;
	private boolean swap;
	private boolean msd;
	private boolean mmr;

	public boolean isAg() {
		return ag;
	}

	public void setAg(boolean ag) {
		propertyChangeSupport.firePropertyChange("ag", this.ag, this.ag = ag);
	}

	public boolean isGrassHopper() {
		return grassHopper;
	}

	public void setGrassHopper(boolean grassHopper) {
		propertyChangeSupport.firePropertyChange("grassHopper",
				this.grassHopper, this.grassHopper = grassHopper);
	}

	public boolean isMotley() {
		return motley;
	}

	public void setMotley(boolean motley) {
		propertyChangeSupport.firePropertyChange("motley", this.motley,
				this.motley = motley);
	}

	public boolean isSwap() {
		return swap;
	}

	public void setSwap(boolean swap) {
		propertyChangeSupport.firePropertyChange("swap", this.swap,
				this.swap = swap);
	}

	public boolean isMsd() {
		return msd;
	}

	public void setMsd(boolean msd) {
		propertyChangeSupport.firePropertyChange("msd", this.msd,
				this.msd = msd);
	}

	public boolean isMmr() {
		return mmr;
	}

	public void setMmr(boolean mmr) {
		propertyChangeSupport.firePropertyChange("mmr", this.mmr,
				this.mmr = mmr);
	}

}
