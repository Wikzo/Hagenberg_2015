function getDefibByBezirk(url) {

	var bezirkCount = 23;

    $.ajax({
        url: url,
        dataType: "JSON"
        }).done(function(data) {
            
            //Das JSON-Dataset besitzt eine andere Datenreihenfolge, als es Googlecharts benötigt.
            //Daher erzeuge ich ein Zwischenarray, das die gewünschte Datenform für Googlecharts besitzt
            //und befülle es mit den einzelnen Daten aus dem JSON-Dataset der Stadt Wien.
            var statesArray = [['Long', 'Lat', 'Name']];
            var zaehler = 0;       
            $.each(data.features, function() {

                var stateitem = [parseInt(this.properties['BEZIRK']),this.properties['INFO']];
                //console.log(this.properties['BEZIRK']);
                //console.log(stateitem);
                statesArray.push(stateitem);
                zaehler++;
                });
                
                statesArray.sort();
                //Array für die 23 Wiener Bezirke wird vordefiniert - Zähler des jeweiligen Bezirk wird beim durchlaufen erhöht
                var statbezirk = [];
				for (var i = 0; i < bezirkCount+1; i++) statbezirk[i] = 0;
                
				for (var i = 0;i<statesArray.length;i++) {
                    var bezirk = statesArray[i];
                    //console.log(bezirk[0]);
                    statbezirk[bezirk[0]]++;                    
                    
                }
                
                
                //console.log(statbezirk);
                
                var statready = [['Bezirk', 'Defianzahl']];
                for (var j = 1;j<=bezirkCount;j++)
                {
                    var zw = [j + ". Bezirk",statbezirk[j]];
                    statready.push(zw);
                    
                }
                //console.log(statready);
                
                //Optionen für das Google-Diagramm festlegen
                var options = {
                    title: 'Statistik - Defibrillatoren pro Bezirk in Wien'
                };                
                
                var statesData = google.visualization.arrayToDataTable(statready);
                var chart = new google.visualization.PieChart(document.getElementById('map_div'));
                chart.draw(statesData,options);
                
                //Tabellarische Ausgabe
                //in der letzten Reihe des Arrays wird noch die Gesamtsumme an Defis hinzugefügt
                var tabData = statready;
                var endline = ['Gesamtanzahl Wien', (statesArray.length-1)];
                tabData.push(endline);

                var tabready = google.visualization.arrayToDataTable(tabData);
                var table = new google.visualization.Table(document.getElementById('table_div'));
                table.draw(tabready, {showRowNumber: false});                       
        });
 }
