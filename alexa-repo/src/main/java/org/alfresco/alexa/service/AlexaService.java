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

/**
 * Alexa service
 * 
 * @author ltworek
 *
 */
public class AlexaService {

	public static final String TOKEN_SEPARATOR = "#";

	private static Log logger = LogFactory.getLog(AlexaService.class);

	private PersonService personService;
	private NodeService nodeService;

	private ShaPasswordEncoderImpl encoder;
	private Map<String, Skill> skills = new HashMap<String, Skill>();

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

	/**
	 * Validate access token provided by AVS
	 * 
	 * @param encodedAccessToken	token from query
	 * @param clientId	skill id
	 * @return
	 */
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

	/**
	 * Send alexa notification using NotifyMe skill
	 * 
	 * @param text	notification text
	 */
	public void sendNotification(String text) {
		// TODO url from properties
		String urlString = "https://api.notifymyecho.com/v1/NotifyMe";
		URL url;
		try {

			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			JSONObject json = new JSONObject();
			json.put("notification", text);
			// TODO accesscode from properties
			json.put("accessCode", "...");

			String urlParameters = json.toString();

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
			e.printStackTrace();
		}

	}

	public void registerSkill(AlfrescoVoiceSkill alexaSkill) {
		this.skills.put(alexaSkill.getSkillId(), alexaSkill.buildSkill());
	}

	public void unregisterSkill(String skillId) {
		if (this.skills.containsKey(skillId)) {
			this.skills.remove(skillId);
		}
	}

	public Skill getSkillById(String skillId) {
		return this.skills.get(skillId);
	}

}
