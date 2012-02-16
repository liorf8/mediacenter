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
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MiscUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ReturnObj;

/**
 * Class for user authentification.
 * 
 * @author mhertram
 * 
 */
public class Authenticator
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final String		CLASS_NAME				= Authenticator.class.getName();
	/**
	 * Message Digest Algorithm. Possible: MD2 (16 Bytes), MD5 (16), SHA-1 (20), SHA-256 (32), SHA-384 (48), SHA-512 (64)
	 */
	public final static String		MSG_DIGEST_ALGORITHM	= "SHA-512";

	/**
	 * Random Number Generator (RNG) algorithm. Possible: SHA1PRNG
	 */
	public final static String		RNG_ALGORITHM			= "SHA1PRNG";

	/**
	 * The name of the charset encoding used for getting a hashed password. For US-ASCII Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin
	 * block of the Unicode character set ISO-8859-1 ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1 UTF-8 Eight-bit UCS Transformation Format UTF-16BE
	 * Sixteen-bit UCS Transformation Format, big-endian byte order UTF-16LE Sixteen-bit UCS Transformation Format, little-endian byte order UTF-16
	 * Sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order mark
	 */
	public final static String		CHARSET_NAME			= "UTF-8";

	/**
	 * How many times the hash password string is hashed to extend computation time for attackers. The regular user will not notice the prolonged time
	 * because he does this only one time and this process takes only a short percentage of the full operation of the sign on. But hackers time is
	 * almost 100% trying Passwords, so they will notice.
	 * */
	public final static int			ITERATIONS				= 1000;

	/**
	 * The length in bytes of the generated Salt (bits are bytes * 8).
	 */
	public final static int			SALT_LENGTH				= 8;

	/**
	 * Maximal login and password length.
	 */
	public final static int			MAX_LENGTH				= 255;

	private static Authenticator	instance;

	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static synchronized Authenticator getInstance() throws Exception
	{
		if (instance == null)
			instance = new Authenticator();
		return instance;
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private final Logger	logger	= Logger.getLogger(CLASS_NAME);

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Authenticator() throws Exception
	{
		try
		{
			try
			{
				logger.addHandler(new FileHandler(CLASS_NAME + ".log", false));
				logger.setLevel(Level.ALL);
				logger.info(CLASS_NAME + " Logger started!");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			throw new Exception(CLASS_NAME + " <init> failed: " + e.getMessage(), e.getCause());
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public long idForLogin(Connection con, String login) throws SQLException, IOException
	{
		logger.entering(CLASS_NAME, "idForLogin", new Object[] { con, login });
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSIdForLogin.sql"));
			ps.setString(1, login);
			logger.finest("PreparedStatement: " + ps);
			rs = ps.executeQuery();
			logger.finest("ResultSet: " + rs);
			long id = -1L;
			if (rs.next())
			{
				id = rs.getLong(1);
				if (rs.next())
					throw new SQLException("Duplicate entry for user with login: " + login);
			}
			logger.exiting(CLASS_NAME, "idForLogin", id);
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
		logger.entering(CLASS_NAME, "loginForId", new Object[] { con, id });
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
			logger.exiting(CLASS_NAME, "loginForId", login);
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
	 * @return An return code of the ones specified in {@link ReturnObj}.
	 * @throws SQLException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public short changePw(Connection con, long id, String newPw, String currentPw) throws SQLException, IOException, NoSuchAlgorithmException
	{
		logger.entering(CLASS_NAME, "changePW", new Object[] { con, id, newPw, currentPw });
		short returnCode = ReturnObj.SUCCESS;
		PreparedStatement ps = null;
		try
		{
			if (!authenticate(con, id, currentPw))
			{
				returnCode = ReturnObj.ACCESS_DENIED;
			}
			else
			{
				// Database query
				ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSChangePw.sql"));
				ps.setString(1, newPw);
				ps.setLong(2, id);
				int affectedRows = ps.executeUpdate();
				if (affectedRows != 1)
					throw new SQLException("Change of password failed for id " + id + ": Affected Rows: " + affectedRows);
				con.commit();
			}
			logger.exiting(CLASS_NAME, "changePw", returnCode);
			return returnCode;
		}
		catch (Exception e)
		{
			System.err.println("Rolling back due to: " + e.getClass().getName() + ": " + e.getMessage());
			con.rollback();
			if (e instanceof SQLException)
				throw (SQLException) e;
			else if (e instanceof IOException)
				throw (IOException) e;
			else if (e instanceof NoSuchAlgorithmException)
				throw (NoSuchAlgorithmException) e;
			else
			{
				e.printStackTrace();
				return ReturnObj.INTERNAL_SERVER_ERROR;
			}
		}
		finally
		{
			IOUtil.close(ps);
		}
	}

	// --------------------------------------------------------------------------------
	/**
	 * Changes the entry in the column <i>'login'</i> for the user specified by the id. Does an intern <code>con.commit()</code>.
	 * 
	 * @param con
	 * @param id
	 * @param newLogin
	 * @param pw
	 * @return An return code of the ones specified in {@link ReturnObj}.
	 * @throws SQLException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public short changeLogin(Connection con, long id, String newLogin, String pw) throws SQLException, IOException, NoSuchAlgorithmException
	{
		logger.entering(CLASS_NAME, "changeLogin", new Object[] { con, id, newLogin, pw });
		short returnCode = ReturnObj.SUCCESS;
		PreparedStatement ps = null;
		try
		{
			if (!authenticate(con, id, pw))
			{
				returnCode = ReturnObj.ACCESS_DENIED;
			}
			else
			{
				if (userExists(con, newLogin))
				{
					returnCode = ReturnObj.CONFLICT;
				}
				else
				{
					// Database query
					ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSChangeLogin.sql"));
					ps.setString(1, newLogin);
					ps.setLong(2, id);
					int affectedRows = ps.executeUpdate();
					if (affectedRows != 1)
						throw new SQLException("Change of login failed for id " + id + ": Affected Rows: " + affectedRows);
					con.commit();
				}
			}
			logger.exiting(CLASS_NAME, "changePW", returnCode);
			return returnCode;

		}
		catch (Exception e)
		{
			System.err.println("Rolling back due to: " + e.getClass().getName() + ": " + e.getMessage());
			con.rollback();
			if (e instanceof SQLException)
				throw (SQLException) e;
			else if (e instanceof IOException)
				throw (IOException) e;
			else if (e instanceof NoSuchAlgorithmException)
				throw (NoSuchAlgorithmException) e;
			else
			{
				e.printStackTrace();
				return ReturnObj.INTERNAL_SERVER_ERROR;
			}
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
	 * @return The id of the new user (<code>new {@link ReturnObj}(id)<Long></code>) or <code>new {@link ReturnObj}({@link ReturnObj#CONFLICT}, msg)<Long></code> if the user already
	 *         exists.
	 * @throws SQLException
	 *             If the database is unavailable or anything bad happens.
	 * @throws NoSuchAlgorithmException
	 *             If the Message Digest algorithm {@link Authenticator#MSG_DIGEST_ALGORITHM} or the Random Number Generator algorithm
	 *             {@link Authenticator#RNG_ALGORITHM} are not supported by the JVM.
	 * @throws IOException
	 *             If the SQL statement file could not be loaded successfully.
	 */
	public ReturnObj<Long> insertUser(Connection con, String login, String pw) throws SQLException, NoSuchAlgorithmException, IOException
	{
		logger.entering(CLASS_NAME, "insertUser", new Object[] { con, login, pw });
		PreparedStatement ps = null;
		try
		{
			ReturnObj<Long> returnValue;
			long id;
			id = idForLogin(con, login);
			if (id > 0)
			{
				String msg = "User already exists: " + login;
				logger.finer(msg);
				returnValue = new ReturnObj<Long>(ReturnObj.CONFLICT, msg);
			}
			// User with this login does not exist
			else
			{
				byte[] bSalt = generateSalt(RNG_ALGORITHM, SALT_LENGTH);
				// Digest computation
				byte[] bDigest = hash(MSG_DIGEST_ALGORITHM, ITERATIONS, pw, bSalt);
				String sDigest = byteToURLSafeBase64(bDigest);
				String sSalt = byteToURLSafeBase64(bSalt);
				logger.finest("Digest: " + sDigest + "(" + sDigest.length() + ")");
				logger.finest("Salt: " + sSalt + "(" + sSalt.length() + ")");

				// Database query
				ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSInsertUser.sql"));
				ps.setString(1, login);
				ps.setString(2, sDigest);
				ps.setString(3, sSalt);
				logger.finest("PreparedStatement: " + ps.toString());
				int affectedRows = ps.executeUpdate();
				logger.finest("affectedRows: " + affectedRows);
				if (affectedRows != 1)
					throw new SQLException("Insert failed for " + login + ". Affected rows: " + affectedRows);
				id = idForLogin(con, login);
				if (id < 1)
					throw new SQLException("Insert failed for " + login + ". Illegal ID returned: " + id);
				returnValue = new ReturnObj<Long>(id);
			}

			logger.exiting(CLASS_NAME, "insertUser", returnValue);
			return returnValue;
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
		logger.entering(CLASS_NAME, "authenticate", new Object[] { con, id, pw });
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			boolean authenticated = false;
			boolean userExist = true;
			// INPUT VALIDATION
			if (!MiscUtil.checkStringLength(pw, 0, MAX_LENGTH))
			{
				logger.finer("User does not exist. pw null, empty or too long(" + MAX_LENGTH + "): " + pw);
				// TIME RESISTANT ATTACK
				// Computation time is equal to the time needed by a legitimate user
				userExist = false;
				id = -1L;
				pw = "";
			}

			ps = con.prepareStatement(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "PSSelectPwSalt.sql"));
			ps.setLong(1, id);
			logger.finest("PreparedStatement: " + ps.toString());
			rs = ps.executeQuery();
			String sDigest, sSalt;
			if (rs.next())
			{
				sDigest = rs.getString("PW");
				sSalt = rs.getString("SALT");
				// DATABASE VALIDATION
				if (sDigest == null || sSalt == null)
				{
					throw new SQLException("Database inconsistent: Salt or Digested Password altered for id: " + id);
				}
				if (rs.next())
				{ // Should not happen, because login is the primary key
					throw new SQLException("Database inconsistent: Two user with the same id: " + id);
				}
			}
			else
			{
				// TIME RESISTANT ATTACK (Even if the user does not exist the
				// Computation time is equal to the time needed for a legitimate user)
				logger.finer("User did not exist: " + id);
				sDigest = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000==";
				sSalt = "00000000000=";
				userExist = false;
			}
			logger.finest("Digest: " + sDigest + "(" + sDigest.length() + ")");
			logger.finest("Salt: " + sSalt + "(" + sSalt.length() + ")");
			byte[] bDigest = base64ToByte(sDigest);
			byte[] bSalt = base64ToByte(sSalt);

			// Compute the new DIGEST
			byte[] proposedDigest = hash(MSG_DIGEST_ALGORITHM, ITERATIONS, pw, bSalt);
			String sProposedDigest = byteToURLSafeBase64(proposedDigest);
			logger.finest("Proposed Digest: " + sProposedDigest + "(" + sProposedDigest.length() + ")");

			authenticated = Arrays.equals(proposedDigest, bDigest) && userExist;
			logger.exiting(CLASS_NAME, "authenticate", authenticated);
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
	private byte[] hash(String algorithm, int iterations, String pw, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		logger.entering(CLASS_NAME, "getHash", new Object[] { iterations, pw, Arrays.toString(salt) });
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		digest.reset();
		digest.update(salt);
		byte[] input = digest.digest(pw.getBytes(CHARSET_NAME));
		for (int i = 0; i < iterations; i++)
		{
			digest.reset();
			input = digest.digest(input);
		}
		logger.exiting(CLASS_NAME, "getHash", Arrays.toString(input));
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
	private static byte[] generateSalt(String algorithm, int length) throws NoSuchAlgorithmException
	{
		// Uses a secure Random not a simple Random
		SecureRandom random = SecureRandom.getInstance(algorithm);
		// Salt generation 64 bits long
		byte[] bSalt = new byte[length];
		random.nextBytes(bSalt);
		return bSalt;
	}

	// --------------------------------------------------------------------------------
	public static String generatePw(String algorithm, int length) throws NoSuchAlgorithmException
	{
		return byteToURLSafeBase64(generateSalt(algorithm, length));
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
