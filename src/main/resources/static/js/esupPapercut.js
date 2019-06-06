/*
 * Licensed to EsupPortail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * EsupPortail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//Couleurs pour graphiques stats
var seed = 11;
function random() {
    var x = Math.sin(seed++) * 1000;
    return x - Math.floor(x);
}
var generateColors = [];
var generateBorderColors = [];
var generateStackColors = [];
for(var k = 0; k < 100; k++) {
	var color = Math.floor(random()*256) + ',' + Math.floor(random()*256) + ',' + Math.floor(random()*256);
	generateColors.push('rgba(' + color + ', 0.6)');
	generateStackColors.push('rgba(' + color + ', 0.6)');
	generateBorderColors.push('rgba(' + color + ', 1)'); 
}

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
	        	var a=0;
	    		$.each(data.nombre, function(index, value) {
	    			var keyMois = Object.keys(value);
	    			console.log(value);
	    			for(i=1;i<=12;i++){
	        			inlineLabels.push(i);
	        			if($.inArray(i.toString(), keyMois)>-1){
			        		inlineValeurs.push(value[i]);
	        			}else{
			        		inlineValeurs.push(null);
	        			}			
	    			}
	                inlineDatasets.push({
	 		            label: index,
	 			         backgroundColor: generateColors[a],
	 			         borderColor: generateColors[a],
	 			         pointColor: generateBorderColors[a],
	 			         pointBorderColor: "#fff",
	 			         pointHoverBorderColor: "#fff",
	 			         pointBackgroundColor: generateColors[a],
	 		            data: inlineValeurs
	                });a++;inlineValeurs = [];
	            });	     
	         	var dataMois = {
	     		    labels: ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"],
	     		    datasets: inlineDatasets
	     		};       	
	         	var ctx3 = $("#transactionsNombreChart").get(0).getContext("2d");
	         	var myLineChart = new Chart(ctx3, {
	        		type: 'line',
	        		data: dataMois,
	        		options: {
	        			responsive: true,
	        			 scales:{
	        	                xAxes:[{
	        	        			ticks: {
	        	        				autoSkip: false
	        	        			}
	        	                }]
	        	            },
			                legend: {
			                    position: 'bottom'
			                },
			                tooltips: {
			                	mode: 'label'
			                },
			                hover: {
			                    mode: 'dataset'
			                }
	        		}
	        	});  	
	         	//Montants       	
	         	var inlineTitles = [];
	         	var inlineLabels = [];
	        	var inlineValeurs = [];
	        	var inlineDatasets = [];
	        	var color = ["151,187,205,0.2", "220,220,220,0.2", "35, 60, 72, 0.2", "138, 235, 254, 0.2"]
	        	var fullColor = ["151,187,205,1", "220,220,220,1", "35, 60, 72, 1", "138, 235, 254, 1"]
	        	var a=0;
	    		$.each(data.montants, function(index, value) {
	    			var keyMois = Object.keys(value);
	    			for(i=1;i<=12;i++){
	        			inlineLabels.push(i);
	        			if($.inArray(i.toString(), keyMois)>-1){
			        		inlineValeurs.push(value[i]/coefMontant);
	        			}else{
			        		inlineValeurs.push(null);
	        			}			
	    			}
	                inlineDatasets.push({
	 		            label: index,
	 			         backgroundColor: generateColors[a],
	 			         borderColor: generateColors[a],
	 			         pointColor: generateBorderColors[a],
	 			         pointBorderColor: "#fff",
	 			         pointHoverBorderColor: "#fff",
	 			         pointBackgroundColor: generateColors[a],
	 		            data: inlineValeurs
	                });a++;inlineValeurs = [];
	            });	     
	         	var dataMois = {
	     		    labels: ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"],
	     		    datasets: inlineDatasets
	     		};       	
	         	var ctx3 = $("#transactionsMontantChart").get(0).getContext("2d");
	         	var myLineChart = new Chart(ctx3, {
	        		type: 'line',
	        		data: dataMois,
	        		options: {
	        			 scales:{
	        				 type:"category",
	        	                xAxes:[{
	        	        			ticks: {
	        	        				autoSkip: false
	        	        			}
	        	                }],
	        	                yAxes:[{
	        	                	type: "linear"
	        	                }]
	        	            },
			                legend: {
			                    position: 'bottom'
			                },
			                tooltips: {
			                	mode: 'label'
			                },
			                hover: {
			                    mode: 'dataset'
			                }
	        		}
	        	});  
	         	//Cumul Nb de transactions       	
	         	var inlineTitles = [];
	         	var inlineLabels = [];
	        	var inlineValeurs = [];
	        	var inlineDatasets = [];
	        	var color = ["151,187,205,0.2", "220,220,220,0.2", "35, 60, 72, 0.2", "138, 235, 254, 0.2"]
	        	var fullColor = ["151,187,205,1", "220,220,220,1", "35, 60, 72, 1", "138, 235, 254, 1"]
	        	var a=0;
	    		$.each(data.cumulTransac, function(index, value) {
	    			var keyMois = Object.keys(value);
	    			for(i=1;i<=12;i++){
	        			inlineLabels.push(i);
	        			if($.inArray(i.toString(), keyMois)>-1){
			        		inlineValeurs.push(value[i]);
	        			}else{
			        		inlineValeurs.push(null);
	        			}			
	    			}
	                inlineDatasets.push({
	 		            label: index,
	 			         backgroundColor: generateColors[a],
	 			         borderColor: generateColors[a],
	 			         pointColor: generateBorderColors[a],
	 			         pointBorderColor: "#fff",
	 			         pointHoverBorderColor: "#fff",
	 			         pointBackgroundColor: generateColors[a],
	 		            data: inlineValeurs
	                });a++;inlineValeurs = [];
	            });	     
	         	var dataMois = {
	     		    labels: ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"],
	     		    datasets: inlineDatasets
	     		};       	
	         	var ctx3 = $("#transactionsCumulTransactionsChart").get(0).getContext("2d");
	         	var myLineChart = new Chart(ctx3, {
	        		type: 'line',
	        		data: dataMois,
	        		options: {
	        			 responsive: true,
	        			 scales:{
	        				 type:"category",
	        	                xAxes:[{
	        	        			ticks: {
	        	        				autoSkip: false
	        	        			}
	        	                }]
	        	            },
			                legend: {
			                    position: 'bottom'
			                },
			                tooltips: {
			                	mode: 'label'
			                },
			                hover: {
			                    mode: 'dataset'
			                }	                
	        	            
	        		}
	        	});   	
	         	//Cumul Montant des transactions
	         	var inlineTitles = [];
	         	var inlineLabels = [];
	        	var inlineValeurs = [];
	        	var inlineDatasets = [];
	        	var color = ["151,187,205,0.2", "220,220,220,0.2", "35, 60, 72, 0.2", "138, 235, 254, 0.2"]
	        	var fullColor = ["151,187,205,1", "220,220,220,1", "35, 60, 72, 1", "138, 235, 254, 1"]
	        	var a=0;
	    		$.each(data.cumulMontant, function(index, value) {
	    			var keyMois = Object.keys(value);
	    			for(i=1;i<=12;i++){
	        			inlineLabels.push(i);
	        			if($.inArray(i.toString(), keyMois)>-1){
			        		inlineValeurs.push(value[i]/coefMontant);
	        			}else{
			        		inlineValeurs.push(null);
	        			}			
	    			}
	                inlineDatasets.push({
	 		            label: index,
	 			         backgroundColor: generateColors[a],
	 			         borderColor: generateColors[a],
	 			         pointColor: generateBorderColors[a],
	 			         pointBorderColor: "#fff",
	 			         pointHoverBorderColor: "#fff",
	 			         pointBackgroundColor: generateColors[a],
	 		            data: inlineValeurs
	                });a++;inlineValeurs = [];
	            });	     
	         	var dataMois = {
	     		    labels: ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"],
	     		    datasets: inlineDatasets
	     		};       	
	         	var ctx3 = $("#transactionsCumulMontantChart").get(0).getContext("2d");
	         	var myLineChart = new Chart(ctx3, {
	        		type: 'line',
	        		data: dataMois,
	        		options: {
	        			 responsive: true,
	        			 scales:{
	        	                xAxes:[{
	        	        			ticks: {
	        	        				autoSkip: false
	        	        			}
	        	                }]
	        	            },
			                legend: {
			                    position: 'bottom'
			                },
			                tooltips: {
			                	mode: 'label'
			                },
			                hover: {
			                    mode: 'dataset'
			                }
	        		}
	        	});   	 
	        }
	    });
	}
});