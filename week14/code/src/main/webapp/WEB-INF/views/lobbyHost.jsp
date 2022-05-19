<!DOCTYPE html>
<html lang="en">
   <head>
      <title>Maze Game</title>
      <link rel="icon" href="img/Icon.png">
      <script>
         window.onload = function () {
             setTimeout(function() { document.getElementById("form").submit(); }, 5000);
         };
      </script>
      <link href="css/styles.css" rel="stylesheet" type="text/css">
   </head>
   <body>
   <video autoplay muted loop id="bg">
     <source src="bg.mp4" type="video/mp4">
   </video>
      <div class="container">
         <div class="formContainer">
            <br>
            <p>Joined players: </p>
            ${players}
            <form id="form" action="/LobbyHost" method="GET">
               <input type="hidden" name="gameID" value="${gameID}">
               <input type="hidden" name="mapChoice" value="${mapChoice}">
               <input type="hidden" name="playerID" value="${playerID}">
               <input type="hidden" name="playerName" value="${playerName}">
            </form>
            <form action="/GameField" method="GET">
               <input type="hidden" name="gameID" value="${gameID}">
               <input type="hidden" name="mapChoice" value="${mapChoice}">
               <input type="hidden" name="playerID" value="${playerID}">
               <input type="hidden" name="playerName" value="${playerName}">
               <input id="launch" type="submit" value="Start Game"/>
            </form>
         </div>
      </div>
   </body>
</html>



