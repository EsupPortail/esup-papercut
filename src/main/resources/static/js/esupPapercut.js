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

	// Ajouter la transparence aux couleurs de fond des datasets
	chartData.datasets.forEach(dataset => {
		if (dataset.fill) {
			// Convertir rgba et ajuster l'opacité à 0.2
			const color = dataset.backgroundColor;
			if (color.startsWith('rgba')) {
				// Format: rgba(r, g, b, a) - remplacer le dernier chiffre par 0.2
				dataset.backgroundColor = color.replace(/[\d.]+\)$/, '0.2)');
			}
		}
	});

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
							fill: true,
							tension: 0.4,
							pointRadius: 4,
							pointHoverRadius: 6,
							pointBackgroundColor: generateColors[a],
							pointBorderColor: "#fff",
							pointBorderWidth: 2,
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
							fill: true,
							tension: 0.4,
							pointRadius: 4,
							pointHoverRadius: 6,
							pointBackgroundColor: generateColors[a],
							pointBorderColor: "#fff",
							pointBorderWidth: 2,
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
							fill: true,
							tension: 0.4,
							pointRadius: 4,
							pointHoverRadius: 6,
							pointBackgroundColor: generateColors[a],
							pointBorderColor: "#fff",
							pointBorderWidth: 2,
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
							fill: true,
							tension: 0.4,
							pointRadius: 4,
							pointHoverRadius: 6,
							pointBackgroundColor: generateColors[a],
							pointBorderColor: "#fff",
							pointBorderWidth: 2,
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

	document.querySelectorAll('[data-clipboard-text], [data-clipboard-target]').forEach(function (button) {
		button.addEventListener('click', function () {
			var text = button.getAttribute('data-clipboard-text');
			if (!text) {
				var target = button.getAttribute('data-clipboard-target');
				var targetEl = target ? document.querySelector(target) : null;
				text = targetEl ? (targetEl.value || targetEl.textContent) : '';
			}
			if (!text) {
				return;
			}
			if (text.charAt(0) === '/') {
				text = window.location.origin + text;
			}
			var showCopiedState = function () {
				if (!button.dataset.originalText) {
					button.dataset.originalText = button.textContent;
				}
				button.textContent = 'Copié !';
				button.classList.add('btn-success');
				button.classList.remove('btn-outline-secondary');
				setTimeout(function () {
					button.textContent = button.dataset.originalText;
					button.classList.remove('btn-success');
					button.classList.add('btn-outline-secondary');
				}, 1200);
			};
			if (navigator.clipboard && window.isSecureContext) {
				navigator.clipboard.writeText(text).then(showCopiedState).catch(function () {});
				return;
			}
			var textarea = document.createElement('textarea');
			textarea.value = text;
			textarea.setAttribute('readonly', '');
			textarea.style.position = 'absolute';
			textarea.style.left = '-9999px';
			document.body.appendChild(textarea);
			textarea.select();
			try {
				document.execCommand('copy');
				showCopiedState();
			} catch (err) {
				// No fallback available.
			}
			document.body.removeChild(textarea);
		});
	});

	// Affiche l'URL CSV complète dans la modale en préfixant l'origine du navigateur.
	var csvOnlineModal = document.getElementById('csvOnlineModal');
	if (csvOnlineModal) {
		csvOnlineModal.addEventListener('shown.bs.modal', function () {
			var urlEl = document.getElementById('csv-online-url');
			if (!urlEl) {
				return;
			}
			var baseUrl = urlEl.getAttribute('data-base-url') || urlEl.getAttribute('href') || '';
			if (baseUrl.charAt(0) === '/') {
				baseUrl = window.location.origin + baseUrl;
			}
			urlEl.textContent = baseUrl;
			urlEl.setAttribute('href', baseUrl);
		});
	}

	// Initialiser les tooltips Bootstrap 5
	const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
	const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
		return new bootstrap.Tooltip(tooltipTriggerEl)
	})
});

