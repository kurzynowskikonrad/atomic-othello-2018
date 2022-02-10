import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';

const othelloApp = ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();


var webSocket = new WebSocket("ws://" + window.location.host + "/api/game-state");
// var webSocket = new WebSocket("ws://localhost:8080/api/game-state");

webSocket.onmessage = function(event) {
  var gameState = JSON.parse(event.data);
  console.log(gameState);
  othelloApp.setState({
    board: gameState.board,
    currentPlayer: gameState.currentPlayer,
    lastMove: gameState.lastMove,
    status: gameState.status,
    result: gameState.gameResult,
    p1Name: gameState.config.p1Name,
    p1Connected: gameState.playerOne.status === "connected",
    p2Name: gameState.config.p2Name,
    p2Connected: gameState.playerTwo.status === "connected"
  });
};