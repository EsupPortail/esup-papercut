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
// Couleurs pour graphiques stats
var seed = 11;
function random() {
	var x = Math.sin(seed++) * 1000;
	return x - Math.floor(x);
}
var generateColors = [];
var generateBorderColors = [];
var generateStackColors = [];
for (var k = 0; k < 100; k++) {
	var color = Math.floor(random() * 256) + ',' + Math.floor(random() * 256) + ',' + Math.floor(random() * 256);
	generateColors.push('rgba(' + color + ', 0.6)');
	generateStackColors.push('rgba(' + color + ', 0.6)');
	generateBorderColors.push('rgba(' + color + ', 1)');
}

// Fonction helper pour créer les graphiques
function createChart(canvasId, chartData, options) {
	const ctx = document.getElementById(canvasId).getContext("2d");
	return new Chart(ctx, {
		type: 'line',
		data: chartData,
		options: options
	});
}

document.addEventListener('DOMContentLoaded', function () {
	// Stats
	if (typeof statsPapercut !== "undefined") {
		fetch(statsPapercut)
			.then(response => response.json())
			.then(data => {
				const coefMontant = 100;
				const labels = ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"];

				// Nombre de transactions
				{
					let inlineValeurs = [];
					let inlineDatasets = [];
					let a = 0;

					Object.entries(data.nombre).forEach(([index, value]) => {
						const keyMois = Object.keys(value);
						console.log(value);
						inlineValeurs = [];
						for (let i = 1; i <= 12; i++) {
							if (keyMois.includes(i.toString())) {
								inlineValeurs.push(value[i]);
							} else {
								inlineValeurs.push(null);
							}
						}
						inlineDatasets.push({
							label: index,
							backgroundColor: generateColors[a],
							borderColor: generateColors[a],
							borderWidth: 2,
							fill: false,
							tension: 0.1,
							data: inlineValeurs
						});
						a++;
					});

					const dataMois = {
						labels: labels,
						datasets: inlineDatasets
					};

					createChart("transactionsNombreChart", dataMois, {
						responsive: true,
						scales: {
							x: {
								ticks: {
									autoSkip: false
								}
							}
						},
						plugins: {
							legend: {
								position: 'bottom'
							},
							tooltip: {
								mode: 'index',
								intersect: false
							}
						},
						interaction: {
							mode: 'index',
							intersect: false
						}
					});
				}

				// Montants
				{
					let inlineValeurs = [];
					let inlineDatasets = [];
					let a = 0;

					Object.entries(data.montants).forEach(([index, value]) => {
						const keyMois = Object.keys(value);
						inlineValeurs = [];
						for (let i = 1; i <= 12; i++) {
							if (keyMois.includes(i.toString())) {
								inlineValeurs.push(value[i] / coefMontant);
							} else {
								inlineValeurs.push(null);
							}
						}
						inlineDatasets.push({
							label: index,
							backgroundColor: generateColors[a],
							borderColor: generateColors[a],
							borderWidth: 2,
							fill: false,
							tension: 0.1,
							data: inlineValeurs
						});
						a++;
					});

					const dataMois = {
						labels: labels,
						datasets: inlineDatasets
					};

					createChart("transactionsMontantChart", dataMois, {
						responsive: true,
						scales: {
							x: {
								ticks: {
									autoSkip: false
								}
							},
							y: {
								type: "linear"
							}
						},
						plugins: {
							legend: {
								position: 'bottom'
							},
							tooltip: {
								mode: 'index',
								intersect: false
							}
						},
						interaction: {
							mode: 'index',
							intersect: false
						}
					});
				}

				// Cumul Nb de transactions
				{
					let inlineValeurs = [];
					let inlineDatasets = [];
					let a = 0;

					Object.entries(data.cumulTransac).forEach(([index, value]) => {
						const keyMois = Object.keys(value);
						inlineValeurs = [];
						for (let i = 1; i <= 12; i++) {
							if (keyMois.includes(i.toString())) {
								inlineValeurs.push(value[i]);
							} else {
								inlineValeurs.push(null);
							}
						}
						inlineDatasets.push({
							label: index,
							backgroundColor: generateColors[a],
							borderColor: generateColors[a],
							borderWidth: 2,
							fill: false,
							tension: 0.1,
							data: inlineValeurs
						});
						a++;
					});

					const dataMois = {
						labels: labels,
						datasets: inlineDatasets
					};

					createChart("transactionsCumulTransactionsChart", dataMois, {
						responsive: true,
						scales: {
							x: {
								ticks: {
									autoSkip: false
								}
							}
						},
						plugins: {
							legend: {
								position: 'bottom'
							},
							tooltip: {
								mode: 'index',
								intersect: false
							}
						},
						interaction: {
							mode: 'index',
							intersect: false
						}
					});
				}

				// Cumul Montant des transactions
				{
					let inlineValeurs = [];
					let inlineDatasets = [];
					let a = 0;

					Object.entries(data.cumulMontant).forEach(([index, value]) => {
						const keyMois = Object.keys(value);
						inlineValeurs = [];
						for (let i = 1; i <= 12; i++) {
							if (keyMois.includes(i.toString())) {
								inlineValeurs.push(value[i] / coefMontant);
							} else {
								inlineValeurs.push(null);
							}
						}
						inlineDatasets.push({
							label: index,
							backgroundColor: generateColors[a],
							borderColor: generateColors[a],
							borderWidth: 2,
							fill: false,
							tension: 0.1,
							data: inlineValeurs
						});
						a++;
					});

					const dataMois = {
						labels: labels,
						datasets: inlineDatasets
					};

					createChart("transactionsCumulMontantChart", dataMois, {
						responsive: true,
						scales: {
							x: {
								ticks: {
									autoSkip: false
								}
							}
						},
						plugins: {
							legend: {
								position: 'bottom'
							},
							tooltip: {
								mode: 'index',
								intersect: false
							}
						},
						interaction: {
							mode: 'index',
							intersect: false
						}
					});
				}
			})
			.catch(error => console.error('Erreur lors du chargement des données:', error));
	}

	// Initialiser les tooltips Bootstrap 5
	const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
	const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
		return new bootstrap.Tooltip(tooltipTriggerEl)
	})
});

