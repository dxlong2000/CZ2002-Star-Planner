// package stars;

import java.io.*; 
import java.util.*;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Class to encrypt and decrypt our passwords for logging into the programme.
 * @author Group7
 *
 */
class Crypto {
    Cipher ecipher;
    Cipher dcipher;
    byte[] salt = {
        (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
        (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    int iterationCount = 19;

    /**
     * Encrypts a plain text password using a secret key, to protect user information in the database.
     * @param secretKey
     * @param plainText
     * @return the encrypted password
     */
    public String encrypt(String secretKey, String plainText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException {
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        ecipher = Cipher.getInstance(key.getAlgorithm());
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        String charSet = "UTF-8";
        byte[] in = plainText.getBytes(charSet);
        byte[] out = ecipher.doFinal(in);
        String encStr = new String(Base64.getEncoder().encode(out));
        return encStr;
    }

    /**
     * Decrypts an encrypted password, when given the appropriate secret key.
     * @param secretKey
     * @param encryptedText
     * @return decrypted password
     */
    public String decrypt(String secretKey, String encryptedText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException,
            IOException {
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        dcipher = Cipher.getInstance(key.getAlgorithm());
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        byte[] enc = Base64.getDecoder().decode(encryptedText);
        byte[] utf8 = dcipher.doFinal(enc);
        String charSet = "UTF-8";
        String plainStr = new String(utf8, charSet);
        return plainStr;
    }
}

/**
 * Class to manage logging in to the programme.
 * @author Group7
 *
 */
public class LoginOut {
	/**
	 * Login database file, currently set to "LoginDB.csv"
	 */
	private static String fileName = "LoginDB.csv";
	private static String key = "LongDo12345";
	private static Crypto cryptoUtil = new Crypto();

	/**
	 * Function to create a database of login information.
	 */
	public static void initialize_database() {
		try (PrintWriter writer = new PrintWriter(new File(fileName))) {
			StringBuilder sb = new StringBuilder();
			sb.append("Network User");
			sb.append(',');
			sb.append("Domain");
			sb.append(',');
			sb.append("Password");
			sb.append('\n');
			
			writer.write(sb.toString());
			writer.flush();
			writer.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * @param username
	 * @param domain
	 * @param password
	 * @param isLogin
	 */
	public static boolean canAddOrNot(String username, String domain, String password, boolean isLogin)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		password = cryptoUtil.encrypt(key, password);
		String userInformation = username + "," + domain + "," + password;
		String line = "";  
		try {  
			BufferedReader br = new BufferedReader(new FileReader(fileName));  
			while ((line = br.readLine()) != null) { 
				String[] data = line.split(",");   
				if(userInformation.compareTo(line)==0) {
					return false;
				}else if(data[0].compareTo(username)==0 && data[2].compareTo(password)!=0){
					if(isLogin){
						System.out.println("Wrong password, please try again!");
						return true;
					}
				}
			}
		}catch (IOException e){
			e.printStackTrace();  
		}
		if(isLogin){
			System.out.println("The account is not in the database, please try again!");
		}
		return true;
	}
	
	/**
	 * Rewrites the csv file with the database of login information.
	 * @param newdb
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static void rewriteCSV(ArrayList<ArrayList<String>> newdb)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		try (PrintWriter writer = new PrintWriter(new File(fileName))) {
			StringBuilder sb = new StringBuilder();
			
			for(ArrayList<String> row:newdb){
				addUser(row.get(0), row.get(1), row.get(2));
			}
			writer.flush();
			writer.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Allows user to change their password for login.
	 * @param username
	 * @param domain
	 * @param newPassowd
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static void changePassword(String username, String domain, String newPassowd) throws IOException,
			InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		username = username.toUpperCase();
		domain = domain.toUpperCase();
		newPassowd = cryptoUtil.encrypt(key, newPassowd);

		ArrayList<ArrayList<String>> olddb = new ArrayList<ArrayList<String>>();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		ArrayList<String> lines = new ArrayList<>();
		String line = null;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		for(String s:lines){
			olddb.add(new ArrayList<String>(Arrays.asList(s.split(","))));
		}

		for(ArrayList<String> arr:olddb){
			if(arr.get(0).compareTo(username)==0 && arr.get(1).compareTo(domain)==0){
				arr.set(2, newPassowd);
			}
		}
		rewriteCSV(olddb);
	}
	
	/**
	 * Adds user login information to the database of login information.
	 * @param username
	 * @param domain
	 * @param password
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static void addUser(String username, String domain, String password)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		username = username.toUpperCase();
		domain = domain.toUpperCase();
		if(canAddOrNot(username, domain, password, false)) {
			try   {  
				FileWriter writer = new FileWriter(fileName, true);  
				StringBuilder sb = new StringBuilder();
				sb.append(username);
				sb.append(",");
				sb.append(domain);
				sb.append(",");
				sb.append(password);
				sb.append('\n');
							
				writer.write(sb.toString());
				writer.close();
			}catch (IOException e)   {  
				e.printStackTrace();  
			}
		}else {
			System.out.println("User existed, cannot add!");
		}
	}
	
	/**
	 * Used to login the user into the programme.
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String[] login() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		Scanner sc = new Scanner(System.in);
		String temp = "";
		System.out.print("User name: ");
		String username = sc.next().toUpperCase();
		
		System.out.print("Choose the domain: \n (1) Student\n (2) Staff\n");
		temp = sc.next();
		while(temp.compareTo("1") != 0 && temp.compareTo("2") != 0) {
			System.out.print("Choose the domain: \n (1) Student\n (2) Staff\n");
			temp = sc.next();
		}
		String domain = "";
		if(temp.compareTo("1") == 0) {
			domain = "STUDENT";
		}else if (temp.compareTo("2") == 0) {
			domain = "STAFF";
		}
		Console console = System.console();		
		System.out.print("Password: ");
		char[] pw = console.readPassword();
		String password = new String(pw);
		
		// System.out.print("Password: ");
		// String password = new String(sc.next());

		//System.out.println(username);
		//System.out.println(domain);
		//System.out.println(password);

		if(!canAddOrNot(username, domain, password, true)){
			String[] log = {username, domain};
			return log;
		}
		else {
			String[] log = {"", ""};
			return log;
		}
	}

	// public static void main(String [] args) throws InvalidKeyException, NoSuchAlgorithmException,
	// 		InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
	// 		IllegalBlockSizeException, BadPaddingException, IOException {

	// 	initialize_database();
	// 	addUser("Long", "Staff", cryptoUtil.encrypt(key, "1"));
	// 	addUser("GEREMIE", "Staff", cryptoUtil.encrypt(key, "1"));
	// 	addUser("Kimberley", "Staff", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N1940", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N1000", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N2000", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N3000", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N4000", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N5000", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N6000", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N7000", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N8000", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N9000", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N9900", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N8800", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N7700", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N6600", "Student", cryptoUtil.encrypt(key, "1"));
	// 	addUser("N5500", "Student", cryptoUtil.encrypt(key, "1"));

	// 	changePassword("Long", "Staff", "2");
	// 	System.out.println(login().toString());
	// }
}