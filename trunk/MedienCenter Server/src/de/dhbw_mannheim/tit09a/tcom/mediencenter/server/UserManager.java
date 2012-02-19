package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.AuthenticationException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.KeyAlreadyExistsException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.KeyDoesNotExistExpcetion;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MiscUtil;

/**
 * Class for user authentification.
 * 
 * @author mhertram
 * 
 */
public class UserManager extends Manager
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	/**
	 * Message Digest Algorithm. Possible: MD2 (16 Bytes), MD5 (16), SHA-1 (20), SHA-256 (32), SHA-384 (48), SHA-512 (64)
	 */
	public final static String	MSG_DIGEST_ALGORITHM	= "SHA-512";

	/**
	 * Random Number Generator (RNG) algorithm. Possible: SHA1PRNG
	 */
	public final static String	RNG_ALGORITHM			= "SHA1PRNG";

	/**
	 * The name of the charset encoding used for getting a hashed password. For US-ASCII Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin
	 * block of the Unicode character set ISO-8859-1 ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1 UTF-8 Eight-bit UCS Transformation Format UTF-16BE
	 * Sixteen-bit UCS Transformation Format, big-endian byte order UTF-16LE Sixteen-bit UCS Transformation Format, little-endian byte order UTF-16
	 * Sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order mark
	 */
	public final static String	CHARSET_NAME			= "UTF-8";

	/**
	 * How many times the hash password string is hashed to extend computation time for attackers. The regular user will not notice the prolonged time
	 * because he does this only one time and this process takes only a short percentage of the full operation of the sign on. But hackers time is
	 * almost 100% trying Passwords, so they will notice.
	 * */
	public final static int		ITERATIONS				= 1000;

	/**
	 * The length in bytes of the generated Salt (bits are bytes * 8).
	 */
	public final static int		SALT_LENGTH				= 8;

	/**
	 * Maximal login and password length.
	 */
	public final static int		MAX_LENGTH				= 255;

	public final static String	VALID_USERNAME			= "[\\w\\d-_ ]+";

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	UserManager() throws Exception
	{
		super();
	}

	// --------------------------------------------------------------------------------
	@Override
	protected void init() throws Exception
	{
		initLogging(Level.ALL);
	}

	@Override
	protected void rollbackInit()
	{
		// Nothing
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public long idForLogin(Connection con, String login) throws SQLException, IOException
	{
		logger.debug("ENTRY {} {}", con, login);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSIdForLogin.sql"));
			ps.setString(1, login);
			logger.debug("PreparedStatement: " + ps);
			rs = ps.executeQuery();
			logger.debug("ResultSet: " + rs);
			long id = -1L;
			if (rs.next())
			{
				id = rs.getLong(1);
				if (rs.next())
					throw new SQLException("Duplicate entry for user with login: " + login);
			}
			logger.debug("EXIT {}", id);
			return id;
		}
		finally
		{
			IOUtil.close(ps);
			IOUtil.close(rs);
		}
	}

	// --------------------------------------------------------------------------------
	public String loginForId(Connection con, long id) throws SQLException, IOException
	{
		logger.debug("ENTRY {} {}", con, id);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSLoginForId.sql"));
			ps.setLong(1, id);
			rs = ps.executeQuery();
			String login = null;
			if (rs.next())
			{
				login = rs.getString(1);
				if (rs.next())
					throw new SQLException("Duplicate entry for user with id: " + id);

			}
			logger.debug("EXIT {}", login);
			return login;
		}
		finally
		{
			IOUtil.close(ps);
			IOUtil.close(rs);
		}
	}

	// --------------------------------------------------------------------------------
	public boolean userExists(Connection con, long id) throws SQLException, IOException
	{
		return (loginForId(con, id) != null);
	}

	// --------------------------------------------------------------------------------
	public boolean userExists(Connection con, String login) throws SQLException, IOException
	{
		return (idForLogin(con, login) > 0L);
	}

	// --------------------------------------------------------------------------------
	/**
	 * Changes the entry in the column <i>'pw'</i> to the <code>newPw</code> for the user specified by the id. Does an intern
	 * <code>con.commit()</code>.
	 * 
	 * @param con
	 * @param id
	 * @param newPw
	 * @param currentPw
	 * @return An return code of the ones specified in {@link SimpleReturnValue}.
	 * @throws SQLException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public void changePw(Connection con, long id, String newPw, String currentPw) throws SQLException, IOException, NoSuchAlgorithmException
	{
		logger.debug("ENTRY {} {} {} {}", new Object[] { con, id, newPw, currentPw });

		// Input validation
		MiscUtil.checkStringLength(newPw, 1, MAX_LENGTH);
		if (!authenticate(con, id, currentPw))
			throw new AuthenticationException(id + "", currentPw);
		
		// Set the pw to the new one
		setPw(con, id, newPw);

		logger.debug("EXIT {}");
	}

	// --------------------------------------------------------------------------------
	/**
	 * Resets the pw of the user specified by the login to a generated one.
	 * 
	 * @param con
	 * @param login
	 * @return A new generated pw.
	 * @throws NoSuchAlgorithmException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void resetPw(Connection con, String login) throws NoSuchAlgorithmException, SQLException, IOException
	{
		logger.debug("ENTRY {} {}", new Object[] { con, login });

		// Input validation
		long id = idForLogin(con, login);
		if (!userExists(con, id))
			throw new KeyDoesNotExistExpcetion(login);
		
		// Set the pw to the a generated one
		String newPw = generatePw();
		setPw(con, id, newPw);

		logger.info("User " + login + " requested password reset. New password: " + newPw);
		logger.debug("EXIT");
	}

	// --------------------------------------------------------------------------------
	/**
	 * Changes the entry in the column <i>'login'</i> for the user specified by the id. Does an intern <code>con.commit()</code>.
	 * 
	 * @param con
	 * @param id
	 * @param newLogin
	 * @param pw
	 * @return An return code of the ones specified in {@link SimpleReturnValue}.
	 * @throws SQLException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public void changeLogin(Connection con, long id, String newLogin, String pw) throws SQLException, IOException, NoSuchAlgorithmException
	{
		logger.debug("ENTER {} {} {} {}", new Object[] { con, id, newLogin, pw });
		PreparedStatement ps = null;
		try
		{
			// Input validation
			MiscUtil.checkStringLength(newLogin, 1, MAX_LENGTH);
			MiscUtil.ensureValidString(newLogin, VALID_USERNAME);
			if (!authenticate(con, id, pw))
				throw new AuthenticationException(id + "", pw);

			// Get the id for 'newLogin'. If > 0L, the user already exists.
			// But if the returned id equals this user's id, he can rename himself (f.i. mAx -> Max).
			long returnedId = idForLogin(con, newLogin);
			if (returnedId > 0L && id != returnedId)
				throw new KeyAlreadyExistsException("A user named " + newLogin + " already exists");

			// Database query
			ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSChangeLogin.sql"));
			ps.setString(1, newLogin);
			ps.setLong(2, id);
			int affectedRows = ps.executeUpdate();
			if (affectedRows != 1)
				throw new SQLException("Change of login failed for id " + id + ": Affected Rows: " + affectedRows);
			con.commit();

			logger.debug("EXIT");
		}
		catch (IllegalArgumentException | SQLException | IOException | NoSuchAlgorithmException e)
		{
			ServerMain.MAIN_LOGGER.warn("Rolling back due to: " + e.toString());
			con.rollback();
			throw e;
		}
		finally
		{
			IOUtil.close(ps);
		}
	}

	// --------------------------------------------------------------------------------
	/**
	 * Inserts a new user in the database. <b>IMPORTANT: Makes no intern commit.</b> You have to call <code>con.commit()</code> yourself afterwards.
	 * 
	 * @param con
	 *            An open connection to a database.
	 * @param login
	 *            The login of the user.
	 * @param pw
	 *            The password of the user.
	 * @return The id of the new user or -1L if the user already exists.
	 * @throws SQLException
	 *             If the database is unavailable or anything bad happens.
	 * @throws NoSuchAlgorithmException
	 *             If the Message Digest algorithm {@link UserManager#MSG_DIGEST_ALGORITHM} or the Random Number Generator algorithm
	 *             {@link UserManager#RNG_ALGORITHM} are not supported by the JVM.
	 * @throws IOException
	 *             If the SQL statement file could not be loaded successfully.
	 */
	public long insertUser(Connection con, String login, String pw) throws SQLException, NoSuchAlgorithmException, IOException
	{
		logger.debug("ENTER {} {} {}", new Object[] { con, login, pw });
		PreparedStatement ps = null;
		try
		{
			// Input validation
			MiscUtil.ensureValidString(login, UserManager.VALID_USERNAME);
			MiscUtil.checkStringLength(login, 1, UserManager.MAX_LENGTH);
			MiscUtil.checkStringLength(pw, 1, UserManager.MAX_LENGTH);

			// Get the id for the login
			long id;
			id = idForLogin(con, login);
			if (id > 0)
				throw new KeyAlreadyExistsException(login);

			// Compute digest and get generated salt
			byte[] bSalt = generateSalt();
			byte[] bDigest = hash(pw, bSalt);
			String digest = byteToURLSafeBase64(bDigest);
			String salt = byteToURLSafeBase64(bSalt);

			// Database query
			ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSInsertUser.sql"));
			ps.setString(1, login);
			ps.setString(2, digest);
			ps.setString(3, salt);
			logger.trace("PreparedStatement: " + ps.toString());
			int affectedRows = ps.executeUpdate();
			if (affectedRows != 1)
				throw new SQLException("Insert failed for " + login + ". Affected rows: " + affectedRows);
			id = idForLogin(con, login);
			if (id < 1)
				throw new SQLException("Insert failed for " + login + ". Illegal ID returned: " + id);

			logger.debug("EXIT {}", id);
			return id;
		}
		finally
		{
			IOUtil.close(ps);
		}
	}

	// --------------------------------------------------------------------------------
	/**
	 * Authenticates the user with a given login and password If password and/or login is null then always returns false. If the user does not exist
	 * in the database returns false.
	 * 
	 * @param con
	 *            An open connection to a database
	 * @param id
	 *            The id of the user
	 * @param pw
	 *            The password of the user
	 * @return Returns true if the user is authenticated, false otherwise
	 * @throws SQLException
	 *             If the database is inconsistent or unavailable ( (Two users with the same login, salt or digested password altered etc.)
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm SHA-1 is not supported by the JVM
	 */
	public boolean authenticate(Connection con, long id, String pw) throws SQLException, NoSuchAlgorithmException
	{
		logger.debug("ENTRY {} {} {}", new Object[] { con, id, pw });
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			boolean authenticated = false;
			boolean userExist = true;
			
			// Input validation
			if (!MiscUtil.checkStringLength(pw, 0, MAX_LENGTH))
			{
				logger.debug("User does not exist. pw null, empty or too long(" + MAX_LENGTH + "): " + pw);
				// TIME RESISTANT ATTACK
				// Computation time is equal to the time needed by a legitimate user
				userExist = false;
				id = -1L;
				pw = "";
			}

			// Get the stored digest and salt
			ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSSelectPwSalt.sql"));
			ps.setLong(1, id);
			logger.debug("PreparedStatement: " + ps.toString());
			rs = ps.executeQuery();
			String storedDigest, storedSalt;
			if (rs.next())
			{
				storedDigest = rs.getString("PW");
				storedSalt = rs.getString("SALT");
				// DATABASE VALIDATION
				if (storedDigest == null || storedSalt == null)
					throw new SQLException("Database inconsistent: Salt or Digested Password altered for id: " + id);

				// Should not happen, because login is the primary key
				if (rs.next())
					throw new SQLException("Database inconsistent: Two user with the same id: " + id);
			}
			else
			{
				// TIME RESISTANT ATTACK (Even if the user does not exist the
				// computation time is equal to the time needed for a legitimate user)
				logger.debug("User did not exist: " + id);
				storedDigest = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000==";
				storedSalt = "00000000000=";
				userExist = false;
			}
			logger.debug("storedDigest: " + storedDigest + "(" + storedDigest.length() + ")");
			logger.debug("storedSalt: " + storedSalt + "(" + storedSalt.length() + ")");
			byte[] bStoredDigest = base64ToByte(storedDigest);
			byte[] bStoredSalt = base64ToByte(storedSalt);

			// Compute the new digest
			byte[] bProposedDigest = hash(pw, bStoredSalt);
			String proposedDigest = byteToURLSafeBase64(bProposedDigest);
			logger.debug("Proposed Digest: " + proposedDigest + "(" + proposedDigest.length() + ")");

			// Check for equality
			authenticated = Arrays.equals(bProposedDigest, bStoredDigest) && userExist;
			logger.debug("EXIT {}", authenticated);
			return authenticated;
		}
		catch (IOException ex)
		{
			throw new SQLException("Database inconsistant: Salt or Digested Password altered!");
		}
		finally
		{
			IOUtil.close(rs);
			IOUtil.close(ps);
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	/**
	 * From a password, a number of iterations and a salt, returns the corresponding digest
	 * 
	 * @param iterations
	 *            The number of iterations of the algorithm
	 * @param pw
	 *            he password to encrypt
	 * @param salt
	 *            The salt
	 * @return The digested password
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm doesn't exist
	 * @throws UnsupportedEncodingException
	 *             If the Character Encoding is not supported
	 */
	private byte[] hash(String pw, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		logger.debug("ENTER {} {} {}", new Object[] { pw, Arrays.toString(salt) });
		MessageDigest digest = MessageDigest.getInstance(MSG_DIGEST_ALGORITHM);
		digest.reset();
		digest.update(salt);
		byte[] input = digest.digest(pw.getBytes(CHARSET_NAME));
		for (int i = 0; i < ITERATIONS; i++)
		{
			digest.reset();
			input = digest.digest(input);
		}
		logger.debug("EXIT {}", input);
		return input;
	}

	// --------------------------------------------------------------------------------
	/**
	 * From a base 64 representation, returns the corresponding byte[]
	 * 
	 * @param data
	 *            The base64 representation
	 * @return byte[] An byte array
	 */
	private static byte[] base64ToByte(String data)
	{
		return Base64.decodeBase64(data);
	}

	// --------------------------------------------------------------------------------
	/**
	 * From a byte[] returns a url safe base 64 representation
	 * 
	 * @param data
	 *            byte[]
	 * @return String
	 */
	private static String byteToURLSafeBase64(byte[] data)
	{
		return Base64.encodeBase64URLSafeString(data);
	}

	// --------------------------------------------------------------------------------
	private static byte[] generateSalt() throws NoSuchAlgorithmException
	{
		// Uses a secure Random not a simple Random
		SecureRandom random = SecureRandom.getInstance(RNG_ALGORITHM);
		// Salt generation 64 bits long
		byte[] bSalt = new byte[SALT_LENGTH];
		random.nextBytes(bSalt);
		return bSalt;
	}

	// --------------------------------------------------------------------------------
	private static String generatePw() throws NoSuchAlgorithmException
	{
		return byteToURLSafeBase64(generateSalt());
	}

	// --------------------------------------------------------------------------------
	private void setPw(Connection con, long id, String newPw) throws SQLException, IOException, NoSuchAlgorithmException
	{
		PreparedStatement ps = null;
		try
		{
			byte[] bSalt = generateSalt();
			byte[] bDigest = hash(newPw, bSalt);
			String digest = byteToURLSafeBase64(bDigest);
			String salt = byteToURLSafeBase64(bSalt);

			// Database query
			ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSSetPwSalt.sql"));
			ps.setString(1, digest);
			ps.setString(2, salt);
			ps.setLong(3, id);
			int affectedRows = ps.executeUpdate();
			if (affectedRows != 1)
				throw new SQLException("Change of password failed for id " + id + ": Affected Rows: " + affectedRows);
			con.commit();
		}
		catch (SQLException | IOException e)
		{
			ServerMain.MAIN_LOGGER.warn("Rolling back due to: " + e.toString());
			con.rollback();
			throw e;
		}
		finally
		{
			IOUtil.close(ps);
		}
	}
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
