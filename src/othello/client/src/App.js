import React, { Component } from 'react';

import Game from './components/Game';

import './App.css';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div className="App">
        <Game board={this.state.board}
              currentPlayer={this.state.currentPlayer}
              p1Name={this.state.p1Name}
              p1Connected={this.state.p1Connected}
              p2Name={this.state.p2Name}
              p2Connected={this.state.p2Connected}
              status={this.state.status}
              result={this.state.result}
              lastMove={this.state.lastMove} />
      </div>
    );
  }
}

export default App;


