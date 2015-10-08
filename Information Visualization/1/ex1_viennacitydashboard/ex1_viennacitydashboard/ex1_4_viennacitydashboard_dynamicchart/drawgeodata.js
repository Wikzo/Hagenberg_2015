function drawGeoData(url,labelKey) {

        $.ajax({
          url: url,
          dataType: "JSON"
        }).done(function(data) {
            
                //Das JSON-Dataset besitzt eine andere Datenreihenfolge, als es Googlecharts benötigt.
                //Daher erzeuge ich ein Zwischenarray, das die gewünschte Datenform für Googlecharts besitzt
                //und befülle es mit den einzelnen Daten aus dem JSON-Dataset der Stadt Wien.
                
				var statesArray = [['Long', 'Lat', 'Name']]; //headerInfo
                var zaehler = 0;       
                $.each(data.features, function() {
                    //Das Koordinaten-Datenfeld im Dataset besteht aus einem Array - daher muss dieses aufgesplitter werden und die Reihenfolge von Longitudenal und Lateral vertauscht werden
                    var lat = this.geometry.coordinates[0];
                    //console.log(lat);
                    var long = this.geometry.coordinates[1];
                    //console.log(long);
                    var stateitem = [long,lat,this.properties[labelKey]];
                    //console.log(this.properties[labelKey]);
                    //console.log(stateitem);
                    statesArray.push(stateitem);
                    zaehler++;
                });
                //console.log(statesArray); 
                //alert("Derzeit befinden sich "+zaehler+" Einträge in der Datenbank der Stadt Wien.");
                var statesData = google.visualization.arrayToDataTable(statesArray);
                
				
				var map = new google.visualization.Map(document.getElementById('map_div'));
                map.draw(statesData, {showTip: true});
				
                
                var divstat2 = document.getElementById("table_div");
                divstat2.textContent = "";
                
                ;
                        
        });
      }