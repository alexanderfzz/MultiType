package grooop;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import grooop.pojo.User;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String SECRETKEY = "NotSoSecretSecretKeyThatHappensToBeAsInsecureAsICanMakeItToBe";
    private static final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRETKEY)).build();

    public static String generateToken(User user) {
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);

        String token = JWT.create().withHeader(headerMap)
                .withClaim("username", user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .sign(Algorithm.HMAC256(SECRETKEY));
        return token;
    }

    public static boolean validateToken(String token) {

        if (token == null) {
            return false;
        }

        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static Map<String, Claim> getTokenClaims(String token) {
        if (token == null) {
            return null;
        }
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
        } catch (Exception e) {
            return null;
        }
        return decodedJWT.getClaims();
    }
}
