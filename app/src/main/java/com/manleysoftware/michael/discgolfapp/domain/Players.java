package com.manleysoftware.michael.discgolfapp.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Players extends ArrayList<Player> implements Serializable, List<Player> {
    List<Player> players;

    private int currentPlayerSelected;

    public int currentPlayerSelected() {
        return currentPlayerSelected;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Players(List<Player> players) {
        this.players = players;
    }

    public Players() {
        players = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Players{" +
                "players=" + players +
                '}';
    }

    public int size() {
        return players.size();
    }

    @Override
    public boolean isEmpty() {
        return players.isEmpty();
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return players.contains(o);
    }

    @NonNull
    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return players.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return players.toArray(a);
    }

    @Override
    public boolean add(Player player) {
        return players.add(player);
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return players.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return players.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Player> c) {
        return players.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends Player> c) {
        return players.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return players.removeAll(c);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return players.retainAll(c);
    }

    @Override
    public void clear() {
        players.clear();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return players.equals(o);
    }

    @Override
    public int hashCode() {
        return players.hashCode();
    }

    @Override
    public Player get(int index) {
        return players.get(index);
    }

    @Override
    public Player set(int index, Player element) {
        return players.set(index, element);
    }

    @Override
    public void add(int index, Player element) {
        players.add(index, element);
    }

    @Override
    public Player remove(int index) {
        return players.remove(index);
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return players.indexOf(o);
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return players.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<Player> listIterator() {
        return players.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<Player> listIterator(int index) {
        return players.listIterator(index);
    }

    @NonNull
    @Override
    public List<Player> subList(int fromIndex, int toIndex) {
        return players.subList(fromIndex, toIndex);
    }

    public void nextPlayer() {
        currentPlayerSelected++;
        if (currentPlayerSelected > players.size() - 1) {
            currentPlayerSelected = players.size() - 1;
        }
    }

    public void previousPlayer() {
        currentPlayerSelected--;
        if (currentPlayerSelected < 0) {
            currentPlayerSelected = 0;
        }
    }

    public Player currentPlayer() {
        return get(currentPlayerSelected);
    }
}
