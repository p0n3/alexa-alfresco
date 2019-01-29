var docName = document.properties["cm:name"].split(".")[0];
var docs = docName.split(" - ");
var amountArr = docs[1].split(" ");

var type = docs[0];
var amount = amountArr[0];
var currency = amountArr[1];

var currencyMap = {
	"EUR" : "euro",
	"GBP" : "pounds",
	"USD" : "dollars"
};

alexa.sendNotification("You have a new unpaid "+type.toLowerCase()+" bill for "+amount+" "+currencyMap[currency]);
