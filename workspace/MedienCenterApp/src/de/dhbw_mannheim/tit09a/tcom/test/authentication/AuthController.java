package de.dhbw_mannheim.tit09a.tcom.test.authentication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class AuthController
{
    // --------------------------------------------------------------------------------
    // -- Class Variable(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    // Message Digest Algorithm
    // Possible: MD2 (16 Bytes), MD5 (16), SHA-1 (20), SHA-256 (32), SHA-384 (48), SHA-512 (64)
    private final static String MSG_DIGEST_ALGORITHM = "SHA-256";

    // Random Number Generation, Possible: SHA1PRNG
    private final static String RNG_ALGORITHM = "SHA1PRNG";

    /*
     * US-ASCII Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set
     * ISO-8859-1 ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1 UTF-8 Eight-bit UCS Transformation Format
     * UTF-16BE Sixteen-bit UCS Transformation Format, big-endian byte order UTF-16LE Sixteen-bit UCS
     * Transformation Format, little-endian byte order UTF-16 Sixteen-bit UCS Transformation Format, byte
     * order identified by an optional byte-order mark
     */
    private final static String CHAR_ENCODING = "UTF-8";

    // How many times the hash password string is hashed to extend computation time for attackers.
    // The regular user will not notice the prolonged time because he does this only one time
    // and this process takes only a short percentage of the full operation of the sign on.
    // But hackers time is almost 100% trying Passwords, so they will notice.s
    @SuppressWarnings("unused")
    private final static int ITERATIONS = 1000;

    // The Length in Bytes of the generated Salt (bits are bytes*8)
    private final static int SALT_LENGTH = 8;

    // --------------------------------------------------------------------------------
    // -- Public Method(s) ------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public static byte[] generateSalt() throws NoSuchAlgorithmException
    {
	// Uses a secure Random not a simple Random
	SecureRandom random = SecureRandom.getInstance(RNG_ALGORITHM);
	// random.setSeed(System.currentTimeMillis());

	// Salt generation SALT_LENGTH*8 bits long
	byte[] bSalt = new byte[SALT_LENGTH];
	random.nextBytes(bSalt);
	return bSalt;
    }

    // --------------------------------------------------------------------------------
    public static byte[] getHash(int iterations, String password, byte[] salt)
	    throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
	MessageDigest digest = MessageDigest.getInstance(MSG_DIGEST_ALGORITHM);
	digest.reset();
	digest.update(salt);
	byte[] input = digest.digest(password.getBytes(CHAR_ENCODING));
	for (int i = 0; i < iterations; i++)
	{
	    digest.reset();
	    input = digest.digest(input);
	}
	return input;
    }

    // --------------------------------------------------------------------------------
    public static byte[] base64SToBytes(String data) throws IOException
    {
	return Base64.decodeBase64(data);
    }

    // --------------------------------------------------------------------------------
    public static String bytesToBase64(byte[] data)
    {
	return Base64.encodeBase64String(data);
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
