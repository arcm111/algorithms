import java.math.BigInteger;
import java.util.Random;

/**
 * RSA public key cryptosystem.
 * RSA relies on the dramatic difference between the ease of finding large 
 * prime numbers and the difficulty of factoring the product of two large
 * prime numbers.
 * Each client has a public key that can be accessed by anyone and a private 
 * key that is kept a secret. The private key is used to encypt a message and
 * the public key is used to decrypt it.
 * Based on the chinese remainder theorem such that:
 * (M^e)^d === M mod p and (M^e)^d === M mod q
 * therefore, (M^e)^d = M mod n.
 */
public class RSA {
    private final static Random random = new Random();
    private final static BigInteger one = new BigInteger("1");

    private BigInteger d; // private key
    private BigInteger e; // public key
    private BigInteger n; // pq

    /**
     * Generates an B-bit public and private key.
     * Finds two large primes p and q and computes phi(n) where n = pq,
     * then it uses phi(n) to generate the public and private key which are
     * multiplicative inverse of each other modulo phi(n).
     *
     * @param B bit-size of each p and q
     */
    public RSA(int B) {
        BigInteger p = BigInteger.probablePrime(B/2, random);
        BigInteger q = BigInteger.probablePrime(B/2, random);
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

        this.n = p.multiply(q);
        // public key e must be a relatively small odd integer
        // in practice, a common value for e is 2^16 + 1
        this.e = new BigInteger("65537");
        this.d = e.modInverse(phi); // e inverse modulo phi(n)
    }

    /**
     * Encrypts a message using the private key.
     * Running time is <em>O(1)</em> modular operations and <em>O(B^2)</em>
     * bit operations; {@code lg(e) O(1)} because e is a small odd integer.
     *
     * @param message the message to encrypt.
     * @return the encrypted message
     */
    public String encrypt(String message) {
        BigInteger M = stringToInteger(message);
        BigInteger C = M.modPow(e, n);
        return C.toString(16).toUpperCase();
    }

    /**
     * Decrypts a message using the public key.
     * Running time is <em>O(B)</em> modular operations and <em>O(B^3)</em>
     * bit operations; {@code lg(d) <= B}.
     *
     * @param message the message to encrypt.
     * @return the encrypted message
     */
    public String decrypt(String hex) {
        BigInteger C = new BigInteger(hex, 16);
        BigInteger M = C.modPow(d, n);
        return new String(M.toByteArray());
    }

    /**
     * Converts a string to integer representation.
     *
     * @param message the message to convert
     * @return integer representation on the message
     */
    private BigInteger stringToInteger(String message) {
        return new BigInteger(message.getBytes());
    }

    public String toString() {
        String s = "";
        s += "public  = " + e  + "\n";
        s += "private = " + d + "\n";
        s += "modulus = " + n;
        return s;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        int B = 1024;
        RSA key = new RSA(B);
        System.out.println(key);
        System.out.println();

        String message = "Hello, this is a secret message!";
        System.out.println("message   = " + message);
        String encrypted = key.encrypt(message);
        System.out.println("encrypted = " + encrypted);
        String decrypted = key.decrypt(encrypted);
        System.out.println("decrypted = " + decrypted);
    }
}
