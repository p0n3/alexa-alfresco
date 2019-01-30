# Alexa-Alfresco ingegration

This AMP module allows you to easily create a new Alexa's skill integrated with ACS. It uses the official Java SDK made by Amazon.

Features:
* account linking between ACS and Alexa
* session attributes
* intent Slots
* both java and javascript skills

You can also find couple examples of usage - from a basic skill that returns user name to more complex example which use session atributtes, slots and several stages.

## Getting Started

### Add new skill under Alexa developer console

1. Go to https://developer.amazon.com/alexa/console/ask
2. Create a new custom skill from scratch.
3. Select HTTPS as the endpoint type.
4. Set an endpoint url: https://<your domain>/alfresco/service/api/alexa/endpoint 
5. Under Account Linking set: 
	1. Authorization URI: https://<your domain>/share/page/alexa-authentication
	2. Client ID: <your skill id>
	3. Scope: documents, tasks
	4. Domain list: <your domain>
6. Create a new intent.

### Add new skill under Alfresco

#### Java skill

1. Crate new class which extends `AlfrescoVoiceSkill`.
2. Implement one of the intents interfaces: `AlfrescoVoiceSimpleIntent`, `AlfrescoVoiceSessionIntent`
3. Configure a bean for the class. For example:
```
<bean id="alexa.sample.helloUser" class="org.alfresco.alexa.sample.UserNameSkill" parent="alexa.skill">
	<property name="skillName">
		<value>Hello User</value>
	</property>
	<property name="skillId">
		<value>amzn1.ask.skill.6aac0bad-4288-4c3f-9575-4c4064eda0ce</value>
	</property>
	<property name="helpText">
		<value>Say hello</value>
	</property>
	<property name="welcomeText">
		<value>Welcome to the Alfresco</value>
	</property>
	
	<property name="serviceRegistry">
		<ref bean="ServiceRegistry" />
	</property>
</bean>
```

#### JavaScript skill

1. Create a folder.
2. Add `loadIntent.js` as a folder script rule.
3. Add a js file (<intent_id>.js) to the folder. For example:
```
"test 123";
```

## Authors

* **Łukasz Tworek** - *Initial work, SDK integration, account linking* - [p0n3](https://github.com/p0n3)
* **Uros Vukasinovic** - *Invoice service, testing* - [uvukasinovic](https://github.com/uvukasinovic)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details