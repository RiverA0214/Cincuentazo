package edu.univalle.cincuentazo.model;

import java.util.HashSet;
import java.util.Set;

public class EliminatedPlayers {

    private final Set<IPlayer> eliminated = new HashSet<>();

    public void add(IPlayer player) {
        eliminated.add(player);
    }

    public boolean contains(IPlayer player) {
        return eliminated.contains(player);
    }

    public Set<IPlayer> getAll() {
        return new HashSet<>(eliminated);
    }

    public int count() {
        return eliminated.size();
    }
}
