<!DOCTYPE html>
<html lang="en">
   <head>
      <title>Maze Game</title>
      <link rel="icon" href="img/Icon.png">
      <style type="text/css">
         .frame {width: 100%; height: 85%; position: relative; margin: auto;}
         .frame button {position: absolute; width: 10%;}
         .east {left: 70%; top: 40%;}
         .west {left: 20%; top: 40%;}
         .north {left: 45%; top: 0;}
         .south {left: 45%; top: 80%;}
         .center {left: 45%; top: 40%; background-color: #FFF; animation-name: color; animation-duration: 2s; animation-iteration-count: infinite;}
         .image {width: 80%;}
         .sticky {
         z-index: 1;
         position: sticky;
         top: 10px;
         padding: 5px;
         background: rgba(127,127,255,0.2);
         -webkit-backdrop-filter: blur(10px);
         backdrop-filter: blur(10px);
         }
         .frame button:after {
         content: "";
         display: block;
         padding-bottom: 0%;
         }
         .image:after {
         content: "";
         display: block;
         padding-bottom: 100%;
         }
         .stickyLeft {
         z-index: 1;
         position: fixed;
         top: 90px;
         left: 10px;
         height: 80%;
         width: 140px;
         padding: 5px;
         margin-top: 50px;
         background: rgba(127,127,255,0.2);
         -webkit-backdrop-filter: blur(10px);
         backdrop-filter: blur(10px);
         overflow-x: hidden;
         padding-top: 20px;
         }
         .stickyRight {
         z-index: 1;
         position: fixed;
         top: 90px;
         right: 10px;
         height: 80%;
         width: 140px;
         padding: 5px;
         margin-top: 50px;
         background: rgba(127,127,255,0.2);
         -webkit-backdrop-filter: blur(10px);
         backdrop-filter: blur(10px);
         overflow-x: hidden;
         padding-top: 20px;
         }
         @keyframes color {
         0% {
         background-color: #FFF;
         }
         50% {
         background-color: #FF0000;
         }
         100 {
         background-color: #FFF;
         }
         }
      </style>
      <script>
         function startTimer(duration, display) {
             var timer = duration, minutes, seconds;
             setInterval(function () {
                 minutes = parseInt(timer / 60, 10);
                 seconds = parseInt(timer % 60, 10);

                 minutes = minutes < 0 ? 0 : minutes;
                 seconds = seconds < 0 ? 0 : seconds;

                 minutes = minutes < 10 ? "0" + minutes : minutes;
                 seconds = seconds < 10 ? "0" + seconds : seconds;

                 display.textContent = minutes + ":" + seconds;

                 timer--;


             }, 1000);
         }

         window.onload = function () {
             var remaining = ${time}-1;
             display = document.querySelector('#time');
             startTimer(remaining, display);

             setTimeout(function() { document.getElementById("form").submit(); }, 5000);
         };
      </script>
      ${flashlight}
      <link href="css/styles.css" rel="stylesheet" type="text/css">
   </head>
   <body>
      <video autoplay muted loop id="bg">
         <source src="bg.mp4" type="video/mp4">
      </video>
      <div class="sticky" style="overflow: auto;">
         <div style="display:inline-block;">
            <p>${message}</p>
         </div>
         <div style="float: right;">
            <br><strong id="time"></strong><br><br>
            <form action="/GameField" method="GET">
               <input type="hidden" name="gameID" value="${gameID}">
               <input type="hidden" name="mapChoice" value="${mapChoice}">
               <input type="hidden" name="playerID" value="${playerID}">
               <input type="hidden" name="playerName" value="${playerName}">
               <button type="submit" name="command" value="t">View Status</button>
               <button type="submit" name="command" value="e">Exit Game</button>
            </form>
         </div>
      </div>
      <div class="stickyRight" style="overflow: auto;">
         <div style="display:inline-block;">
            <p>Defeated Players<br>-----------------------</p>
         </div>
         ${defeatedPlayers}
      </div>
      <form action="/GameField" method="GET">
         <input type="hidden" name="gameID" value="${gameID}">
         <input type="hidden" name="mapChoice" value="${mapChoice}">
         <input type="hidden" name="playerID" value="${playerID}">
         <input type="hidden" name="playerName" value="${playerName}">
         ${tiebreakerSidebar}
      </form>
      <div class="frame">
         <form action="/GameField" method="GET">
            <input type="hidden" name="gameID" value="${gameID}">
            <input type="hidden" name="mapChoice" value="${mapChoice}">
            <input type="hidden" name="playerID" value="${playerID}">
            <input type="hidden" name="playerName" value="${playerName}">
            <button type="submit" class="east" name="command" value="d">
            <img alt="east" class="image" src="${east}">
            </button>
            <button type="submit" class="north" name="command" value="w">
            <img alt="north" class="image" src="${north}">
            </button>
            <button type="submit" class="west" name="command" value="a">
            <img alt="west" class="image" src="${west}">
            </button>
            <button type="submit" class="south" name="command" value="s">
            <img alt="south" class="image" src="${south}">
            </button>
            ${enemy}
         </form>
         <form id="form" action="/GameField" method="GET">
            <input type="hidden" name="gameID" value="${gameID}">
            <input type="hidden" name="mapChoice" value="${mapChoice}">
            <input type="hidden" name="playerID" value="${playerID}">
            <input type="hidden" name="playerName" value="${playerName}">
         </form>
      </div>
      <div style="position: relative;">
         <form action="/GameField" method="GET">
            <input type="hidden" name="gameID" value="${gameID}">
            <input type="hidden" name="mapChoice" value="${mapChoice}">
            <input type="hidden" name="playerID" value="${playerID}">
            <input type="hidden" name="playerName" value="${playerName}">
            ${shopItems}
         </form>
      </div>
   </body>
   <script>
      minutes = parseInt(${time} / 60, 10);
      seconds = parseInt(${time} % 60, 10);
      minutes = minutes < 10 ? "0" + minutes : minutes;
      seconds = seconds < 10 ? "0" + seconds : seconds;
      document.querySelector('#time').textContent = minutes + ":" + seconds;
   </script>
</html>