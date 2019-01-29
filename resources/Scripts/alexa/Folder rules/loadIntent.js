var fileName = document.properties["cm:name"];

alexa.unregisterScriptIntent(fileName.substring(0,fileName.lastIndexOf(".")));
alexa.registerScriptIntent(document);