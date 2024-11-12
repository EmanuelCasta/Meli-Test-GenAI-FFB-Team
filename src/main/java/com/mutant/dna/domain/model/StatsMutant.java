package com.mutant.dna.domain.model;

public class StatsMutant {
    private int count_mutant_dna;
    private int count_human_dna;
    private double ratio;

    public StatsMutant(int count_human_dna,int count_mutant_dna){
        this.count_human_dna = count_human_dna;
        this.count_mutant_dna = count_mutant_dna;
        this.ratio = (double) this.count_mutant_dna / this.count_human_dna;

    }

    public int getCount_mutant_dna() {
        return count_mutant_dna;
    }

    public int getCount_human_dna() {
        return count_human_dna;
    }

    public double getRatio() {
        return ratio;
    }
}
