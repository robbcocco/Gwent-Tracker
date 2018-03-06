package com.robbcocco.gwenttracker.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;

import com.robbcocco.gwenttracker.database.dao.CardDao;

import javax.inject.Inject;

/**
 * Created by rober on 3/6/2018.
 */

public class DatabaseHelper {

    private CardDao dao;

    @Inject
    public DatabaseHelper(CardDatabase cardDatabase) {
        dao = cardDatabase.cardDao();
    }

//    public State getState(int id) {
//        State state = dao.getState(id);
//        state.setCities(dao.getCities(id));
//        return state;
//    }
//
//    public List<State> getStates() {
//        List<State> states = dao.getStates();
//        for (State state : states) {
//            state.setCities(dao.getCities(state.getId()));
//        }
//        return states;
//    }
}