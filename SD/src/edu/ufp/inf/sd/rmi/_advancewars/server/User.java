package edu.ufp.inf.sd.rmi._advancewars.server;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Date;

/**
 *
 * @author rmoreira
 */
public class User {

    private String uname;
    private String pword;
    private static SecretKey key=io.jsonwebtoken.security.Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public User(String uname, String pword) {
        this.uname = uname;
        this.pword = pword;
    }


    public static String generateJWT(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        Date expirationTime = new Date(currentTimeMillis + 3600000); // 1 hour


        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public static boolean verifyJWT(String jwt) {
        try {
            Jws<Claims> parsedJwt = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt);

            return parsedJwt.getBody().getSubject().equals(jwt);
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public String toString() {
        return "User{" + "uname=" + uname + ", pword=" + pword + '}';
    }

    /**
     * @return the uname
     */
    public String getUname() {
        return uname;
    }

    /**
     * @param uname the uname to set
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * @return the pword
     */
    public String getPword() {
        return pword;
    }

    /**
     * @param pword the pword to set
     */
    public void setPword(String pword) {
        this.pword = pword;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
