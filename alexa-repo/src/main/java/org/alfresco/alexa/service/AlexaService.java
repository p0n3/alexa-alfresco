package org.alfresco.alexa.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.alfresco.alexa.AlfrescoVoiceSkill;
import org.alfresco.alexa.model.AlexaModel;
import org.alfresco.repo.security.authentication.ShaPasswordEncoderImpl;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.amazon.ask.Skill;

public class AlexaService {

	public static final String TOKEN_SEPARATOR = "#";
	private static Log logger = LogFactory.getLog(AlexaService.class);

	private PersonService personService;
	private NodeService nodeService;

	private ShaPasswordEncoderImpl encoder;
	private Map<String, Skill> skills = new HashMap<String, Skill>();

	// public static void main(String[] args) {
	//
	// String token = "5e186c2a-f430-4503-9426-09ddabef33f8";
	// //generateRandomToken();
	// String clientId = "id_klienta";
	// String userName = "admin";
	//
	// String hashedToken = hashToken(token, clientId);
	// String accessToken = generateAccessToken(token, userName);
	//
	// System.out.println(hashedToken);
	// System.out.println(accessToken);
	// System.out.println(validateAccessToken(accessToken, clientId));
	// }

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void init() {

	}

	public String generateRandomToken() {
		return UUID.randomUUID().toString();
	}

	public String hashToken(String token, String clientId) {
		if (this.encoder == null) {
			this.encoder = new ShaPasswordEncoderImpl(256);
		}
		return this.encoder.encodePassword(token, clientId);
	}

	public String generateAccessToken(String token, String userId) {
		String enc = new String(token + TOKEN_SEPARATOR + userId);
		return Base64.encodeBase64String(enc.getBytes());
	}

	public String validateAccessToken(String encodedAccessToken, String clientId) {
		String accessToken = new String(Base64.decodeBase64(encodedAccessToken));
		int separatorPosition = accessToken.indexOf(TOKEN_SEPARATOR);
		String token = accessToken.substring(0, separatorPosition);
		String userName = accessToken.substring(separatorPosition + 1);

		String hashedToken = hashToken(token, clientId);

		NodeRef person = this.personService.getPerson(userName);
		List<String> userHashes = (List<String>) this.nodeService.getProperty(person, AlexaModel.PROP_AUTH_TOKENS);
		for (String userHash : userHashes) {

			if (hashedToken.equals(userHash)) {
				return userName;
			}

		}

		return null;
	}

	public void sendNotification(String text) {
		// FIXME url
		String urlString = "https://api.notifymyecho.com/v1/NotifyMe";
		URL url;
		try {

			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			JSONObject json = new JSONObject();
			json.put("notification", text);
			//FIXME accesscode 
			json.put("accessCode", "amzn1.ask.account.AEF4AXCW6GCFGSEGGQCHLWFWZX3UUBMB2XOI5HAF2KBIKY4J2IKO4LEVWF2Q5MKMESRSHNXKT266TUDEQMIF2OSHDX2VIAP2BXHDDCMRXOAEJAVTWG7SLNYSWKRYYUWSLZKIE66DC3F7Q6LUZAFCG7VXUQVAHY64HN2BNKZ2UBAQMKEFH2HTADGJWAB7BD2U52GG7TLXFEY5SVY");

			String urlParameters = json.toString(); // postData.toString();

			byte[] postDataBytes = urlParameters.getBytes(StandardCharsets.UTF_8);

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestProperty("Content-Type", "application/json");

			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postDataBytes);
			}

			int status = conn.getResponseCode();

			String result = null;
			InputStream is = null;
			BufferedReader br = null;

			try {
				is = conn.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));

				String line = null;
				StringBuffer sb = new StringBuffer();
				while (((line = br.readLine()) != null)) {
					sb.append(line);
				}

				result = sb.toString();

				logger.debug("Notification response:" + status + " Result: " + result);

			} finally {
				if (br != null) {
					br.close();
				}

				if (is != null) {
					is.close();
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void registerSkill(AlfrescoVoiceSkill alexaSkill) {
		this.skills.put(alexaSkill.getSkillId(), alexaSkill.buildSkill());
	}

	public Skill getSkillById(String skillId) {
		return this.skills.get(skillId);
	}

}
