
$(document).ready(function() {
//Stats 
	if(typeof statsPapercut != "undefined"){
	    $.ajax({
	        url: statsPapercut,
	        type: 'GET',
	        dataType : 'json',
	        success : function(data) {
	         	//Date	         	
	         	var inlineTitles = [];
	         	var inlineLabels = [];
	        	var inlineValeurs = [];
	        	var inlineDatasets = [];
	        	var coefMontant = 100;
	        	var color = ["151,187,205,0.2", "220,220,220,0.2", "35, 60, 72, 0.2", "138, 235, 254, 0.2"]
	        	var fullColor = ["151,187,205,1", "220,220,220,1", "35, 60, 72, 1", "138, 235, 254, 1"]
	        	var legends = [];
	        	var a=0;
	        	
	    		$.each(data.nombre, function(index, value) {
	    			//legendes
	    			legends[index]="rgba("+fullColor[a]+")";
	    			var keyMois = Object.keys(value);
	    			for(i=1;i<=12;i++){
	        			inlineLabels.push(i);
	        			if($.inArray(i.toFixed(1), keyMois)>-1){
			        		inlineValeurs.push(value[i.toFixed(1)]);
	        			}else{
			        		inlineValeurs.push(null);
	        			}			
	    			}
	                inlineDatasets.push({
	 		            label: index,
	 		            fillColor: "rgba("+color[a]+")",
	 		            strokeColor: "rgba("+fullColor[a]+")",
	 		            pointColor: "rgba("+fullColor[a]+")",
	 		            pointStrokeColor: "#fff",
	 		            pointHighlightFill: "#fff",
	 		            pointHighlightStroke: "rgba("+fullColor[a]+")",
	 		            data: inlineValeurs
	                });a++;inlineValeurs = [];
	            });	     
	         	var dataMois = {
	     		    labels: ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"],
	     		    datasets: inlineDatasets
	     		};       	
	         	var ctx3 = $("#transactionsNombreChart").get(0).getContext("2d");
	         	var myLineChart = new Chart(ctx3).Line(dataMois,{
	         		//responsive : true
	         	});  
	         	//Légende
	         	var years = "<br />";
	         	for (var key in legends){
	         		var splitKey = key.split("."); 
	         		var year = "<span style='padding:3px;margin-left:2px;color:white;font-weight:bold;background-color:" + legends[key] +";'>"+ splitKey[0] + "</span>";
	         		years += year;
	         	}
	         	$("#transactionsNombreChart").after(years);
	         	//Montants       	
	         	var inlineTitles = [];
	         	var inlineLabels = [];
	        	var inlineValeurs = [];
	        	var inlineDatasets = [];
	        	var color = ["151,187,205,0.2", "220,220,220,0.2", "35, 60, 72, 0.2", "138, 235, 254, 0.2"]
	        	var fullColor = ["151,187,205,1", "220,220,220,1", "35, 60, 72, 1", "138, 235, 254, 1"]
	        	var legends = [];
	        	var a=0;
	    		$.each(data.montants, function(index, value) {
	    			//legendes
	    			legends[index]="rgba("+fullColor[a]+")";
	    			var keyMois = Object.keys(value);
	    			for(i=1;i<=12;i++){
	        			inlineLabels.push(i);
	        			if($.inArray(i.toFixed(1), keyMois)>-1){
			        		inlineValeurs.push(value[i.toFixed(1)]/coefMontant);
	        			}else{
			        		inlineValeurs.push(null);
	        			}			
	    			}
	                inlineDatasets.push({
	 		            label: index,
	 		            fillColor: "rgba("+color[a]+")",
	 		            strokeColor: "rgba("+fullColor[a]+")",
	 		            pointColor: "rgba("+fullColor[a]+")",
	 		            pointStrokeColor: "#fff",
	 		            pointHighlightFill: "#fff",
	 		            pointHighlightStroke: "rgba("+fullColor[a]+")",
	 		            data: inlineValeurs
	                });a++;inlineValeurs = [];
	            });	     
	         	var dataMois = {
	     		    labels: ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"],
	     		    datasets: inlineDatasets
	     		};       	
	         	var ctx3 = $("#transactionsMontantChart").get(0).getContext("2d");
	         	var myLineChart = new Chart(ctx3).Line(dataMois,{
	         		//responsive : true
	         	});  
	         	//Légende
	         	var years = "<br />";
	         	for (var key in legends){
	         		var splitKey = key.split("."); 
	         		var year = "<span style='padding:3px;margin-left:2px;color:white;font-weight:bold;background-color:" + legends[key] +";'>"+ splitKey[0] + "</span>";
	         		years += year;
	         	}
	         	$("#transactionsMontantChart").after(years);  	
	         	//Cumul Nb de transactions       	
	         	var inlineTitles = [];
	         	var inlineLabels = [];
	        	var inlineValeurs = [];
	        	var inlineDatasets = [];
	        	var color = ["151,187,205,0.2", "220,220,220,0.2", "35, 60, 72, 0.2", "138, 235, 254, 0.2"]
	        	var fullColor = ["151,187,205,1", "220,220,220,1", "35, 60, 72, 1", "138, 235, 254, 1"]
	        	var legends = [];
	        	var a=0;
	    		$.each(data.cumulTransac, function(index, value) {
	    			//legendes
	    			legends[index]="rgba("+fullColor[a]+")";
	    			var keyMois = Object.keys(value);
	    			for(i=1;i<=12;i++){
	        			inlineLabels.push(i);
	        			if($.inArray(i.toFixed(1), keyMois)>-1){
			        		inlineValeurs.push(value[i.toFixed(1)]);
	        			}else{
			        		inlineValeurs.push(null);
	        			}			
	    			}
	                inlineDatasets.push({
	 		            label: index,
	 		            fillColor: "rgba("+color[a]+")",
	 		            strokeColor: "rgba("+fullColor[a]+")",
	 		            pointColor: "rgba("+fullColor[a]+")",
	 		            pointStrokeColor: "#fff",
	 		            pointHighlightFill: "#fff",
	 		            pointHighlightStroke: "rgba("+fullColor[a]+")",
	 		            data: inlineValeurs
	                });a++;inlineValeurs = [];
	            });	     
	         	var dataMois = {
	     		    labels: ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"],
	     		    datasets: inlineDatasets
	     		};       	
	         	var ctx3 = $("#transactionsCumulTransactionsChart").get(0).getContext("2d");
	         	var myLineChart = new Chart(ctx3).Line(dataMois,{
	         		//responsive : true
	         	});  
	         	//Légende
	         	var years = "<br />";
	         	for (var key in legends){
	         		var splitKey = key.split("."); 
	         		var year = "<span style='padding:3px;margin-left:2px;color:white;font-weight:bold;background-color:" + legends[key] +";'>"+ splitKey[0] + "</span>";
	         		years += year;
	         	}
	         	$("#transactionsCumulTransactionsChart").after(years);  	
	         	//Cumul Montant des transactions
	         	var inlineTitles = [];
	         	var inlineLabels = [];
	        	var inlineValeurs = [];
	        	var inlineDatasets = [];
	        	var color = ["151,187,205,0.2", "220,220,220,0.2", "35, 60, 72, 0.2", "138, 235, 254, 0.2"]
	        	var fullColor = ["151,187,205,1", "220,220,220,1", "35, 60, 72, 1", "138, 235, 254, 1"]
	        	var legends = [];
	        	var a=0;
	    		$.each(data.cumulMontant, function(index, value) {
	    			//legendes
	    			legends[index]="rgba("+fullColor[a]+")";
	    			var keyMois = Object.keys(value);
	    			for(i=1;i<=12;i++){
	        			inlineLabels.push(i);
	        			if($.inArray(i.toFixed(1), keyMois)>-1){
			        		inlineValeurs.push(value[i.toFixed(1)]/coefMontant);
	        			}else{
			        		inlineValeurs.push(null);
	        			}			
	    			}
	                inlineDatasets.push({
	 		            label: index,
	 		            fillColor: "rgba("+color[a]+")",
	 		            strokeColor: "rgba("+fullColor[a]+")",
	 		            pointColor: "rgba("+fullColor[a]+")",
	 		            pointStrokeColor: "#fff",
	 		            pointHighlightFill: "#fff",
	 		            pointHighlightStroke: "rgba("+fullColor[a]+")",
	 		            data: inlineValeurs
	                });a++;inlineValeurs = [];
	            });	     
	         	var dataMois = {
	     		    labels: ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"],
	     		    datasets: inlineDatasets
	     		};       	
	         	var ctx3 = $("#transactionsCumulMontantChart").get(0).getContext("2d");
	         	var myLineChart = new Chart(ctx3).Line(dataMois,{
	         		//responsive : true
	         	});  
	         	//Légende
	         	var years = "<br />";
	         	for (var key in legends){
	         		var splitKey = key.split("."); 
	         		var year = "<span style='padding:3px;margin-left:2px;color:white;font-weight:bold;background-color:" + legends[key] +";'>"+ splitKey[0] + "</span>";
	         		years += year;
	         	}
	         	$("#transactionsCumulMontantChart").after(years);  
	        }
	    });
	}
});