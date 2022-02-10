import React, { Component } from 'react';

import logo from './AtomicGames-2018-Logo.svg';
// import logo from './atomic-games-logo.svg';
import PlayerOneSidebar from './PlayerOneSidebar'
import Board from './Board'
import PlayerTwoSidebar from './PlayerTwoSidebar'
import Messages from './Messages'

import './Game.css';

class Game extends Component {
  render() {
    return (
      <div className="Game">
        <div className="Game-title-area">
          <div className="Game-logo"> <img src={logo} alt="Othello"/> </div>
        </div>
        <div className="Game-content-area">
          <PlayerOneSidebar name={this.props.p1Name} currentPlayer={this.props.currentPlayer} connected={this.props.p1Connected}/>
          <Board data={this.props.board} lastMove={this.props.lastMove} />
          <PlayerTwoSidebar name={this.props.p2Name} currentPlayer={this.props.currentPlayer} connected={this.props.p2Connected} />
        </div>
        <Messages status={this.props.status}
                  result={this.props.result}
                  p1Name={this.props.p1Name}
                  p2Name={this.props.p2Name} />
      </div>
    );
  }
}

export default Game;


