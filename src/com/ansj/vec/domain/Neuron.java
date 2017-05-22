package com.ansj.vec.domain;

import java.io.Serializable;

public abstract class Neuron implements Comparable<Neuron>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int freq;
	public Neuron parent;
	public int code;

	@Override
	public int compareTo(Neuron o) {
		// TODO Auto-generated method stub
		if (this.freq > o.freq) {
			return 1;
		} else {
			return -1;
		}
	}

}
