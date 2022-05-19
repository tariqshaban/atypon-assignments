<!DOCTYPE html>
<html lang="en">
   <head>
      <title>Maze Game</title>
      <link rel="icon" href="img/Icon.png">
      <style>
         .selectedMap{visibility: hidden;}
      </style>
      <script>
         function showSelected(x) {
             var hid = document.getElementsByClassName("selectedMap");
             if(hid[0].offsetWidth > 0 && hid[0].offsetHeight > 0) {
                 hid[0].innerHTML="Selected map: "+x;
                 hid[0].style.visibility = "visible";
             }
             document.getElementById("mapChoice").value=x;
         }
         function checkMapChoice(x) {
             var hid = document.getElementsByClassName("selectedMap");
             if(hid[0].innerHTML=="placeholder") {
             alert("Please select a map");
             return false;
             }
             return true;
         }
      </script>
      <link href="css/styles.css" rel="stylesheet" type="text/css">
   </head>
   <body>
   <video autoplay muted loop id="bg">
     <source src="bg.mp4" type="video/mp4">
   </video>
      <div class="container">
         <label>Select a map:</label><br><br>
         ${maps}
         <p class="selectedMap">placeholder</p>
         <form action="/LobbyHost" method="GET" onsubmit="return checkMapChoice()">
            <input type="hidden" name="playerName" value="${playerName}">
            <input type="hidden" id="mapChoice" name="mapChoice" value="">
            <input id="launch" type="submit"/>
         </form>
      </div>
      </div>
   </body>
</html>