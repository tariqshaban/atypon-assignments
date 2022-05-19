<!DOCTYPE html>
<html lang="en">
   <head>
      <title>Maze Game</title>
      <link rel="icon" href="img/Icon.png">
      <script>
         window.onload = function () {
             setTimeout(function() { document.getElementById("form").submit(); }, 5000);
         };

         function join(x) {
         document.getElementById('gameID').value=x;
         }
      </script>
      <link href="css/styles.css" rel="stylesheet" type="text/css">
   </head>
   <body>
   <video autoplay muted loop id="bg">
     <source src="bg.mp4" type="video/mp4">
   </video>
      <div class="container">
         <div class="formContainer">
            <p>Games List:</p>
            <form action="/Lobby" method="GET">
               <input type="hidden" id="gameID" name="gameID" value="">
               <input type="hidden" name="playerName" value="${playerName}">
               ${games}
            </form>
         </div>
      </div>
      <form id="form" action="/Join" method="GET">
         <input type="hidden" name="playerName" value="${playerName}">
      </form>
   </body>
</html>



