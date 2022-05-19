<!DOCTYPE html>
<html lang="en">
   <head>
      <title>Maze Game</title>
      <link rel="icon" href="img/Icon.png">
      <script>
         function addName() {
             document.getElementById("playerJoin").value = document.getElementById("playerField").value;
             document.getElementById("playerHost").value = document.getElementById("playerField").value;
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
         <label>Name:</label>
         <input type="text" id="playerField" value="Player">
         <div class="formContainer">
            <form class="sideBySideForm" action="/Join" method="GET" onsubmit="return addName()">
               <input type="hidden" id="playerJoin" name="playerName">
               <input class="button" id="join" type="submit" value="join game" />
            </form>
            <form class="sideBySideForm" action="/Host" method="GET" onsubmit="return addName()">
               <input type="hidden" id="playerHost" name="playerName">
               <input class="button" id="hidden" type="submit" value="host game" />
            </form>
         </div>
      </div>
   </body>
</html>