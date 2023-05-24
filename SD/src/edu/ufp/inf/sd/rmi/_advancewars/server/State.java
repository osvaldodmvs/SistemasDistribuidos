package edu.ufp.inf.sd.rmi._advancewars.server;

import java.io.Serializable;

/**
 * 
 * @author rui
 */
public class State implements Serializable {
    private String msg;
    private String id;


    //TODO : ID DO STATE = ID DO UTILIZADOR
    /**
     * 
     * @param id
     * @param m 
     */
    public State(String id, String m) {
        this.id = id;
        this.msg = m;
    }

    /**
     * 
     * @return 
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return 
     */
    public String getInfo(){
        return this.msg;
    }

    /**
     * 
     * @param m 
     */
    public void setInfo(String m){
        this.msg = m;
    }
}
