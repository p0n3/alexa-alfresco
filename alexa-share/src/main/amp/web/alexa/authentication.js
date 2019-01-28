/**
 * Alexa authentication component.
 * 
 * @namespace Alexa
 * @class Alexa.Authentication
 */
Alexa = {};
(function()
{

   /**
    * YUI Library aliases
    */
   var Dom = YAHOO.util.Dom,
       Event = YAHOO.util.Event,
       Bubbling = YAHOO.Bubbling;

   /**
    * Alfresco Slingshot aliases
    */
   var $html = Alfresco.util.encodeHTML,
		$siteURL = Alfresco.util.siteURL;
	
   Alexa.Authentication = function(htmlId)
   {
	  Alexa.Authentication.superclass.constructor.call(this, "Alexa.Authentication", htmlId, ["container"]);
      
      //Bubbling.on("beforeFormRuntimeInit", this.onBeforeFormRuntimeInit, this);
      Bubbling.on("afterFormRuntimeInit", this.onAfterFormRuntimeInit, this);
	  
      return this;
   };
   
   YAHOO.extend(Alexa.Authentication, Alfresco.component.Base,
   {
      /**
       * Object container for initialization options
       *
       * @property options
       * @type object
       */
      options:
      {
      
      },
      
      /**
       * Currently visible Search Form object
       */
      currentForm: null,
      
      /**
       * Fired by YUI when parent element is available for scripting.
       * Component initialisation, including instantiation of YUI widgets and event listener binding.
       *
       * @method onReady
       */
      onReady: function()
      {
//         var me = this;
//		 var temp = sessionStorage.getItem('siteID');
//		 if(temp != null){
//			 var objJson = JSON.parse(temp);
//			 if (objJson != Alfresco.constants.SITE) {
//				 sessionStorage.clear();
//			 }
//		 }
    	
    	var queryParams = Alfresco.util.getQueryStringParameters();
    	
    	if(queryParams["response_type"] !== "token") {
    		window.location.href = Alfresco.constants.URL_CONTEXT;
    		return;
    	}
    	
    	// okienko
    	Alfresco.util.PopupManager.displayPrompt(
		{
				 title: "Permission request",
				 text: "Alexa is requesting permission to your tasks and documents.",
				 noEscape: true,
				 buttons: [
				 {
					text: "Deny",
					handler: function() {
					
						window.location.href = Alfresco.constants.URL_CONTEXT;
						return;
						
					} 
				 },
				 {
					text: "Accept",
					handler: function() { 
						Alfresco.util.PopupManager.displayPrompt({title: "Permission request", text: "Connecting...", buttons: []});
						

						Alfresco.util.Ajax.jsonPost({
								url: Alfresco.constants.PROXY_URI_RELATIVE+"api/alexa/authorization",
								dataObj: queryParams,
								successCallback: {
									fn: function(res) {
										if(res.json.access_token != null)
										{
											window.location.href = queryParams["redirect_uri"]+"#state="+queryParams["state"]+"&access_token="+res.json.access_token+"&token_type=Bearer";
										}
									},
									scope: this
								}
							});
						
						// todo fail
					},
					isDefault: true
				 }]
				});

    	
			
		
      },
      
      onAfterFormRuntimeInit: function DocSearch_onAfterFormRuntimeInit(layer, args)
      {
    	  
      }
      
      
	  
   });
})();